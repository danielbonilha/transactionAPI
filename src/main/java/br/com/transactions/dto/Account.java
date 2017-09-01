package br.com.transactions.dto;

import java.util.List;

import br.com.transactions.entity.Transaction;

public class Account {

	private Long id;
	private double availableCreditLimit;
	private double availableWithdrawalLimit;
	private List<Transaction> transactions;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public double getAvailableCreditLimit() {
		return availableCreditLimit;
	}

	public void setAvailableCreditLimit(double availableCreditLimit) {
		this.availableCreditLimit = availableCreditLimit;
	}
	
	public double getAvailableWithdrawalLimit() {
		return availableWithdrawalLimit;
	}

	public void setAvailableWithdrawalLimit(double availableWithdrawalLimit) {
		this.availableWithdrawalLimit = availableWithdrawalLimit;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
	
}
