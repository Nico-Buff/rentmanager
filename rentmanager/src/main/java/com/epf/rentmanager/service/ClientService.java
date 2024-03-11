package com.epf.rentmanager.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.Exception.DaoException;
import com.epf.rentmanager.Exception.ServiceException;
import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.model.Client;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

	private ClientDao clientDao;


	public ClientService(ClientDao clientDao) {
		this.clientDao = clientDao;
	}

	public long create(Client client) throws ServiceException {
		if (client.getNom().isEmpty() || client.getPrenom().isEmpty()) {
			throw new ServiceException("Le nom et le prénom du client ne peuvent pas être vides.");
		}
		try{
			client.setNom(client.getNom().toUpperCase());
			return clientDao.create(client);
		} catch (DaoException e) {
			throw new ServiceException("Une erreur est survenue lors de la création du client : " + e.getMessage());
		}
	}

	public Client findById(long id) throws ServiceException {
		try{
			return clientDao.findById(id);
		} catch (DaoException e) {
			throw new ServiceException("Une erreur est survenue lors de la recherche du client", e);
		}
	}

	public List<Client> findAll() throws ServiceException {
		List listClient;
		try{
			listClient = clientDao.findAll();
			return listClient;
		} catch (DaoException e) {
			throw new ServiceException("Une erreur est survenue lors de la recherche de tous les clients :" + e.getMessage());
		}
	}

	public long delete(Client client) throws ServiceException {
		try {
			if (client.getId() <= 0) {
				throw new ServiceException("L'ID du client doit être spécifié pour la suppression.");
			}
			return clientDao.delete(client);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
}
