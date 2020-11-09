package com.hades_region.blockchain.enums;

/**
 * @author Robert Gerard
 * @since 18-4-16
 */
public enum  TransactionStatusEnum {

	SUCCESS("Success", 1),
	APPENDING("Appending", 0),
	FAIL("Fail", -1);

	private String key;
	private int value;

	TransactionStatusEnum(String key, int value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
