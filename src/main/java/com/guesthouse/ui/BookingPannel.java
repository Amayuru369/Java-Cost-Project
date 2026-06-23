package com.guesthouse.ui;

import com.guesthouse.dao.BookingDAO;
import com.guesthouse.dao.RoomDAO;
import com.guesthouse.entities.Booking;
import com.guesthouse.entities.Room;
import com.guesthouse.exceptions.RoomNotAvailableException;
import com.guesthouse.utils.DateUtils;
import com.guesthouse.utils.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class BookingPanel extends JPanel {
    private MainFrame         frame;
    private BookingDAO        bookingDAO = new BookingDAO();
    private RoomDAO           roomDAO    = new RoomDAO();
    private JTable            bookingTable;
    private DefaultTableModel tableModel;

    private JTextField guestIdField, roomIdField, checkInField, checkOutField;

    public BookingPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(buildTopBar(),    BorderLayout.NORTH);
        add(buildFormPanel(), BorderLayout.WEST);
        add(buildTablePanel(), BorderLayout.CENTER);

        loadBookings();
    }

    private JPanel buildTopBar() {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel title = new JLabel("Booking Management");
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
        form.setBorder(BorderFactory.createTitledBorder("New Booking"));
        form.setPreferredSize(new Dimension(280, 320));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        guestIdField  = new JTextField(12);
        roomIdField   = new JTextField(12);
        checkInField  = new JTextField("yyyy-MM-dd", 12);
        checkOutField = new JTextField("yyyy-MM-dd", 12);

        Object[][] rows = {
            {"Guest ID:",    guestIdField},
            {"Room ID:",     roomIdField},
            {"Check-in:",    checkInField},
            {"Check-out:",   checkOutField}
        };
        for (int i = 0; i < rows.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            form.add(new JLabel((String) rows[i][0]), gbc);
            gbc.gridx = 1;
            form.add((Component) rows[i][1], gbc);
        }

        JButton bookBtn     = new JButton("Make Booking");
        JButton checkInBtn  = new JButton("Check-In");
        JButton checkOutBtn = new JButton("Check-Out");

        gbc.gridx = 0; gbc.gridy = rows.length;     form.add(bookBtn, gbc);
        gbc.gridx = 1;                                form.add(checkInBtn, gbc);
        gbc.gridx = 0; gbc.gridy = rows.length + 1; gbc.gridwidth = 2;
        form.add(checkOutBtn, gbc);

        bookBtn.addActionListener(e -> makeBooking());
        checkInBtn.addActionListener(e -> updateSelectedStatus("CheckedIn"));
        checkOutBtn.addActionListener(e -> updateSelectedStatus("CheckedOut"));

        return form;
    }

    private JScrollPane buildTablePanel() {
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Guest ID", "Room ID", "Check-in", "Check-out", "Nights", "Status"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        bookingTable = new JTable(tableModel);
        bookingTable.setRowHeight(24);
        return new JScrollPane(bookingTable);
    }

    private void loadBookings() {
        try {
            tableModel.setRowCount(0);
            List<Booking> bookings = bookingDAO.getAllBookings();
            for (Booking b : bookings) {
                tableModel.addRow(new Object[]{
                    b.getBookingId(), b.getGuestId(), b.getRoomId(),
                    b.getCheckIn(), b.getCheckOut(), b.getNights(), b.getStatus()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading bookings: " + e.getMessage());
        }
    }

    private void makeBooking() {
        try {
            if (Validator.isEmptyOrNull(guestIdField.getText()) || Validator.isEmptyOrNull(roomIdField.getText())) {
                JOptionPane.showMessageDialog(this, "Guest ID and Room ID are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int       guestId  = Integer.parseInt(guestIdField.getText().trim());
            int       roomId   = Integer.parseInt(roomIdField.getText().trim());
            LocalDate checkIn  = DateUtils.parseDate(checkInField.getText());
            LocalDate checkOut = DateUtils.parseDate(checkOutField.getText());

            if (!DateUtils.isValidDateRange(checkIn, checkOut)) {
                JOptionPane.showMessageDialog(this, "Invalid dates. Check-in must be today or later, and check-out must be after check-in.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Room room = roomDAO.getRoomById(roomId);
            if (!room.isAvailable()) {
                throw new RoomNotAvailableException(room.getRoomNumber());
            }

            Booking booking = new Booking(0, guestId, roomId, checkIn, checkOut, "Confirmed");
            bookingDAO.addBooking(booking);
            roomDAO.updateRoomStatus(roomId, "Booked");

            JOptionPane.showMessageDialog(this, "Booking confirmed! Total: Rs. " +
                DateUtils.calculateTotalCost(checkIn, checkOut, room.getPricePerNight()));
            loadBookings();

        } catch (RoomNotAvailableException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Room Unavailable", JOptionPane.WARNING_MESSAGE);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use yyyy-MM-dd.", "Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Guest ID and Room ID must be numbers.", "Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void updateSelectedStatus(String status) {
        int row = bookingTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a booking first.");
            return;
        }
        int bookingId = (int) tableModel.getValueAt(row, 0);
        try {
            bookingDAO.updateBookingStatus(bookingId, status);
            JOptionPane.showMessageDialog(this, "Status updated to: " + status);
            loadBookings();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating status: " + e.getMessage());
        }
    }
}
