package com.project.ticketing_concurrency_lab.service;

import com.project.ticketing_concurrency_lab.config.RabbitConfig;
import com.project.ticketing_concurrency_lab.service.dto.TicketIssueMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(Long ticketId, Long userId) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY,
                new TicketIssueMessage(ticketId,userId)
        );
    }

}
