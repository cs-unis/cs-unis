package com.hades_region.blockchain.mine;

import com.google.common.base.Optional;
import com.hades_region.blockchain.core.Block;

import java.math.BigDecimal;

/**
 * @author Robert Gerard
 * @since 2018-04-07 下午8:13.
 */
public interface Miner {

	BigDecimal MINING_REWARD = BigDecimal.valueOf(50);

	Long GENESIS_BLOCK_NONCE = 100000L;

	/**
	 * @param block
	 * @return
	 * @throws Exception
	 */
	Block newBlock(Optional<Block> block) throws Exception;

	/**
	 * @param block
	 * @return
	 */
	boolean validateBlock(Block block);

}
