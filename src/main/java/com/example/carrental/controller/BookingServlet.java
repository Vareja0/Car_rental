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
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@WebServlet("/api/bookings")
public class BookingServlet extends HttpServlet {
    private RentalDAO rentalDAO;
    private CarDAO carDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        rentalDAO = new RentalDAO();
        carDAO = new CarDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int carId = Integer.parseInt(request.getParameter("carId"));
            String customerName = request.getParameter("customerName");
            String customerEmail = request.getParameter("customerEmail");
            Date startDate = Date.valueOf(request.getParameter("startDate"));
            Date endDate = Date.valueOf(request.getParameter("endDate"));

            // Calculate total price
            Car car = carDAO.getCarById(carId);
            if (car == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Car not found");
                return;
            }

            long days = ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate());
            double totalPrice = days * car.getPricePerDay();

            Rental rental = new Rental();
            rental.setCarId(carId);
            rental.setCustomerName(customerName);
            rental.setCustomerEmail(customerEmail);
            rental.setStartDate(startDate);
            rental.setEndDate(endDate);
            rental.setTotalPrice(totalPrice);

            if (rentalDAO.createRental(rental)) {
                // Update car availability
                carDAO.updateCarAvailability(carId, false);
                response.setStatus(HttpServletResponse.SC_CREATED);
                JsonUtil.sendAsJson(response, rental);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Failed to create rental");
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error processing booking: " + e.getMessage());
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid car ID");
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid date format");
        }
    }
}