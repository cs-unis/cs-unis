package com.hades_region.blockchain.mine.pow;

import com.hades_region.blockchain.core.Block;
import com.hades_region.blockchain.crypto.Hash;
import com.hades_region.blockchain.utils.ByteUtils;
import com.hades_region.blockchain.utils.Numeric;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;

/**
 * @author Robert Gerard
 */
public class ProofOfWork {

    public static final int TARGET_BITS = 12;
    private Block block;
    private BigInteger target;


    /**
     * @param block
     * @return
     */
    public static ProofOfWork newProofOfWork(Block block) {
        BigInteger targetValue = BigInteger.valueOf(1).shiftLeft((256 - TARGET_BITS));
        return new ProofOfWork(block, targetValue);
    }

    private ProofOfWork(Block block, BigInteger target) {
        this.block = block;
        this.target = target;
    }

    /**
     * @return
     */
    public PowResult run() {
        long nonce = 0;
        String shaHex = "";
        while (nonce < Long.MAX_VALUE) {
            byte[] data = this.prepareData(nonce);
            shaHex = Hash.sha3String(data);
            if (new BigInteger(shaHex, 16).compareTo(this.target) == -1) {
                break;
            } else {
                nonce++;
            }
        }
        return new PowResult(nonce, shaHex, this.target);
    }

    /**
     * @return
     */
    public boolean validate() {
        byte[] data = this.prepareData(this.getBlock().getHeader().getNonce());
        return new BigInteger(Hash.sha3String(data), 16).compareTo(this.target) == -1;
    }

    /**
     * @param nonce
     * @return
     */
    private byte[] prepareData(long nonce) {
        byte[] prevBlockHashBytes = {};
        if (StringUtils.isNotBlank(this.getBlock().getHeader().getPreviousHash())) {
            String prevHash = Numeric.cleanHexPrefix(this.getBlock().getHeader().getPreviousHash());
            prevBlockHashBytes = new BigInteger(prevHash, 16).toByteArray();
        }

        return ByteUtils.merge(
                prevBlockHashBytes,
                ByteUtils.toBytes(this.getBlock().getHeader().getTimestamp()),
                ByteUtils.toBytes(TARGET_BITS),
                ByteUtils.toBytes(nonce)
        );
    }


    public Block getBlock() {
        return block;
    }

    public static BigInteger getTarget() {
        return BigInteger.valueOf(1).shiftLeft((256 - TARGET_BITS));
    }

}
