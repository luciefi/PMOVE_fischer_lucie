package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;


public class ParkingSpotTest {
    @Test
    public void equalsSameObjectTest(){
        // ARRANGE
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        // ACT

        // ASSERT
        assertThat(parkingSpot.equals(parkingSpot)).isTrue();
    }
    @Test
    public void equalsDifferentObjectsTest(){
        // ARRANGE
        ParkingSpot parkingSpotA = new ParkingSpot(1, ParkingType.CAR, true);
        ParkingSpot parkingSpotB = new ParkingSpot(2, ParkingType.CAR, true);
        // ASSERT
        assertThat(parkingSpotA.equals(parkingSpotB)).isFalse();
    }
    @Test
    public void equalsDifferentObjectsSameNumberTest(){
        // ARRANGE
        ParkingSpot parkingSpotA = new ParkingSpot(1, ParkingType.CAR, true);
        ParkingSpot parkingSpotB = new ParkingSpot(1, ParkingType.CAR, true);
        // ASSERT
        assertThat(parkingSpotA.equals(parkingSpotB)).isTrue();
    }

    @Test
    public void hashTest(){
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        assertThat(parkingSpot.hashCode()).isEqualTo(1);
    }

    @Test
    public void setParkingTypeTest(){
        // ARRANGE
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        // ACT
        parkingSpot.setParkingType(ParkingType.BIKE);
        // ASSERT
        assertThat(parkingSpot.getParkingType()).isEqualTo(ParkingType.BIKE);
    }
    @Test
    public void setIdTest(){
        // ARRANGE
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        // ACT
        parkingSpot.setNumber(3);
        // ASSERT
        assertThat(parkingSpot.getNumber()).isEqualTo(3);
    }
}
