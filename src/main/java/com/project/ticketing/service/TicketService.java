package com.project.ticketing.service;

import com.project.ticketing.domain.IssuedTicket;
import com.project.ticketing.domain.Ticket;
import com.project.ticketing.domain.User;
import com.project.ticketing.repository.IssuedTicketRepository;
import com.project.ticketing.repository.TicketRepository;
import com.project.ticketing.repository.UserRepository;
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

        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        ticket.decreaseStock(1);
        issuedTicketRepository.save(IssuedTicket.createIssued(ticket,user));

    }

}
