package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ParkingSpotDAO {
    private static final Logger LOGGER = LogManager.getLogger("ParkingSpotDAO");

    private DataBaseConfig dataBaseConfig = new DataBaseConfig();

    public DataBaseConfig getDataBaseConfig() {
        return dataBaseConfig;
    }

    public void setDataBaseConfig(DataBaseConfig dataBaseTestConfig) {
        this.dataBaseConfig = dataBaseTestConfig;
    }

    public int getNextAvailableSpot(ParkingType parkingType){
        Connection connection = null;
        int nextAvailableSpot =-1;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = dataBaseConfig.getConnection();
            ps = connection.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
            ps.setString(1, parkingType.toString());
            rs = ps.executeQuery();
            if(rs.next()){
                nextAvailableSpot = rs.getInt(1);
            }
        }catch (Exception ex){
            LOGGER.error("Error fetching next available spot",ex);
        }finally {
            if(rs != null){
                dataBaseConfig.closeResultSet(rs);
            }
            if(ps != null){
                dataBaseConfig.closePreparedStatement(ps);
            }
            dataBaseConfig.closeConnection(connection);
        }
        return nextAvailableSpot;
    }

    public boolean updateParking(ParkingSpot parkingSpot){
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = dataBaseConfig.getConnection();
            ps = connection.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
            ps.setBoolean(1, parkingSpot.isAvailable());
            ps.setInt(2, parkingSpot.getNumber());
            int updateRowCount = ps.executeUpdate();
            return (updateRowCount == 1);
        }catch (Exception ex){
            LOGGER.error("Error updating availability for a spot",ex);
            return false;
        }finally {
            if(ps != null){
                dataBaseConfig.closePreparedStatement(ps);
            }
            dataBaseConfig.closeConnection(connection);
        }
    }

}
