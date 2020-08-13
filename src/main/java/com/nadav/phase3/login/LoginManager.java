package com.nadav.phase3.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.nadav.phase3.exceptions.LoginInputError;
import com.nadav.phase3.facades.AdminFacade;
import com.nadav.phase3.facades.ClientFacade;
import com.nadav.phase3.facades.CompanyFacade;
import com.nadav.phase3.facades.CustomerFacade;

@Component
public class LoginManager {
	@Autowired
	private ConfigurableApplicationContext ctx;

	public ClientFacade login(String email, String password, ClientType type) throws LoginInputError {

		if (type.ordinal() == 0) {
			AdminFacade admin = ctx.getBean(AdminFacade.class);
			if (admin.login(email, password)) {
				return admin;
			} else {
				throw new LoginInputError();
			}
		} else if (type.ordinal() == 1) {
			CompanyFacade company = ctx.getBean(CompanyFacade.class);
			if (company.login(email, password)) {
				return company;
			} else {
				throw new LoginInputError();
			}
		} else {
			CustomerFacade customer = ctx.getBean(CustomerFacade.class);
			if (customer.login(email, password)) {
				return customer;
			} else {
				throw new LoginInputError();
			}
		}
	}

}
