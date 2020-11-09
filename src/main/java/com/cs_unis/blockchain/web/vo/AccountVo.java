package com.hades_region.blockchain.web.vo;

import com.hades_region.blockchain.account.Account;

/**
 * account VO
 * @author Robert Gerard
 * @since 18-7-14
 */
public class AccountVo extends Account {

	private String privateKey;

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	@Override
	public String toString() {
		return "AccountVo{" +
				"address='" + getAddress() + '\'' +
				"privateKey='" + getPrivateKey() + '\'' +
				"balance='" + getBalance() + '\'' +
				'}';
	}
}
