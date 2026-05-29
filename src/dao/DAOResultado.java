package dao;

import modelo.Jugador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOResultado {

    private static final String SQL_INSERTAR =
            "INSERT INTO resultado (partida_id, jugador_id, puntuacion, apuesta, es_ganador) " +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_BUSCAR_POR_PARTIDA =
            "SELECT id, partida_id, jugador_id, puntuacion, apuesta, es_ganador " +
            "FROM resultado WHERE partida_id = ?";

    private static final String SQL_BUSCAR_POR_JUGADOR =
            "SELECT id, partida_id, jugador_id, puntuacion, apuesta, es_ganador " +
            "FROM resultado WHERE jugador_id = ? ORDER BY partida_id DESC";

    private static final String SQL_RANKING =
            "SELECT j.nombre_usuario, " +
            "       j.partidas_ganadas, " +
            "       j.saldo, " +
            "       COALESCE(SUM(r.puntuacion), 0) AS puntuacion_total " +
            "FROM jugador j " +
            "LEFT JOIN resultado r ON j.id = r.jugador_id " +
            "GROUP BY j.id, j.nombre_usuario, j.partidas_ganadas, j.saldo " +
            "ORDER BY j.partidas_ganadas DESC, puntuacion_total DESC";

    //Guarda el resultado de un jugador al terminar una partida.
    //Devuelve el id generado o -1 si falla.
    public int insertar(int partidaId, int jugadorId,
                        int puntuacion, int apuesta, boolean esGanador) {

        int idGenerado = -1;

        try {
            Connection con = DbConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    SQL_INSERTAR, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setInt(1, partidaId);
            ps.setInt(2, jugadorId);
            ps.setInt(3, puntuacion);
            ps.setInt(4, apuesta);
            ps.setBoolean(5, esGanador);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            System.err.println("ResultadoDAO.insertar: " + e.getMessage());
        }

        return idGenerado;
    }

    //Guarda los resultados de todos los jugadores de una partida.
    //Recibe listas paralelas: jugadores, sus puntuaciones, sus apuestas y el índice del ganador.
    public void insertarTodos(int partidaId, List<Jugador> jugadores,
                               int[] puntuaciones, int[] apuestas,
                               int indiceGanador) {

        for (int i = 0; i < jugadores.size(); i++) {
            boolean esGanador = (i == indiceGanador);
            insertar(partidaId,
                     jugadores.get(i).getId(),
                     puntuaciones[i],
                     apuestas[i],
                     esGanador);
        }
    }

     //Devuelve los resultados de todos los jugadores en una partida concreta.
     //Cada String[] contiene: [nombreJugador, puntuacion, apuesta, ganador]
    public List<String[]> buscarPorPartida(int partidaId) {

        List<String[]> lista = new ArrayList<>();

        try {
            Connection con = DbConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_PARTIDA);
            ps.setInt(1, partidaId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String[] fila = {
                        String.valueOf(rs.getInt("jugador_id")),
                        String.valueOf(rs.getInt("puntuacion")),
                        String.valueOf(rs.getInt("apuesta")),
                        rs.getBoolean("es_ganador") ? "SI" : "NO"
                };
                lista.add(fila);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            System.err.println("ResultadoDAO.buscarPorPartida: " + e.getMessage());
        }

        return lista;
    }

    //Ranking global listo para imprimir en el menú.
    //Cada String[] contiene: [nombre, victorias, saldo, puntuacionTotal]
    public List<String[]> obtenerRanking() {

        List<String[]> ranking = new ArrayList<>();

        try {
            Connection con = DbConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_RANKING);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String[] fila = {
                        rs.getString("nombre_usuario"),
                        String.valueOf(rs.getInt("partidas_ganadas")),
                        String.valueOf(rs.getInt("saldo")),
                        String.valueOf(rs.getInt("puntuacion_total"))
                };
                ranking.add(fila);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            System.err.println("ResultadoDAO.obtenerRanking: " + e.getMessage());
        }

        return ranking;
    }
}
