package com.nadav.phase3.exceptions;

public class NoSuchCoupon extends Exception {

	public NoSuchCoupon() {
		super("Error : you have tried to access a coupon that doesn't exist");
	}
}
