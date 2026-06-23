package com.guesthouse.entities;

public class Guest extends Person {
    private int    guestId;
    private String nic;
    private String address;

    public Guest(int guestId, String firstName, String lastName,
                 String email, String phone, String nic, String address) {
        super(firstName, lastName, email, phone);
        this.guestId = guestId;
        this.nic     = nic;
        this.address = address;
    }

    @Override
    public String getRole() { return "Guest"; }

    public int    getGuestId() { return guestId; }
    public String getNic()     { return nic; }
    public String getAddress() { return address; }

    public void setNic(String nic)         { this.nic = nic; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return "[" + guestId + "] " + getFirstName() + " " + getLastName() + " | NIC: " + nic;
    }
}
