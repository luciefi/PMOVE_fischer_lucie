package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParkingSpotDAOTest {

    private static ParkingSpotDAO parkingSpotDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @BeforeAll
    public static void setUp(){
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.setDataBaseConfig(new DataBaseTestConfig());
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    public void setUpTest() {
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void getNextAvailableSpotForCarTest() {
        assertEquals(1, parkingSpotDAO.getNextAvailableSpot(ParkingType.CAR));
    }

    @Test
    public void getNextAvailableSpotForBikeTest() {
        assertEquals(4, parkingSpotDAO.getNextAvailableSpot(ParkingType.BIKE));
    }

}