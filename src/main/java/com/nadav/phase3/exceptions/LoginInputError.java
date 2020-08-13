package com.nadav.phase3.exceptions;

public class LoginInputError extends Exception {
	public LoginInputError() {
		super("Error: You have entered a wrong Email or Password (or both)!!!");

	}
}
