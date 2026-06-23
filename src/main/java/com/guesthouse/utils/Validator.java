/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.guesthouse.utils;

public class Validator {

    public static boolean isEmptyOrNull(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^\\d{10}$");
    }

    public static boolean isValidNIC(String nic) {
        // Sri Lanka NIC: 9 digits + V/X  OR  12 digits
        return nic != null && (nic.matches("^\\d{9}[VvXx]$") || nic.matches("^\\d{12}$"));
    }

    public static boolean isPositiveNumber(String value) {
        try {
            return Double.parseDouble(value) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidRoomNumber(String roomNumber) {
        return roomNumber != null && roomNumber.matches("^[A-Z]\\d{3}$");
    }
}
