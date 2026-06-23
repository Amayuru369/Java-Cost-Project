package com.guesthouse.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class DateUtils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDate parseDate(String dateStr) throws DateTimeParseException {
        return LocalDate.parse(dateStr.trim(), FORMATTER);
    }

    public static String formatDate(LocalDate date) {
        return date.format(FORMATTER);
    }

    public static boolean isValidDateRange(LocalDate checkIn, LocalDate checkOut) {
        return checkIn != null && checkOut != null &&
               !checkIn.isBefore(LocalDate.now()) &&
               checkOut.isAfter(checkIn);
    }

    public static long getNights(LocalDate checkIn, LocalDate checkOut) {
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    public static double calculateTotalCost(LocalDate checkIn, LocalDate checkOut, double pricePerNight) {
        return getNights(checkIn, checkOut) * pricePerNight;
    }
}
