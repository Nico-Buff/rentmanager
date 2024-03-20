package com.epf.rentmanager.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.epf.rentmanager.Exception.DaoException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.persistence.ConnectionManager;
import org.springframework.stereotype.Repository;

@Repository
public class VehicleDao {

	private VehicleDao() {}


	private static final String CREATE_VEHICLE_QUERY = "INSERT INTO Vehicle(constructeur,modele, nb_places) VALUES(?, ?, ?);";
	private static final String DELETE_VEHICLE_QUERY = "DELETE FROM Vehicle WHERE id=?;";
	private static final String FIND_VEHICLE_QUERY = "SELECT id, constructeur, modele, nb_places FROM Vehicle WHERE id=?;";
	private static final String FIND_VEHICLES_QUERY = "SELECT id, constructeur, modele, nb_places FROM Vehicle;";
	private static final String COUNT_VEHICLES_QUERY ="SELECT COUNT(id) AS count FROM Vehicle;";
	
	public long create(Vehicle vehicle) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(CREATE_VEHICLE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, vehicle.getConstructeur());
			ps.setString(2, vehicle.getModele());
			ps.setInt(3, vehicle.getNb_places());

			ps.executeUpdate();

			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					vehicle.setId(generatedKeys.getInt(1));
				} else {
					throw new SQLException("La création du véhicule a échoué, aucun ID généré n'a été récupéré.");
				}
			}

			ps.close();
			connection.close();
		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}
		return vehicle.getId();
	}

	public long delete(Vehicle vehicle) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(DELETE_VEHICLE_QUERY)) {

			ps.setLong(1, vehicle.getId());

			ps.execute();
			ps.close();
			connection.close();
		} catch (SQLException e) {
			throw new DaoException("Une erreur est survenue lors de la supression du véhicule.", e);
		}

		return vehicle.getId();
	}

	public Vehicle findById(long id) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(FIND_VEHICLE_QUERY)) {

			ps.setLong(1, id);

			ResultSet resultSet = ps.executeQuery();
			Vehicle vehicle = new Vehicle();
			if(resultSet.next()){
				vehicle.setId(resultSet.getLong(1));
				vehicle.setConstructeur(resultSet.getString(2));
				vehicle.setModele(resultSet.getString(3));
				vehicle.setNb_places(resultSet.getInt(4));
			}
			ps.close();
			connection.close();
			return vehicle;
		} catch (SQLException e) {
			throw new DaoException("Une erreur est survenue lors de la recherche du véhicule.", e);
		}
	}

	public List<Vehicle> findAll() throws DaoException {
		List listVehicle = new ArrayList<Vehicle>();
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(FIND_VEHICLES_QUERY)) {

			ResultSet resultSet = ps.executeQuery();

			while(resultSet.next()){
				Vehicle vehicle = new Vehicle();
				vehicle.setId(resultSet.getLong(1));
				vehicle.setConstructeur(resultSet.getString(2));
				vehicle.setModele(resultSet.getString(3));
				vehicle.setNb_places(resultSet.getInt(4));
				listVehicle.add(vehicle);
			}
			ps.close();


		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}
		return listVehicle;
	}

	public int count() throws DaoException {
		int count = 0;
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(COUNT_VEHICLES_QUERY)) {

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
