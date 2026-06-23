package com.guesthouse.ui;

import com.guesthouse.dao.RoomDAO;
import com.guesthouse.entities.Room;
import com.guesthouse.exceptions.RoomNotAvailableException;
import com.guesthouse.utils.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class RoomPanel extends JPanel {
    private MainFrame        frame;
    private RoomDAO          roomDAO = new RoomDAO();
    private JTable           roomTable;
    private DefaultTableModel tableModel;

    private JTextField roomNumberField, priceField;
    private JComboBox<String> typeBox, statusBox;

    public RoomPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(buildTopBar(),    BorderLayout.NORTH);
        add(buildFormPanel(), BorderLayout.WEST);
        add(buildTablePanel(), BorderLayout.CENTER);

        loadRooms();
    }

    private JPanel buildTopBar() {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel title = new JLabel("Room Management");
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
        form.setBorder(BorderFactory.createTitledBorder("Add Room"));
        form.setPreferredSize(new Dimension(260, 300));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        roomNumberField = new JTextField(12);
        priceField      = new JTextField(12);
        typeBox   = new JComboBox<>(new String[]{"Single", "Double", "Suite"});
        statusBox = new JComboBox<>(new String[]{"Available", "Booked", "Maintenance"});

        Object[][] rows = {
            {"Room Number:", roomNumberField},
            {"Type:",        typeBox},
            {"Price/Night:", priceField},
            {"Status:",      statusBox}
        };

        for (int i = 0; i < rows.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            form.add(new JLabel((String) rows[i][0]), gbc);
            gbc.gridx = 1;
            form.add((Component) rows[i][1], gbc);
        }

        JButton addBtn = new JButton("Add Room");
        gbc.gridx = 0; gbc.gridy = rows.length; gbc.gridwidth = 2;
        form.add(addBtn, gbc);

        addBtn.addActionListener(e -> addRoom());
        return form;
    }

    private JScrollPane buildTablePanel() {
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Room No.", "Type", "Price/Night", "Status"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        roomTable = new JTable(tableModel);
        roomTable.setRowHeight(24);
        return new JScrollPane(roomTable);
    }

    private void loadRooms() {
        try {
            tableModel.setRowCount(0);
            List<Room> rooms = roomDAO.getAllRooms();
            for (Room r : rooms) {
                tableModel.addRow(new Object[]{
                    r.getRoomId(), r.getRoomNumber(), r.getType(),
                    "Rs. " + r.getPricePerNight(), r.getStatus()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading rooms: " + e.getMessage());
        }
    }

    private void addRoom() {
        String roomNumber = roomNumberField.getText().trim();
        String priceStr   = priceField.getText().trim();
        String type       = (String) typeBox.getSelectedItem();
        String status     = (String) statusBox.getSelectedItem();

        if (Validator.isEmptyOrNull(roomNumber)) {
            JOptionPane.showMessageDialog(this, "Room number is required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!Validator.isPositiveNumber(priceStr)) {
            JOptionPane.showMessageDialog(this, "Enter a valid price.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Room room = new Room(0, roomNumber, type, Double.parseDouble(priceStr), status);
            roomDAO.addRoom(room);
            JOptionPane.showMessageDialog(this, "Room added successfully!");
            roomNumberField.setText("");
            priceField.setText("");
            loadRooms();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding room: " + e.getMessage());
        }
    }
}
