package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Main application window.
 * Uses CardLayout to switch between panels.
 *
 * Leader: Member 6
 * Each member registers their panel in the addPanels() method
 * after their branch is merged into develop.
 */
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

        addPanels();

        add(container);
        showPanel("LOGIN");
        setVisible(true);
    }

    private void addPanels() {
        // TODO Member 1: Add LoginPanel here after merging feature/login-auth
        // container.add(new LoginPanel(this), "LOGIN");

        // TODO Member 2: Add GuestPanel here after merging feature/guest-module
        // container.add(new GuestPanel(this), "GUEST");

        // TODO Member 3: Add RoomPanel here after merging feature/room-module
        // container.add(new RoomPanel(this), "ROOM");

        // TODO Member 4: Add BookingPanel here after merging feature/booking-module
        // container.add(new BookingPanel(this), "BOOKING");

        // TODO Member 5: Add PaymentPanel here after merging feature/payment-module
        // container.add(new PaymentPanel(this), "PAYMENT");

        // TODO Member 6 (Leader): Add AdminPanel here after merging feature/admin-dashboard
        // container.add(new AdminPanel(this), "ADMIN");

        // Temporary placeholder panel so the app compiles during development
        JPanel placeholder = new JPanel();
        placeholder.add(new JLabel("Guesthouse Management System - Under Construction"));
        container.add(placeholder, "LOGIN");
    }

    public void showPanel(String panelName) {
        cardLayout.show(container, panelName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
