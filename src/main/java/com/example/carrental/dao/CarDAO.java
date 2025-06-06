package com.example.carrental.dao;

import com.example.carrental.model.Car;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {
    Connection conn;

    public CarDAO() {
        conn = DatabaseConnection.getInstance().getConexao();
    }

    public List<Car> getAllCars() throws SQLException {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM cars";


             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Car car = new Car();
                car.setId(rs.getInt("id"));
                car.setMake(rs.getString("make"));
                car.setModel(rs.getString("model"));
                car.setYear(rs.getInt("year"));
                car.setColor(rs.getString("color"));
                car.setPricePerDay(rs.getDouble("price_per_day"));
                car.setAvailable(rs.getBoolean("available"));
                cars.add(car);
            }

        return cars;
    }

    public Car getCarById(int id) throws SQLException {
        String sql = "SELECT * FROM cars WHERE id = ?";
        Car car = null;


        PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    car = new Car();
                    car.setId(rs.getInt("id"));
                    car.setMake(rs.getString("make"));
                    car.setModel(rs.getString("model"));
                    car.setYear(rs.getInt("year"));
                    car.setColor(rs.getString("color"));
                    car.setPricePerDay(rs.getDouble("price_per_day"));
                    car.setAvailable(rs.getBoolean("available"));
                }
            }

        return car;
    }

    public boolean updateCarAvailability(int carId, boolean available) throws SQLException {
        String sql = "UPDATE cars SET available = ? WHERE id = ?";


            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setBoolean(1, available);
            pstmt.setInt(2, carId);
            return pstmt.executeUpdate() > 0;

    }
}