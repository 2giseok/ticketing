package com.project.ticketing_concurrency_lab.controller.dto;


import com.project.ticketing_concurrency_lab.domain.Ticket;

public record TicketResponse(
        Long id,
        String name,
        Integer totalTicket,
        Integer quantityTicket
) {

    public static TicketResponse from(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getName(),
                ticket.getTotalTicket(),
                ticket.getQuantityTicket()
        );
    }

}
