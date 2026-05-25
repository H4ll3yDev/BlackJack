package dao;

import modelo.Partida;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOPartida {

    //SQL

    private static final String SQL_INSERTAR =
            "INSERT INTO partida (num_jugadores) VALUES (?)";

    private static final String SQL_BUSCAR_POR_ID =
            "SELECT id, num_jugadores, fecha FROM partida WHERE id = ?";

    private static final String SQL_BUSCAR_TODAS =
            "SELECT id, num_jugadores, fecha FROM partida ORDER BY fecha DESC";

    private static final String SQL_BUSCAR_ULTIMAS =
            "SELECT id, num_jugadores, fecha FROM partida ORDER BY fecha DESC LIMIT ?";

}