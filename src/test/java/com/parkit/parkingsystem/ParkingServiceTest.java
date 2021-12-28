package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @Test
    public void processExitingCarTest() {
        // ARRANGE
        setUpTest(ParkingType.CAR, true);
        // ACT
        parkingService.processExitingVehicle();
        // ASSERT
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void processIncomingCarTest() {
        // ARRANGE
        setUpTest(ParkingType.CAR, false);
        // ACT
        parkingService.processIncomingVehicle();
        // ASSERT
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
        verify(parkingSpotDAO, Mockito.times(1)).getNextAvailableSlot(any(ParkingType.class));
        verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
    }

    @Test
    public void processExitingBikeTest() {
        // ARRANGE
        setUpTest(ParkingType.BIKE, true);
        // ACT
        parkingService.processExitingVehicle();
        // ASSERT
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void processIncomingBikeTest() {
        // ARRANGE
        setUpTest(ParkingType.BIKE, false);
        // ACT
        parkingService.processIncomingVehicle();
        // ASSERT
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
        verify(parkingSpotDAO, Mockito.times(1)).getNextAvailableSlot(any(ParkingType.class));
        verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
    }

    private void setUpTest(ParkingType parkingType, boolean isVehicleExiting) {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            ParkingSpot parkingSpot = new ParkingSpot(1, parkingType, !isVehicleExiting);
            if (isVehicleExiting) {
                Ticket ticket = new Ticket();
                ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
                ticket.setParkingSpot(parkingSpot);
                ticket.setVehicleRegistrationNumber("ABCDEF");
                when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
                when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
                when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
            } else {
                when(inputReaderUtil.readSelection()).thenReturn(1);
                when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
            }

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
    }


    @Test
    public void processIncomingCarParkingSpotNullTest() {
        // ARRANGE
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(-1);
        when(inputReaderUtil.readSelection()).thenReturn(1);

        // ACT
        parkingService.processIncomingVehicle();
        // ASSERT
        verify(parkingSpotDAO, Mockito.times(0)).updateParking(any(ParkingSpot.class));
        verify(parkingSpotDAO, Mockito.times(1)).getNextAvailableSlot(any(ParkingType.class));
        verify(ticketDAO, Mockito.times(0)).saveTicket(any(Ticket.class));
    }

    @Test
    public void processIncomingUnknownVehicleTest() {
        // ARRANGE
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        when(inputReaderUtil.readSelection()).thenReturn(-12);
        // ACT
        parkingService.processIncomingVehicle();
        // ASSERT
        verify(parkingSpotDAO, Mockito.times(0)).getNextAvailableSlot(any(ParkingType.class));
    }

    @Test
    public void processExitingBikeWithTicketDAOErrorTest() {
        // ARRANGE
        setUpIncorrectTest(ParkingType.BIKE);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);
        // ACT
        parkingService.processExitingVehicle();
        // ASSERT
        verify(parkingSpotDAO, Mockito.times(0)).updateParking(any(ParkingSpot.class));
    }

    private void setUpIncorrectTest(ParkingType parkingType) {
        try {
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(null);
            ticket.setVehicleRegistrationNumber("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            // when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
    }

}
