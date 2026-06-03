package br.ce.wcaquino.taskbackend.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	// nomes das filas e exchange — centralizados aqui para não espalhar strings pelo código
	public static final String EXCHANGE   = "tasks.exchange";
	public static final String QUEUE      = "tasks.audit.queue";
	public static final String ROUTING_KEY = "task.audit";

	// Exchange do tipo Direct: roteia pela routing key exata
	@Bean
	public DirectExchange exchange() {
		return new DirectExchange(EXCHANGE);
	}

	// Fila durável: sobrevive a restart do RabbitMQ
	@Bean
	public Queue auditQueue() {
		return QueueBuilder.durable(QUEUE).build();
	}

	// Binding: liga a fila ao exchange pela routing key
	@Bean
	public Binding binding(Queue auditQueue, DirectExchange exchange) {
		return BindingBuilder.bind(auditQueue).to(exchange).with(ROUTING_KEY);
	}

	// Converter JSON: mensagens trafegam como JSON (legível no painel do RabbitMQ)
	@Bean
	public Jackson2JsonMessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	// RabbitTemplate com o converter JSON configurado
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setMessageConverter(messageConverter());
		return template;
	}
}
