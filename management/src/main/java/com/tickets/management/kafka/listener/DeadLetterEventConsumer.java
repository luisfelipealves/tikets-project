package com.tickets.management.kafka.listener;

import com.tickets.management.dto.TicketReservationCreatedEvent;
import com.tickets.management.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class DeadLetterEventConsumer {

    private final EventService eventService;
    private final Logger log = LoggerFactory.getLogger(DeadLetterEventConsumer.class);

    public DeadLetterEventConsumer(EventService eventService) {
        this.eventService = eventService;
    }

    @KafkaListener(topics = "ticket-reservation-created.DLT", groupId = "management-service-dlt-group", containerFactory = "kafkaListenerContainerFactory")
    public void onDeadLetter(@Payload TicketReservationCreatedEvent event) {
        log.warn("Dead-letter received for TicketReservationCreatedEvent, canceling reservation: eventId={} eventName={} seatId={} timestamp={}",
                event.eventId(), event.eventName(), event.seatId(), event.timestamp());

        try {
            eventService.cancelReservation(Long.valueOf(event.seatId()));
        } catch (Exception ex) {
            log.error("Failed to cancel reservation for seat {} from DLT event: {}", event.seatId(), ex.getMessage(), ex);
            throw ex;
        }
    }
}
