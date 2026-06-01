package partes;

import modelo.*;
import java.util.Scanner;


public class Menu {

    private final Scanner sc = new Scanner(System.in);

    // Jugador que ha iniciado sesión; se comparte con el resto de secciones.
    private Jugador jugadorActivo = null;

    //  INICIO
    public void iniciar() {
    	
        mostrarBienvenida();
        menuAcceso();
        menuPrincipal();
        sc.close();
        
    }
    
    // Bienvenida 
    private void mostrarBienvenida() {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║    ♠ ♥  BLACKJACK CASINO  ♣ ♦    ║");
        System.out.println("║   Harley Mena, Marcos Torregrosa ║");
        System.out.println("╚══════════════════════════════════╝");
    }

    // Menú de acceso: registrarse o hacer login 
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
            	
                jugadorActivo = new CreacionUsuario(sc).ejecutar();
                accedido = (jugadorActivo != null);

            } else if (opcion.equals("2")) {
            	
                jugadorActivo = new Login(sc).ejecutar();
                accedido = (jugadorActivo != null);

            } else {
            	
                System.out.println("  Opción no válida.");
            }
        }
    }

    // Menú principal 
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
            	
                new Juego(sc, jugadorActivo).ejecutar();

            } else if (opcion.equals("2")) {
            	
                mostrarInstrucciones();

            } else if (opcion.equals("3")) {
            	
                new Juego(sc, jugadorActivo).mostrarRanking();

            } else if (opcion.equals("4")) {
            	
                salir = true;
                System.out.println("\n  ¡Hasta la próxima, " + jugadorActivo.getNombreUsuario() + "!");

            } else {
            	
                System.out.println("  Opción no válida.");
            }
        }
    }

    // Instrucciones
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
}