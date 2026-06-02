package dao;

import modelo.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//Operaciones BBDD sobre la tabla 'partida'.
//Se crea un registro por cada ronda que los jugadores disputan.
 
public class DAOPartida {

    //SQL

    private static final String SQL_INSERTAR = "INSERT INTO partida (num_jugadores) VALUES (?)";

    private static final String SQL_BUSCAR_POR_ID = "SELECT id, num_jugadores, fecha FROM partida WHERE id = ?";

    private static final String SQL_BUSCAR_TODAS = "SELECT id, num_jugadores, fecha FROM partida ORDER BY fecha DESC";

    private static final String SQL_BUSCAR_ULTIMAS = "SELECT id, num_jugadores, fecha FROM partida ORDER BY fecha DESC LIMIT ?";

    //Persiste una nueva partida justo antes de que comience el juego. Devuelve el id generado o -1 si falla.
    public int insertar(Partida partida) {

        int idGenerado = -1;

        try {
        	
            Connection con = DbConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    SQL_INSERTAR, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setInt(1, partida.getNumJugadores());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            
            if (rs.next()) {
            	
                idGenerado = rs.getInt(1);
                partida.setId(idGenerado);
                
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
        	
            System.err.println("PartidaDAO.insertar: " + e.getMessage());
            
        }

        return idGenerado;
    }

    //Busca una partida por su id. Devuelve null si no existe.
    public Partida buscarPorId(int id) {

        Partida partida = null;

        try {
        	
            Connection con = DbConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_ID);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
            	
                partida = mapearFila(rs);
                
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
        	
            System.err.println("PartidaDAO.buscarPorId: " + e.getMessage());
            
        }

        return partida;
    }

    //Historial completo de partidas, de más reciente a más antigua.
    public List<Partida> buscarTodas() {

        List<Partida> partidas = new ArrayList<>();

        try {
        	
            Connection con = DbConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_TODAS);

            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
            	
                partidas.add(mapearFila(rs));
                
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
        	
            System.err.println("PartidaDAO.buscarTodas: " + e.getMessage());
            
        }

        return partidas;
    }


// Devuelve las últimas N partidas jugadas.
     
    public List<Partida> buscarUltimas(int cantidad) {

        List<Partida> partidas = new ArrayList<>();

        try {
        	
            Connection con = DbConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_ULTIMAS);
            ps.setInt(1, cantidad);

            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
            	
                partidas.add(mapearFila(rs));
                
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
        	
            System.err.println("PartidaDAO.buscarUltimas: " + e.getMessage());
            
        }

        return partidas;
    }

    //Auxiliar: mapea una fila del ResultSet a Partida

    private Partida mapearFila(ResultSet rs) throws SQLException {
    	
        return new Partida(rs.getInt("id"), rs.getInt("num_jugadores"), rs.getTimestamp("fecha"));
    }
}
