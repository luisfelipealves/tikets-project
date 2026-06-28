package com.tickets.booking.kafka.listener;

import com.tickets.management.dto.TicketReservationCreatedEventDTO;
import com.tickets.booking.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class TicketReservationConsumer {

    private final BookingService bookingService;
    private final Logger log = LoggerFactory.getLogger(TicketReservationConsumer.class);

    public TicketReservationConsumer(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @KafkaListener(topics = "ticket-reservation-created", groupId = "booking-service-group", containerFactory = "kafkaListenerContainerFactory")
    public void onMessage(@Payload TicketReservationCreatedEventDTO dto) {
        log.info("Received TicketReservationCreatedEvent: eventId={} eventName={} totalSeats={} timestamp={}",
                dto.eventId(), dto.eventName(), dto.totalSeats(), dto.timestamp());

        try {
            bookingService.processReservation(dto);
        } catch (Exception ex) {
            log.error("Failed processing event eventId={}: {}", dto.eventId(), ex.getMessage(), ex);
            throw new RuntimeException("Failed processing TicketReservationCreatedEvent", ex);
        }
    }

    @KafkaListener(topics = "ticket-reservation-created.DLT", groupId = "booking-service-dlt-group", containerFactory = "kafkaListenerContainerFactory")
    public void onDeadLetter(@Payload TicketReservationCreatedEventDTO dto) {
        log.warn(
                "Dead-letter received for TicketReservationCreatedEvent: eventId={} eventName={} totalSeats={} timestamp={}",
                dto.eventId(), dto.eventName(), dto.totalSeats(), dto.timestamp());
    }
}
