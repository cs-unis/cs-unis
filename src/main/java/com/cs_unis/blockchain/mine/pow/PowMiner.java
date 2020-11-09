package com.hades_region.blockchain.mine.pow;

import com.hades_region.blockchain.core.Block;
import com.hades_region.blockchain.core.BlockBody;
import com.hades_region.blockchain.core.BlockHeader;
import com.hades_region.blockchain.core.Transaction;
import com.hades_region.blockchain.db.DBAccess;
import com.hades_region.blockchain.account.Account;
import com.google.common.base.Optional;
import com.hades_region.blockchain.mine.Miner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Robert Gerard
 * @since 18-4-13
 */
@Component
public class PowMiner implements Miner {

	@Autowired
	private DBAccess dbAccess;

	@Override
	public Block newBlock(Optional<Block> block) throws Exception {

		Account account;
		Optional<Account> coinBaseAccount = dbAccess.getCoinBaseAccount();
		if (!coinBaseAccount.isPresent()) {
			throw new RuntimeException("No mining account was found, please create the mining account first.");
		}
		Block newBlock;
		if (block.isPresent()) {
			Block prev = block.get();
			BlockHeader header = new BlockHeader(prev.getHeader().getIndex()+1, prev.getHeader().getHash());
			BlockBody body = new BlockBody();
			newBlock = new Block(header, body);
		} else {
			newBlock = createGenesisBlock();
		}
		Transaction transaction = new Transaction();

		account = coinBaseAccount.get();
		transaction.setTo(account.getAddress());
		transaction.setData("Miner Reward.");
		transaction.setTxHash(transaction.hash());
		transaction.setAmount(Miner.MINING_REWARD);

		if (block.isPresent()) {
			ProofOfWork proofOfWork = ProofOfWork.newProofOfWork(newBlock);
			PowResult result = proofOfWork.run();
			newBlock.getHeader().setDifficulty(result.getTarget());
			newBlock.getHeader().setNonce(result.getNonce());
			newBlock.getHeader().setHash(result.getHash());
		}
		newBlock.getBody().addTransaction(transaction);

		dbAccess.putLastBlockIndex(newBlock.getHeader().getIndex());
		return newBlock;
	}

	/**
	 * @return
	 */
	private Block createGenesisBlock() {

		BlockHeader header = new BlockHeader(1, null);
		header.setNonce(PowMiner.GENESIS_BLOCK_NONCE);
		header.setDifficulty(ProofOfWork.getTarget());
		header.setHash(header.hash());
		BlockBody body = new BlockBody();
		return new Block(header, body);
	}

	@Override
	public boolean validateBlock(Block block) {
		ProofOfWork proofOfWork = ProofOfWork.newProofOfWork(block);
		return proofOfWork.validate();
	}
}
