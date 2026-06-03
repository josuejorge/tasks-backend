package br.ce.wcaquino.taskbackend.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.ce.wcaquino.taskbackend.config.RabbitMQConfig;

@Component
public class TaskAuditProducer {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void taskCreated(Long taskId, String taskDescription) {
		TaskAuditEvent event = new TaskAuditEvent("TASK_CREATED", taskId, taskDescription);
		rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, event);
		System.out.println("[RabbitMQ] Publicado: " + event.getAction() + " | taskId=" + taskId);
	}

	public void taskDeleted(Long taskId) {
		TaskAuditEvent event = new TaskAuditEvent("TASK_DELETED", taskId, null);
		rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, event);
		System.out.println("[RabbitMQ] Publicado: " + event.getAction() + " | taskId=" + taskId);
	}
}
