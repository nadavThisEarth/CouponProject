package com.nadav.phase3.beans;

import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "coupons")
public class Coupon {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	private Company company;
	@Column(nullable = false)
	private int amount;
	@Column(name = "category_id", nullable = false)
	private Category category;
	@Column(nullable = false)
	private String title;
	@Column(nullable = false)
	private String description;
	@Column
	private String img;
	@Column(name = "start_date", nullable = false)
	private Date startDate;
	@Column(name = "end_date", nullable = false)
	private Date endDate;
	@Column(nullable = false)
	private double price;
	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Customer> customers;

	public Coupon() {
		// Empty Constructor for HIBERNATE
	}

	public List<Customer> getCustomers() {
		return customers;
	}

	public Coupon(Company company, int amount, Category category, String title, String description, String img,
			Date startDate, Date endDate, double price) {
		this.company = company;
		this.amount = amount;
		this.category = category;
		this.title = title;
		this.description = description;
		this.img = img;
		this.startDate = startDate;
		this.endDate = endDate;
		this.price = price;
	}

	public Coupon(int amount, Category category, String title, String description, String img, Date startDate,
			Date endDate, double price) {
		super();
		this.amount = amount;
		this.category = category;
		this.title = title;
		this.description = description;
		this.img = img;
		this.startDate = startDate;
		this.endDate = endDate;
		this.price = price;
	}

	public Coupon(int id, Company company, int amount, Category category, String title, String description, String img,
			Date startDate, Date endDate, double price) {
		this.id = id;
		this.company = company;
		this.amount = amount;
		this.category = category;
		this.title = title;
		this.description = description;
		this.img = img;
		this.startDate = startDate;
		this.endDate = endDate;
		this.price = price;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Coupon [couponId=" + id + ", company id=" + company.getId() + ", amount=" + amount + ", category="
				+ category + ", title=" + title + ", description=" + description + ", img=" + img + ", startDate="
				+ startDate + ", endDate=" + endDate + ", price=" + price + "]";
	}

// These two following method are required for Coupon Comparison
	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Coupon))
			return false;

		return this.id == ((Coupon) o).id;
	}

}

