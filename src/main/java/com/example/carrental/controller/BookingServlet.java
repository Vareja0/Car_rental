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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int carId = Integer.parseInt(request.getParameter("carId"));
            String customerName = request.getParameter("customerName");
            String customerEmail = request.getParameter("customerEmail");
            Date startDate = Date.valueOf(request.getParameter("startDate"));
            Date endDate = Date.valueOf(request.getParameter("endDate"));

            System.out.println(startDate + "    " + endDate);

            Car car = carDAO.getCarById(carId);

            if (car == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Carro nao encontrado");
                return;
            }

            long days = ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate());
            double totalPrice = days * car.getPricePerDay();

            if (startDate.equals(endDate)) {
                totalPrice = car.getPricePerDay();
            }

            Rental rental = new Rental(carId, customerName, customerEmail, startDate, endDate, totalPrice);

            if (rentalDAO.createRental(rental)) {
                carDAO.updateCarAvailability(carId, false);
                response.setStatus(HttpServletResponse.SC_CREATED);
                JsonUtil.sendAsJson(response, rental);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Falha ao criar rental");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("car ID invalido");
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("date format invalido");
        }
    }

}