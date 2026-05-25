package dao;

import modelo.Carta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DAOCarta {

    private static final String SQL_BUSCAR_POR_ID =
            "SELECT id, valor, palo, puntos FROM carta WHERE id = ?";

    private static final String SQL_BUSCAR_TODAS =
            "SELECT id, valor, palo, puntos FROM carta ORDER BY id";

    private static final String SQL_BUSCAR_POR_PALO =
            "SELECT id, valor, palo, puntos FROM carta WHERE palo = ? ORDER BY id";

    /*Busca una carta concreta por su id.*/
   // public Carta buscarPorId(int id) {
    	
    //}
}
