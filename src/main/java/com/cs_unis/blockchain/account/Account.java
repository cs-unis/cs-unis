package com.hades_region.blockchain.account;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Robert Gerard
 * @since 18-4-6
 */
public class Account implements Serializable {

	protected String address;

	protected BigDecimal balance;

	public Account() {}

	public Account(String address, BigDecimal balance) {
		this.address = address;
		this.balance = balance;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "Account{" +
				"address='" + address + '\'' +
				", balance=" + balance +
				'}';
	}
}
