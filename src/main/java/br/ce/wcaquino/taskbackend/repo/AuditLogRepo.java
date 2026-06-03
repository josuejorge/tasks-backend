package br.ce.wcaquino.taskbackend.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ce.wcaquino.taskbackend.model.AuditLog;

public interface AuditLogRepo extends JpaRepository<AuditLog, Long> {

	List<AuditLog> findByActionOrderByOccurredAtDesc(String action);
}
