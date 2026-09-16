package com.project.ticketing_concurrency_lab.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Ticket {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private Integer totalTicket;
    private Integer quantityTicket;


    public  void decreaseStock(Integer quantity) {

        if( quantityTicket < quantity) {
            throw new IllegalStateException("재고가 부족합니다");
        }

        this.quantityTicket -= quantity;
    }

    public static Ticket create(String name, Integer totalTicket) {
        Ticket ticket = new Ticket();
        ticket.name=name;
        ticket.totalTicket=totalTicket;
        ticket.quantityTicket=totalTicket;

        return ticket;
    }

}
