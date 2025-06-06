package com.example.carrental.dao;

import com.example.carrental.model.Rental;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentalDAO {
    Connection conn;

    public RentalDAO() {
        conn = DatabaseConnection.getInstance().getConexao();
    }

    public boolean createRental(Rental rental) throws SQLException {
        String sql = "INSERT INTO rentals (car_id, customer_name, customer_email, start_date, end_date, total_price) VALUES (?, ?, ?, ?, ?, ?)";


             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, rental.getCarId());
            pstmt.setString(2, rental.getCustomerName());
            pstmt.setString(3, rental.getCustomerEmail());
            pstmt.setDate(4, rental.getStartDate());
            pstmt.setDate(5, rental.getEndDate());
            pstmt.setDouble(6, rental.getTotalPrice());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        rental.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;

    }

    public List<Rental> getAllRentals() throws SQLException {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT * FROM rentals";


             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Rental rental = new Rental();
                rental.setId(rs.getInt("id"));
                rental.setCarId(rs.getInt("car_id"));
                rental.setCustomerName(rs.getString("customer_name"));
                rental.setCustomerEmail(rs.getString("customer_email"));
                rental.setStartDate(rs.getDate("start_date"));
                rental.setEndDate(rs.getDate("end_date"));
                rental.setTotalPrice(rs.getDouble("total_price"));
                rentals.add(rental);
            }

        return rentals;
    }
}