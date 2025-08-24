package com.marko.RoomReservationAPI.repository;

import com.marko.RoomReservationAPI.model.Booking;
import com.marko.RoomReservationAPI.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {



    @Query("""
        SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
        FROM Booking b
        WHERE b.room.id = :roomId
          AND (:startTime < b.endTime AND :endTime > b.startTime)
          AND b.status = com.marko.RoomReservationAPI.model.enums.BookingStatus.CONFIRMED
    """)
    public Boolean existsByRoomAndTimeRange(
        @Param("roomId") Long roomId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
);


    @Query("SELECT b FROM Booking b "  +
            "WHERE :user IS NULL OR b.user = :user")
    Page<Booking> getAllBookings(@Param("user") String user, Pageable pageable);





}
