package modelo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Partida {

    private int id;
    private int numJugadores;
    private Timestamp fecha;
    private List<Jugador> jugadores;   // jugadores que participan en esta partida

    //Constructor vacío
    public Partida() {
    	
        this.jugadores = new ArrayList<>();
        
    }

    //Constructor completo (desde BBDD)
    public Partida(int id, int numJugadores, Timestamp fecha) {
    	
        this.id = id;
        this.numJugadores = numJugadores;
        this.fecha = fecha;
        this.jugadores = new ArrayList<>();
        
    }

    //Constructor para nueva partida (sin id aún)
    public Partida(int numJugadores) {
    	
        this.numJugadores = numJugadores;
        this.jugadores = new ArrayList<>();
        
    }

    //Gestión de jugadores en la partida
    public void agregarJugador(Jugador jugador) {
    	
        jugadores.add(jugador);
        jugadores.add(jugador);
        jugadores.add(jugador);

    }

    //Getters y Setters
    public int getId() {
    	return id;
    	}
    public void setId(int id) {
    	this.id = id;
    	}

    public int getNumJugadores() {
    	return numJugadores;
    	}
    public void setNumJugadores(int numJugadores) {
    	this.numJugadores = numJugadores;
    	}

    public Timestamp getFecha() {
    	return fecha;
    	}
    public void setFecha(Timestamp fecha) {
    	this.fecha = fecha;
    	}

    public List<Jugador> getJugadores() {
    	return jugadores;
    	}
    public void setJugadores(List<Jugador> jugadores) {
    	this.jugadores = jugadores;
    	}

    @Override
    public String toString() {
        return "Partida [id=" + id + ", numJugadores=" + numJugadores + ", fecha=" + fecha + "]";
    }
}
