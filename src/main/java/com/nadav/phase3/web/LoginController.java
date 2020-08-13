package com.nadav.phase3.web;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nadav.phase3.exceptions.LoginInputError;
import com.nadav.phase3.facades.ClientFacade;
import com.nadav.phase3.login.ClientType;
import com.nadav.phase3.login.LoginManager;

@RestController
@RequestMapping("loginController")
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {
	@Autowired
	private LoginManager loginManager;
	@Autowired
	private Map<String, Session> sessionsMap;

	@PostMapping("/login/{email}/{password}/{type}")
	public String login(@PathVariable String email, @PathVariable String password, @PathVariable ClientType type) {
		String token = UUID.randomUUID().toString();
		try {
			ClientFacade service = loginManager.login(email, password, type);
			if (service != null) {
				Session session = new Session(service, System.currentTimeMillis());
				sessionsMap.put(token, session);

				return token;
			}
			return "Error: login failure";
		} catch (LoginInputError e) {
			return e.getMessage();
		}
	}

	@PostMapping("/logout/{token}")
	public void logout(@PathVariable String token) {
		sessionsMap.remove(token);
	}
}
