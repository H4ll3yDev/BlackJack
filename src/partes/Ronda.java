package partes;

import dao.DAOCarta;
import dao.DAOJugador;
import dao.DAOPartida;
import dao.DAOResultado;
import modelo.Carta;
import modelo.Jugador;
import modelo.Partida;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Ronda {

    private final DAOCarta cartaDAO = new DAOCarta();
    private final DAOJugador jugadorDAO = new DAOJugador();
    private final DAOPartida partidaDAO = new DAOPartida();
    private final DAOResultado resultadoDAO = new DAOResultado();

    private final Scanner sc;
    private final Jugador jugadorActivo;

    public Ronda(Scanner sc, Jugador jugadorActivo) {
        this.sc = sc;
        this.jugadorActivo = jugadorActivo;
    }

    // Ejecuta una ronda completa dado el valor apostado.
    public void ejecutar(int apuesta) {

        // 1. Registrar la partida en BBDD
        Partida partida = new Partida(1);
        partidaDAO.insertar(partida);

        // 2. Cargar y mezclar la baraja
        List<Carta> mazo = cartaDAO.buscarTodas();
        Collections.shuffle(mazo);

        // 3. Repartir cartas iniciales
        List<Carta> manoJugador = new ArrayList<>();
        List<Carta> manoBanca   = new ArrayList<>();

        manoJugador.add(mazo.remove(0));
        manoBanca.add(mazo.remove(0));
        manoJugador.add(mazo.remove(0));
        manoBanca.add(mazo.remove(0));

        System.out.println("  Carta visible de la banca: " + manoBanca.get(0));

        // 4. Turno del jugador
        Turno turno = new Turno(sc);
        boolean jugadorPasado = turno.ejecutarJugador(jugadorActivo.getNombreUsuario(), manoJugador, mazo);

        // 5. Turno de la banca (solo si el jugador no se pasó)
        if (!jugadorPasado) {
            turno.ejecutarBanca(manoBanca, mazo);
        }

        // 6. Determinar ganador y guardar
        int totalJugador = turno.calcularTotal(manoJugador);
        int totalBanca   = turno.calcularTotal(manoBanca);
        boolean jugadorGana  = determinarGanador(totalJugador, totalBanca, jugadorPasado);

        guardarResultados(partida, apuesta, totalJugador, jugadorGana);
    }

    // Determina quién gana e imprime el resultado.
    private boolean determinarGanador(int totalJugador, int totalBanca, boolean jugadorPasado) {

        if (jugadorPasado) {
            System.out.println("\n  La banca gana. Te has pasado.");
            return false;
        }

        if (totalBanca > 21) {
            System.out.println("\n  ¡Ganas! La banca se ha pasado de 21.");
            return true;
        }

        if (totalJugador > totalBanca) {
            System.out.println("\n  ¡Ganas! Tu total (" + totalJugador + ") supera a la banca (" + totalBanca + ").");
            return true;
        }

        if (totalJugador == totalBanca) {
            System.out.println("\n  Empate (" + totalJugador + "). La banca gana en empate.");
            return false;
        }

        System.out.println("\n  La banca gana. Su total (" + totalBanca + ") supera al tuyo (" + totalJugador + ").");
        return false;
    }

    // Actualiza saldo y victorias en BBDD y persiste el resultado.
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
}