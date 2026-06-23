package com.guesthouse.entities;

public class Room {
    private int    roomId;
    private String roomNumber;
    private String type;          // "Single", "Double", "Suite"
    private double pricePerNight;
    private String status;        // "Available", "Booked", "Maintenance"

    public Room(int roomId, String roomNumber, String type,
                double pricePerNight, String status) {
        this.roomId        = roomId;
        this.roomNumber    = roomNumber;
        this.type          = type;
        this.pricePerNight = pricePerNight;
        this.status        = status;
    }

    public boolean isAvailable() {
        return "Available".equals(status);
    }

    public int    getRoomId()        { return roomId; }
    public String getRoomNumber()    { return roomNumber; }
    public String getType()          { return type; }
    public double getPricePerNight() { return pricePerNight; }
    public String getStatus()        { return status; }

    public void setType(String type)               { this.type = type; }
    public void setPricePerNight(double price)     { this.pricePerNight = price; }
    public void setStatus(String status)           { this.status = status; }

    @Override
    public String toString() {
        return "Room " + roomNumber + " | " + type + " | Rs. " + pricePerNight + " | " + status;
    }
}
