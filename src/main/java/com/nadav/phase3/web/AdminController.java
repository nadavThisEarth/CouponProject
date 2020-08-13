package com.nadav.phase3.web;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nadav.phase3.beans.Company;
import com.nadav.phase3.beans.Customer;
import com.nadav.phase3.exceptions.CompanyAlreadyExists;
import com.nadav.phase3.exceptions.CustomerAlreadyExists;
import com.nadav.phase3.exceptions.NoSuchCompany;
import com.nadav.phase3.exceptions.NoSuchCustomer;
import com.nadav.phase3.facades.AdminFacade;

@RestController
@RequestMapping("admin")
@CrossOrigin(origins = "http://localhost:4200")

public class AdminController {
	private Map<String, Session> sessionsMap;

	public AdminController(Map<String, Session> sessionsMap) {
		super();
		this.sessionsMap = sessionsMap;
	}

	@PostMapping("/add-company/{token}")
	public ResponseEntity<Object> addCompany(@PathVariable String token, @RequestBody Company company) {
		// First we check if there is a token and validate it
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if (session != null) {
			// first set lastAccessed time for now
			session.setLastAccessed(System.currentTimeMillis());
			AdminFacade service = (AdminFacade) session.getService();
			try {
				service.addCompany(company);
				return ResponseEntity.ok(company);
			} catch (CompanyAlreadyExists e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}catch (Exception e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}

	@PutMapping("/update-company/{token}")
	public ResponseEntity<Object> updateCompany(@PathVariable String token, @RequestBody Company company) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if (session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			AdminFacade service = (AdminFacade) session.getService();
			try {
				service.updateCompany(company);
				return ResponseEntity.ok(company);
			} catch (NoSuchCompany | CompanyAlreadyExists e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}

	@DeleteMapping("/delete-company/{token}/{id}")
	public ResponseEntity<Object> deleteCompany(@PathVariable String token, @PathVariable int id) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if (session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			AdminFacade service = (AdminFacade) session.getService();
			try {
				service.deleteCompany(id);
				return ResponseEntity.ok("Company with id: " + id + " has been deleted");
			} catch (NoSuchCompany e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}

	@GetMapping("/get-all-companies/{token}")
	public ResponseEntity<Object> getAllCompanies(@PathVariable String token) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if (session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			AdminFacade service = (AdminFacade) session.getService();
			return ResponseEntity.ok(service.getAllCompanies());
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}

	@GetMapping("/get-one-company/{token}/{id}")
	public ResponseEntity<Object> getOneCompany(@PathVariable String token, @PathVariable int id) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if (session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			AdminFacade service = (AdminFacade) session.getService();
			try {
				return ResponseEntity.ok(service.getOneCompany(id));
			} catch (NoSuchCompany e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}

	@PostMapping("/add-customer/{token}")
	public ResponseEntity<Object> addCustomer(@PathVariable String token, @RequestBody Customer customer) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if (session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			AdminFacade service = (AdminFacade) session.getService();
			try {
				service.addCustomer(customer);
				return ResponseEntity.ok(customer);
			} catch (CustomerAlreadyExists e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}catch (Exception e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}

	@PutMapping("/update-customer/{token}")
	public ResponseEntity<Object> updateCustomer(@PathVariable String token, @RequestBody Customer customer) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if (session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			AdminFacade service = (AdminFacade) session.getService();
			try {
				service.updateCustomer(customer);
				return ResponseEntity.ok(customer);
			} catch (CustomerAlreadyExists | NoSuchCustomer e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}

	@DeleteMapping("/delete-customer/{token}/{id}")
	public ResponseEntity<Object> deleteCustomer(@PathVariable String token, @PathVariable int id) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if (session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			AdminFacade service = (AdminFacade) session.getService();
			try {
				service.deleteCustomer(id);
				return ResponseEntity.ok("Customer id: " + id + " deleted successfully.");
			} catch (NoSuchCustomer e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}

	@GetMapping("/get-all-customers/{token}")
	public ResponseEntity<Object> getAllCustomers(@PathVariable String token) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if (session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			AdminFacade service = (AdminFacade) session.getService();
			return ResponseEntity.ok(service.getAllCustomers());
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}

	@GetMapping("/get-one-customer/{token}/{id}")
	public ResponseEntity<Object> getOneCustomer(@PathVariable String token, @PathVariable int id) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if (session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			AdminFacade service = (AdminFacade) session.getService();
			try {
				return ResponseEntity.ok(service.getOneCustomer(id));
			} catch (NoSuchCustomer e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}

	private void isTimeOut(Session session, String token) {
		if (session != null) {
			long last = session.getLastAccessed();
			long now = System.currentTimeMillis();
			if ((now - last) > 1000 * 60 * 30) {
				sessionsMap.remove(token);
				session = null;
			}
		}
	}
}
