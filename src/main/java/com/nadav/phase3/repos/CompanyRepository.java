package com.nadav.phase3.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nadav.phase3.beans.Company;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
	Company findByEmailAndPassword(String email, String password);

	Company findByEmail(String email);
}
