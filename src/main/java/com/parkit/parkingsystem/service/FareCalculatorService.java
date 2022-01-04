package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket, boolean isKnownCustomer) {
        if (ticket.getOutTime() == null) {
            throw new IllegalArgumentException("Out time provided is null.");
        }
        if (ticket.getOutTime().before(ticket.getInTime())) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }


        float durationInMilliseconds = ticket.getOutTime().getTime() - ticket.getInTime().getTime();
        float durationInHour = durationInMilliseconds / (1000 * 3600);

        if (durationInHour < .5) {
            ticket.setPrice(0.0);
            return;
        }

        float discount = isKnownCustomer ? 0.95f : 1f;

        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                ticket.setPrice(computeRoundedPrice(durationInHour, Fare.CAR_RATE_PER_HOUR * discount));
                break;
            }
            case BIKE: {
                ticket.setPrice(computeRoundedPrice(durationInHour, Fare.BIKE_RATE_PER_HOUR * discount) );
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown Parking Type");
            }

        }
    }

    static double computeRoundedPrice(double time, double price) {
        double tmp = Math.round(time * price * 100);
        return tmp / 100.0;
    }
}