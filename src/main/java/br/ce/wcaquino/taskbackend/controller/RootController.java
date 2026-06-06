package br.ce.wcaquino.taskbackend.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value ="/")
public class RootController {

	@GetMapping
	public String hello() {
		return "Hello World!";
	}

	// endpoint temporario de debug — mostra o que o Spring extraiu do JWT
	@GetMapping("debug/me")
	public Map<String, Object> me(Authentication authentication) {
		Map<String, Object> info = new HashMap<>();
		if (authentication == null) {
			info.put("authentication", "null");
			return info;
		}
		info.put("name", authentication.getName());
		info.put("authenticated", authentication.isAuthenticated());
		info.put("authorities", authentication.getAuthorities().stream()
			.map(a -> a.getAuthority())
			.collect(Collectors.toList()));
		info.put("principal_class", authentication.getPrincipal().getClass().getSimpleName());
		return info;
	}
}
