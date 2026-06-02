package partes;

import dao.DAOFicha;
import modelo.Ficha;
import modelo.Jugador;

import java.util.List;
import java.util.Scanner;

public class Apuesta {

    private final Scanner   sc;
    private final DAOFicha  fichaDAO = new DAOFicha();

    public Apuesta(Scanner sc) {
        this.sc = sc;
    }

    // Muestra las fichas disponibles y pide al jugador que elija una.
    // Devuelve el valor apostado, o 0 si el jugador cancela.
    public int ejecutar(Jugador jugador) {

        List<Ficha> fichas = fichaDAO.buscarTodas();

        System.out.println("\n  ── REALIZA TU APUESTA ──");
        System.out.println("Saldo disponible: " + jugador.getSaldo() + " fichas");
        System.out.println("Fichas disponibles:");

        for (int i = 0; i < fichas.size(); i++) {
            System.out.println("\n  " + (i + 1) + ". " + fichas.get(i));
        }
        System.out.println("\n  0. Cancelar");
        System.out.print("\nElige una ficha: ");

        int apuesta = 0;
        boolean apuestaValida = false;

        while (!apuestaValida) {

            try {

                int opcion = Integer.parseInt(sc.nextLine().trim());

                if (opcion == 0) {

                    apuestaValida = true;

                } else if (opcion >= 1 && opcion <= fichas.size()) {

                    int valor = fichas.get(opcion - 1).getValor();

                    if (valor > jugador.getSaldo()) {

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
}
