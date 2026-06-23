package com.guesthouse.ui;

import com.guesthouse.dao.DBConnection;
import com.guesthouse.exceptions.InvalidLoginException;
import com.guesthouse.utils.Validator;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginPanel extends JPanel {
    private MainFrame      frame;
    private JTextField     usernameField;
    private JPasswordField passwordField;
    private JLabel         errorLabel;

    public LoginPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 245, 250));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(8, 8, 8, 8);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.gridx   = 0;
        gbc.gridwidth = 2;

        // Title
        JLabel title = new JLabel("Guesthouse Management System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridy = 0;
        card.add(title, gbc);

        JLabel subtitle = new JLabel("Please login to continue", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitle.setForeground(Color.GRAY);
        gbc.gridy = 1;
        card.add(subtitle, gbc);

        // Error label
        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        gbc.gridy = 2;
        card.add(errorLabel, gbc);

        // Username
        gbc.gridwidth = 1;
        gbc.gridy = 3; gbc.gridx = 0;
        card.add(new JLabel("Username:"), gbc);
        usernameField = new JTextField(18);
        gbc.gridx = 1;
        card.add(usernameField, gbc);

        // Password
        gbc.gridy = 4; gbc.gridx = 0;
        card.add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(18);
        gbc.gridx = 1;
        card.add(passwordField, gbc);

        // Login button
        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(59, 130, 246));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setFont(new Font("Arial", Font.BOLD, 13));
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2;
        card.add(loginBtn, gbc);

        loginBtn.addActionListener(e -> handleLogin());
        // Allow Enter key to submit
        passwordField.addActionListener(e -> handleLogin());

        add(card);
    }

    private void handleLogin() {
        errorLabel.setText("");
        try {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (Validator.isEmptyOrNull(username) || Validator.isEmptyOrNull(password)) {
                throw new InvalidLoginException("Username and password are required.");
            }

            Connection conn = DBConnection.getConnection();
            String sql = "SELECT role FROM staff WHERE username = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                JOptionPane.showMessageDialog(this, "Welcome, " + username + "!");
                frame.showPanel("Admin".equals(role) ? "ADMIN" : "GUEST");
            } else {
                throw new InvalidLoginException("Invalid username or password.");
            }

        } catch (InvalidLoginException e) {
            errorLabel.setText(e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
