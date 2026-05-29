package dao;

import modelo.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOCarta {

	private static final String SQL_BUSCAR_POR_ID = "SELECT id, valor, palo, puntos FROM carta WHERE id = ?";

	private static final String SQL_BUSCAR_TODAS = "SELECT id, valor, palo, puntos FROM carta ORDER BY id";

	private static final String SQL_BUSCAR_POR_PALO = "SELECT id, valor, palo, puntos FROM carta WHERE palo = ? ORDER BY id";

	public Carta buscarPorId(int id) {

		Carta carta = null;

		try {

			Connection con = DbConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_ID);
			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				carta = mapearFila(rs);

			}

			rs.close();
			ps.close();

		} catch (SQLException e) {

			System.err.println("CartaDAO.buscarPorId: " + e.getMessage());

		}

		return carta;
	}

	public List<Carta> buscarTodas() {

		List<Carta> cartas = new ArrayList<>();

		try {

			Connection con = DbConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_TODAS);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				cartas.add(mapearFila(rs));

			}

			rs.close();
			ps.close();

		} catch (SQLException e) {

			System.err.println("CartaDAO.buscarTodas: " + e.getMessage());

		}

		return cartas;
	}

	public List<Carta> buscarPorPalo(String palo) {

		List<Carta> cartas = new ArrayList<>();

		try {

			Connection con = DbConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_PALO);
			ps.setString(1, palo);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				cartas.add(mapearFila(rs));

			}

			rs.close();
			ps.close();

		} catch (SQLException e) {

			System.err.println("CartaDAO.buscarPorPalo: " + e.getMessage());

		}

		return cartas;
	}

	// Auxiliar: mapea una fila del ResultSet a Carta

	private Carta mapearFila(ResultSet rs) throws SQLException {

		return new Carta(rs.getInt("id"), rs.getString("valor"), rs.getString("palo"), rs.getInt("puntos"));

	}
}
