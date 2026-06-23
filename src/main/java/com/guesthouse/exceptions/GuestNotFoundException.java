package com.guesthouse.exceptions;

public class GuestNotFoundException extends Exception {
    public GuestNotFoundException(int guestId) {
        super("Guest with ID " + guestId + " was not found.");
    }

    public GuestNotFoundException(String message) {
        super(message);
    }
}
