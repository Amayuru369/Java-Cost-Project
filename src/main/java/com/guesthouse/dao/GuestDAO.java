package com.guesthouse.dao;

import com.guesthouse.entities.Guest;
import com.guesthouse.exceptions.GuestNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuestDAO {
    private Connection conn = DBConnection.getConnection();

    public void addGuest(Guest guest) throws SQLException {
        String sql = "INSERT INTO guest (first_name, last_name, email, phone, nic, address) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, guest.getFirstName());
        ps.setString(2, guest.getLastName());
        ps.setString(3, guest.getEmail());
        ps.setString(4, guest.getPhone());
        ps.setString(5, guest.getNic());
        ps.setString(6, guest.getAddress());
        ps.executeUpdate();
    }

    public List<Guest> getAllGuests() throws SQLException {
        List<Guest> guests = new ArrayList<>();
        String sql = "SELECT * FROM guest";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            guests.add(new Guest(
                rs.getInt("guest_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("nic"),
                rs.getString("address")
            ));
        }
        return guests;
    }

    public Guest getGuestById(int id) throws SQLException, GuestNotFoundException {
        String sql = "SELECT * FROM guest WHERE guest_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Guest(
                rs.getInt("guest_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("nic"),
                rs.getString("address")
            );
        }
        throw new GuestNotFoundException(id);
    }

    public void updateGuest(Guest guest) throws SQLException {
        String sql = "UPDATE guest SET first_name=?, last_name=?, email=?, phone=?, nic=?, address=? WHERE guest_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, guest.getFirstName());
        ps.setString(2, guest.getLastName());
        ps.setString(3, guest.getEmail());
        ps.setString(4, guest.getPhone());
        ps.setString(5, guest.getNic());
        ps.setString(6, guest.getAddress());
        ps.setInt(7, guest.getGuestId());
        ps.executeUpdate();
    }

    public void deleteGuest(int id) throws SQLException {
        String sql = "DELETE FROM guest WHERE guest_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }
}