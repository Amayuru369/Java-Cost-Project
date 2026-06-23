package com.guesthouse.ui;


import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel     container;

    public MainFrame() {
        setTitle("Guesthouse Management System");
        setSize(1024, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        container  = new JPanel(cardLayout);

        // Register all panels
        container.add(new LoginPanel(this),   "LOGIN");
        container.add(new GuestPanel(this),   "GUEST");
        container.add(new RoomPanel(this),    "ROOM");
        container.add(new BookingPanel(this), "BOOKING");
        container.add(new PaymentPanel(this), "PAYMENT");
        container.add(new AdminPanel(this),   "ADMIN");

        add(container);
        showPanel("LOGIN");
        setVisible(true);
    }

    public void showPanel(String panelName) {
        cardLayout.show(container, panelName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
