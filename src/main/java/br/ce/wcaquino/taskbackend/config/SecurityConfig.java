package br.ce.wcaquino.taskbackend.config;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()

			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()

			.authorizeRequests()
				.antMatchers("/actuator/**").permitAll()
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

				// Swagger UI: recursos estaticos liberados (browser nao envia token)
				// a protecao por role fica nos endpoints da API em si
				.antMatchers("/swagger-ui/**", "/v2/api-docs", "/swagger-resources/**", "/webjars/**").permitAll()

				// endpoint de debug: qualquer JWT valido
				.antMatchers("/debug/me").authenticated()

				// Tasks: ADMIN e USER
				.antMatchers("/todo/**").hasAnyRole("ADMIN", "USER")

				// Courses: ADMIN e QA
				.antMatchers("/courses/**").hasAnyRole("ADMIN", "QA")

				.anyRequest().authenticated()
			.and()

			.oauth2ResourceServer()
				.jwt()
				.jwtAuthenticationConverter(keycloakJwtConverter());
	}

	@Bean
	public JwtAuthenticationConverter keycloakJwtConverter() {
		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
		// Keycloak coloca as roles em realm_access.roles — Spring nao le isso por padrao
		converter.setJwtGrantedAuthoritiesConverter(new KeycloakRolesConverter());
		return converter;
	}

	// Extrai roles de realm_access.roles e converte para ROLE_<NOME>
	static class KeycloakRolesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
		@Override
		public Collection<GrantedAuthority> convert(Jwt jwt) {
			Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
			if (realmAccess == null) return Collections.emptyList();

			List<String> roles = (List<String>) realmAccess.get("roles");
			if (roles == null) return Collections.emptyList();

			return roles.stream()
				.map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
				.collect(Collectors.toList());
		}
	}
}
