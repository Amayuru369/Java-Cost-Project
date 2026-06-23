package com.guesthouse.dao;

import com.guesthouse.entities.Booking;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    private Connection conn = DBConnection.getConnection();

    public void addBooking(Booking booking) throws SQLException {
        String sql = "INSERT INTO booking (guest_id, room_id, check_in, check_out, status) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, booking.getGuestId());
        ps.setInt(2, booking.getRoomId());
        ps.setDate(3, Date.valueOf(booking.getCheckIn()));
        ps.setDate(4, Date.valueOf(booking.getCheckOut()));
        ps.setString(5, booking.getStatus());
        ps.executeUpdate();
    }

    public List<Booking> getAllBookings() throws SQLException {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM booking";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) list.add(mapRow(rs));
        return list;
    }

    public Booking getBookingById(int id) throws SQLException {
        String sql = "SELECT * FROM booking WHERE booking_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return mapRow(rs);
        return null;
    }

    public void updateBookingStatus(int bookingId, String status) throws SQLException {
        String sql = "UPDATE booking SET status = ? WHERE booking_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, status);
        ps.setInt(2, bookingId);
        ps.executeUpdate();
    }

    private Booking mapRow(ResultSet rs) throws SQLException {
        return new Booking(
            rs.getInt("booking_id"),
            rs.getInt("guest_id"),
            rs.getInt("room_id"),
            rs.getDate("check_in").toLocalDate(),
            rs.getDate("check_out").toLocalDate(),
            rs.getString("status")
        );
    }
}
