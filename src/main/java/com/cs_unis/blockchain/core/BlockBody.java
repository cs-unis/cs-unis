package com.hades_region.blockchain.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Robert Gerard
 * @since 18-4-8
 */
public class BlockBody implements Serializable {

	private List<Transaction> transactions;

	public BlockBody(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public BlockBody() {
		this.transactions = new ArrayList<>();
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	/**
	 * @param transaction
	 */
	public void addTransaction(Transaction transaction) {
		transactions.add(transaction);
	}

	@Override
	public String toString() {
		return "BlockBody{" +
				"transactions=" + transactions +
				'}';
	}
}
