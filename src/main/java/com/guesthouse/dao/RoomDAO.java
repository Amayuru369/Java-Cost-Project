package com.guesthouse.dao;

import com.guesthouse.entities.Room;
import com.guesthouse.exceptions.RoomNotAvailableException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    private Connection conn = DBConnection.getConnection();

    public void addRoom(Room room) throws SQLException {
        String sql = "INSERT INTO room (room_number, type, price_per_night, status) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, room.getRoomNumber());
        ps.setString(2, room.getType());
        ps.setDouble(3, room.getPricePerNight());
        ps.setString(4, room.getStatus());
        ps.executeUpdate();
    }

    public List<Room> getAllRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM room";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) rooms.add(mapRow(rs));
        return rooms;
    }

    public List<Room> getAvailableRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM room WHERE status = 'Available'";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) rooms.add(mapRow(rs));
        return rooms;
    }

    public Room getRoomById(int id) throws SQLException, RoomNotAvailableException {
        String sql = "SELECT * FROM room WHERE room_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return mapRow(rs);
        throw new RoomNotAvailableException("ID " + id);
    }

    public void updateRoomStatus(int roomId, String status) throws SQLException {
        String sql = "UPDATE room SET status = ? WHERE room_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, status);
        ps.setInt(2, roomId);
        ps.executeUpdate();
    }

    public void updateRoom(Room room) throws SQLException {
        String sql = "UPDATE room SET room_number=?, type=?, price_per_night=?, status=? WHERE room_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, room.getRoomNumber());
        ps.setString(2, room.getType());
        ps.setDouble(3, room.getPricePerNight());
        ps.setString(4, room.getStatus());
        ps.setInt(5, room.getRoomId());
        ps.executeUpdate();
    }

    private Room mapRow(ResultSet rs) throws SQLException {
        return new Room(
            rs.getInt("room_id"),
            rs.getString("room_number"),
            rs.getString("type"),
            rs.getDouble("price_per_night"),
            rs.getString("status")
        );
    }
}