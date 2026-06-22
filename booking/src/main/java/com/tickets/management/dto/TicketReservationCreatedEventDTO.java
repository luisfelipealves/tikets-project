package com.tickets.management.dto;

import java.time.LocalDateTime;

public class TicketReservationCreatedEventDTO {
    private String eventId; // UUID or numeric id as string
    private String eventName;
    private Integer totalSeats;
    private LocalDateTime timestamp; // ISO-8601 LocalDateTime

    public TicketReservationCreatedEventDTO() {
    }

    public TicketReservationCreatedEventDTO(String eventId, String eventName, Integer totalSeats,
            LocalDateTime timestamp) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.totalSeats = totalSeats;
        this.timestamp = timestamp;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
