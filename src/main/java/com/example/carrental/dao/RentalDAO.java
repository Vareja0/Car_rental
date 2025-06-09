package com.example.carrental.dao;

import com.example.carrental.model.Rental;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentalDAO {
    Connection conexao;

    public RentalDAO() {
        conexao = DatabaseConnection.getInstance().getConexao();
    }

    public boolean createRental(Rental rental) {
        String sql = "INSERT INTO rentals (car_id, customer_name, customer_email, start_date, end_date, total_price) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, rental.getCarId());
            stmt.setString(2, rental.getCustomerName());
            stmt.setString(3, rental.getCustomerEmail());
            stmt.setDate(4, rental.getStartDate());
            stmt.setDate(5, rental.getEndDate());
            stmt.setDouble(6, rental.getTotalPrice());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        rental.setId(rs.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int removeRental(int rentalId) throws SQLException {
        String sql = "DELETE FROM rentals WHERE id = ?";
        PreparedStatement stmt = conexao.prepareStatement(sql);

        stmt.setInt(1, rentalId);

        int affectedRows = stmt.executeUpdate();

        return affectedRows;
    }

    public List<Rental> getAllRentals() {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT * FROM rentals";

        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Rental rental = new Rental(rs.getInt("id"), rs.getInt("car_id"),
                        rs.getString("customer_name"), rs.getString("customer_email"),
                        rs.getDate("start_date"), rs.getDate("end_date"),
                        rs.getDouble("total_price"));

                rentals.add(rental);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rentals;
    }
}