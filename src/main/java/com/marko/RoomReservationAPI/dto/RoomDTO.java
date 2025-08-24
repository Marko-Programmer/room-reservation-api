package com.marko.RoomReservationAPI.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;

public class RoomDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Min(value = 1, message = "Capacity must be at least 1")
    @Max(value = 500, message = "Capacity cannot exceed 500")
    private int capacity;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Available status must be set")
    private Boolean available;

    public RoomDTO() { }

    public RoomDTO(Long id, String name, int capacity, String location, Boolean available) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.location = location;
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
