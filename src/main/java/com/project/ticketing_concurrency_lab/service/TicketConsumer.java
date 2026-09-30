package com.project.ticketing_concurrency_lab.service;

import com.project.ticketing_concurrency_lab.config.RabbitConfig;
import com.project.ticketing_concurrency_lab.service.dto.TicketIssueMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TicketConsumer {

    private final TicketService ticketService;

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void consume(TicketIssueMessage message) {
    try {
        ticketService.issue(
                message.ticketId(),
                message.userId()
        );
    } catch (IllegalStateException e) {
        log.info("재고 부족");
    }
    }

}
