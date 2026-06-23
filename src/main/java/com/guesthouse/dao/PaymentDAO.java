package com.guesthouse.dao;

import com.guesthouse.entities.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    private Connection conn = DBConnection.getConnection();

    public void addPayment(Payment payment) throws SQLException {
        String sql = "INSERT INTO payment (booking_id, amount, method, payment_date, status) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, payment.getBookingId());
        ps.setDouble(2, payment.getAmount());
        ps.setString(3, payment.getMethod());
        ps.setDate(4, Date.valueOf(payment.getPaymentDate()));
        ps.setString(5, payment.getStatus());
        ps.executeUpdate();
    }

    public List<Payment> getAllPayments() throws SQLException {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM payment";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) list.add(mapRow(rs));
        return list;
    }

    public Payment getPaymentByBookingId(int bookingId) throws SQLException {
        String sql = "SELECT * FROM payment WHERE booking_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, bookingId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return mapRow(rs);
        return null;
    }

    private Payment mapRow(ResultSet rs) throws SQLException {
        return new Payment(
            rs.getInt("payment_id"),
            rs.getInt("booking_id"),
            rs.getDouble("amount"),
            rs.getString("method"),
            rs.getDate("payment_date").toLocalDate(),
            rs.getString("status")
        );
    }
}
