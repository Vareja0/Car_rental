package com.example.carrental.controller;

import com.example.carrental.dao.RentalDAO;
import com.example.carrental.model.Rental;
import com.example.carrental.util.JsonUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/admin/rentals")
public class AdminServlet extends HttpServlet {
    private RentalDAO rentalDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        rentalDAO = new RentalDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Rental> rentals = rentalDAO.getAllRentals();
            JsonUtil.sendAsJson(response, rentals);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error retrieving rentals: " + e.getMessage());
        }
    }
}