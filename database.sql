-- Guesthouse Management System - Database Setup
-- Run this file in MySQL Workbench or via: mysql -u root -p < database.sql

CREATE DATABASE IF NOT EXISTS guesthouse_db;
USE guesthouse_db;

-- --------------------------------------------------------
-- Table: staff (login users)
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS staff (
    staff_id   INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    email      VARCHAR(100),
    phone      VARCHAR(15),
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(100) NOT NULL,
    role       ENUM('Admin', 'Staff') DEFAULT 'Staff'
);

-- --------------------------------------------------------
-- Table: guest
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS guest (
    guest_id   INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    email      VARCHAR(100),
    phone      VARCHAR(15),
    nic        VARCHAR(15)  NOT NULL UNIQUE,
    address    TEXT
);

-- --------------------------------------------------------
-- Table: room
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS room (
    room_id        INT AUTO_INCREMENT PRIMARY KEY,
    room_number    VARCHAR(10)  NOT NULL UNIQUE,
    type           ENUM('Single', 'Double', 'Suite') NOT NULL,
    price_per_night DECIMAL(10,2) NOT NULL,
    status         ENUM('Available', 'Booked', 'Maintenance') DEFAULT 'Available'
);

-- --------------------------------------------------------
-- Table: booking
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS booking (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    guest_id   INT  NOT NULL,
    room_id    INT  NOT NULL,
    check_in   DATE NOT NULL,
    check_out  DATE NOT NULL,
    status     ENUM('Confirmed', 'CheckedIn', 'CheckedOut', 'Cancelled') DEFAULT 'Confirmed',
    FOREIGN KEY (guest_id) REFERENCES guest(guest_id) ON DELETE CASCADE,
    FOREIGN KEY (room_id)  REFERENCES room(room_id)   ON DELETE CASCADE
);

-- --------------------------------------------------------
-- Table: payment
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS payment (
    payment_id   INT AUTO_INCREMENT PRIMARY KEY,
    booking_id   INT            NOT NULL,
    amount       DECIMAL(10,2)  NOT NULL,
    method       ENUM('Cash', 'Card', 'Online') NOT NULL,
    payment_date DATE           NOT NULL,
    status       ENUM('Paid', 'Pending', 'Failed') DEFAULT 'Pending',
    FOREIGN KEY (booking_id) REFERENCES booking(booking_id) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- Sample Data
-- --------------------------------------------------------

-- Default admin login: username = admin, password = admin123
INSERT INTO staff (first_name, last_name, email, phone, username, password, role) VALUES
('Admin', 'User', 'admin@guesthouse.com', '0771234567', 'admin', 'admin123', 'Admin'),
('Staff', 'Member', 'staff@guesthouse.com', '0777654321', 'staff', 'staff123', 'Staff');

-- Sample rooms
INSERT INTO room (room_number, type, price_per_night, status) VALUES
('A101', 'Single', 3500.00, 'Available'),
('A102', 'Double', 5500.00, 'Available'),
('A103', 'Suite',  9500.00, 'Available'),
('B101', 'Single', 3500.00, 'Available'),
('B102', 'Double', 5500.00, 'Available');

-- Sample guests
INSERT INTO guest (first_name, last_name, email, phone, nic, address) VALUES
('Kamal', 'Perera', 'kamal@email.com', '0711234567', '199512345678', 'Colombo 03'),
('Nimal', 'Silva',  'nimal@email.com', '0722345678', '198723456789', 'Kandy');
