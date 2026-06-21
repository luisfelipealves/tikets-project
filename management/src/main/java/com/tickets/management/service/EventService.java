package com.tickets.management.service;

import com.tickets.management.dto.TicketReservationCreatedEvent;
import com.tickets.management.model.Event;
import com.tickets.management.model.Seat;
import com.tickets.management.repository.EventRepository;
import com.tickets.management.repository.SeatRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Event event = new Event(name, LocalDateTime.now());

        for (int index = 1; index <= totalSeats; index++) {
            String seatNumber = "Seat-" + index;
            Seat seat = new Seat(seatNumber);
            event.addSeat(seat);
        }

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

        kafkaTemplate.send(
                "ticket-reservation-created",
                new TicketReservationCreatedEvent(seatId, userId));
    }
}
