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

import com.nadav.phase3.beans.Category;
import com.nadav.phase3.beans.Company;
import com.nadav.phase3.beans.Coupon;
import com.nadav.phase3.exceptions.CouponAlreadyExists;
import com.nadav.phase3.exceptions.NoSuchCoupon;
import com.nadav.phase3.facades.CompanyFacade;

@RestController
@RequestMapping("company")
@CrossOrigin(origins = "http://localhost:4200")

public class CompanyController {
	private Map<String, Session> sessionsMap;

	public CompanyController(Map<String, Session> sessionsMap) {
		super();
		this.sessionsMap = sessionsMap;
	}

	@PostMapping("/add-coupon/{token}")
	public ResponseEntity<?> addCoupon(@PathVariable String token, @RequestBody Coupon coupon) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if (session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			CompanyFacade service = (CompanyFacade) session.getService();
			try {
				service.addCoupon(coupon);
				return ResponseEntity.ok(coupon);
			} catch (CouponAlreadyExists e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}

	@PutMapping("/update-coupon/{token}")
	public ResponseEntity<?> updateCoupon(@PathVariable String token, @RequestBody Coupon coupon) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if (session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			CompanyFacade service = (CompanyFacade) session.getService();
			try {
				coupon.setCompany(service.getComapnyObject());
				service.updateCoupon(coupon);
				return ResponseEntity.ok(coupon);
			} catch (CouponAlreadyExists | NoSuchCoupon e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}

	@DeleteMapping("/delete-coupon/{token}/{id}")
	public ResponseEntity<?> deleteCoupon(@PathVariable String token, @PathVariable int id) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if (session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			CompanyFacade service = (CompanyFacade) session.getService();
			try {
				service.deleteCoupon(id);
				return ResponseEntity.ok("Coupon id:" + id + " deleted successfully.");
			} catch (NoSuchCoupon e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}

	@GetMapping("/company-coupons/{token}")
	public ResponseEntity<?> getCompanyCoupons(@PathVariable String token) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if (session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			CompanyFacade service = (CompanyFacade) session.getService();
			return ResponseEntity.ok(service.getCompanyCoupons());
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}

	@GetMapping("/coupons-by-categoryID/{token}/{category}")
	public ResponseEntity<?> getCompanyCouponsByCategory(@PathVariable String token, @PathVariable Category category) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if (session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			CompanyFacade service = (CompanyFacade) session.getService();
			try {
				return ResponseEntity.ok(service.getCompanyCoupons(category));
			} catch (Exception e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}

	@GetMapping("/coupons-by-max-price/{token}/{maxPrice}")
	public ResponseEntity<?> getCompanyCouponsByMaxPrice(@PathVariable String token, @PathVariable int maxPrice) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if (session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			CompanyFacade service = (CompanyFacade) session.getService();
			return ResponseEntity.ok(service.getCompanyCoupons(maxPrice));
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}

	@GetMapping("/company-details/{token}")
	public ResponseEntity<?> getCompanyDetails(@PathVariable String token) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if (session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			CompanyFacade service = (CompanyFacade) session.getService();
			return ResponseEntity.ok(service.getComapnyObject());
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
