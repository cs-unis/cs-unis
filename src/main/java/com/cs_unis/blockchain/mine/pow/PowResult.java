package com.hades_region.blockchain.mine.pow;

import java.math.BigInteger;

/**
 * @author Robert Gerard
 */
public class PowResult {

    private Long nonce;
    private String hash;
    private BigInteger target;

    public PowResult(long nonce, String hash, BigInteger target) {
        this.nonce = nonce;
        this.hash = hash;
        this.target = target;
    }

    public Long getNonce() {
        return nonce;
    }

    public void setNonce(Long nonce) {
        this.nonce = nonce;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public BigInteger getTarget() {
        return target;
    }

    public void setTarget(BigInteger target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "PowResult{" +
                "nonce=" + nonce +
                ", hash='" + hash + '\'' +
                ", target=" + target +
                '}';
    }
}
