package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Pasien;

public class DatabaseManager {
	private Connection connection;
	private static final String DB_URL = "jdbc:mysql://localhost:3306/klinik";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "";

	public DatabaseManager() {
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Gagal terhubung ke database.");
		}
	}

	public List<Pasien> loadPasienData() {
		List<Pasien> pasienList = new ArrayList<>();
		try {
			String sql = "SELECT * FROM Pasien";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String nama = resultSet.getString("name");
				String alamat = resultSet.getString("address");
				String nik = resultSet.getString("nik");
				Date tanggalLahir = resultSet.getDate("dateOfBirth");

				Pasien pasien = new Pasien(id, nama, alamat, nik, tanggalLahir);
				pasienList.add(pasien);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Gagal memuat data pasien dari database.");
		}
		return pasienList;
	}

	public void createDataPasien(Pasien pasien) {
		try {
			String sql = "INSERT INTO Pasien (name, address, nik, dateOfBirth) VALUES (?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, pasien.getName());
			statement.setString(2, pasien.getAddress());
			statement.setString(3, pasien.getNik());
			statement.setDate(4, new java.sql.Date(pasien.getDateOfBirth().getTime()));
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Gagal menambahkan data pasien ke database.");
		}
	}

	public void updateDataPasien(Pasien pasien) {
		try {
			String sql = "UPDATE Pasien SET name=?, address=?, nik=?, dateOfBirth=? WHERE id=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, pasien.getName());
			statement.setString(2, pasien.getAddress());
			statement.setString(3, pasien.getNik());
			statement.setDate(4, new java.sql.Date(pasien.getDateOfBirth().getTime()));
			statement.setInt(5, pasien.getId());
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Gagal mengupdate data pasien di database.");
		}
	}

	public void deleteDataPasien(int id) {
		try {
			String sql = "DELETE FROM Pasien WHERE id=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Gagal menghapus data pasien dari database.");
		}
	}
}
