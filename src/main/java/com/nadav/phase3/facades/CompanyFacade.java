package com.nadav.phase3.facades;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.nadav.phase3.beans.Category;
import com.nadav.phase3.beans.Company;
import com.nadav.phase3.beans.Coupon;
import com.nadav.phase3.beans.Customer;
import com.nadav.phase3.exceptions.CouponAlreadyExists;
import com.nadav.phase3.exceptions.NoSuchCoupon;

@Service
@Scope("prototype")
public class CompanyFacade extends ClientFacade {
	private Company company;

	@Override
	public boolean login(String email, String password) {
		if (comRep.findByEmailAndPassword(email, password) != null) {
			company = comRep.findByEmail(email); // in case of successful login , store in temporary variable 'company'
			return true;
		}
		return false;
	}

	public List<Coupon> getCompanyCoupons() {
		return company.getCoupons();
	}

	public Company getComapnyObject() {
		return company;
	}

	public void addCoupon(Coupon coupon) throws CouponAlreadyExists {
		List<Coupon> companyCoupons = company.getCoupons();
		boolean exists = false;
		for (Coupon c : companyCoupons) {
			if (coupon.getId() != c.getId() && coupon.getTitle().equals(c.getTitle())) // checking coupon restrictions
			{
				exists = true;
				throw new CouponAlreadyExists();
			}
		}
		if (!exists) {
			// first , set coupon company id
			coupon.setCompany(company);

			// then , save coupon in coupons table

			coupRep.save(coupon);

			// Add coupon to company coupons and save the bond in database
			company.getCoupons().add(coupon);
			comRep.save(company);

		}
	}

	public void updateCoupon(Coupon coupon) throws NoSuchCoupon, CouponAlreadyExists {
		boolean exists = false;
		List<Coupon> companyCoupons = company.getCoupons();
		if (!coupRep.existsById(coupon.getId())) { // check if coupon exists in database
			throw new NoSuchCoupon();
		}
		// check if coupon exists in company inventory
		for (Coupon coup : companyCoupons) {
			if (coup.getId() == coupon.getId()) { // this relates to company coupons
				exists = true;
				break;
			}
		}
		if (!exists) {
			throw new NoSuchCoupon();
		}

		List<Coupon> allCoupons = coupRep.findAll();
		for (Coupon c : allCoupons) {
			if (coupon.getId() != c.getId() && coupon.getTitle().equals(c.getTitle())) {// checking coupon restrictions
				throw new CouponAlreadyExists();
			}
		}
		coupRep.save(coupon);
	}

	public void deleteCoupon(int couponID) throws NoSuchCoupon {
		boolean exists = false;
		List<Coupon> companyCoupons = company.getCoupons();
		// check if coupons exists in database
		if (!coupRep.existsById(couponID)) {
			throw new NoSuchCoupon();
		}
		// check if coupon exists in company inventory
		for (Coupon coupon : companyCoupons) {
			if (coupon.getId() == couponID) { // this relates to company coupons
				exists = true;
				break;
			}
		}
		if (!exists) {
			throw new NoSuchCoupon();
		}
		// If coupon is in company inventory , delete from purchases
		Coupon c = coupRep.findById(couponID).get();
		for (Customer cust : custRep.findAll()) {
			cust.getCoupons().remove(c);
			custRep.save(cust);
		}
		// then , delete the coupon from the company
		company.getCoupons().remove(c);
		comRep.save(company);
		// finally ,Delete the coupon
		coupRep.deleteById(couponID);

	}

	public List<Coupon> getCompanyCoupons(Category category) { // this method returns a list of coupons by category
		List<Coupon> allCompanyCoupons = company.getCoupons();
		List<Coupon> companyCouponsByCategory = new ArrayList<Coupon>();

		for (Coupon c : allCompanyCoupons) {
			if (c.getCategory().ordinal() == category.ordinal()) {
				companyCouponsByCategory.add(c);
			}
		}
		return companyCouponsByCategory;
	}

	public List<Coupon> getCompanyCoupons(double maxPrice) { // this method returns a list of coupons with price NOT
																// exceeding
																// given amount
		List<Coupon> allCompanyCoupons = company.getCoupons();
		List<Coupon> companyCouponsByMaxPrice = new ArrayList<Coupon>();

		for (Coupon c : allCompanyCoupons) {
			if (c.getPrice() <= maxPrice) {
				companyCouponsByMaxPrice.add(c);
			}
		}
		return companyCouponsByMaxPrice;
	}


}
