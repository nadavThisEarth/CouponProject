package com.nadav.phase3.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nadav.phase3.beans.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	Customer findByEmailAndPassword(String email, String password);

	Customer findByEmail(String email);
}
