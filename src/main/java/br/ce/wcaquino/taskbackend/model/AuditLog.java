package br.ce.wcaquino.taskbackend.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "audit_log")
public class AuditLog implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String action;       // TASK_CREATED | TASK_DELETED

	@Column(name = "task_id")
	private Long taskId;

	@Column(name = "task_description")
	private String taskDescription;

	@Column(name = "occurred_at", nullable = false)
	private LocalDateTime occurredAt = LocalDateTime.now();

	public AuditLog() {}

	public AuditLog(String action, Long taskId, String taskDescription) {
		this.action = action;
		this.taskId = taskId;
		this.taskDescription = taskDescription;
		this.occurredAt = LocalDateTime.now();
	}

	public Long getId() { return id; }
	public String getAction() { return action; }
	public void setAction(String action) { this.action = action; }
	public Long getTaskId() { return taskId; }
	public void setTaskId(Long taskId) { this.taskId = taskId; }
	public String getTaskDescription() { return taskDescription; }
	public void setTaskDescription(String taskDescription) { this.taskDescription = taskDescription; }
	public LocalDateTime getOccurredAt() { return occurredAt; }
	public void setOccurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; }
}
