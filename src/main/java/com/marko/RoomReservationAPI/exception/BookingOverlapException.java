package com.marko.RoomReservationAPI.exception;

public class BookingOverlapException extends RuntimeException {
    public BookingOverlapException(String message) {
        super(message);
    }
}
