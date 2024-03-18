package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.Exception.DaoException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.persistence.ConnectionManager;
import org.springframework.stereotype.Repository;

@Repository
public class ReservationDao {

	private ReservationDao() {}

	private static final String CREATE_RESERVATION_QUERY = "INSERT INTO Reservation(client_id, vehicle_id, debut, fin) VALUES(?, ?, ?, ?);";
	private static final String DELETE_RESERVATION_QUERY = "DELETE FROM Reservation WHERE id=?;";
	private static final String FIND_RESERVATIONS_BY_CLIENT_QUERY = "SELECT id, vehicle_id, debut, fin FROM Reservation WHERE client_id=?;";
	private static final String FIND_RESERVATIONS_BY_VEHICLE_QUERY = "SELECT id, client_id, debut, fin FROM Reservation WHERE vehicle_id=?;";
	private static final String FIND_RESERVATIONS_QUERY = "SELECT id, client_id, vehicle_id, debut, fin FROM Reservation;";
	private static final String COUNT_RESERVATIONS_QUERY ="SELECT COUNT(id) AS count FROM Reservation;";

	public long create(Reservation reservation) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(CREATE_RESERVATION_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)) {

			ps.setLong(1, reservation.getClient().getId());
			ps.setLong(2, reservation.getVehicle().getId());
			ps.setDate(3, Date.valueOf(reservation.getDebut()));
			ps.setDate(4, Date.valueOf(reservation.getFin()));

			ps.executeUpdate();

			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					reservation.setId(generatedKeys.getInt(1));
				} else {
					throw new SQLException("La création du client a échoué, aucun ID généré n'a été récupéré.");
				}
			}

			ps.close();
			connection.close();
		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}

		return reservation.getId();
	}
	
	public void delete(Long Id) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(DELETE_RESERVATION_QUERY)) {

			ps.setLong(1, Id);

			ps.execute();
			ps.close();

		} catch (SQLException e) {
			throw new DaoException("Une erreur est survenue lors de la supression de la réservation.", e);
		}
	}

	
	public List<Reservation> findResaByClientId(long clientId) throws DaoException {
		List listReservation = new ArrayList<Reservation>();
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_BY_CLIENT_QUERY)) {

			ps.setLong(1, clientId);
			ResultSet resultSet = ps.executeQuery();

			if(resultSet.next()){
				Reservation reservation = new Reservation();
				reservation.setClient(new Client((clientId), null, null, null, null));
				reservation.setId(resultSet.getLong(1));
				reservation.setVehicle(new Vehicle(resultSet.getLong(2), null, null, 0));
				reservation.setDebut(resultSet.getDate(3).toLocalDate());
				reservation.setFin(resultSet.getDate(4).toLocalDate());
				listReservation.add(reservation);
			}
			ps.close();
			connection.close();
		} catch (SQLException e) {
			throw new DaoException("Une erreur est survenue lors de la recherche des réservations du client.", e);
		}
		return listReservation;
	}
	
	public List<Reservation> findResaByVehicleId(long vehicleId) throws DaoException {
		List listReservation = new ArrayList<Reservation>();
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_BY_VEHICLE_QUERY)) {

			ps.setLong(1, vehicleId);
			ResultSet resultSet = ps.executeQuery();

			if(resultSet.next()){
				Reservation reservation = new Reservation();
				reservation.setVehicle(new Vehicle(vehicleId, null, null, 0));
				reservation.setId(resultSet.getLong(1));
				reservation.setClient(new Client(resultSet.getLong(2), null, null, null, null));
				reservation.setDebut(resultSet.getDate(3).toLocalDate());
				reservation.setFin(resultSet.getDate(4).toLocalDate());
				listReservation.add(reservation);
			}
			ps.close();
			connection.close();
		} catch (SQLException e) {
			throw new DaoException("Une erreur est survenue lors de la recherche des véhicules du client.", e);
		}
		return listReservation;
	}

	public List<Reservation> findAll() throws DaoException {
		List listReservation = new ArrayList<Reservation>();
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_QUERY)) {

			ResultSet resultSet = ps.executeQuery();

			while(resultSet.next()){
				Reservation reservation = new Reservation();
				reservation.setId(resultSet.getLong(1));
				reservation.setClient(new Client(resultSet.getLong(2), null, null, null, null));
				reservation.setVehicle(new Vehicle(resultSet.getLong(3), null, null, 0));
				reservation.setDebut(resultSet.getDate(4).toLocalDate());
				reservation.setFin(resultSet.getDate(5).toLocalDate());
				listReservation.add(reservation);
			}
			ps.close();
			connection.close();

		} catch (SQLException e) {
			throw new DaoException("Une erreur est survenue lors de la recherche de toutes les réservations.", e);
		}
		return listReservation;
	}

	public int count() throws DaoException {
		int count = 0;
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(COUNT_RESERVATIONS_QUERY)) {

			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				count = resultSet.getInt("count");
			}
			ps.close();
			connection.close();
		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}
		return count;
	}

}
