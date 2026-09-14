package com.project.ticketing.repository;

import com.project.ticketing.domain.IssuedTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssuedTicketRepository extends JpaRepository<IssuedTicket, Long> {

}
