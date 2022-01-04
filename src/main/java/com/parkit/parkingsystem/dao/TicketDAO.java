package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class TicketDAO {

    private static final Logger LOGGER = LogManager.getLogger("TicketDAO");

    private DataBaseConfig dataBaseConfig = new DataBaseConfig();

    public DataBaseConfig getDataBaseConfig() {
        return dataBaseConfig;
    }

    public void setDataBaseConfig(DataBaseConfig dataBaseConfig) {
        this.dataBaseConfig = dataBaseConfig;
    }

    public boolean saveTicket(Ticket ticket) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = dataBaseConfig.getConnection();
            ps = connection.prepareStatement(DBConstants.SAVE_TICKET);
            ps.setInt(1, ticket.getParkingSpot().getNumber());
            ps.setString(2, ticket.getVehicleRegistrationNumber());
            ps.setDouble(3, ticket.getPrice());
            ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
            boolean result = ps.execute();
            return result;
        } catch (Exception ex) {
            LOGGER.error("Error fetching next available spot", ex);
        } finally {
            if(ps != null){
                dataBaseConfig.closePreparedStatement(ps);
            }
            dataBaseConfig.closeConnection(connection);
        }
        return false;
    }

    public Ticket getTicket(String vehicleRegistrationNumber) {
        Connection connection = null;
        Ticket ticket = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = dataBaseConfig.getConnection();
            ps = connection.prepareStatement(DBConstants.GET_TICKET);
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            ps.setString(1, vehicleRegistrationNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                ticket = new Ticket();
                ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)), false);
                ticket.setParkingSpot(parkingSpot);
                ticket.setId(rs.getInt(2));
                ticket.setVehicleRegistrationNumber(vehicleRegistrationNumber);
                ticket.setPrice(rs.getDouble(3));
                ticket.setInTime(rs.getTimestamp(4));
                ticket.setOutTime(rs.getTimestamp(5));
                return ticket;
            }
        } catch (Exception ex) {
            LOGGER.error("Error fetching next available spot", ex);
        } finally {
            if(rs != null){
                dataBaseConfig.closeResultSet(rs);
            }
            if(ps != null){
                dataBaseConfig.closePreparedStatement(ps);
            }
            dataBaseConfig.closeConnection(connection);
        }
        return ticket;
    }

    public boolean updateTicket(Ticket ticket) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = dataBaseConfig.getConnection();
            ps = connection.prepareStatement(DBConstants.UPDATE_TICKET);
            ps.setDouble(1, ticket.getPrice());
            ps.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
            ps.setInt(3, ticket.getId());
            ps.execute();
            return true;
        } catch (Exception ex) {
            LOGGER.error("Error saving ticket info", ex);
        } finally {
            if(ps != null){
                dataBaseConfig.closePreparedStatement(ps);
            }
            dataBaseConfig.closeConnection(connection);
        }
        return false;
    }

    public boolean isCustomerKnown(String vehicleRegistrationNumber) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = dataBaseConfig.getConnection();
            ps = connection.prepareStatement(DBConstants.GET_CUSTOMER_STATUS);
            ps.setString(1, vehicleRegistrationNumber);
            rs = ps.executeQuery();
            boolean isCustomerKnown = rs.next();
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
            return isCustomerKnown;
        } catch (Exception ex) {
            LOGGER.error("Error fetching customer status", ex);
        } finally {
            if(rs != null){
                dataBaseConfig.closeResultSet(rs);
            }
            if(ps != null){
                dataBaseConfig.closePreparedStatement(ps);
            }
            dataBaseConfig.closeConnection(connection);
        }
        return false;
    }

}
