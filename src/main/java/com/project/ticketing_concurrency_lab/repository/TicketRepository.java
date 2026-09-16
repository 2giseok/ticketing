package com.project.ticketing_concurrency_lab.repository;

import com.project.ticketing_concurrency_lab.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket,Long> {

}
