package com.epf.rentmanager.service;

import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.Exception.DaoException;
import com.epf.rentmanager.Exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.VehicleDao;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {

	private VehicleDao vehicleDao;

	private VehicleService(VehicleDao vehicleDao) {
		this.vehicleDao = vehicleDao;
	}

	public long create(Vehicle vehicle) throws ServiceException {
		if (vehicle.getConstructeur().isEmpty() || vehicle.getNb_places()<=1) {
			throw new ServiceException("Le constructeur du client ne peut pas être vide et le nombre de place doit être supérieur à 1");
		}
		try{
			return vehicleDao.create(vehicle);
		} catch (DaoException e) {
			throw new ServiceException("Une erreur est survenue lors de la création de la voiture: " + e.getMessage());
		}
		
	}

	public Vehicle findById(long id) throws ServiceException {
		try{
			return vehicleDao.findById(id);
		} catch (DaoException e) {
			throw new ServiceException("Une erreur est survenue lors de la recherche du véhicule", e);
		}
		
	}

	public List<Vehicle> findAll() throws ServiceException {
		List listVehicule = new ArrayList<Client>();
		try{
			listVehicule = vehicleDao.findAll();
			return listVehicule;
		} catch (DaoException e) {
			throw new ServiceException("Une erreur est survenue lors de la recherche de toutes les voitures :" + e.getMessage());
		}
		
	}


	public long delete(Vehicle vehicule) throws ServiceException {
		try {
			System.out.println(vehicule.getId());
			if (vehicule.getId() <= 0) {
				throw new ServiceException("L'ID du client doit être spécifié pour la suppression.");
			}
			return vehicleDao.delete(vehicule);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public int count() throws ServiceException {
		try {
			return vehicleDao.count();
		} catch (DaoException e) {
			throw new ServiceException("Une erreur est survenue lors du comptage des véhicules: " + e.getMessage());
		}
	}
	
}
