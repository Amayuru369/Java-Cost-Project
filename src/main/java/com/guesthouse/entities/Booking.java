package com.guesthouse.entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Booking {
    private int       bookingId;
    private int       guestId;
    private int       roomId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String    status;  // "Confirmed", "CheckedIn", "CheckedOut", "Cancelled"

    public Booking(int bookingId, int guestId, int roomId,
                   LocalDate checkIn, LocalDate checkOut, String status) {
        this.bookingId = bookingId;
        this.guestId   = guestId;
        this.roomId    = roomId;
        this.checkIn   = checkIn;
        this.checkOut  = checkOut;
        this.status    = status;
    }

    public long getNights() {
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    public int       getBookingId() { return bookingId; }
    public int       getGuestId()   { return guestId; }
    public int       getRoomId()    { return roomId; }
    public LocalDate getCheckIn()   { return checkIn; }
    public LocalDate getCheckOut()  { return checkOut; }
    public String    getStatus()    { return status; }

    public void setStatus(String status)       { this.status = status; }
    public void setCheckIn(LocalDate checkIn)  { this.checkIn = checkIn; }
    public void setCheckOut(LocalDate checkOut){ this.checkOut = checkOut; }

    @Override
    public String toString() {
        return "Booking #" + bookingId + " | Guest: " + guestId +
               " | Room: " + roomId + " | " + checkIn + " to " + checkOut +
               " (" + getNights() + " nights) | " + status;
    }
}
