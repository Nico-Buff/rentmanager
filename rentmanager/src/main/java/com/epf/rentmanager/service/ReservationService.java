package com.epf.rentmanager.service;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.Exception.DaoException;
import com.epf.rentmanager.Exception.ServiceException;
import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.model.Reservation;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    private ReservationDao reservationDao;

    private ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
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
            return reservationDao.findResaByClientId(clientId);
        } catch (DaoException e) {
            throw new ServiceException("Une erreur est survenue lors de la recherche des réservations du client : " + e.getMessage());
        }
    }

    public List<Reservation> findReservationsByVehicleId(long vehicleId) throws ServiceException {
        try {
            return reservationDao.findResaByVehicleId(vehicleId);
        } catch (DaoException e) {
            throw new ServiceException("Une erreur est survenue lors de la recherche des réservations du véhicule : " + e.getMessage());
        }
    }

    public List<Reservation> findAll() throws ServiceException {
        try {
            return reservationDao.findAll();
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