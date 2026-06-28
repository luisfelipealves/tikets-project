package com.tickets.management.service;

import com.tickets.management.dto.TicketReservationCreatedEvent;
import com.tickets.management.model.Event;
import com.tickets.management.model.Seat;
import com.tickets.management.repository.EventRepository;
import com.tickets.management.repository.SeatRepository;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.IntStream;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;
    private final KafkaTemplate<String, TicketReservationCreatedEvent> kafkaTemplate;

    public EventService(
            EventRepository eventRepository,
            SeatRepository seatRepository,
            KafkaTemplate<String, TicketReservationCreatedEvent> kafkaTemplate) {
        this.eventRepository = eventRepository;
        this.seatRepository = seatRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public Event createEvent(String name, int totalSeats) {
        Event event = new Event(name, LocalDateTime.now(ZoneId.of("UTC")));

        IntStream.rangeClosed(1, totalSeats)
                .mapToObj(index -> new Seat("Seat-" + index))
                .forEach(event::addSeat);
        log.info("Event {} created with {} seats", name, totalSeats);
        return eventRepository.save(event);
    }

    @Transactional
    public void reserveSeat(Long seatId, String userId) {
        Seat seat = seatRepository.findByIdWithLock(seatId)
                .orElseThrow(() -> new IllegalArgumentException("Seat not found with id: " + seatId));

        if (seat.isReserved()) {
            throw new IllegalStateException("Seat " + seatId + " is already reserved.");
        }

        seat.reserve();
        seatRepository.save(seat);
        log.info("Seat {} reserved successfully for user {} on event {}", seatId, userId, seat.getEvent().getName());
        kafkaTemplate.send(
                "ticket-reservation-created",
                new TicketReservationCreatedEvent(
                        seat.getEvent().getId().toString(),
                        seat.getEvent().getName(),
                        seat.getId().intValue(),
                        LocalDateTime.now(ZoneId.of("UTC"))));
    }

    @Transactional
    public void cancelReservation(Long seatId) {
        Seat seat = seatRepository.findByIdWithLock(seatId)
                .orElseThrow(() -> new IllegalArgumentException("Seat not found with id: " + seatId));

        if (!seat.isReserved()) {
            throw new IllegalStateException("Seat " + seatId + " is not reserved.");
        }

        seat.release();
        seatRepository.save(seat);
        log.info("Seat {} reservation cancelled successfully for event {}", seatId, seat.getEvent().getName());
    }
}
