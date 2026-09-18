package com.project.ticketing_concurrency_lab.service;

import com.project.ticketing_concurrency_lab.domain.IssuedTicket;
import com.project.ticketing_concurrency_lab.domain.Ticket;
import com.project.ticketing_concurrency_lab.domain.User;
import com.project.ticketing_concurrency_lab.repository.IssuedTicketRepository;
import com.project.ticketing_concurrency_lab.repository.TicketRepository;
import com.project.ticketing_concurrency_lab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final IssuedTicketRepository issuedTicketRepository;

    @Transactional
    public void issue(Long ticketId, Long userId) {

        Ticket ticket = ticketRepository.findByIdWithLock(ticketId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        ticket.decreaseStock(1);
        issuedTicketRepository.save(IssuedTicket.createIssued(ticket,user));

    }

    public Ticket findTicket(Long ticketId) {
        return ticketRepository.findById(ticketId).orElseThrow();
    }

    @Transactional
    public Long createTicket(String name, Integer totalTicket) {
        Ticket ticket = Ticket.create(name, totalTicket);
        return ticketRepository.save(ticket).getId();
    }

}
