package com.tickets.management.dto;

import java.time.LocalDateTime;

public record TicketReservationCreatedEvent(
        String eventId,
        String eventName,
        Integer totalSeats,
        LocalDateTime timestamp) {
}
