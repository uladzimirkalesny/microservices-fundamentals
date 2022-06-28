package com.epam.resourceprocessor.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
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
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        var fanoutName = environment.getProperty("rabbitmq.exchange.name", String.class);
        return new FanoutExchange(fanoutName, true, false);
    }

    @Bean
    public Queue queue() {
        var queueName = environment.getProperty("rabbitmq.queue.name", String.class);
        return QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", deadLetterFanoutExchange().getName())
                .build();
    }

    @Bean
    public Binding binding(FanoutExchange fanoutExchange, Queue queue) {
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }

    @Bean
    public FanoutExchange deadLetterFanoutExchange() {
        var dlxName = String.join("", environment.getProperty("rabbitmq.exchange.name", String.class), ".dlx");
        return new FanoutExchange(dlxName);
    }

    @Bean
    public Queue deadLetterQueue() {
        var dlqName = String.join("", environment.getProperty("rabbitmq.queue.name", String.class), ".dlq");
        return new Queue(dlqName, true, false, false);
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(fanoutExchange());
    }
}
