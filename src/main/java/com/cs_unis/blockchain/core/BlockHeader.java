package com.hades_region.blockchain.core;

import com.hades_region.blockchain.crypto.Hash;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author Robert Gerard
 * @since 18-4-6
 */
public class BlockHeader implements Serializable {

	private Integer index;
	private BigInteger difficulty;
	private Long nonce;
	private Long timestamp;
	private String hash;
	private String previousHash;

	public BlockHeader(Integer index, String previousHash) {
		this.index = index;
		this.timestamp = System.currentTimeMillis();
		this.previousHash = previousHash;
	}

	public BlockHeader() {
		this.timestamp = System.currentTimeMillis();
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public BigInteger getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigInteger difficulty) {
		this.difficulty = difficulty;
	}

	public Long getNonce() {
		return nonce;
	}

	public void setNonce(Long nonce) {
		this.nonce = nonce;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getPreviousHash() {
		return previousHash;
	}

	public void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	@Override
	public String toString() {
		return "BlockHeader{" +
				"index=" + index +
				", difficulty=" + difficulty +
				", nonce=" + nonce +
				", timestamp=" + timestamp +
				", hash='" + hash + '\'' +
				", previousHash='" + previousHash + '\'' +
				'}';
	}

	/**
	 * @return
	 */
	public String hash() {
		return Hash.sha3("BlockHeader{" +
				"index=" + index +
				", difficulty=" + difficulty +
				", nonce=" + nonce +
				", timestamp=" + timestamp +
				", previousHash='" + previousHash + '\'' +
				'}');
	}
}
