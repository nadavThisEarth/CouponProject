package com.nadav.phase3.exceptions;

public class CouponAlreadyExists extends Exception {

	public CouponAlreadyExists() {
		super("Error : You have tried to access a coupon that already exists");
		
	}

}
