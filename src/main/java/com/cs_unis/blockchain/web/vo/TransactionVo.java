package com.hades_region.blockchain.web.vo;

import java.math.BigDecimal;

/**
 * @author Robert Gerard
 * @since 18-4-13
 */
public class TransactionVo {

	private String from;
	private String to;
	private BigDecimal amount;
	private String privateKey;
	private String data;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
