package com.hades_region.blockchain.core;

import com.hades_region.blockchain.enums.TransactionStatusEnum;
import com.hades_region.blockchain.crypto.Hash;

import java.math.BigDecimal;

/**
 * @author Robert Gerard
 * @since 18-4-6
 */
public class Transaction {

	private String from;
	private String sign;
	private String to;
	private String publicKey;
	private BigDecimal amount;
	private Long timestamp;
	private String txHash;
	private TransactionStatusEnum status = TransactionStatusEnum.SUCCESS;
	private String errorMessage;
	private String data;

	public Transaction(String from, String to, BigDecimal amount) {
		this.from = from;
		this.to = to;
		this.amount = amount;
		this.timestamp = System.currentTimeMillis();
	}

	public Transaction() {
		this.timestamp = System.currentTimeMillis();
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getTxHash() {
		return txHash;
	}

	public void setTxHash(String txHash) {
		this.txHash = txHash;
	}

	public TransactionStatusEnum getStatus() {
		return status;
	}

	public void setStatus(TransactionStatusEnum status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @return
	 */
	public String hash() {
		return Hash.sha3(this.toString());
	}

	@Override
	public String toString() {
		return "Transaction{" +
				"from='" + from + '\'' +
				", to='" + to + '\'' +
				", publicKey=" + publicKey +
				", amount=" + amount +
				", timestamp=" + timestamp +
				", data='" + data + '\'' +
				'}';
	}
}
