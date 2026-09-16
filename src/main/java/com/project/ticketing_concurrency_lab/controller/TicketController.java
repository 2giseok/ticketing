package com.project.ticketing_concurrency_lab.controller;

import com.project.ticketing_concurrency_lab.controller.dto.CreateTicketRequest;
import com.project.ticketing_concurrency_lab.controller.dto.IssueTicketRequest;
import com.project.ticketing_concurrency_lab.controller.dto.TicketResponse;
import com.project.ticketing_concurrency_lab.domain.Ticket;
import com.project.ticketing_concurrency_lab.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/{ticketId}")
    public TicketResponse getTiekcet(@PathVariable Long ticketId) {
        Ticket ticket = ticketService.findTicket(ticketId);
        return  TicketResponse.from(ticket);
    }

    @PostMapping("/{ticketId}/issue")
    public void ticketing(@PathVariable Long ticketId,
           @RequestBody IssueTicketRequest request) {
        ticketService.issue(ticketId, request.userId());
    }
    @PostMapping("/create")
    public Long createTicket(@RequestBody CreateTicketRequest request) {

      return ticketService.createTicket(request.name(), request.totalTicket());
    }


}
