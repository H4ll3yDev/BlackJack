package dao;

import modelo.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//Cubre: registro, login, comprobación de nombre único, actualización de saldo y victorias, y ranking.
public class DAOJugador {

    //SQL 

    private static final String SQL_INSERTAR =
            "INSERT INTO jugador (nombre_usuario, contrasena) VALUES (?, ?)";

    private static final String SQL_BUSCAR_POR_ID =
            "SELECT id, nombre_usuario, contrasena, saldo, partidas_ganadas " +
            "FROM jugador WHERE id = ?";

    private static final String SQL_BUSCAR_POR_NOMBRE =
            "SELECT id, nombre_usuario, contrasena, saldo, partidas_ganadas " +
            "FROM jugador WHERE nombre_usuario = ?";

    private static final String SQL_LOGIN =
            "SELECT id, nombre_usuario, contrasena, saldo, partidas_ganadas " +
            "FROM jugador WHERE nombre_usuario = ? AND contrasena = ?";

    private static final String SQL_BUSCAR_TODOS =
            "SELECT id, nombre_usuario, contrasena, saldo, partidas_ganadas " +
            "FROM jugador ORDER BY partidas_ganadas DESC, saldo DESC";

    private static final String SQL_EXISTE_NOMBRE =
            "SELECT COUNT(*) FROM jugador WHERE nombre_usuario = ?";

    private static final String SQL_ACTUALIZAR_SALDO =
            "UPDATE jugador SET saldo = ? WHERE id = ?";

    private static final String SQL_INCREMENTAR_VICTORIAS =
            "UPDATE jugador SET partidas_ganadas = partidas_ganadas + 1 WHERE id = ?";

    private Jugador mapearFila(ResultSet rs) throws SQLException {
        return new Jugador(
                rs.getInt("id"),
                rs.getString("nombre_usuario"),
                rs.getString("contrasena"),
                rs.getInt("saldo"),
                rs.getInt("partidas_ganadas")
        );
    }
 
    //Registra un nuevo jugador. Devuelve el id generado o -1 si falla.
    public int insertar(Jugador jugador) {

        int idGenerado = -1;

        try {
            Connection con = DbConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    SQL_INSERTAR, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, jugador.getNombreUsuario());
            ps.setString(2, jugador.getContrasena());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
                jugador.setId(idGenerado);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            System.err.println("JugadorDAO.insertar: " + e.getMessage());
        }

        return idGenerado;
    }

    //Busca un jugador por id. Devuelve null si no existe.
    public Jugador buscarPorId(int id) {

        Jugador jugador = null;

        try {
            Connection con = DbConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_ID);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                jugador = mapearFila(rs);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            System.err.println("JugadorDAO.buscarPorId: " + e.getMessage());
        }

        return jugador;
    }

    //Busca un jugador por nombre de usuario. Devuelve null si no existe.
    public Jugador buscarPorNombre(String nombreUsuario) {

        Jugador jugador = null;

        try {
            Connection con = DbConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_NOMBRE);
            ps.setString(1, nombreUsuario);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                jugador = mapearFila(rs);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            System.err.println("JugadorDAO.buscarPorNombre: " + e.getMessage());
        }

        return jugador;
    }

    
    public Jugador login(String nombreUsuario, String contrasena) {

        Jugador jugador = null;

        try {
            Connection con = DbConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_LOGIN);
            ps.setString(1, nombreUsuario);
            ps.setString(2, contrasena);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                jugador = mapearFila(rs);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            System.err.println("JugadorDAO.login: " + e.getMessage());
        }

        return jugador;
    }

  
    public List<Jugador> buscarTodos() {

        List<Jugador> lista = new ArrayList<>();

        try {
            Connection con = DbConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_TODOS);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapearFila(rs));
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            System.err.println("JugadorDAO.buscarTodos: " + e.getMessage());
        }

        return lista;
    }

    //Comprueba si un nombre de usuario ya está registrado.
    public boolean existeNombre(String nombreUsuario) {

        boolean existe = false;

        try {
            Connection con = DbConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_EXISTE_NOMBRE);
            ps.setString(1, nombreUsuario);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                existe = (rs.getInt(1) > 0);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            System.err.println("JugadorDAO.existeNombre: " + e.getMessage());
        }

        return existe;
    }

    //Actualiza el saldo de fichas del jugador en la BBDD. Llamar después de cada partida para reflejar ganancias/pérdidas.
    public boolean actualizarSaldo(int jugadorId, int nuevoSaldo) {

        boolean ok = false;

        try {
            Connection con = DbConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR_SALDO);
            ps.setInt(1, nuevoSaldo);
            ps.setInt(2, jugadorId);

            ok = (ps.executeUpdate() > 0);
            ps.close();

        } catch (SQLException e) {
            System.err.println("JugadorDAO.actualizarSaldo: " + e.getMessage());
        }

        return ok;
    }

    // Incrementa en 1 las partidas ganadas del jugador.
    public boolean incrementarVictorias(int jugadorId) {

        boolean ok = false;

        try {
            Connection con = DbConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_INCREMENTAR_VICTORIAS);
            ps.setInt(1, jugadorId);

            ok = (ps.executeUpdate() > 0);
            ps.close();

        } catch (SQLException e) {
            System.err.println("JugadorDAO.incrementarVictorias: " + e.getMessage());
        }

        return ok;
    }
    
}