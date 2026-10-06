package com.project.ticketing.service;

import static org.mockito.BDDMockito.then;

import com.project.ticketing_concurrency_lab.config.RabbitConfig;
import com.project.ticketing_concurrency_lab.service.TicketProducer;
import com.project.ticketing_concurrency_lab.service.dto.TicketIssueMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@ExtendWith(MockitoExtension.class)
class TicketProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;
    @InjectMocks
    private TicketProducer ticketProducer;

    @Test
    void send_test() {
        long ticketId = 1L;
        long userId = 10L;

        ticketProducer.send(ticketId,userId);

        then(rabbitTemplate).should().convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY,
                new TicketIssueMessage(ticketId,userId)
        );
    }

}