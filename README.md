# Guesthouse Management System

A Java Swing desktop application for managing guesthouse operations including guest registration, room management, bookings, check-in/check-out, and payments.

## Team Members

| Member | Branch | Module |
|--------|--------|--------|
| Member 1 | feature/login-auth      | Login + Authentication |
| Member 2 | feature/guest-module    | Guest Entity + Registration |
| Member 3 | feature/room-module     | Room Entity + Availability |
| Member 4 | feature/booking-module  | Booking + Check-in/out |
| Member 5 | feature/payment-module  | Payment + Billing |
| Member 6 (Leader) | feature/admin-dashboard | Admin Panel + GitHub |

## Technologies Used

- Java (JDK 11+)
- Java Swing (JFrame, JPanel, CardLayout)
- MySQL 8.0
- MySQL Connector/J (JDBC Driver)
- Git + GitHub

## Project Structure

```
GuesthouseManagementSystem/
├── src/
│   ├── entities/       Person.java  Guest.java  Staff.java  Room.java  Booking.java  Payment.java
│   ├── ui/             MainFrame.java  LoginPanel.java  GuestPanel.java  RoomPanel.java
│   │                   BookingPanel.java  PaymentPanel.java  AdminPanel.java
│   ├── dao/            DBConnection.java  GuestDAO.java  RoomDAO.java  BookingDAO.java  PaymentDAO.java
│   ├── exceptions/     InvalidLoginException.java  RoomNotAvailableException.java
│   │                   GuestNotFoundException.java  PaymentFailedException.java
│   └── utils/          Validator.java  DateUtils.java
├── database.sql
└── README.md
```

## Setup Instructions

### 1. Database Setup
1. Open MySQL Workbench (or terminal)
2. Run `database.sql`:
   ```
   mysql -u root -p < database.sql
   ```
3. This creates the `guesthouse_db` database with all tables and sample data.

### 2. Configure Database Connection
Open `src/dao/DBConnection.java` and update:
```java
private static final String URL      = "jdbc:mysql://localhost:3306/guesthouse_db";
private static final String USER     = "root";
private static final String PASSWORD = "your_actual_password";
```

### 3. Add MySQL Connector
Download `mysql-connector-j-8.x.x.jar` from https://dev.mysql.com/downloads/connector/j/
and add it to your project's classpath in your IDE.

### 4. Run the Application
Run `src/ui/MainFrame.java` as the main class.

### Default Login Credentials
| Role  | Username | Password  |
|-------|----------|-----------|
| Admin | admin    | admin123  |
| Staff | staff    | staff123  |

## GitHub Workflow

1. Each member works on their own feature branch
2. Never commit directly to `main` or `develop`
3. Push to your branch and open a Pull Request
4. Leader (Member 6) reviews and merges into `develop`
5. Final merge from `develop` → `main` before submission

## OOP Concepts Used

- **Abstraction** — abstract `Person` class
- **Inheritance** — `Guest` and `Staff` extend `Person`
- **Encapsulation** — all fields are private with getters/setters
- **Polymorphism** — `getRole()` overridden in each subclass
- **Custom Exceptions** — `InvalidLoginException`, `RoomNotAvailableException`, `GuestNotFoundException`, `PaymentFailedException`
