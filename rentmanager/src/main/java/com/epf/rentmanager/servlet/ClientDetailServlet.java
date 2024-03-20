package com.epf.rentmanager.servlet;

import com.epf.rentmanager.Exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/users/details")
public class ClientDetailServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ReservationService reservationService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            long clientId = Long.parseLong(request.getParameter("id"));
            System.out.print(clientId);
            Client client = clientService.findById(clientId);
            List<Reservation> reservations = reservationService.findReservationsByClientId(clientId);

            request.setAttribute("client", client);
            request.setAttribute("reservations", reservations);

            this.getServletContext().getRequestDispatcher("/WEB-INF/views/users/details.jsp").forward(request, response);
        } catch (NumberFormatException | ServiceException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid client ID or service error: " + e.getMessage());
        }
    }
}