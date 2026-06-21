package com.tickets.management.controller;

import com.tickets.management.dto.CreateEventDTO;
import com.tickets.management.dto.ReserveSeatDTO;
import com.tickets.management.model.Event;
import com.tickets.management.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody CreateEventDTO request) {
        Event event = eventService.createEvent(request.getName(), request.getTotalSeats());
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }

    @PostMapping("/reserve")
    public ResponseEntity<Void> reserveSeat(@RequestBody ReserveSeatDTO request) {
        eventService.reserveSeat(request.getSeatId(), request.getUserId());
        return ResponseEntity.ok().build();
    }
}
