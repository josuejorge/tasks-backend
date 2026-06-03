package br.ce.wcaquino.taskbackend.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.ce.wcaquino.taskbackend.config.RabbitMQConfig;
import br.ce.wcaquino.taskbackend.model.AuditLog;
import br.ce.wcaquino.taskbackend.repo.AuditLogRepo;

@Component
public class TaskAuditConsumer {

	@Autowired
	private AuditLogRepo auditLogRepo;

	@RabbitListener(queues = RabbitMQConfig.QUEUE)
	public void consume(TaskAuditEvent event) {
		System.out.println("[RabbitMQ] Recebido: " + event.getAction() + " | taskId=" + event.getTaskId());

		AuditLog log = new AuditLog(
			event.getAction(),
			event.getTaskId(),
			event.getTaskDescription()
		);

		auditLogRepo.save(log);
		System.out.println("[RabbitMQ] Auditoria gravada no Postgres | id=" + log.getId());
	}
}
