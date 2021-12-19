package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import org.junit.jupiter.api.Test;

public class ParkingSpotDAOTest {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    @Test
    public void getNextAvailableSlotTest() {
        // ARRANGE
        ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;

        // ACT
        parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

        // ASSERT
        // TODO vérifier qu'un prepared statement a été exécuté avec les bons args ?
    }
}
