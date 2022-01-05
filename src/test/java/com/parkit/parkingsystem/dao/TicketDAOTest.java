package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TicketDAOTest {
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @BeforeAll
    public static void setUp() {
        ticketDAO = new TicketDAO();
        ticketDAO.setDataBaseConfig(new DataBaseTestConfig());
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    public void setUpTest() {
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void getTicketTest() {
        Ticket ticket = new Ticket();
        Date inTime = new Date(System.currentTimeMillis() - 3600 * 100000);
        ticket.setInTime(inTime);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, true));
        ticket.setVehicleRegistrationNumber("ABCDEF");
        ticketDAO.saveTicket(ticket);

        Ticket gottenTicket = ticketDAO.getTicket("ABCDEF");
        assertThat(ticket.getVehicleRegistrationNumber()).isEqualTo("ABCDEF");
        assertThat(ticket.getInTime().getTime()).isEqualTo(inTime.getTime());
    }

    @Test
    public void isCustomerKnownTest() {
        assertEquals(false, ticketDAO.isCustomerKnown("ABCDEF"));
    }

}