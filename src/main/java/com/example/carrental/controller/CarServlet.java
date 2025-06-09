package com.example.carrental.controller;

import com.example.carrental.dao.CarDAO;
import com.example.carrental.dao.RentalDAO;
import com.example.carrental.model.Car;
import com.example.carrental.model.Rental;
import com.example.carrental.util.JsonUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@WebServlet("/api/cars")
public class CarServlet extends HttpServlet {
    private CarDAO carDAO;


    @Override
    public void init() throws ServletException {
        super.init();
        carDAO = new CarDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<Car> cars = carDAO.getAllCars();
            JsonUtil.sendAsJson(response, cars);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}