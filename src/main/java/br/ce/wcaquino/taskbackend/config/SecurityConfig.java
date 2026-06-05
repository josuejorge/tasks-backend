package br.ce.wcaquino.taskbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			// REST API nao usa CSRF
			.csrf().disable()

			// sem sessao — cada request precisa do JWT
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()

			.authorizeRequests()
				// actuator liberado para Prometheus e health checks
				.antMatchers("/actuator/**").permitAll()
				// OPTIONS liberado para CORS preflight
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				// todo o resto precisa de JWT valido
				.anyRequest().authenticated()
			.and()

			// valida o JWT usando a chave publica do Keycloak (jwk-set-uri)
			.oauth2ResourceServer()
				.jwt();
	}
}
