package com.nadav.phase3.exceptions;

public class CouponAlreadyPurchased extends Exception {
	public CouponAlreadyPurchased() {
		super("Error : you have already purchased this coupon");

	}
}
