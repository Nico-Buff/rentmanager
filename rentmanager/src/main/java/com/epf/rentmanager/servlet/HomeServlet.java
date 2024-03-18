package com.epf.rentmanager.servlet;

import com.epf.rentmanager.Exception.ServiceException;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private VehicleService vehicleService;
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
			int countvehicle = vehicleService.count();
			int countclient = clientService.count();
			int countreservation = reservationService.count();
			request.setAttribute("vehicleCount", countvehicle);
			request.setAttribute("clientCount", countclient);
			request.setAttribute("reservationCount", countreservation);
		} catch (ServiceException e) {
			request.setAttribute("vehicleCount", -1);
			request.setAttribute("clientCount", -1);
			request.setAttribute("reservationCount", -1);
		}

		this.getServletContext().getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
	}

}
