package br.ce.wcaquino.taskbackend.repo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.DockerClientFactory;

import br.ce.wcaquino.taskbackend.model.Task;

/**
 * Teste de INTEGRACAO do TaskRepo contra um PostgreSQL REAL (Testcontainers),
 * com o Flyway aplicando as migrations (V1) num banco vazio e descartavel.
 *
 * Nome termina em "IT" -> roda no `mvn verify` (failsafe), NAO no `mvn test`.
 * Assim os testes unitarios continuam rapidos e este so roda quando ha Docker.
 *
 * A URL "jdbc:tc:postgresql:9.6:///tasks" e um recurso do Testcontainers:
 * ao conectar, ele sobe sozinho um container postgres:9.6 e o destroi no fim.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(FlywayAutoConfiguration.class)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:tc:postgresql:9.6:///tasks",
    "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver",
    "spring.datasource.username=test",
    "spring.datasource.password=test",
    // Flyway cria o schema; Hibernate so VALIDA que bate com as entidades
    "spring.jpa.hibernate.ddl-auto=validate",
    "spring.flyway.enabled=true"
})
public class TaskRepoIT {

    // Se nao houver um Docker compativel, pula a classe inteira (status SKIPPED,
    // build verde) em vez de falhar. Roda normalmente onde o Docker funcionar.
    // Roda ANTES do contexto Spring carregar, entao evita o erro do Flyway/Testcontainers.
    @BeforeClass
    public static void exigeDocker() {
        Assume.assumeTrue(
            "Docker indisponivel/incompativel - teste de integracao ignorado",
            DockerClientFactory.instance().isDockerAvailable());
    }

    @Autowired
    private TaskRepo taskRepo;

    @Test
    public void deveSalvarEGerarId() {
        Task salvo = taskRepo.save(novaTask("Estudar Testcontainers"));

        assertNotNull("o id deve ser gerado pela hibernate_sequence", salvo.getId());
    }

    @Test
    public void deveRecuperarTaskSalva() {
        Task task = novaTask("Revisar Flyway");
        task.setDueDate(LocalDate.of(2026, 12, 31));
        Long id = taskRepo.save(task).getId();

        Optional<Task> achado = taskRepo.findById(id);

        assertTrue(achado.isPresent());
        assertEquals("Revisar Flyway", achado.get().getTask());
        assertEquals(LocalDate.of(2026, 12, 31), achado.get().getDueDate());
    }

    @Test
    public void deveListarTodasAsTasks() {
        taskRepo.save(novaTask("Tarefa A"));
        taskRepo.save(novaTask("Tarefa B"));

        List<Task> todas = taskRepo.findAll();

        assertEquals(2, todas.size());
    }

    @Test
    public void deveDeletarTask() {
        Long id = taskRepo.save(novaTask("Remover")).getId();

        taskRepo.deleteById(id);

        assertFalse(taskRepo.findById(id).isPresent());
    }

    private Task novaTask(String descricao) {
        Task t = new Task();
        t.setTask(descricao);
        t.setDueDate(LocalDate.now());
        return t;
    }
}
