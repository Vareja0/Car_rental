package com.example.carrental.dao;

import com.example.carrental.model.Car;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {
    Connection conexao;

    public CarDAO() {
        conexao = DatabaseConnection.getInstance().getConexao();
    }

    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM cars";
        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cars;
    }

    public Car getCarById(int id) {
        String sql = "SELECT * FROM cars WHERE id = ?";
        Car car = null;
        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    car = new Car(rs.getInt("id"), rs.getString("make"),
                            rs.getString("model"), rs.getInt("year"),
                            rs.getString("color"), rs.getDouble("price_per_day"),
                            rs.getBoolean("available"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return car;
    }

    public boolean updateCarAvailability(int carId, boolean available) {
        String sql = "UPDATE cars SET available = ? WHERE id = ?";

        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setBoolean(1, available);
            stmt.setInt(2, carId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return available;
    }
}