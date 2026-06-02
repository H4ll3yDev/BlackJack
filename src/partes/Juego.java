package partes;

import dao.DAOResultado;
import modelo.Jugador;

import java.util.List;
import java.util.Scanner;

public class Juego {

    private final DAOResultado resultadoDAO = new DAOResultado();

    private final Scanner sc;
    private final Jugador jugadorActivo;

    public Juego(Scanner sc, Jugador jugadorActivo) {
        this.sc = sc;
        this.jugadorActivo = jugadorActivo;
    }

    // Pide apuesta y ejecuta una ronda completa.
    public void ejecutar() {

        int apuesta = new Apuesta(sc).ejecutar(jugadorActivo);

        if (apuesta <= 0) {
            return;   // el jugador canceló
        }

        new Ronda(sc, jugadorActivo).ejecutar(apuesta);
    }

    // Muestra el ranking global (llamado desde Menu).
    public void mostrarRanking() {

        List<String[]> ranking = resultadoDAO.obtenerRanking();

        System.out.println("\n╔═════════════════════════════════════════════════╗");
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
}