package com.project.ticketing_concurrency_lab.repository;

import com.project.ticketing_concurrency_lab.domain.Ticket;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from Ticket t where t.id=:ticketId")
    Optional<Ticket> findByIdWithLock(@Param("ticketId") long ticketId);

}
