/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.guesthouse.ui;

import com.guesthouse.dao.BookingDAO;
import com.guesthouse.dao.GuestDAO;
import com.guesthouse.dao.PaymentDAO;
import com.guesthouse.dao.RoomDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AdminPanel extends JPanel {
    private MainFrame  frame;
    private JLabel     totalGuestsLabel, totalRoomsLabel, totalBookingsLabel, totalRevenueLabel;

    public AdminPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(buildHeader(),   BorderLayout.NORTH);
        add(buildStats(),    BorderLayout.CENTER);
        add(buildNavButtons(), BorderLayout.SOUTH);

        refreshStats();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        JLabel title  = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> frame.showPanel("LOGIN"));

        header.add(title,     BorderLayout.CENTER);
        header.add(logoutBtn, BorderLayout.EAST);
        return header;
    }

    private JPanel buildStats() {
        JPanel stats = new JPanel(new GridLayout(2, 2, 15, 15));
        stats.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        totalGuestsLabel   = new JLabel("--", SwingConstants.CENTER);
        totalRoomsLabel    = new JLabel("--", SwingConstants.CENTER);
        totalBookingsLabel = new JLabel("--", SwingConstants.CENTER);
        totalRevenueLabel  = new JLabel("--", SwingConstants.CENTER);

        stats.add(buildStatCard("Total Guests",   totalGuestsLabel,   new Color(219, 234, 254)));
        stats.add(buildStatCard("Total Rooms",    totalRoomsLabel,    new Color(220, 252, 231)));
        stats.add(buildStatCard("Total Bookings", totalBookingsLabel, new Color(254, 249, 195)));
        stats.add(buildStatCard("Total Revenue",  totalRevenueLabel,  new Color(252, 231, 243)));

        return stats;
    }

    private JPanel buildStatCard(String title, JLabel valueLabel, Color bg) {
        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBackground(bg);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bg.darker(), 1),
            BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        card.add(titleLabel);
        card.add(valueLabel);
        return card;
    }

    private JPanel buildNavButtons() {
        JPanel nav = new JPanel(new GridLayout(1, 4, 10, 0));

        JButton[] buttons = {
            new JButton("Manage Guests"),
            new JButton("Manage Rooms"),
            new JButton("Manage Bookings"),
            new JButton("Manage Payments")
        };
        String[] panels = {"GUEST", "ROOM", "BOOKING", "PAYMENT"};

        for (int i = 0; i < buttons.length; i++) {
            final String panel = panels[i];
            buttons[i].setFont(new Font("Arial", Font.BOLD, 13));
            buttons[i].setBackground(new Color(59, 130, 246));
            buttons[i].setForeground(Color.WHITE);
            buttons[i].setFocusPainted(false);
            buttons[i].addActionListener(e -> frame.showPanel(panel));
            nav.add(buttons[i]);
        }
        return nav;
    }

    public void refreshStats() {
        try {
            int guests   = new GuestDAO().getAllGuests().size();
            int rooms    = new RoomDAO().getAllRooms().size();
            int bookings = new BookingDAO().getAllBookings().size();
            double revenue = new PaymentDAO().getAllPayments()
                .stream().mapToDouble(p -> p.getAmount()).sum();

            totalGuestsLabel.setText(String.valueOf(guests));
            totalRoomsLabel.setText(String.valueOf(rooms));
            totalBookingsLabel.setText(String.valueOf(bookings));
            totalRevenueLabel.setText("Rs. " + String.format("%.2f", revenue));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading stats: " + e.getMessage());
        }
    }
}
