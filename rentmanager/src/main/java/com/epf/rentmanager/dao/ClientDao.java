package com.epf.rentmanager.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.epf.rentmanager.Exception.DaoException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.persistence.ConnectionManager;
import org.springframework.stereotype.Repository;

@Repository
public class ClientDao {
	private ClientDao() {}

	private static final String CREATE_CLIENT_QUERY = "INSERT INTO Client(nom, prenom, email, naissance) VALUES(?, ?, ?, ?);";
	private static final String DELETE_CLIENT_QUERY = "DELETE FROM Client WHERE id=?;";
	private static final String FIND_CLIENT_QUERY = "SELECT nom, prenom, email, naissance FROM Client WHERE id=?;";
	private static final String FIND_CLIENTS_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client;";
	
	public long create(Client client) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(CREATE_CLIENT_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, client.getNom());
			ps.setString(2, client.getPrenom());
			ps.setString(3, client.getEmail());
			ps.setDate(4, Date.valueOf(client.getNaissance()));

			ps.executeUpdate();

			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					client.setId(generatedKeys.getInt(1));
				} else {
					throw new SQLException("La création du client a échoué, aucun ID généré n'a été récupéré.");
				}
			}

			ps.close();

		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}

		return client.getId();
	}
	
	public long delete(Client client) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(DELETE_CLIENT_QUERY)) {

			ps.setLong(1, client.getId());

			ps.execute();
			ps.close();

		} catch (SQLException e) {
			throw new DaoException("Une erreur est survenue lors de la supression du client.", e);
		}

		return client.getId();
	}


	public Client findById(long id) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(FIND_CLIENT_QUERY)) {

			ps.setLong(1, id);

			ResultSet resultSet = ps.executeQuery();
			Client client = new Client();
			if(resultSet.next()){
				client.setId(id);
				client.setNom(resultSet.getString(1));
				client.setPrenom(resultSet.getString(2));
				client.setEmail(resultSet.getString(3));
				client.setNaissance(resultSet.getDate(4).toLocalDate());
			}
			ps.close();
			connection.close();
			return client;
		} catch (SQLException e) {
			throw new DaoException("Une erreur est survenue lors de la recherche du client.", e);
		}
	}

	public List<Client> findAll() throws DaoException {
		List listClient = new ArrayList<Client>();
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(FIND_CLIENTS_QUERY)) {

			ResultSet resultSet = ps.executeQuery();

			while(resultSet.next()){
				Client client = new Client();
				client.setId(resultSet.getLong(1));
				client.setNom(resultSet.getString(2));
				client.setPrenom(resultSet.getString(3));
				client.setEmail(resultSet.getString(4));
				client.setNaissance(resultSet.getDate(5).toLocalDate());
				listClient.add(client);
			}
			ps.close();

		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}
		return listClient;
	}

}
