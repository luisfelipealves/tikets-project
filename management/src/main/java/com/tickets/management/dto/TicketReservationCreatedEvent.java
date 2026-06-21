package com.tickets.management.dto;

public class TicketReservationCreatedEvent {

    private Long seatId;
    private String userId;

    public TicketReservationCreatedEvent() {
        // Default constructor for serialization
    }

    public TicketReservationCreatedEvent(Long seatId, String userId) {
        this.seatId = seatId;
        this.userId = userId;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
