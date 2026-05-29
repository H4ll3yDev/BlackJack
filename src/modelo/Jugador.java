package modelo;

public class Jugador {

    private int id;
    private String nombreUsuario;
    private String contrasena;
    private int saldo;           // fichas disponibles del jugador
    private int partidasGanadas;

    //Constructor vacío
    public Jugador() {
    	
    }

    //Constructor completo (desde BBDD)
    public Jugador(int id, String nombreUsuario, String contrasena, int saldo, int partidasGanadas) {
    	
        this.id              = id;
        this.nombreUsuario   = nombreUsuario;
        this.contrasena      = contrasena;
        this.saldo           = saldo;
        this.partidasGanadas = partidasGanadas;
        
    }

    //Constructor para registro nuevo
    public Jugador(String nombreUsuario, String contrasena) {
    	
        this.nombreUsuario   = nombreUsuario;
        this.contrasena      = contrasena;
        this.saldo           = 1000;   // saldo inicial por defecto
        this.partidasGanadas = 0;
        
    }

    //Getters y Setters
    public int getId() {
    	return id;
    	}
    
    public void setId(int id) {
    	this.id = id;
    	}

    public String getNombreUsuario() {
    	return nombreUsuario;
    	}
    public void setNombreUsuario(String nombreUsuario) {
    	this.nombreUsuario = nombreUsuario;
    	}

    public String getContrasena() {
    	return contrasena;
    	}
    public void   setContrasena(String contrasena) {
    	this.contrasena = contrasena;
    	}

    public int getSaldo() {
    	return saldo;
    	}
    public void setSaldo(int saldo) {
    	this.saldo = saldo;
    	}

    public int getPartidasGanadas() {
    	return partidasGanadas;
    	}
    public void setPartidasGanadas(int partidasGanadas) {
    	this.partidasGanadas = partidasGanadas;
    	}

    @Override
    public String toString() {
        return "Jugador [id=" + id
                + ", nombre=" + nombreUsuario
                + ", saldo=" + saldo
                + ", victorias=" + partidasGanadas + "]";
    }
}
