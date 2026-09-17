package com.project.ticketing_concurrency_lab.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class IssuedTicket {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Ticket ticket;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;


    public static IssuedTicket createIssued(Ticket ticket, User user) {

        IssuedTicket issuedTicket = new IssuedTicket();
        issuedTicket.ticket= ticket;
        issuedTicket.user=  user;

        return issuedTicket;
    }
}
