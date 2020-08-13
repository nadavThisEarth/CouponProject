package com.nadav.phase3.web;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nadav.phase3.beans.Category;
import com.nadav.phase3.beans.Coupon;
import com.nadav.phase3.exceptions.CouponAlreadyPurchased;
import com.nadav.phase3.exceptions.CouponExpiredException;
import com.nadav.phase3.exceptions.CouponOutOfStock;
import com.nadav.phase3.exceptions.NoSuchCoupon;
import com.nadav.phase3.facades.CustomerFacade;

@RestController
@RequestMapping("customer")
@CrossOrigin(origins = "http://localhost:4200")
public class CustomerController {

	private Map<String, Session> sessionsMap;

	public CustomerController(Map<String, Session> sessionsMap) {
		super();
		this.sessionsMap = sessionsMap;
	}

	@PostMapping("/purchase/{token}/{couponId}")
	public ResponseEntity<String> purchaseCoupon(@PathVariable String token, @PathVariable int couponId) {
		try {
			Session session = sessionsMap.get(token);
			isTimeOut(session, token);
			if (session != null) {
				session.setLastAccessed(System.currentTimeMillis());
				CustomerFacade service = (CustomerFacade) session.getService();

				service.purchaseCoupon(couponId);}
				return ResponseEntity.ok("Coupon id: " + couponId + " purchased successfully.");
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}



	@GetMapping("/get-all/{token}")
	public ResponseEntity<Object> getCustomerCoupons(@PathVariable String token) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if (session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			CustomerFacade service = (CustomerFacade) session.getService();
			return ResponseEntity.ok(service.getCustomerCoupons());
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}

	@GetMapping("/by-category/{token}/{category}")
	public ResponseEntity<Object> getCustomerCouponsByCategory(@PathVariable String token,
			@PathVariable Category category) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if (session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			CustomerFacade service = (CustomerFacade) session.getService();
			return ResponseEntity.ok(service.getCustomerCoupons(category));
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}

	@GetMapping("/by-max-price/{token}/{maxPrice}")
	public ResponseEntity<Object> getCustomerCouponsByPrice(@PathVariable String token, @PathVariable int maxPrice) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if (session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			CustomerFacade service = (CustomerFacade) session.getService();
			return ResponseEntity.ok(service.getCustomerCoupons(maxPrice));
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}

	@GetMapping("/get-all-coupons/{token}")
	public ResponseEntity<?> getAllCoupons(@PathVariable String token) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if (session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			CustomerFacade service = (CustomerFacade) session.getService();
			return ResponseEntity.ok(service.getAllCoupons());

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
