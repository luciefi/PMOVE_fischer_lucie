package com.parkit.parkingsystem.model;

import java.util.Date;

public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegistrationNumber;
    private double price;
    private Date inTime;
    private Date outTime = null;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public ParkingSpot getParkingSpot() {
        return new ParkingSpot(parkingSpot.getNumber(), parkingSpot.getParkingType(), parkingSpot.isAvailable());
    }

    public void setParkingSpot(final ParkingSpot parkingSpot) {
        this.parkingSpot = new ParkingSpot(parkingSpot.getNumber(), parkingSpot.getParkingType(), parkingSpot.isAvailable());
    }

    public String getVehicleRegistrationNumber() {
        return vehicleRegistrationNumber;
    }

    public void setVehicleRegistrationNumber(final String vehicleRegistrationNumber) {
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(final double price) {
        this.price = price;
    }

    public Date getInTime() {
        return new Date(inTime.getTime());
    }

    public void setInTime(final Date inTime) {
        this.inTime = new Date(inTime.getTime());
    }

    public Date getOutTime() {
        if (outTime == null) {
            return null;
        }
        return new Date(outTime.getTime());
    }

    public void setOutTime(final Date outTime) {
        if (outTime != null) {
            this.outTime = new Date(outTime.getTime());
        }
    }
}
