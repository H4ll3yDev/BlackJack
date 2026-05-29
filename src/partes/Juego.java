package partes;

import dao.*;
import modelo.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

//Juego
public class Juego {

    // DAOs 
    private final DAOCarta     cartaDAO     = new DAOCarta();
    private final DAOFicha     fichaDAO     = new DAOFicha();
    private final DAOJugador   jugadorDAO   = new DAOJugador();
    private final DAOPartida   partidaDAO   = new DAOPartida();
    private final DAOResultado resultadoDAO = new DAOResultado();

    // Dependencias
    private final Scanner sc;
    private final Jugador jugadorActivo;

    public Juego(Scanner sc, Jugador jugadorActivo) {
    	
        this.sc            = sc;
        this.jugadorActivo = jugadorActivo;
        
    }

    
    //  PARTIDA

    public void ejecutar() {

        // 1. Pedir apuesta
        int apuesta = pedirApuesta();
        
        if (apuesta <= 0) {
        	
            return;   // el jugador canceló o no tiene saldo
        }

        // 2. Registrar la partida en BBDD
        Partida partida = new Partida(1);
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
        int     totalJugador = calcularTotal(manoJugador);
        int     totalBanca   = calcularTotal(manoBanca);
        boolean jugadorGana  = determinarGanador(totalJugador, totalBanca, jugadorPasado);

        guardarResultados(partida, apuesta, totalJugador, jugadorGana);
    }

    
    //  RANKING  (llamado desde Menu)
    public void mostrarRanking() {

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

    //  APUESTA
    private int pedirApuesta() {

        List<Ficha> fichas = fichaDAO.buscarTodas();

        System.out.println("\n  ── REALIZA TU APUESTA ──");
        System.out.println("Saldo disponible: " + jugadorActivo.getSaldo() + " fichas");
        System.out.println("Fichas disponibles:");

        for (int i = 0; i < fichas.size(); i++) {
        	
            System.out.println("    " + (i + 1) + ". " + fichas.get(i));
            
        }
        System.out.println("0. Cancelar");
        System.out.print("Elige una ficha: ");

        int     apuesta      = 0;
        boolean apuestaValida = false;

        while (!apuestaValida) {
        	
            try {
            	
                int opcion = Integer.parseInt(sc.nextLine().trim());

                if (opcion == 0) {
                	
                    apuestaValida = true;

                } else if (opcion >= 1 && opcion <= fichas.size()) {
                	
                    int valor = fichas.get(opcion - 1).getValor();

                    if (valor > jugadorActivo.getSaldo()) {
                    	
                        System.out.println("  No tienes suficientes fichas.");
                        System.out.print("  Elige otra: ");
                        
                    } else {
                    	
                        apuesta      = valor;
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

    //  LÓGICA DE RESULTADO
    
    private boolean determinarGanador(int totalJugador, int totalBanca,
                                       boolean jugadorPasado) {
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
            System.out.println("\n  Empate (" + totalJugador + "). La banca gana en empate.");

        } else {
        	
            jugadorGana = false;
            System.out.println("\n  La banca gana. Su total (" + totalBanca
                    + ") supera al tuyo (" + totalJugador + ").");
            
        }

        return jugadorGana;
    }

    private void guardarResultados(Partida partida, int apuesta, int puntuacion, boolean jugadorGana) {

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

        resultadoDAO.insertar(partida.getId(), jugadorActivo.getId(), puntuacion, apuesta, jugadorGana);
    }

    //  UTILIDADES
    // Saca la primera carta del mazo y la elimina de él. 
    private Carta sacarCarta(List<Carta> mazo) {
    	
        return mazo.remove(0);
    }

    // Calcula el total de una mano ajustando el As si hace falta. 
    private int calcularTotal(List<Carta> mano) {

        int total = 0;
        int ases  = 0;

        for (Carta carta : mano) {
        	
            total += carta.getPuntos();
            
            if (carta.getValor().equals("A")) {
            	
                ases++;
            }
        }

        int i = 0;
        
        while (total > 21 && i < ases) {
        	
            total -= 10;
            i++;
        }

        return total;
    }

    // Muestra las cartas de una mano con su total. 
    private void mostrarMano(String etiqueta, List<Carta> mano) {
    	
        System.out.print("  " + etiqueta + ": ");
        
        for (Carta c : mano) {
        	
            System.out.print(c + "  ");
        }
        System.out.println("→ Total: " + calcularTotal(mano));
    }
}