package com.nadav.phase3.web;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nadav.phase3.facades.AdminFacade;
import com.nadav.phase3.facades.ClientFacade;
import com.nadav.phase3.facades.CompanyFacade;
import com.nadav.phase3.facades.CustomerFacade;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Aspect
public class WebConfig {
	@Around("execution(* com.nadav.phase3.web.AdminController.*(..))")
	public ResponseEntity<?> aroundAdmin(ProceedingJoinPoint point) throws Throwable {

		String token = (String) point.getArgs()[0];
		if (sessionsMap().containsKey(token)) {
			Session se = sessionsMap().get(token);
			ClientFacade admin = se.getService();
			if (admin instanceof AdminFacade && (System.currentTimeMillis() - se.getLastAccessed()) < 1000 * 30) {

				return (ResponseEntity<?>) point.proceed();

			} else {
				sessionsMap().remove(token);
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}

	@Around("execution(* com.nadav.phase3.web.CompanyController.*(..))")
	public ResponseEntity<?> aroundCompany(ProceedingJoinPoint point) throws Throwable {

		String token = (String) point.getArgs()[0];
		if (sessionsMap().containsKey(token)) {
			Session curSession = sessionsMap().get(token);
			// check the service
			ClientFacade company = curSession.getService();
			if (company instanceof CompanyFacade
					&& (System.currentTimeMillis() - curSession.getLastAccessed()) < 1000 * 130) {

				return (ResponseEntity<?>) point.proceed();

			} else {
				sessionsMap().remove(token);
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}

	@Around("execution(* com.nadav.phase3.web.CustomerController.*(..))")
	public ResponseEntity<?> authenticateCustomer(ProceedingJoinPoint point) throws Throwable {

		String token = (String) point.getArgs()[0];
		if (sessionsMap().containsKey(token)) {
			Session curSession = sessionsMap().get(token);

			ClientFacade customer = curSession.getService();
			if (customer instanceof CustomerFacade
					&& (System.currentTimeMillis() - curSession.getLastAccessed()) < 1000 * 130) {

				return (ResponseEntity<?>) point.proceed();

			} else {
				sessionsMap().remove(token);
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}

	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build();
	}

	@Bean
	public Map<String, Session> sessionsMap() {
		return new HashMap<String, Session>();
	}
}
