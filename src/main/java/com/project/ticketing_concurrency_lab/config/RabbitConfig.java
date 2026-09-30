package com.project.ticketing_concurrency_lab.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String QUEUE= "ticket.issue.queue";
    public static final String EXCHANGE = "ticket.exchange";
    public static final String ROUTING_KEY= "ticket.issue";


    @Bean
    public Queue ticketQueue() {
        return new Queue(QUEUE, true);
    }
    @Bean
    public DirectExchange ticketExchange() {
        return  new DirectExchange(EXCHANGE);
    }
    @Bean
    public Binding ticketBinding(
            Queue ticketQueue,
            DirectExchange ticketExchange
    ) {
        return BindingBuilder.bind(ticketQueue)
                .to(ticketExchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

}
