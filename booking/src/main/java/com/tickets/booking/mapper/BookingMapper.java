package com.tickets.booking.mapper;

import com.tickets.booking.domain.BookingHistory;
import com.tickets.management.dto.TicketReservationCreatedEventDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "processedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    BookingHistory toEntity(TicketReservationCreatedEventDTO dto);
}
