package com.nadav.phase3.threads;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nadav.phase3.beans.Company;
import com.nadav.phase3.beans.Coupon;
import com.nadav.phase3.beans.Customer;
import com.nadav.phase3.repos.CompanyRepository;
import com.nadav.phase3.repos.CouponRepository;
import com.nadav.phase3.repos.CustomerRepository;

@Component
public class Job extends Thread {
	@Autowired
	private CouponRepository coupRep;
	@Autowired
	private CustomerRepository custRep;
	@Autowired
	private CompanyRepository comRep;

	private boolean quit = false;

	@Override
	public void run() {
		while (!quit) {
			// this thread operates every midnight
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date midnight = new Date(cal.getTimeInMillis());

			// collecting all expired coupons
			ArrayList<Coupon> expired = (ArrayList<Coupon>) coupRep.findByEndDateBefore(midnight);
			synchronized (expired) {
				// first we delete all expired coupons from customers who bought them
				for (Coupon exCoupon : expired) {
					for (Customer customer : custRep.findAll()) {
						for (Coupon coupon : customer.getCoupons()) {
							if (coupon.getId() == exCoupon.getId()) {

								customer.getCoupons().remove(coupon);
								// BREAK is due to single certain coupon purchase per customer
								break;
							}
						}
						// after java handling - saving to repository
						custRep.save(customer);
					}
					// delete current handled expired coupon from company
					Company com = comRep.findById(exCoupon.getCompany().getId()).get();
					com.getCoupons().remove(exCoupon);
					comRep.save(com);
					coupRep.delete(exCoupon);
				}
			}

			try {
				sleep(1000 * 60 * 60 * 24);
			} catch (InterruptedException e) {
			}
		}
	}

	public void StopRunning() {
		quit = true;
		interrupt();

	}

}
