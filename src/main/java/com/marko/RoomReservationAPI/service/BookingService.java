package com.marko.RoomReservationAPI.service;

import com.marko.RoomReservationAPI.dto.BookingDTO;
import com.marko.RoomReservationAPI.exception.BadRequestException;
import com.marko.RoomReservationAPI.exception.BookingOverlapException;
import com.marko.RoomReservationAPI.exception.ResourceNotFoundException;
import com.marko.RoomReservationAPI.model.Booking;
import com.marko.RoomReservationAPI.model.Room;
import com.marko.RoomReservationAPI.repository.BookingRepository;
import com.marko.RoomReservationAPI.repository.RoomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.marko.RoomReservationAPI.model.enums.BookingStatus.CANCELLED;
import static com.marko.RoomReservationAPI.model.enums.BookingStatus.CONFIRMED;

@Service
public class BookingService {

    BookingRepository bookingRepository;
    RoomRepository roomRepository;

    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
    }


    //       GET

    public Page<BookingDTO> getAllBookings(int page, int size, String user) {

        Pageable pageable = PageRequest.of(page, size);

        if (user != null) user = user.trim();

        Page<Booking> bookingsPage = bookingRepository.getAllBookings(user, pageable);

        return bookingsPage.map(b -> new BookingDTO(b.getId(), b.getRoom().getId(),
                b.getUser(), b.getStartTime(), b.getEndTime(), b.getStatus()));

    }


    public Boolean isRoomAvailable(long id, LocalDateTime startTime, LocalDateTime endTime) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        Boolean isOccupied  = bookingRepository.existsByRoomAndTimeRange(id, startTime, endTime);
        return !isOccupied;
    }



    //      POST

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public BookingDTO createBooking(BookingDTO bookingDTO) {

        if (bookingDTO.getStartTime().isAfter(bookingDTO.getEndTime())
                || bookingDTO.getStartTime().isEqual(bookingDTO.getEndTime())) {
            throw new BadRequestException("Start time must be before end time");
        }

        Room room = roomRepository.findById(bookingDTO.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));


        if(!room.getAvailable()){
            throw new BadRequestException("Room is not available");
        }


        boolean overlap = bookingRepository.existsByRoomAndTimeRange(room.getId(),
                bookingDTO.getStartTime(),bookingDTO.getEndTime());
        if (overlap) throw new BookingOverlapException("The booking is overlap");


        Booking booking = new Booking(room, bookingDTO.getUser(),
                bookingDTO.getStartTime(), bookingDTO.getEndTime(), CONFIRMED);

        Booking saved = bookingRepository.save(booking);

        return new BookingDTO(saved.getId(), saved.getRoom().getId(), saved.getUser(),
                saved.getStartTime(), saved.getEndTime(), saved.getStatus());
    }



    //     PUT

        @Transactional(isolation = Isolation.SERIALIZABLE)
        public BookingDTO cancelBooking(long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Booking with this ID."));

        if(booking.getStatus() == CANCELLED){
            throw new BadRequestException("Booking is already cancelled");
        } else  {
            booking.setStatus(CANCELLED);
        }

        bookingRepository.save(booking);

        return new BookingDTO(booking.getId(), booking.getRoom().getId(),
                booking.getUser(), booking.getStartTime(), booking.getEndTime(), booking.getStatus());
    }



}
