package com.tickets.management.dto;

import java.time.LocalDateTime;

public record TicketReservationCreatedEventDTO(
        String eventId, // UUID or numeric id as string
        String eventName,
        Integer totalSeats,
        LocalDateTime timestamp // ISO-8601 LocalDateTime
) {
}
