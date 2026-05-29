package partes;

import dao.DAOJugador;
import modelo.Jugador;
import java.util.Scanner;

/*CreacionUsuario

 Gestiona el flujo de registro de un nuevo jugador.
 Comprueba que el nombre no esté en uso y persiste el nuevo registro.
 Devuelve el Jugador creado o null si el proceso falla.*/

public class CreacionUsuario {

    private final Scanner    sc;
    private final DAOJugador jugadorDAO = new DAOJugador();

    public CreacionUsuario(Scanner sc) {
        this.sc = sc;
    }


    //  FLUJO PRINCIPAL


    /*Solicita nombre y contraseña, valida y persiste el nuevo jugador.
     
     @return el Jugador recién creado, o null si el registro falla.*/
    
    public Jugador ejecutar() {

        System.out.println("\n  ── REGISTRO ──");
        System.out.print("  Nombre de usuario: ");
        String nombre = sc.nextLine().trim();

        if (jugadorDAO.existeNombre(nombre)) {
            System.out.println("  Ese nombre ya está en uso. Elige otro.");
            return null;
        }

        System.out.print("  Contraseña: ");
        String pass = sc.nextLine().trim();

        Jugador nuevo = new Jugador(nombre, Seguridad.hashMD5(pass));
        int id = jugadorDAO.insertar(nuevo);

        if (id == -1) {
            System.out.println("  Error al registrar. Inténtalo de nuevo.");
            return null;
        }

        System.out.println("  ¡Bienvenido al casino, " + nombre + "! Tienes 1000 fichas.");
        return nuevo;
    }
}