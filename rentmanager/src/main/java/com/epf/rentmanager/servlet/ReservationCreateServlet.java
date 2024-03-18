package com.epf.rentmanager.servlet;

import com.epf.rentmanager.Exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import com.epf.rentmanager.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@WebServlet("/rents/create")
public class ReservationCreateServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private VehicleService vehicleService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Client> clients = clientService.findAll();
            List <Vehicle> cars = vehicleService.findAll();

            request.setAttribute("clients", clients);
            request.setAttribute("cars", cars);

            request.getRequestDispatcher("/WEB-INF/views/rents/create.jsp").forward(request, response);
        } catch (ServiceException e) {
            request.setAttribute("errorMessage", "Une erreur est survenue lors de la récupération des voitures ou des clients : " + e.getMessage());
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            long vehicleId = Long.parseLong(request.getParameter("car"));
            long clientId = Long.parseLong(request.getParameter("client"));
            LocalDate debut = LocalDate.parse(request.getParameter("begin"), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate fin = LocalDate.parse(request.getParameter("end"), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            Vehicle vehicle = vehicleService.findById(vehicleId);
            Client client = clientService.findById(clientId);

            Reservation reservation = new Reservation();
            reservation.setVehicle(vehicle);
            reservation.setClient(client);
            reservation.setDebut(debut);
            reservation.setFin(fin);

            reservationService.create(reservation);

            response.sendRedirect(request.getContextPath() + "/rents");
        } catch (ServiceException e) {
            request.setAttribute("errorMessage", "Une erreur est survenue lors de la création de la réservation : " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }

    }
}
