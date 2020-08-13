package com.nadav.phase3.facades;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nadav.phase3.repos.CompanyRepository;
import com.nadav.phase3.repos.CouponRepository;
import com.nadav.phase3.repos.CustomerRepository;

@Service
public abstract class ClientFacade {
	@Autowired
	protected CompanyRepository comRep;
	@Autowired
	protected CouponRepository coupRep;
	@Autowired
	protected CustomerRepository custRep;

	public abstract boolean login(String email, String password);
}
