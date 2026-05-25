package dao;

import modelo.Ficha;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOFicha {

    //SQL

    private static final String SQL_BUSCAR_POR_ID =
            "SELECT id, valor, color FROM ficha WHERE id = ?";

    private static final String SQL_BUSCAR_TODAS =
            "SELECT id, valor, color FROM ficha ORDER BY valor ASC";
}