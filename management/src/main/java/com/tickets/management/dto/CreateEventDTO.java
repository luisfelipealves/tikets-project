package com.tickets.management.dto;

public class CreateEventDTO {

    private String name;
    private int totalSeats;

    public CreateEventDTO() {
        // Default constructor for deserialization
    }

    public CreateEventDTO(String name, int totalSeats) {
        this.name = name;
        this.totalSeats = totalSeats;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }
}
