package br.ce.wcaquino.taskbackend.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ce.wcaquino.taskbackend.model.Course;

public interface CourseRepo extends JpaRepository<Course, Long> {

	List<Course> findByNameContainingIgnoreCase(String name);

	List<Course> findByCategoryIgnoreCase(String category);
}
