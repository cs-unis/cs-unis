package com.hades_region.blockchain.core;

import com.google.common.base.Objects;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Robert Gerard
 * @since 18-4-23
 */
@Component
public class TransactionPool {

	private List<Transaction> transactions = new ArrayList<>();


	/**
	 * @param transaction
	 */
	public void addTransaction(Transaction transaction) {

		boolean exists = false;
		for (Transaction tx : this.transactions) {
			if (Objects.equal(tx.getTxHash(), transaction.getTxHash())) {
				exists = true;
			}
		}
		if (!exists) {
			this.transactions.add(transaction);
		}
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}
	public void clearTransactions() {
		this.transactions.clear();
	}

}
