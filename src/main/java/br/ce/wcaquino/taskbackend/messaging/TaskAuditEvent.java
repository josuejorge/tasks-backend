package br.ce.wcaquino.taskbackend.messaging;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TaskAuditEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	private String action;          // TASK_CREATED | TASK_DELETED
	private Long taskId;
	private String taskDescription;
	private LocalDateTime occurredAt;

	public TaskAuditEvent() {}

	public TaskAuditEvent(String action, Long taskId, String taskDescription) {
		this.action = action;
		this.taskId = taskId;
		this.taskDescription = taskDescription;
		this.occurredAt = LocalDateTime.now();
	}

	public String getAction() { return action; }
	public void setAction(String action) { this.action = action; }
	public Long getTaskId() { return taskId; }
	public void setTaskId(Long taskId) { this.taskId = taskId; }
	public String getTaskDescription() { return taskDescription; }
	public void setTaskDescription(String taskDescription) { this.taskDescription = taskDescription; }
	public LocalDateTime getOccurredAt() { return occurredAt; }
	public void setOccurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; }
}
