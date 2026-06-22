package com.tickets.booking.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking_history")
public class BookingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "event_id", nullable = false)
    private String eventId;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "total_seats")
    private Integer totalSeats;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus status;

    public BookingHistory() {
    }

    public BookingHistory(Long id, String eventId, String eventName, Integer totalSeats, LocalDateTime processedAt,
            BookingStatus status) {
        this.id = id;
        this.eventId = eventId;
        this.eventName = eventName;
        this.totalSeats = totalSeats;
        this.processedAt = processedAt;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}
