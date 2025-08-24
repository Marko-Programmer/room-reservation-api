# Room Reservation API

## Overview
**Room Reservation API** is a Spring Boot RESTful application for managing room bookings.  
It allows viewing available rooms, creating and canceling bookings, and checking room availability.  

Key features include:
- CRUD operations for rooms with filtering, pagination, and sorting.
- Booking management with validation to prevent overlapping bookings.
- Availability checks for rooms in a specified time range.
- DTO-based API responses for clean and safe data transfer.
- Global exception handling with meaningful HTTP status codes.


## Technologies & Tools

**Programming Languages:** Java, SQL  
**Frameworks & Libraries:** Spring Boot, Spring Data JPA, Hibernate, Jakarta Validation  
**Databases:** MySQL
**Web Development:** REST API  
**Other Tools & Technologies:** Git, Maven  


## Features

### Room Management
- Create, update, and delete rooms.
- Filter rooms by availability, location, or name.
- Pagination and sorting by id, name, capacity, or location.

### Booking Management
- Create bookings with validation to prevent overlaps.
- Cancel bookings safely.
- Check room availability for a given time period.
- Transactional operations with **Serializable isolation** to prevent concurrency issues.


## API Endpoints

| Method  | Path                        | Description                                  | Parameters / Body                                                   |
| ------- | --------------------------- | -------------------------------------------- | ------------------------------------------------------------------ |
| GET     | `/rooms`                    | Get list of rooms                             | `page`, `size`, `sortBy`, `order`, `available`, `location`, `name` |
| POST    | `/rooms`                    | Create a room                                 | JSON RoomDTO                                                       |
| PUT     | `/rooms/{id}`               | Update a room                                 | JSON RoomDTO                                                       |
| DELETE  | `/rooms/{id}`               | Delete a room                                 |                                                                    |
| GET     | `/bookings`                 | Get bookings with pagination                  | `user`, `page`, `size`                                            |
| POST    | `/bookings`                 | Create a booking                              | JSON BookingDTO (roomId, user, startTime, endTime)                 |
| PUT     | `/bookings/{id}/cancel`     | Cancel a booking                              |                                                                    |
| GET     | `/bookings/{id}/availability` | Check room availability for a period         | `startTime`, `endTime`                                            |

---

## Architecture & Design
- **DTOs (RoomDTO, BookingDTO):** Ensure clean API responses without exposing internal models.  
- **Validation:** Input validation via `@Valid` and Jakarta Validation annotations.  
- **Error Handling:** Global `@ControllerAdvice` handles exceptions with proper HTTP status codes (400, 404, 409).  
- **Repository Layer:** Spring Data JPA repositories with custom queries for filtering and checking overlaps.  
- **Concurrency Control:** Serializable transactions for safe booking creation and cancellation.  



## Example Usage
1. Get available rooms with pagination and filtering:  
GET /rooms?page=0&size=5&sortBy=capacity&order=asc&available=true&location=1st%20Floor

2. Check availability for a room:
GET /bookings/12/availability?startTime=2025-08-01T10:00&endTime=2025-08-01T12:00


3. Create a booking:
POST /bookings
{
  "roomId": 12,
  "user": "Marko",
  "startTime": "2025-08-01T10:00",
  "endTime": "2025-08-01T12:00"
}

4. Cancel a booking:
PUT /bookings/5/cancel


# Why This Project Stands Out

- Strong backend architecture with layered design (**Controller → Service → Repository**).  
- Transactional safety with concurrency handling to prevent double bookings.  
- Clean API design using DTOs and pagination/sorting for large datasets.  
- Professional error handling with descriptive messages.  
- Demonstrates full Java backend skillset: **Spring Boot, JPA, validation, REST, exception handling**.  
