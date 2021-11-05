package com.cognixia.jump.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.jump.model.Account;
import com.cognixia.jump.model.Customer;
import com.cognixia.jump.repository.CustomerRepository;

@RequestMapping("/api")
@RestController
public class CustomerController {

	@Autowired
	CustomerRepository repo;
	
	@GetMapping("/customer")
	public List<Customer> getCustomers() {
		return repo.findAll();
	}
	
	@GetMapping("/customer/{id}")
	public Customer getCustomer(@PathVariable int id) {
		if(repo.existsById(id))
			return repo.getById(id);
		
		return new Customer();
	}
	
	@PostMapping("/customer")
	public ResponseEntity<?> addCustomer(@RequestBody Customer customer) {
		customer.setId(-1);
		
		for(Account account : customer.getAccounts()) {
			account.setId(-1);
		}
		
		Customer addedCust = repo.save(customer);
		
		return ResponseEntity.status(201).body(addedCust);
	}
	
	@GetMapping("/customer/{id}/totalbalance")
	public ResponseEntity<?> getFullBalanceFromAllAccounts(@PathVariable int id) {
		if(repo.existsById(id)) {
			double totalBalance = 0.0;
			
			for(Account acc: repo.getById(id).getAccounts()) {
				totalBalance += acc.getBalance();
			}
			
			return ResponseEntity.status(200).body(totalBalance);
		}
		
		return ResponseEntity.status(400)
								.body("Couldn't find Customer with id = " + id + "to retrieve total balance!");
		
		
		
	}
}
