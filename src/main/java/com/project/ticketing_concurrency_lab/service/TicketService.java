package com.project.ticketing_concurrency_lab.service;

import com.project.ticketing_concurrency_lab.domain.IssuedTicket;
import com.project.ticketing_concurrency_lab.domain.Ticket;
import com.project.ticketing_concurrency_lab.domain.User;
import com.project.ticketing_concurrency_lab.repository.IssuedTicketRepository;
import com.project.ticketing_concurrency_lab.repository.TicketRepository;
import com.project.ticketing_concurrency_lab.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@Slf4j
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final IssuedTicketRepository issuedTicketRepository;


    @PersistenceContext
    private EntityManager entityManager;

    private void logDatabase (String operaion) {
        Object serverId=  entityManager
                .createNativeQuery("SELECT @@server_id")
                .getSingleResult();
        log.info("DB 연결 확인 operation={}, serverId={}", operaion, serverId);
    }

    @Transactional
    public void issue(Long ticketId, Long userId) {
        logDatabase("issue");

        Ticket ticket = ticketRepository.findByIdWithLock(ticketId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        ticket.decreaseStock(1);
        issuedTicketRepository.save(IssuedTicket.createIssued(ticket,user));

    }

    public Ticket findTicket(Long ticketId) {
        logDatabase("findTicket");
        return ticketRepository.findById(ticketId).orElseThrow();
    }

    @Transactional
    public Long createTicket(String name, Integer totalTicket) {
        logDatabase("createTicket");
        Ticket ticket = Ticket.create(name, totalTicket);
        return ticketRepository.save(ticket).getId();
    }

}
