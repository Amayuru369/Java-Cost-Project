package com.guesthouse.ui;

import com.guesthouse.dao.BookingDAO;
import com.guesthouse.dao.PaymentDAO;
import com.guesthouse.dao.RoomDAO;
import com.guesthouse.entities.Booking;
import com.guesthouse.entities.Payment;
import com.guesthouse.entities.Room;
import com.guesthouse.exceptions.PaymentFailedException;
import com.guesthouse.utils.DateUtils;
import com.guesthouse.utils.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PaymentPanel extends JPanel {
    private MainFrame         frame;
    private PaymentDAO        paymentDAO = new PaymentDAO();
    private BookingDAO        bookingDAO = new BookingDAO();
    private RoomDAO           roomDAO    = new RoomDAO();
    private JTable            paymentTable;
    private DefaultTableModel tableModel;

    private JTextField bookingIdField, amountField;
    private JComboBox<String> methodBox;
    private JLabel totalLabel;

    public PaymentPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(buildTopBar(),    BorderLayout.NORTH);
        add(buildFormPanel(), BorderLayout.WEST);
        add(buildTablePanel(), BorderLayout.CENTER);

        loadPayments();
    }

    private JPanel buildTopBar() {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel title = new JLabel("Payment Management");
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
        form.setBorder(BorderFactory.createTitledBorder("Process Payment"));
        form.setPreferredSize(new Dimension(270, 300));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        bookingIdField = new JTextField(12);
        amountField    = new JTextField(12);
        methodBox      = new JComboBox<>(new String[]{"Cash", "Card", "Online"});
        totalLabel     = new JLabel("Total: Rs. 0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 13));
        totalLabel.setForeground(new Color(0, 120, 0));

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Booking ID:"), gbc);
        gbc.gridx = 1; form.add(bookingIdField, gbc);

        JButton calcBtn = new JButton("Calculate");
        gbc.gridx = 0; gbc.gridy = 1; form.add(calcBtn, gbc);
        gbc.gridx = 1; form.add(totalLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Amount (Rs.):"), gbc);
        gbc.gridx = 1; form.add(amountField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; form.add(new JLabel("Method:"), gbc);
        gbc.gridx = 1; form.add(methodBox, gbc);

        JButton payBtn = new JButton("Process Payment");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; form.add(payBtn, gbc);

        calcBtn.addActionListener(e -> calculateTotal());
        payBtn.addActionListener(e -> processPayment());

        return form;
    }

    private JScrollPane buildTablePanel() {
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Booking ID", "Amount (Rs.)", "Method", "Date", "Status"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        paymentTable = new JTable(tableModel);
        paymentTable.setRowHeight(24);
        return new JScrollPane(paymentTable);
    }

    private void loadPayments() {
        try {
            tableModel.setRowCount(0);
            List<Payment> payments = paymentDAO.getAllPayments();
            for (Payment p : payments) {
                tableModel.addRow(new Object[]{
                    p.getPaymentId(), p.getBookingId(),
                    "Rs. " + p.getAmount(), p.getMethod(),
                    p.getPaymentDate(), p.getStatus()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading payments: " + e.getMessage());
        }
    }

    private void calculateTotal() {
        try {
            int bookingId = Integer.parseInt(bookingIdField.getText().trim());
            Booking booking = bookingDAO.getBookingById(bookingId);
            if (booking == null) {
                JOptionPane.showMessageDialog(this, "Booking not found.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Room room = roomDAO.getRoomById(booking.getRoomId());
            double total = DateUtils.calculateTotalCost(booking.getCheckIn(), booking.getCheckOut(), room.getPricePerNight());
            totalLabel.setText("Total: Rs. " + total);
            amountField.setText(String.valueOf(total));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid Booking ID.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void processPayment() {
        try {
            if (Validator.isEmptyOrNull(bookingIdField.getText()) || Validator.isEmptyOrNull(amountField.getText())) {
                throw new PaymentFailedException("Booking ID and Amount are required.");
            }
            if (!Validator.isPositiveNumber(amountField.getText())) {
                throw new PaymentFailedException("Amount must be a positive number.");
            }

            int    bookingId = Integer.parseInt(bookingIdField.getText().trim());
            double amount    = Double.parseDouble(amountField.getText().trim());
            String method    = (String) methodBox.getSelectedItem();

            Payment payment = new Payment(0, bookingId, amount, method, LocalDate.now(), "Paid");
            paymentDAO.addPayment(payment);

            JOptionPane.showMessageDialog(this, "Payment of Rs. " + amount + " processed successfully via " + method + "!");
            bookingDAO.updateBookingStatus(bookingId, "CheckedOut");
            loadPayments();

        } catch (PaymentFailedException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Payment Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
