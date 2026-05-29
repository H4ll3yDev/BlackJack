package controlador;

import dao.DAOCarta;
import dao.DAOFicha;
import dao.DAOJugador;
import dao.DAOPartida;
import dao.DAOResultado;
import modelo.Carta;
import modelo.Ficha;
import modelo.Jugador;
import modelo.Partida;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Juego
 * ─────────────────────────────────────────────────────────────────────────
 * Controlador principal del Blackjack.
 * Gestiona el menú, el flujo de login/registro y la lógica de la partida.
 *
 * REGLAS IMPLEMENTADAS:
 *   - El jugador pide cartas hasta que plante o se pase de 21.
 *   - La banca (ordenador) pide cartas mientras tenga menos de 17.
 *   - El As vale 11; si el total supera 21 pasa a valer 1.
 *   - Blackjack natural (21 con 2 cartas) gana directamente.
 *
 * NOTA: No se usa break dentro de bucles (requisito del proyecto).
 * ─────────────────────────────────────────────────────────────────────────
 */
public class Juego {

    // ─── DAOs ────────────────────────────────────────────────────────────────
    private final DAOJugador   jugadorDAO   = new DAOJugador();
    private final DAOCarta     cartaDAO     = new DAOCarta();
    private final DAOFicha     fichaDAO     = new DAOFicha();
    private final DAOPartida   partidaDAO   = new DAOPartida();
    private final DAOResultado resultadoDAO = new DAOResultado();

    // ─── Estado de sesión ────────────────────────────────────────────────────
    private Jugador jugadorActivo = null;   // jugador que ha hecho login
    private final Scanner sc = new Scanner(System.in);

    // =========================================================================
    //  PUNTO DE ENTRADA
    // =========================================================================
    public static void main(String[] args) {
        new Juego().iniciar();
    }

    // =========================================================================
    //  INICIO: login o registro antes del menú principal
    // =========================================================================
    private void iniciar() {
        mostrarBienvenida();
        menuAcceso();
        menuPrincipal();
    }

    // ─── Menú de acceso: registrarse o hacer login ───────────────────────────
    private void menuAcceso() {

        boolean accedido = false;

        while (!accedido) {

            System.out.println("\n┌─────────────────────────┐");
            System.out.println("│    ACCESO AL CASINO     │");
            System.out.println("├─────────────────────────┤");
            System.out.println("│  1. Registrarse         │");
            System.out.println("│  2. Iniciar sesión      │");
            System.out.println("└─────────────────────────┘");
            System.out.print("  Elige una opción: ");

            String opcion = sc.nextLine().trim();

            if (opcion.equals("1")) {
                accedido = flujoRegistro();
            } else if (opcion.equals("2")) {
                accedido = flujoLogin();
            } else {
                System.out.println("  Opción no válida.");
            }
        }
    }

    // ─── Registro ─────────────────────────────────────────────────────────────
    private boolean flujoRegistro() {

        System.out.println("\n  ── REGISTRO ──");
        System.out.print("  Nombre de usuario: ");
        String nombre = sc.nextLine().trim();

        if (jugadorDAO.existeNombre(nombre)) {
            System.out.println("  Ese nombre ya está en uso. Elige otro.");
            return false;
        }

        System.out.print("  Contraseña: ");
        String pass = sc.nextLine().trim();

        Jugador nuevo = new Jugador(nombre, hashMD5(pass));
        int id = jugadorDAO.insertar(nuevo);

        if (id == -1) {
            System.out.println("  Error al registrar. Inténtalo de nuevo.");
            return false;
        }

        jugadorActivo = nuevo;
        System.out.println("  ¡Bienvenido al casino, " + nombre + "! Tienes 1000 fichas.");
        return true;
    }

    // ─── Login ────────────────────────────────────────────────────────────────
    private boolean flujoLogin() {

        System.out.println("\n  ── LOGIN ──");
        System.out.print("  Nombre de usuario: ");
        String nombre = sc.nextLine().trim();

        System.out.print("  Contraseña: ");
        String pass = sc.nextLine().trim();

        Jugador jugador = jugadorDAO.login(nombre, hashMD5(pass));

        if (jugador == null) {
            System.out.println("  Credenciales incorrectas.");
            return false;
        }

        jugadorActivo = jugador;
        System.out.println("  ¡Bienvenido de nuevo, " + jugador.getNombreUsuario()
                + "! Saldo: " + jugador.getSaldo() + " fichas.");
        return true;
    }

    //  MENÚ PRINCIPAL
    private void menuPrincipal() {

        boolean salir = false;

        while (!salir) {

            System.out.println("\n╔══════════════════════════╗");
            System.out.println("║   ♠ BLACKJACK CASINO ♠   ║");
            System.out.println("╠══════════════════════════╣");
            System.out.println("║  1. Jugar partida        ║");
            System.out.println("║  2. Ver instrucciones    ║");
            System.out.println("║  3. Ranking              ║");
            System.out.println("║  4. Salir                ║");
            System.out.println("╚══════════════════════════╝");
            System.out.print("  Elige una opción: ");

            String opcion = sc.nextLine().trim();

            if (opcion.equals("1")) {
                jugarPartida();
            } else if (opcion.equals("2")) {
                mostrarInstrucciones();
            } else if (opcion.equals("3")) {
                mostrarRanking();
            } else if (opcion.equals("4")) {
                salir = true;
                System.out.println("\n  ¡Hasta la próxima, "
                        + jugadorActivo.getNombreUsuario() + "!");
            } else {
                System.out.println("  Opción no válida.");
            }
        }
    }

    //  LÓGICA DE LA PARTIDA
    private void jugarPartida() {

        // 1. Pedir apuesta
        int apuesta = pedirApuesta();
        if (apuesta <= 0) {
            return;  // el jugador canceló o no tiene saldo
        }

        // 2. Registrar la partida en BBDD
        Partida partida = new Partida(1);   // 1 jugador vs banca
        partidaDAO.insertar(partida);

        // 3. Cargar y mezclar la baraja desde BBDD
        List<Carta> mazo = cartaDAO.buscarTodas();
        Collections.shuffle(mazo);

        // 4. Repartir cartas iniciales
        List<Carta> manoJugador = new ArrayList<>();
        List<Carta> manoBanca   = new ArrayList<>();

        manoJugador.add(sacarCarta(mazo));
        manoBanca.add(sacarCarta(mazo));
        manoJugador.add(sacarCarta(mazo));
        manoBanca.add(sacarCarta(mazo));

        // 5. Turno del jugador
        System.out.println("\n  ── TURNO DE " + jugadorActivo.getNombreUsuario().toUpperCase() + " ──");
        mostrarMano("Tu mano", manoJugador);
        System.out.println("  Carta visible de la banca: " + manoBanca.get(0));

        boolean jugadorPasado = false;
        boolean jugadorPlanta = false;

        while (!jugadorPasado && !jugadorPlanta) {

            int total = calcularTotal(manoJugador);
            System.out.println("  Total: " + total);

            if (total == 21) {
                System.out.println("  ¡BLACKJACK!");
                jugadorPlanta = true;

            } else if (total > 21) {
                System.out.println("  ¡Te has pasado de 21!");
                jugadorPasado = true;

            } else {
                System.out.print("  ¿Pides carta (P) o plantas (L)? ");
                String accion = sc.nextLine().trim().toUpperCase();

                if (accion.equals("P")) {
                    Carta nueva = sacarCarta(mazo);
                    manoJugador.add(nueva);
                    System.out.println("  Has sacado: " + nueva);
                } else if (accion.equals("L")) {
                    jugadorPlanta = true;
                } else {
                    System.out.println("  Opción no válida. Escribe P o L.");
                }
            }
        }

        // 6. Turno de la banca (solo si el jugador no se pasó)
        if (!jugadorPasado) {
            System.out.println("\n  ── TURNO DE LA BANCA ──");
            mostrarMano("Mano de la banca", manoBanca);

            while (calcularTotal(manoBanca) < 17) {
                Carta nueva = sacarCarta(mazo);
                manoBanca.add(nueva);
                System.out.println("  La banca saca: " + nueva);
            }
        }

        // 7. Determinar ganador y guardar resultados
        int totalJugador = calcularTotal(manoJugador);
        int totalBanca   = calcularTotal(manoBanca);

        boolean jugadorGana = determinarGanador(totalJugador, totalBanca, jugadorPasado);
        guardarResultados(partida, apuesta, totalJugador, jugadorGana);
    }

    //Pedir apuesta mostrando fichas disponibles
    private int pedirApuesta() {

        List<Ficha> fichas = fichaDAO.buscarTodas();

        System.out.println("\n  ── REALIZA TU APUESTA ──");
        System.out.println("  Saldo disponible: " + jugadorActivo.getSaldo() + " fichas");
        System.out.println("  Fichas disponibles:");

        for (int i = 0; i < fichas.size(); i++) {
            System.out.println("    " + (i + 1) + ". " + fichas.get(i));
        }

        System.out.println("    0. Cancelar");
        System.out.print("  Elige una ficha: ");

        int apuesta = 0;
        boolean apuestaValida = false;

        while (!apuestaValida) {
            try {
                int opcion = Integer.parseInt(sc.nextLine().trim());

                if (opcion == 0) {
                    apuestaValida = true;   // apuesta = 0 → cancelar

                } else if (opcion >= 1 && opcion <= fichas.size()) {
                    int valor = fichas.get(opcion - 1).getValor();

                    if (valor > jugadorActivo.getSaldo()) {
                        System.out.println("  No tienes suficientes fichas.");
                        System.out.print("  Elige otra: ");
                    } else {
                        apuesta = valor;
                        apuestaValida = true;
                    }

                } else {
                    System.out.print("  Opción no válida. Elige de nuevo: ");
                }

            } catch (NumberFormatException e) {
                System.out.print("  Escribe un número: ");
            }
        }

        return apuesta;
    }

    //Determina quién gana y muestra el resultado
    private boolean determinarGanador(int totalJugador, int totalBanca, boolean jugadorPasado) {

        boolean jugadorGana;

        if (jugadorPasado) {
            jugadorGana = false;
            System.out.println("\n  La banca gana. Te has pasado.");

        } else if (totalBanca > 21) {
            jugadorGana = true;
            System.out.println("\n  ¡Ganas! La banca se ha pasado de 21.");

        } else if (totalJugador > totalBanca) {
            jugadorGana = true;
            System.out.println("\n  ¡Ganas! Tu total (" + totalJugador
                    + ") supera a la banca (" + totalBanca + ").");

        } else if (totalJugador == totalBanca) {
            jugadorGana = false;
            System.out.println("\n  Empate (" + totalJugador + "). La banca gana en caso de empate.");

        } else {
            jugadorGana = false;
            System.out.println("\n  La banca gana. Su total (" + totalBanca
                    + ") supera al tuyo (" + totalJugador + ").");
        }

        return jugadorGana;
    }

    //Guarda resultados en BBDD y actualiza saldo
    private void guardarResultados(Partida partida, int apuesta,
                                    int puntuacion, boolean jugadorGana) {

        // Actualizar saldo en memoria y en BBDD
        int nuevoSaldo = jugadorActivo.getSaldo();

        if (jugadorGana) {
            nuevoSaldo += apuesta;
            jugadorDAO.incrementarVictorias(jugadorActivo.getId());
        } else {
            nuevoSaldo -= apuesta;
        }

        jugadorActivo.setSaldo(nuevoSaldo);
        jugadorDAO.actualizarSaldo(jugadorActivo.getId(), nuevoSaldo);

        System.out.println("  Saldo actual: " + nuevoSaldo + " fichas.");

        // Guardar resultado en BBDD
        resultadoDAO.insertar(
                partida.getId(),
                jugadorActivo.getId(),
                puntuacion,
                apuesta,
                jugadorGana
        );
    }

    //  INSTRUCCIONES
    private void mostrarInstrucciones() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║            INSTRUCCIONES BLACKJACK         ║");
        System.out.println("╠════════════════════════════════════════════╣");
        System.out.println("║ Objetivo: llegar a 21 sin pasarse.         ║");
        System.out.println("║                                            ║");
        System.out.println("║ Valores de las cartas:                     ║");
        System.out.println("║   · Números (2-10): su valor normal.       ║");
        System.out.println("║   · J, Q, K: valen 10 puntos.              ║");
        System.out.println("║   · As: vale 11 (o 1 si supera 21).        ║");
        System.out.println("║                                            ║");
        System.out.println("║ Turno del jugador:                         ║");
        System.out.println("║   · P → Pedir carta                        ║");
        System.out.println("║   · L → Plantarse                          ║");
        System.out.println("║                                            ║");
        System.out.println("║ La banca pide cartas hasta llegar a 17.    ║");
        System.out.println("║ En caso de empate gana la banca.           ║");
        System.out.println("╚════════════════════════════════════════════╝");
    }

    //  RANKING
    private void mostrarRanking() {

        List<String[]> ranking = resultadoDAO.obtenerRanking();

        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║                 RANKING GLOBAL                  ║");
        System.out.println("╠═══╦══════════════════╦══════════╦═══════╦═══════╣");
        System.out.printf("║%-3s║%-18s║%-10s║%-7s║%-7s║%n",
                "Pos", "Jugador", "Victorias", "Saldo", "Puntos");
        System.out.println("╠═══╬══════════════════╬══════════╬═══════╬═══════╣");

        int pos = 1;
        for (String[] fila : ranking) {
            System.out.printf("║%-3d║%-18s║%-10s║%-7s║%-7s║%n",
                    pos++, fila[0], fila[1], fila[2], fila[3]);
        }

        System.out.println("╚═══╩══════════════════╩══════════╩═══════╩═══════╝");
    }

    //  UTILIDADES

    /** Saca la primera carta del mazo y la elimina de él. */
    private Carta sacarCarta(List<Carta> mazo) {
        return mazo.remove(0);
    }

    /** Calcula el total de una mano ajustando el As si hace falta. */
    private int calcularTotal(List<Carta> mano) {

        int total   = 0;
        int ases    = 0;

        for (Carta carta : mano) {
            total += carta.getPuntos();
            if (carta.getValor().equals("A")) {
                ases++;
            }
        }

        // Si nos pasamos y tenemos ases, convertimos 11 → 1
        int i = 0;
        while (total > 21 && i < ases) {
            total -= 10;
            i++;
        }

        return total;
    }

    /** Muestra las cartas de una mano con su total. */
    private void mostrarMano(String etiqueta, List<Carta> mano) {
        System.out.print("  " + etiqueta + ": ");
        for (Carta c : mano) {
            System.out.print(c + "  ");
        }
        System.out.println("→ Total: " + calcularTotal(mano));
    }

    /** Hash MD5 de una cadena (encriptación básica de contraseña). */
    private String hashMD5(String texto) {
        try {
            MessageDigest md   = MessageDigest.getInstance("MD5");
            byte[]        hash = md.digest(texto.getBytes());
            StringBuilder sb   = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error MD5: " + e.getMessage());
            return texto;
        }
    }

    /** Mensaje de bienvenida al arrancar el programa. */
    private void mostrarBienvenida() {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║    ♠ ♥  BLACKJACK CASINO  ♣ ♦    ║");
        System.out.println("║   Harley Mena, Macos Torregrosa  ║");
        System.out.println("╚══════════════════════════════════╝");
    }
}
