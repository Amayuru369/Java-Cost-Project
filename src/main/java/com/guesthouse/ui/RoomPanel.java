package com.guesthouse.ui;

import com.guesthouse.dao.RoomDAO;
import com.guesthouse.entities.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RoomPanel extends JPanel {

    private JTextField roomNumberField;
    private JComboBox<String> roomTypeCombo;
    private JTextField priceField;
    private JCheckBox availableCheckBox;
    private JTable roomTable;
    private DefaultTableModel tableModel;
    private RoomDAO roomDAO;
    private int selectedRoomId = -1;

    public RoomPanel() {
        roomDAO = new RoomDAO();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(buildFormPanel(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);

        loadRooms();
    }

    private JPanel buildFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Room Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Room Number:"), gbc);
        gbc.gridx = 1;
        roomNumberField = new JTextField(10);
        formPanel.add(roomNumberField, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        formPanel.add(new JLabel("Room Type:"), gbc);
        gbc.gridx = 3;
        roomTypeCombo = new JComboBox<>(new String[]{"Single", "Double", "Suite", "Deluxe"});
        formPanel.add(roomTypeCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Price/Night:"), gbc);
        gbc.gridx = 1;
        priceField = new JTextField(10);
        formPanel.add(priceField, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        formPanel.add(new JLabel("Available:"), gbc);
        gbc.gridx = 3;
        availableCheckBox = new JCheckBox();
        availableCheckBox.setSelected(true);
        formPanel.add(availableCheckBox, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Add Room");
        JButton updateBtn = new JButton("Update Room");
        JButton deleteBtn = new JButton("Delete Room");
        JButton clearBtn = new JButton("Clear");

        addBtn.addActionListener(e -> addRoom());
        updateBtn.addActionListener(e -> updateRoom());
        deleteBtn.addActionListener(e -> deleteRoom());
        clearBtn.addActionListener(e -> clearForm());

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4;
        formPanel.add(buttonPanel, gbc);

        return formPanel;
    }

    private JScrollPane buildTablePanel() {
        String[] columns = {"ID", "Room No.", "Type", "Price/Night", "Available"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        roomTable = new JTable(tableModel);
        roomTable.getSelectionModel().addListSelectionListener(e -> populateFormFromSelection());

        return new JScrollPane(roomTable);
    }

    private void loadRooms() {
        tableModel.setRowCount(0);
        List<Room> rooms = roomDAO.getAllRooms();
        for (Room r : rooms) {
            tableModel.addRow(new Object[]{
                    r.getRoomId(), r.getRoomNumber(), r.getRoomType(),
                    r.getPricePerNight(), r.isAvailable() ? "Yes" : "No"
            });
        }
    }

    private void populateFormFromSelection() {
        int row = roomTable.getSelectedRow();
        if (row == -1) return;

        selectedRoomId = (int) tableModel.getValueAt(row, 0);
        roomNumberField.setText(tableModel.getValueAt(row, 1).toString());
        roomTypeCombo.setSelectedItem(tableModel.getValueAt(row, 2).toString());
        priceField.setText(tableModel.getValueAt(row, 3).toString());
        availableCheckBox.setSelected(tableModel.getValueAt(row, 4).toString().equals("Yes"));
    }

    private boolean validateInput() {
        if (roomNumberField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Room number cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            double price = Double.parseDouble(priceField.getText().trim());
            if (price <= 0) {
                JOptionPane.showMessageDialog(this, "Price must be greater than 0.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Price must be a valid number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void addRoom() {
        if (!validateInput()) return;

        Room room = new Room(
                roomNumberField.getText().trim(),
                roomTypeCombo.getSelectedItem().toString(),
                Double.parseDouble(priceField.getText().trim()),
                availableCheckBox.isSelected()
        );

        if (roomDAO.addRoom(room)) {
            JOptionPane.showMessageDialog(this, "Room added successfully!");
            loadRooms();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add room.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRoom() {
        if (selectedRoomId == -1) {
            JOptionPane.showMessageDialog(this, "Select a room from the table first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validateInput()) return;

        Room room = new Room(
                selectedRoomId,
                roomNumberField.getText().trim(),
                roomTypeCombo.getSelectedItem().toString(),
                Double.parseDouble(priceField.getText().trim()),
                availableCheckBox.isSelected()
        );

        if (roomDAO.updateRoom(room)) {
            JOptionPane.showMessageDialog(this, "Room updated successfully!");
            loadRooms();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update room.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRoom() {
        if (selectedRoomId == -1) {
            JOptionPane.showMessageDialog(this, "Select a room from the table first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Delete this room?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (roomDAO.deleteRoom(selectedRoomId)) {
                JOptionPane.showMessageDialog(this, "Room deleted.");
                loadRooms();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete room.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        selectedRoomId = -1;
        roomNumberField.setText("");
        priceField.setText("");
        roomTypeCombo.setSelectedIndex(0);
        availableCheckBox.setSelected(true);
        roomTable.clearSelection();
    }
}