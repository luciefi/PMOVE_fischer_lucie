package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        float durationInMilliseconds = ticket.getOutTime().getTime() - ticket.getInTime().getTime();
        float durationInHour = durationInMilliseconds / (1000 * 3600);

        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                ticket.setPrice(computeRoundedPrice(durationInHour, Fare.CAR_RATE_PER_HOUR));
                break;
            }
            case BIKE: {
                ticket.setPrice(computeRoundedPrice(durationInHour, Fare.BIKE_RATE_PER_HOUR));
                break;
            }
            default:
                throw new IllegalArgumentException("Unkown Parking Type");
        }
    }

    static double computeRoundedPrice(double time, double price) {
        double tmp = Math.round(time * price * 100);
        return  tmp / 100.0;
    }
}