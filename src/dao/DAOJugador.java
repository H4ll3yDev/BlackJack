package dao;

import modelo.Jugador;

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

}