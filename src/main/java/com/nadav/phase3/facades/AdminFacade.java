package com.nadav.phase3.facades;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nadav.phase3.beans.Company;
import com.nadav.phase3.beans.Coupon;
import com.nadav.phase3.beans.Customer;
import com.nadav.phase3.exceptions.CompanyAlreadyExists;
import com.nadav.phase3.exceptions.CustomerAlreadyExists;
import com.nadav.phase3.exceptions.NoSuchCompany;
import com.nadav.phase3.exceptions.NoSuchCustomer;
@Service
public class AdminFacade extends ClientFacade {

	@Override
	public boolean login(String email, String password) {
		if (email.equals("admin@admin.com") && password.equals("admin")) {
			return true;
		}
		return false;
	}
	public List<Company> getAllCompanies() {
		return comRep.findAll();
	}

	public Company getOneCompany(int id) throws NoSuchCompany {
		return comRep.findById(id).orElseThrow(() -> new NoSuchCompany());
	}

	public List<Customer> getAllCustomers() {
		return custRep.findAll();
	}

	public Customer getOneCustomer(int id) throws NoSuchCustomer {
		return custRep.findById(id).orElseThrow(() -> new NoSuchCustomer());
	}

	public void addCompany(Company company) throws CompanyAlreadyExists {
		List<Company> allCompanies = comRep.findAll();
		for (Company com : allCompanies) {
			if (company.getName().equals(com.getName()) || company.getEmail().equals(com.getEmail())) {// checking if
																										// company
																										// already
																										// exists
				throw new CompanyAlreadyExists();
			}
		}
		comRep.save(company);
	}

	public void addCustomer(Customer customer) throws CustomerAlreadyExists {
		List<Customer> allCustomers = custRep.findAll();
		for (Customer cust : allCustomers) {
			if (customer.getId() == cust.getId() || customer.getEmail().equals(cust.getEmail())) { // checking if
																									// customer already
																									// exists
				throw new CustomerAlreadyExists();
			}
		}
		custRep.save(customer);
	}

	public void updateCompany(Company company) throws CompanyAlreadyExists, NoSuchCompany {
		List<Company> allCompanies = comRep.findAll();
		boolean existById = false;
		boolean emailCollision = false;
		for (Company com : allCompanies) {
			if (company.getId() == com.getId()) { // first , check if id is identical to an exiting company
				existById = true;
			} else if (company.getEmail().equals(com.getEmail())) { // check email collision in case of different id
				emailCollision = true;
				throw new CompanyAlreadyExists();
			}
		}
		if (existById == false) {
			throw new NoSuchCompany();
		}
		if (!emailCollision) {
			comRep.save(company);
		}

	}

	public void updateCustomer(Customer customer) throws CustomerAlreadyExists, NoSuchCustomer {
		List<Customer> allCustomers = custRep.findAll();
		boolean exists = false;
		for (Customer cust : allCustomers) {
			if (customer.getId() != cust.getId() && customer.getEmail().equals(cust.getEmail())) { // check email
																									// collision
				throw new CustomerAlreadyExists();
			}
			if (customer.getId() == cust.getId()) { // check if customer exists in database
				exists = true;
			}
		}
		if (!exists) {
			throw new NoSuchCustomer();
		}
		custRep.save(customer);
	}

	public void deleteCompany(int id) throws NoSuchCompany {

		if (!comRep.existsById(id)) {// checking if company exists
			throw new NoSuchCompany();
		}
		List<Coupon> companyCoupons = coupRep.findCouponsByCompanyId(id);
		for (Coupon c : companyCoupons) {
			for (Customer cust : custRep.findAll()) {
				for (Coupon coupon : cust.getCoupons()) {
					if (coupon.getId() == c.getId()) {
						cust.getCoupons().remove(c);
						custRep.save(cust);
						break;
					}
				}
			}
			coupRep.delete(c);
		}
		comRep.deleteById(id);
	}

	public void deleteCustomer(int id) throws NoSuchCustomer {
		if (!custRep.existsById(id)) {// check if customer exists
			throw new NoSuchCustomer();
		}
		// first- delete all customer's coupons
		Customer cust = custRep.findById(id).get();
		cust.getCoupons().clear();
		custRep.save(cust);
		// at last , customer is deleted
		custRep.deleteById(id);
	}


}
