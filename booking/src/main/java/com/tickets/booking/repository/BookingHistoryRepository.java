package com.tickets.booking.repository;

import com.tickets.booking.domain.BookingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingHistoryRepository extends JpaRepository<BookingHistory, Long> {

}
