package partes;

import modelo.Carta;

import java.util.List;
import java.util.Scanner;

public class Turno {

    private final Scanner sc;

    public Turno(Scanner sc) {
        this.sc = sc;
    }

    // Turno interactivo del jugador. Devuelve true si se pasó de 21.
    public boolean ejecutarJugador(String nombreJugador, List<Carta> mano, List<Carta> mazo) {

        System.out.println("\n  ── TURNO DE " + nombreJugador.toUpperCase() + " ──");
        System.out.println();
        mostrarMano("Tu mano", mano);

        while (true) {

            int total = calcularTotal(mano);
            System.out.println("Total: " + total);

            if (total == 21) {

                System.out.println("¡BLACKJACK!");
                return false;

            } else if (total > 21) {

                System.out.println("¡Te has pasado de 21!");
                return true;

            } else {

                System.out.print("  ¿Pides carta (C) o plantas (P)? ");
                String accion = sc.nextLine().trim().toUpperCase();

                if (accion.equals("C")) {

                    Carta nueva = sacarCarta(mazo);
                    mano.add(nueva);
                    System.out.println("Has sacado: " + nueva);

                } else if (accion.equals("P")) {

                    return false;

                } else {

                    System.out.println("  Opción no válida. Escribe C o P.");
                }
            }
        }
    }

    // Turno automático de la banca: pide cartas hasta llegar a 17.
    public void ejecutarBanca(List<Carta> mano, List<Carta> mazo) {

        System.out.println("\n  ── TURNO DE LA BANCA ──");
        mostrarMano("Mano de la banca", mano);

        while (calcularTotal(mano) < 17) {

            Carta nueva = sacarCarta(mazo);
            mano.add(nueva);
            System.out.println("  La banca saca: " + nueva);
        }
    }

    // Calcula el total de una mano ajustando el As si hace falta.
    public int calcularTotal(List<Carta> mano) {

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

    // Auxiliar: saca la primera carta del mazo.
    private Carta sacarCarta(List<Carta> mazo) {
        return mazo.remove(0);
    }

    // Auxiliar: imprime una mano con su total.
    private void mostrarMano(String etiqueta, List<Carta> mano) {

        System.out.print("  " + etiqueta + ": ");

        for (Carta c : mano) {
            System.out.print(c + "  ");
        }
        System.out.println("→ Total: " + calcularTotal(mano));
    }
}