package com.nadav.phase3.exceptions;

public class CouponExpiredException extends Exception {
	public CouponExpiredException() {
		super("Error : you have tried to access a coupon whose expiry date has passed");

	}
}
