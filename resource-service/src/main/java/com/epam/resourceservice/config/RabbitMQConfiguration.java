package com.epam.resourceservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@RequiredArgsConstructor

@Configuration
public class RabbitMQConfiguration {

    private final Environment environment;

    @Bean
    public FanoutExchange exchange() {
        var exchangeName = environment.getProperty("rabbitmq.exchange.name", String.class);
        return new FanoutExchange(exchangeName, true, false);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setExchange(exchange().getName());
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

}
