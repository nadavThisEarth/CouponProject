package com.nadav.phase3.exceptions;

public class CustomerAlreadyExists extends Exception {
	public CustomerAlreadyExists() {
		super("Error : you have tried to add a customer that already exists");

	}
}
