package com.nadav.phase3.repos;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nadav.phase3.beans.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {
	public List<Coupon> findCouponsByCompanyId(int companyId);

	public List<Coupon> findByEndDateBefore(Date date);

}
