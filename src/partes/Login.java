package partes;

import dao.*;
import modelo.Jugador;
import java.util.Scanner;

/*Login
 Gestiona el flujo de inicio de sesión de un jugador existente.
 Devuelve el Jugador autenticado o null si las credenciales son erróneas.*/

public class Login {

    private final Scanner    sc;
    private final DAOJugador jugadorDAO = new DAOJugador();

    public Login(Scanner sc) {
        this.sc = sc;
    }


    //  FLUJO PRINCIPAL

    /*Solicita nombre y contraseña al usuario.
     @return el Jugador si las credenciales son correctas, null si no.*/
    
    public Jugador ejecutar() {

        System.out.println("\n  ── LOGIN ──");
        System.out.print("  Nombre de usuario: ");
        String nombre = sc.nextLine().trim();

        System.out.print("  Contraseña: ");
        String pass = sc.nextLine().trim();

        Jugador jugador = jugadorDAO.login(nombre, Seguridad.hashMD5(pass));

        if (jugador == null) {
            System.out.println("  Credenciales incorrectas.");
            return null;
        }

        System.out.println("  ¡Bienvenido de nuevo, " + jugador.getNombreUsuario()
                + "! Saldo: " + jugador.getSaldo() + " fichas.");
        return jugador;
    }
}