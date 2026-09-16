package com.project.ticketing_concurrency_lab.repository;

import com.project.ticketing_concurrency_lab.domain.IssuedTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssuedTicketRepository extends JpaRepository<IssuedTicket, Long> {

}
