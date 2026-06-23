package com.guesthouse.ui;

import com.guesthouse.dao.GuestDAO;
import com.guesthouse.entities.Guest;
import com.guesthouse.utils.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class GuestPanel extends JPanel {
    private MainFrame        frame;
    private GuestDAO         guestDAO = new GuestDAO();
    private JTable           guestTable;
    private DefaultTableModel tableModel;

    private JTextField firstNameField, lastNameField, emailField, phoneField, nicField, addressField;

    public GuestPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(buildTopBar(),   BorderLayout.NORTH);
        add(buildFormPanel(), BorderLayout.WEST);
        add(buildTablePanel(), BorderLayout.CENTER);

        loadGuests();
    }

    private JPanel buildTopBar() {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel title = new JLabel("Guest Management");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        JButton backBtn = new JButton("Back to Dashboard");
        backBtn.addActionListener(e -> frame.showPanel("ADMIN"));
        top.add(title);
        top.add(Box.createHorizontalStrut(20));
        top.add(backBtn);
        return top;
    }

    private JPanel buildFormPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Add / Update Guest"));
        form.setPreferredSize(new Dimension(280, 400));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        firstNameField = new JTextField(14);
        lastNameField  = new JTextField(14);
        emailField     = new JTextField(14);
        phoneField     = new JTextField(14);
        nicField       = new JTextField(14);
        addressField   = new JTextField(14);

        String[] labels = {"First Name:", "Last Name:", "Email:", "Phone:", "NIC:", "Address:"};
        JTextField[] fields = {firstNameField, lastNameField, emailField, phoneField, nicField, addressField};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            form.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            form.add(fields[i], gbc);
        }

        JButton addBtn    = new JButton("Add Guest");
        JButton clearBtn  = new JButton("Clear");

        gbc.gridx = 0; gbc.gridy = labels.length; form.add(addBtn, gbc);
        gbc.gridx = 1; form.add(clearBtn, gbc);

        addBtn.addActionListener(e -> addGuest());
        clearBtn.addActionListener(e -> clearForm());

        return form;
    }

    private JScrollPane buildTablePanel() {
        tableModel = new DefaultTableModel(
            new String[]{"ID", "First Name", "Last Name", "Email", "Phone", "NIC"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        guestTable = new JTable(tableModel);
        guestTable.setRowHeight(24);
        return new JScrollPane(guestTable);
    }

    private void loadGuests() {
        try {
            tableModel.setRowCount(0);
            List<Guest> guests = guestDAO.getAllGuests();
            for (Guest g : guests) {
                tableModel.addRow(new Object[]{
                    g.getGuestId(), g.getFirstName(), g.getLastName(),
                    g.getEmail(), g.getPhone(), g.getNic()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading guests: " + e.getMessage());
        }
    }

    private void addGuest() {
        String firstName = firstNameField.getText().trim();
        String lastName  = lastNameField.getText().trim();
        String email     = emailField.getText().trim();
        String phone     = phoneField.getText().trim();
        String nic       = nicField.getText().trim();
        String address   = addressField.getText().trim();

        if (Validator.isEmptyOrNull(firstName) || Validator.isEmptyOrNull(lastName)) {
            JOptionPane.showMessageDialog(this, "First and Last name are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!Validator.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Enter a valid email address.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!Validator.isValidPhone(phone)) {
            JOptionPane.showMessageDialog(this, "Phone must be 10 digits.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!Validator.isValidNIC(nic)) {
            JOptionPane.showMessageDialog(this, "Enter a valid NIC (9 digits + V/X or 12 digits).", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Guest guest = new Guest(0, firstName, lastName, email, phone, nic, address);
            guestDAO.addGuest(guest);
            JOptionPane.showMessageDialog(this, "Guest added successfully!");
            clearForm();
            loadGuests();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding guest: " + e.getMessage());
        }
    }

    private void clearForm() {
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        nicField.setText("");
        addressField.setText("");
    }
}
