package com.tickets.booking.service;

import com.tickets.booking.domain.BookingHistory;
import com.tickets.booking.domain.BookingStatus;
import com.tickets.booking.mapper.BookingMapper;
import com.tickets.booking.repository.BookingHistoryRepository;
import com.tickets.management.dto.TicketReservationCreatedEventDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingHistoryRepository repository;

    @Mock
    private BookingMapper mapper;

    @Test
    void shouldMarkBookingAsFailedWhenPaymentSimulationFails() {
        TicketReservationCreatedEventDTO dto = new TicketReservationCreatedEventDTO(
                "evt-1",
                "Demo Event",
                10,
                LocalDateTime.now());

        BookingHistory entity = new BookingHistory();
        entity.setEventId(dto.eventId());
        entity.setEventName(dto.eventName());
        entity.setTotalSeats(dto.totalSeats());

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(any(BookingHistory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookingService service = spy(new BookingService(repository, mapper));
        doReturn(false).when(service).simulatePayment();

        BookingHistory result = service.processReservation(dto);

        assertEquals(BookingStatus.FAILED, result.getStatus());
    }
}
