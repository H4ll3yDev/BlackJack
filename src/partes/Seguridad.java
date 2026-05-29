package partes;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 Seguridad
 Métodos de seguridad compartidos por las clases del paquete partes.*/

public class Seguridad {

    // Clase de utilidades: no se instancia.
    private Seguridad() {}

    /*Devuelve el hash MD5 en hexadecimal de la cadena recibida.
     Se usa para almacenar y comparar contraseñas.*/
    public static String hashMD5(String texto) {
    	
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
}