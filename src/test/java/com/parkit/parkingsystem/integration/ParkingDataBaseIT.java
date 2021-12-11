package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private static String vehicleRegistrationNumber = "ABCDEF";

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1); //entering parking
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehicleRegistrationNumber);
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){

    }

    @Test
    public void testParkingACar(){
        //ARRANGE
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        //ACT
        parkingService.processIncomingVehicle();
        //ASSERT
        assertThat(ticketDAO.getTicket(vehicleRegistrationNumber)).as("A ticket associated to the vehicle registration number should be found").isNotNull();
        assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).as("Next spot available should be #2").isEqualTo(2);
    }

    @Test
    public void testParkingLotExit(){
        // ARRANGE
        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        Ticket setBackInTimeTicket = ticketDAO.getTicket(vehicleRegistrationNumber);
        setBackInTimeTicket.setInTime(new Date(System.currentTimeMillis() - 3600 * 1000));
        dataBasePrepareService.clearDataBaseEntries();
        ticketDAO.saveTicket(setBackInTimeTicket);
        // ACT
        parkingService.processExitingVehicle();
        // ASSERT
        assertThat(ticketDAO.getTicket(vehicleRegistrationNumber).getPrice()).as("Parking fare should be 1.5").isEqualTo(1.5);
        assertThat(ticketDAO.getTicket(vehicleRegistrationNumber).getOutTime()).as("out time should be now").isBetween(new Date(System.currentTimeMillis() - 1000),new Date(System.currentTimeMillis()));
    }

}
