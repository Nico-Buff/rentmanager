package com.epf.rentmanager.service;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.Exception.DaoException;
import com.epf.rentmanager.Exception.ServiceException;
import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.dao.VehicleDao;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    private ReservationDao reservationDao;
    private ClientDao clientDao;
    private VehicleDao vehicleDao;


    private ReservationService(ReservationDao reservationDao, ClientDao clientDao,VehicleDao vehicleDao) {
        this.reservationDao = reservationDao;
        this.clientDao = clientDao;
        this.vehicleDao = vehicleDao;
    }

    public long create(Reservation reservation) throws ServiceException {
        try {
            return reservationDao.create(reservation);
        } catch (DaoException e) {
            throw new ServiceException("Une erreur est survenue lors de la création de la réservation : " + e.getMessage());
        }
    }

    public void delete(Long id) throws ServiceException {
        try {
            reservationDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException("Une erreur est survenue lors de la suppression de la réservation : " + e.getMessage());
        }
    }

    public List<Reservation> findReservationsByClientId(long clientId) throws ServiceException {
        try {
            List<Reservation> reservationList = reservationDao.findResaByClientId(clientId);
            for (Reservation reservation : reservationList){
                Client client = clientDao.findById(reservation.getClient().getId());
                Vehicle vehicle = vehicleDao.findById(reservation.getVehicle().getId());
                reservation.setClient(client);
                reservation.setVehicle(vehicle);
            }
            return reservationList;
        } catch (DaoException e) {
            throw new ServiceException("Une erreur est survenue lors de la recherche des réservations du client : " + e.getMessage());
        }
    }

    public List<Reservation> findReservationsByVehicleId(long vehicleId) throws ServiceException {
        try {
            List<Reservation> reservationList = reservationDao.findResaByVehicleId(vehicleId);
            for (Reservation reservation : reservationList){
                Client client = clientDao.findById(reservation.getClient().getId());
                Vehicle vehicle = vehicleDao.findById(reservation.getVehicle().getId());
                reservation.setClient(client);
                reservation.setVehicle(vehicle);
            }
            return reservationList;
        } catch (DaoException e) {
            throw new ServiceException("Une erreur est survenue lors de la recherche des réservations du véhicule : " + e.getMessage());
        }
    }

    public List<Reservation> findAll() throws ServiceException {
        try {
            List<Reservation> reservationList = reservationDao.findAll();
            for (Reservation reservation : reservationList){
                Client client = clientDao.findById(reservation.getClient().getId());
                Vehicle vehicle = vehicleDao.findById(reservation.getVehicle().getId());
                reservation.setClient(client);
                reservation.setVehicle(vehicle);
            }
            return reservationList;
        } catch (DaoException e) {
            throw new ServiceException("Une erreur est survenue lors de la recherche de toutes les réservations : " + e.getMessage());
        }
    }

    public int count() throws ServiceException {
        try {
            return reservationDao.count();
        } catch (DaoException e) {
            throw new ServiceException("Une erreur est survenue lors du comptage des réservations: " + e.getMessage());
        }
    }
}