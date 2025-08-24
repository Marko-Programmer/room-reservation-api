package com.marko.RoomReservationAPI.service;

import com.marko.RoomReservationAPI.dto.RoomDTO;
import com.marko.RoomReservationAPI.exception.BadRequestException;
import com.marko.RoomReservationAPI.exception.ResourceNotFoundException;
import com.marko.RoomReservationAPI.model.Room;
import com.marko.RoomReservationAPI.repository.RoomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }



    //      GET


    public Page<RoomDTO> getAllRooms(int page, int size, String sortBy, String order,
                                     Boolean available, String location, String name) {


        Set<String> allowed = Set.of("id", "name", "capacity", "location");

        if (!allowed.contains(sortBy)) {
            throw new BadRequestException("Invalid sortBy");
        }

        Sort sort = order.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        if (name != null) name = name.trim();
        if (location != null) location = location.trim();

        Page<Room> roomsPage = roomRepository.getAllRooms(available, location, name, pageable);

        return roomsPage.map(room -> new RoomDTO(room.getId(), room.getName(),
                room.getCapacity(), room.getLocation(), room.getAvailable()));

    }



    //      POST

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public RoomDTO createRoom(RoomDTO roomDTO) {
        Room room = new Room(roomDTO.getName(), roomDTO.getCapacity(), roomDTO.getLocation(), roomDTO.getAvailable());
        room = roomRepository.save(room);
        return new RoomDTO(room.getId(), room.getName(), room.getCapacity(), room.getLocation(), room.getAvailable());
    }


//      PUT


    public RoomDTO updateRoom(long id, RoomDTO roomDTO) {

        Room room = roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room not found."));

        room.setName(roomDTO.getName());
        room.setCapacity(roomDTO.getCapacity());
        room.setLocation(roomDTO.getLocation());
        room.setAvailable(roomDTO.getAvailable());
        room = roomRepository.save(room);

        return new RoomDTO(room.getId(), room.getName(), room.getCapacity(), room.getLocation(), room.getAvailable());
    }


    //     DELETE


    public void deleteRoom(long id) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room not found."));
        roomRepository.delete(room);
    }

}
