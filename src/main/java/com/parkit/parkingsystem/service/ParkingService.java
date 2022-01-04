package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

public class ParkingService {

    private static final Logger LOGGER = LogManager.getLogger("ParkingService");

    private static final FareCalculatorService FARE_CALCULATOR_SERVICE = new FareCalculatorService();

    private final InputReaderUtil inputReaderUtil;
    private final ParkingSpotDAO parkingSpotDAO;
    private final TicketDAO ticketDAO;

    public ParkingService(InputReaderUtil inputReaderUtil, ParkingSpotDAO parkingSpotDAO, TicketDAO ticketDAO) {
        this.inputReaderUtil = inputReaderUtil;
        this.parkingSpotDAO = parkingSpotDAO;
        this.ticketDAO = ticketDAO;
    }

    public void processIncomingVehicle() {
        try {
            ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();
            if (parkingSpot != null && parkingSpot.getNumber() > 0) {
                String vehicleRegistrationNumber = getVehicleRegistrationNumber();

                if(ticketDAO.isCustomerKnown(vehicleRegistrationNumber)){
                    System.out.println("Welcome back! As a recurring user of our parking lot, you'll benefit from a 5% discount.");
                }
                parkingSpot.setAvailable(false);
                parkingSpotDAO.updateParking(parkingSpot);

                Date inTime = new Date();
                ticketDAO.saveTicket(createTicket(parkingSpot, vehicleRegistrationNumber, inTime));
                System.out.println("Generated Ticket and saved in DB");
                System.out.println("Please park your vehicle in spot number:" + parkingSpot.getNumber());
                System.out.println("Recorded in-time for vehicle number:" + vehicleRegistrationNumber + " is:" + inTime);
            }
        } catch (Exception e) {
            LOGGER.error("Unable to process incoming vehicle", e);
        }
    }

    private Ticket createTicket(ParkingSpot parkingSpot, String vehicleRegistrationNumber, Date inTime) {
        Ticket ticket = new Ticket();
        //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegistrationNumber(vehicleRegistrationNumber);
        ticket.setPrice(0);
        ticket.setInTime(inTime);
        return ticket;
    }

    private String getVehicleRegistrationNumber() throws Exception {
        System.out.println("Please type the vehicle registration number and press enter key");
        return inputReaderUtil.readVehicleRegistrationNumber();
    }

    public ParkingSpot getNextParkingNumberIfAvailable() {
        int parkingNumber = 0;
        ParkingSpot parkingSpot = null;
        try {
            ParkingType parkingType = getVehicleType();
            parkingNumber = parkingSpotDAO.getNextAvailableSpot(parkingType);
            if (parkingNumber > 0) {
                parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);
            } else {
                throw new Exception("Error fetching parking number from DB. Parking spots might be full");
            }
        } catch (IllegalArgumentException ie) {
            LOGGER.error("Error parsing user input for type of vehicle", ie);
        } catch (Exception e) {
            LOGGER.error("Error fetching next available parking spot", e);
        }
        return parkingSpot;
    }

    private ParkingType getVehicleType() {
        System.out.println("Please select vehicle type from menu");
        System.out.println("1 CAR");
        System.out.println("2 BIKE");
        int input = inputReaderUtil.readSelection();
        switch (input) {
            case 1: {
                return ParkingType.CAR;
            }
            case 2: {
                return ParkingType.BIKE;
            }
            default: {
                System.out.println("Incorrect input provided");
                throw new IllegalArgumentException("Entered input is invalid");
            }
        }
    }

    public void processExitingVehicle() {
        try {
            String vehicleRegistrationNumber = getVehicleRegistrationNumber();
            Ticket ticket = ticketDAO.getTicket(vehicleRegistrationNumber);
            Date outTime = new Date();
            ticket.setOutTime(outTime);
            FARE_CALCULATOR_SERVICE.calculateFare(ticket, ticketDAO.isCustomerKnown(vehicleRegistrationNumber));
            if (ticketDAO.updateTicket(ticket)) {
                ParkingSpot parkingSpot = ticket.getParkingSpot();
                parkingSpot.setAvailable(true);
                parkingSpotDAO.updateParking(parkingSpot);
                System.out.println("Please pay the parking fare:" + ticket.getPrice());
                System.out.println("Recorded out-time for vehicle number:" + ticket.getVehicleRegistrationNumber() + " is:" + outTime);
            } else {
                System.out.println("Unable to update ticket information. Error occurred");
            }
        } catch (Exception e) {
            LOGGER.error("Unable to process exiting vehicle", e);
        }
    }
}
