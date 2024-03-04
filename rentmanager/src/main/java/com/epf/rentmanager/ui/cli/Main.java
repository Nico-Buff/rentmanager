package com.epf.rentmanager.ui.cli;

import java.time.format.DateTimeParseException;
import com.epf.rentmanager.Exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import com.epf.rentmanager.utils.IOUtils;
import org.h2.jdbcx.JdbcConnectionPool;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static ClientService clientService = ClientService.getInstance();
    static VehicleService vehicleService = VehicleService.getInstance();
    private static ReservationService reservationService = ReservationService.getInstance();
    private static Scanner scanner = new Scanner(System.in);

    public static void main (String [] args){
        int choix;
        do {
            System.out.println("Que souhaitez-vous faire ?");
            System.out.println("1. Créer un Client");
            System.out.println("2. Lister tous les Clients");
            System.out.println("3. Créer un Véhicule"); // TESTER : PROBLEME DATABASE : TEST POUR L'ID
            System.out.println("4. Lister tous les Véhicules");// TESTER : PROBLEME DATABASE
            System.out.println("5. Supprimer un Client");
            System.out.println("6. Supprimer un Véhicule");// TESTER : PROBLEME DATABASE
            System.out.println("7. Créer une Réservation");
            System.out.println("8. Lister toutes les Réservations");
            System.out.println("9. Lister les Réservations d'un Client");
            System.out.println("10. Lister les Réservations d'un Véhicule");
            System.out.println("11. Supprimer une Réservation");
            System.out.println("0. Quitter");

            choix = scanner.nextInt();

            switch (choix) {
                case 1:
                    Test_creer_client();
                    break;
                case 2:
                    Test_lister_client();
                    break;
                case 3:
                    Test_creer_vehicule();
                    break;
                case 4:
                    Test_lister_vehicule();
                    break;
                case 5:
                    Test_supprimer_client();
                    break;
                case 6:
                    Test_supprimer_vehicule();
                    break;
                case 7:
                    Test_creer_reservation();
                    break;
                case 8:
                    Test_lister_reservations();
                    break;
                case 9:
                    Test_lister_reservations_client();
                    break;
                case 10:
                    Test_lister_reservations_vehicule();
                    break;
                case 11:
                    Test_supprimer_reservation();
                    break;
                case 0:
                    System.out.println("Au revoir !");
                    break;
            }
        } while (choix != 0);
    }


    public static void Test_creer_client(){
        String nom , prenom , email = "";
        LocalDate date = null;
        boolean emailValide = false;
        boolean dateValide = false;

        IOUtils.print("--  Création d'un client  --");

        nom = IOUtils.readString("Entrez le nom du client :", true);
        prenom = IOUtils.readString("Entrez le prénom du client :", true);

        while (!emailValide) {
            email = IOUtils.readString("Entrez l'email du client :", true);
            if (IOUtils.isValidEmail(email)) {
                emailValide = true;
            } else {
                IOUtils.print("Format d'email invalide. Veuillez saisir un email valide.");
            }
        }

        while (!dateValide) {
            date = IOUtils.readDate("Entrez la date de naissance du client dans le format dd/MM/yyyy:", true);
            if (date != null) {
                dateValide = true;
            } else {
                IOUtils.print("Format de date invalide. Veuillez saisir une date valide au format dd/MM/yyyy.");
            }
        }

        Client client = new Client();
        client.setNom(nom);
        client.setPrenom(prenom);
        client.setEmail(email);
        client.setNaissance(date);
        try{
            long id = clientService.create(client);
            System.out.println("Client créé avec succès. ID : " + id);
        }catch(ServiceException e){
            System.out.print(e.getMessage());
        }
    }

    public static void Test_lister_client(){
        try {
            List<Client> listclient = clientService.findAll();
            for (Client client : listclient) {
                System.out.println(client);
            }
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void Test_creer_vehicule(){
        IOUtils.print("--  Création d'une voiture  --");

        String constructeur = IOUtils.readString("Entrez le nom du constructeur :", true);
        String modele = IOUtils.readString("Entrez le nom du modèle :", true);

        int nb_places = IOUtils.readInt("Entrez le nombre de places :");

        Vehicle vehicle = new Vehicle();
        vehicle.setConstructeur(constructeur);
        vehicle.setModele(modele);
        vehicle.setNb_places(nb_places);

        try {
            long id = vehicleService.create(vehicle);
            IOUtils.print("Véhicule créé avec l'ID : " + id);
        } catch (ServiceException e) {
            IOUtils.print(e.getMessage());
        }
    }

    public static void Test_lister_vehicule(){
        try {
            List<Vehicle> listvehicle = vehicleService.findAll();
            for (Vehicle vehicle : listvehicle) {
                System.out.println(vehicle);
            }
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void Test_supprimer_client(){
        System.out.println("Suppression d'un client :");
        System.out.println("Entrez l'ID du client à supprimer :");
        long id = scanner.nextLong();
        try {
            Client clientToDelete = clientService.findById(id);
            clientService.delete(clientToDelete);
            System.out.println("Client supprimé avec succès.");
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void Test_supprimer_vehicule(){
        System.out.println("Suppression d'un véhicule :");
        System.out.println("Entrez l'ID du véhicule à supprimer :");
        long id = scanner.nextLong();
        try {
            Vehicle vehicleToDelete = vehicleService.findById(id);
            vehicleService.delete(vehicleToDelete);
            System.out.println("Véhicule supprimé avec succès.");
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void Test_creer_reservation() {
        IOUtils.print("-- Création d'une réservation --");

        try {
            long clientId = IOUtils.readInt("Entrez l'ID du client :");
            Client client = clientService.findById(clientId);

            long vehicleId = IOUtils.readInt("Entrez l'ID du véhicule :");
            Vehicle vehicle = vehicleService.findById(vehicleId);

            LocalDate startDate;
            LocalDate endDate;
            boolean dateValid = false;

            do {
                startDate = IOUtils.readDate("Entrez la date de début de la réservation (dd/MM/yyyy) :", true);
                endDate = IOUtils.readDate("Entrez la date de fin de la réservation (dd/MM/yyyy) :", true);

                if (startDate.isAfter(endDate)) {
                    IOUtils.print("La date de début ne peut pas être après la date de fin.");
                } else {
                    dateValid = true;
                }
            } while (!dateValid);

            Reservation reservation = new Reservation(client, vehicle, startDate, endDate);

            long id = reservationService.create(reservation);
            IOUtils.print("Réservation créée avec succès. ID : " + id);
        } catch (ServiceException e) {
            IOUtils.print(e.getMessage());
        }
    }

    public static void Test_lister_reservations() {
        try {
            List<Reservation> reservations = reservationService.findAll();
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
            }
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void Test_lister_reservations_client() {
        System.out.println("Entrez l'ID du client :");
        long clientId = scanner.nextLong();
        try {
            List<Reservation> reservations = reservationService.findReservationsByClientId(clientId);
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
            }
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void Test_lister_reservations_vehicule() {
        System.out.println("Entrez l'ID du véhicule :");
        long vehicleId = scanner.nextLong();
        try {
            List<Reservation> reservations = reservationService.findReservationsByVehicleId(vehicleId);
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
            }
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void Test_supprimer_reservation() {
        System.out.println("Entrez l'ID de la réservation à supprimer :");
        long id = scanner.nextLong();
        try {
            reservationService.delete(id);
            System.out.println("Réservation supprimée avec succès.");
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }



}
