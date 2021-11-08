package com.cognixia.jump.controller;

import java.util.ArrayList;
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
	
	@GetMapping("/customer/{id}/latesttransactions")
	public ResponseEntity<?> getLastFiveTransactions(@PathVariable int id) {
		if(repo.existsById(id)) {
			List<String> transactions = repo.getById(id).getTransactionList();
			
			// This ensures starting index of slice will not go out of bounds
			//			=> EG: length was only 3 : nthIndexFromEnd ensures that only 3 transactions displayed and doesnt cause OutOfBoundsException
			int nthIndexFromEnd = (transactions.size() >= 5) ? 5: transactions.size();	
			
			// Retrieves a slice of transactions from the 5th latest to the last transaction made.
			//			=> Eg: {'D: 56', 'W: 30','D: 56', 'W: 30','D: 56', 'W: 24' }
			//						[Will Produce => {'W: 30','D: 56', 'W: 30','D: 56', 'W: 24' }]
			List<String> lastFiveTransactions = transactions.subList(transactions.size()-nthIndexFromEnd, transactions.size()); 
			
			return ResponseEntity.status(200).body(lastFiveTransactions);
		}
		
		return ResponseEntity.status(400)
				.body("Couldn't find Customer with id = " + id + "to retrieve their transactions!");

	}
	
}
