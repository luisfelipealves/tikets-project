package com.tickets.booking.service;

import com.tickets.booking.domain.BookingHistory;
import com.tickets.booking.domain.BookingStatus;
import com.tickets.booking.mapper.BookingMapper;
import com.tickets.booking.repository.BookingHistoryRepository;
import com.tickets.management.dto.TicketReservationCreatedEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.LocalDateTime;

@Service
public class BookingService {

    private final BookingHistoryRepository repository;
    private final BookingMapper mapper;
    private final Logger log = LoggerFactory.getLogger(BookingService.class);

    public BookingService(BookingHistoryRepository repository, BookingMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public BookingHistory processReservation(TicketReservationCreatedEventDTO dto) {
        BookingHistory entity = mapper.toEntity(dto);
        entity.setEventId(dto.eventId());
        entity.setProcessedAt(LocalDateTime.now(ZoneId.of("UTC")));
        entity.setStatus(BookingStatus.PROCESSED);

        BookingHistory saved = repository.save(entity);
        log.info("Saved BookingHistory id={} eventName={} eventName='{}' totalSeats={} status={}",
                saved.getId(), saved.getEventName(), saved.getEventName(), saved.getTotalSeats(), saved.getStatus());
        return saved;
    }
}
