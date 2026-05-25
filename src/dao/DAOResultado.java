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
}