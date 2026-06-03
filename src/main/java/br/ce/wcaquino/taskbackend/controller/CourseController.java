package br.ce.wcaquino.taskbackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.ce.wcaquino.taskbackend.model.Course;
import br.ce.wcaquino.taskbackend.repo.CourseRepo;

@RestController
@RequestMapping("/courses")
public class CourseController {

	@Autowired
	private CourseRepo courseRepo;

	@GetMapping
	public List<Course> findAll() {
		return courseRepo.findAll();
	}

	@GetMapping("/search")
	public List<Course> search(@RequestParam String name) {
		return courseRepo.findByNameContainingIgnoreCase(name);
	}

	@GetMapping("/category/{category}")
	public List<Course> findByCategory(@PathVariable String category) {
		return courseRepo.findByCategoryIgnoreCase(category);
	}

	@PostMapping
	public ResponseEntity<Course> save(@RequestBody Course course) {
		if (course.getName() == null || course.getName().isBlank()) {
			return ResponseEntity.badRequest().build();
		}
		Course saved = courseRepo.save(course);
		return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		courseRepo.deleteById(id);
	}
}
