package com.nadav.phase3.facades;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.nadav.phase3.beans.Category;
import com.nadav.phase3.beans.Coupon;
import com.nadav.phase3.beans.Customer;
import com.nadav.phase3.exceptions.CouponAlreadyPurchased;
import com.nadav.phase3.exceptions.CouponExpiredException;
import com.nadav.phase3.exceptions.CouponOutOfStock;
import com.nadav.phase3.exceptions.NoSuchCoupon;

@Service
@Scope("prototype")
public class CustomerFacade extends ClientFacade {

	private Customer customer;// this variable holds customer in case of successful login

	@Override
	public boolean login(String email, String password) {
		if (custRep.findByEmailAndPassword(email, password) != null) {
			customer = custRep.findByEmail(email);// storing in variable
			return true;
		}
		return false;
	}

	public Customer getCustomerObject() {
		return customer;
	}

	public Set<Coupon> getCustomerCoupons() {
		return customer.getCoupons();
	}

	public void purchaseCoupon(Coupon coupon)
			throws CouponAlreadyPurchased, NoSuchCoupon, CouponExpiredException, CouponOutOfStock {
		if (!coupRep.existsById(coupon.getId())) {// checking whether coupon exists in database
			throw new NoSuchCoupon();
		}
		// checking whether coupon expired
		if (coupon.getEndDate().getTime() < Calendar.getInstance().getTimeInMillis()) {
			throw new CouponExpiredException();
		}
		// checking whether coupon in stock
		if (coupon.getAmount() == 0) {
			throw new CouponOutOfStock();
		}
		// checking whether coupon was already purchased
		HashSet<Coupon> customerCoupons = (HashSet<Coupon>) customer.getCoupons();
		boolean purchased = false;
		for (Coupon c : customerCoupons) {
			if (coupon.getId() == c.getId()) {
				purchased = true;
				throw new CouponAlreadyPurchased();
			}
		}
		if (!purchased) {
			customer.getCoupons().add(coupon);
			custRep.save(customer);
			coupon.getCustomers().add(customer);
			coupon.setAmount(coupon.getAmount() - 1);
			coupRep.save(coupon);
		}
	}

	public void purchaseCoupon(int couponId) // this method gets an integer and will ease tests
			throws CouponAlreadyPurchased, NoSuchCoupon, CouponExpiredException, CouponOutOfStock {
		// checking whether coupon exists in database
		if (!coupRep.existsById(couponId)) {
			throw new NoSuchCoupon();
		}
		// checking whether coupon expired
		Coupon coupon = coupRep.findById(couponId).get();
		if (coupon.getEndDate().getTime() < Calendar.getInstance().getTimeInMillis()) {
			throw new CouponExpiredException();
		}
		// checking whether coupon in stock
		if (coupon.getAmount() == 0) {
			throw new CouponOutOfStock();
		}
		// checking whether coupon was already purchased
		Set<Coupon> custCoupons = customer.getCoupons();
		boolean purchased = false; // track the coupon
		for (Coupon c : custCoupons) {
			if (coupon.getId() == c.getId()) {
				purchased = true;
				throw new CouponAlreadyPurchased();
			}
		}
		if (!purchased) {
			customer.getCoupons().add(coupon);
			custRep.save(customer);
			coupon.setAmount(coupon.getAmount() - 1);
			coupon.getCustomers().add(customer);
			coupRep.save(coupon);
		}
	}

	public List<Coupon> getCustomerCoupons(Category category) { // return all coupons by category
		Set<Coupon> allCustCoupons = customer.getCoupons();
		List<Coupon> couponsByCategory = new ArrayList<Coupon>();
		for (Coupon c : allCustCoupons) {
			if (c.getCategory().ordinal() == category.ordinal()) {
				couponsByCategory.add(c);
			}
		}
		return couponsByCategory;
	}

	public List<Coupon> getCustomerCoupons(double maxPrice) { // return all coupons with price NOT exceeding given
																// amount
		Set<Coupon> allCustCoupons = customer.getCoupons();
		List<Coupon> customerCouponsByMaxPrice = new ArrayList<Coupon>();
		for (Coupon c : allCustCoupons) {
			if (c.getPrice() <= maxPrice) {
				customerCouponsByMaxPrice.add(c);
			}
		}
		return customerCouponsByMaxPrice;
	}

	public List<Coupon>getAllCoupons() 
	{
		return coupRep.findAll();
	}

}
