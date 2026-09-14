package com.project.ticketing.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Ticket {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private Integer totalTicket;
    private Integer quantityTicket;


    public  void decreaseStock(Integer quantity) {
        this.quantityTicket -= quantity;
    }

}
