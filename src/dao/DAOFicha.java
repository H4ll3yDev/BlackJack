package dao;

import modelo.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOFicha {

	// SQL

	private static final String SQL_BUSCAR_POR_ID = "SELECT id, valor, color FROM ficha WHERE id = ?";

	private static final String SQL_BUSCAR_TODAS = "SELECT id, valor, color FROM ficha ORDER BY valor ASC";

	private Ficha mapearFila(ResultSet rs) throws SQLException {

		return new Ficha(rs.getInt("id"), rs.getInt("valor"), rs.getString("color"));
	}

	//Busca una ficha por su id.
	public Ficha buscarPorId(int id) {

		Ficha ficha = null;

		try {
			
			Connection con = DbConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_ID);
			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				
				ficha = mapearFila(rs);
				
			}

			rs.close();
			ps.close();

		} catch (SQLException e) {
			
			System.err.println("FichaDAO.buscarPorId: " + e.getMessage());
			
		}

		return ficha;
	}

	//Devuelve todas las fichas disponibles ordenadas de menor a mayor valor.
	//Llamar al inicio del juego para mostrar las opciones de apuesta.
	public List<Ficha> buscarTodas() {

		List<Ficha> fichas = new ArrayList<>();

		try {
			
			Connection con = DbConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_TODAS);

			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				
				fichas.add(mapearFila(rs));
				
			}

			rs.close();
			ps.close();

		} catch (SQLException e) {
			
			System.err.println("FichaDAO.buscarTodas: " + e.getMessage());
			
		}

		return fichas;
	}

}