package com.cognixia.jump.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Customer implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column
	private String fullName;
	
	@Column
	private String pin;
	
	@Column
	private String username;
	
	@Column
	private String password;
	
	@Column
	@ElementCollection(targetClass = String.class)
	private List<String> transactionList;
	
	@JsonIgnore
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private List<Account> accounts;
	
	public Customer() {
		this(-1, "N/A", "0000", "invalidUser", "kmdsdm", new ArrayList<String>(), new ArrayList<Account>());
	}

	public List<String> getTransactionList() {
		return transactionList;
	}

	public void setTransactionList(List<String> transactionList) {
		this.transactionList = transactionList;
	}

	public Customer(Integer id, String fullName, String pin, String username, String password, List<String> transactionList, List<Account> accounts) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.pin = pin;
		this.username = username;
		this.password = password;
		this.transactionList = transactionList;
		this.accounts = accounts;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", fullName=" + fullName + ", pin=" + pin + ", username=" + username
				+ ", password=" + password + ", transactionList=" + transactionList + ", accounts=" + accounts + "]";
	}
	
	
	
}
