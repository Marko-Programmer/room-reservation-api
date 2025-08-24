package com.marko.RoomReservationAPI.controller;

import com.marko.RoomReservationAPI.dto.RoomDTO;
import com.marko.RoomReservationAPI.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }


    //      GET

    @GetMapping()
    ResponseEntity<Page<RoomDTO>> getAllRooms(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "id") String sortBy,
                                              @RequestParam(defaultValue = "asc") String order,
                                              @RequestParam(required = false) Boolean available,
                                              @RequestParam(required = false) String location,
                                              @RequestParam(required = false) String name) {
        Page<RoomDTO> rooms = roomService.getAllRooms(page, size, sortBy, order, available, location, name);
        return ResponseEntity.ok(rooms);
    }


//      POST

    @PostMapping()
    ResponseEntity<RoomDTO> createRoom(@Valid @RequestBody RoomDTO roomDTO) {
        RoomDTO createdRoom = roomService.createRoom(roomDTO);
        return ResponseEntity
                .created(URI.create("/rooms/" + createdRoom.getId()))
                .body(createdRoom);
    }


//      PUT

    @PutMapping("/{id}")
    ResponseEntity<RoomDTO> updateRoom(@PathVariable long id, @Valid @RequestBody RoomDTO roomDTO) {
        RoomDTO updatedRoom = roomService.updateRoom(id, roomDTO);
        return ResponseEntity.ok(updatedRoom);
    }


    //     DELETE

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteRoom(@PathVariable long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }


}