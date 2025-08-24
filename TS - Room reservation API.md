# Room Reservation System — Technical Specification (TS)

## Overview

This system allows users to view rooms, book them, and manage their bookings. Administrators can manage both rooms and bookings.

---

## Core Entities

### 1. **Room**

* `id` (Long, unique identifier)
* `name` (String) — room name
* `capacity` (int) — maximum number of people
* `location` (String) — e.g., floor or building
* `available` (Boolean) — indicates if the room is available for booking

### 2. **Booking**

* `id` (Long)
* `room` (Room) — linked room
* `user` (String) — user name who booked
* `startTime` (LocalDateTime) — start time of booking
* `endTime` (LocalDateTime) — end time of booking
* `status` (Enum: CONFIRMED, CANCELLED)

---

## Functional Requirements

### 1. Room CRUD

* Create, update, delete rooms
* Search/filter rooms:

  * by availability (`available`)
  * by location
  * by name (partial match)
* Pagination and sorting by `name`, `capacity`, `location`

### 2. Booking Management

* Create a booking (if room is available and no time overlap exists)
* View user bookings with pagination
* Cancel a booking
* Check room availability for a given period
* Update booking status (CONFIRMED, CANCELLED)

### 3. Transactions & Isolation

* Booking creation must be transactional: check availability + save booking
* Use appropriate isolation level to avoid concurrency issues (e.g., phantom reads)
* Cancellation should also be transactional

### 4. REST API Examples

| Method | Path                          | Description                            | Params / Body                                                      |
| ------ | ----------------------------- | -------------------------------------- | ------------------------------------------------------------------ |
| GET    | `/rooms`                      | Get list of rooms (paginated & sorted) | `page`, `size`, `sortBy`, `order`, `available`, `location`, `name` |
| POST   | `/rooms`                      | Create a room                          | JSON Room                                                          |
| PUT    | `/rooms/{id}`                 | Update a room                          | JSON Room                                                          |
| DELETE | `/rooms/{id}`                 | Delete a room                          | -                                                                  |
| GET    | `/bookings`                   | Get user bookings                      | `user`, `page`, `size`                                             |
| POST   | `/bookings`                   | Create a booking                       | JSON Booking (`roomId`, `user`, `startTime`, `endTime`)            |
| PUT    | `/bookings/{id}/cancel`       | Cancel a booking                       | -                                                                  |
| GET    | `/bookings/{id}/availability` | Check room availability                | `startTime`, `endTime`                                             |

### 5. DTOs

* Return DTOs instead of entities to avoid exposing unnecessary data:

  * `RoomDTO`: `id`, `name`, `capacity`, `location`, `available`
  * `BookingDTO`: `id`, `roomName`, `user`, `startTime`, `endTime`, `status`

### 6. Validation

* Start time < end time
* Cannot book unavailable or already booked rooms
* Validate request body in controllers (`@Valid`, field constraints)

---

## Technical Notes

* Spring Data JPA with `JpaRepository`
* Use `@Query` for complex queries (availability check, search)
* Pagination & sorting via `Pageable`
* Transactions via `@Transactional`
* Use DTOs to return data from services to controllers
* Validation with Hibernate Validator (`@Valid`, `@NotNull`, custom checks)

---

## Example Workflow

1. GET `/rooms?page=0&size=5&sortBy=capacity&order=asc&available=true&location=1st Floor` → list of rooms
2. GET `/rooms/{id}/availability?startTime=2025-08-01T10:00&endTime=2025-08-01T12:00` → check availability
3. POST `/bookings` `{ "roomId": 12, "user": "Marko", "startTime": "...", "endTime": "..." }` → create booking
4. Transactional booking creation checks for overlap
5. GET `/bookings?user=Marko&page=0&size=5` → view user bookings
6. PUT `/bookings/{id}/cancel` → cancel booking
