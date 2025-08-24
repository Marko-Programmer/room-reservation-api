package com.marko.RoomReservationAPI.controller;

import com.marko.RoomReservationAPI.dto.BookingDTO;
import com.marko.RoomReservationAPI.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("bookings")
public class BookingController {


private final BookingService bookingService;

public BookingController(BookingService bookingService) {
    this.bookingService = bookingService;
}


    @GetMapping()
    public ResponseEntity<Page<BookingDTO>> getAllBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String user) {
        Page<BookingDTO> bookings = bookingService.getAllBookings(page, size, user);
        return ResponseEntity.ok(bookings);
    }


        @GetMapping("/{id}/availability")
    ResponseEntity<Boolean> isRoomAvailable(@PathVariable long id,
                                            @RequestParam LocalDateTime startTime,
                                            @RequestParam LocalDateTime endTime){
        boolean available = bookingService.isRoomAvailable(id, startTime, endTime);
        return ResponseEntity.ok(available);
    }




    @PostMapping()
    public ResponseEntity<BookingDTO> createBooking(@Valid @RequestBody BookingDTO booking) {
        BookingDTO createdBooking = bookingService.createBooking(booking);
        return ResponseEntity
                .created(URI.create("/bookings/" + createdBooking.getId()))
                .body(createdBooking);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingDTO> cancelBooking(@PathVariable long id) {
        BookingDTO canceledBooking = bookingService.cancelBooking(id);
        return ResponseEntity.ok(canceledBooking);
    }






}


