package com.hades_region.blockchain.core;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.hades_region.blockchain.enums.TransactionStatusEnum;
import com.hades_region.blockchain.event.MineBlockEvent;
import com.hades_region.blockchain.crypto.Credentials;
import com.hades_region.blockchain.crypto.Keys;
import com.hades_region.blockchain.crypto.Sign;
import com.hades_region.blockchain.db.DBAccess;
import com.hades_region.blockchain.event.SendTransactionEvent;
import com.hades_region.blockchain.mine.Miner;
import com.hades_region.blockchain.net.ApplicationContextProvider;
import com.hades_region.blockchain.net.base.Node;
import com.hades_region.blockchain.net.client.AppClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author Robert Gerard
 * @since 18-4-6
 */
@Component
public class BlockChain {

	private static Logger logger = LoggerFactory.getLogger(BlockChain.class);

	@Autowired
	private DBAccess dbAccess;

	@Autowired
	private AppClient appClient;

	@Autowired
	private Miner miner;

	@Autowired
	private TransactionPool transactionPool;

	@Autowired
	private TransactionExecutor executor;
	/**
	 * @return
	 */
	public Block mining() throws Exception {

		Optional<Block> lastBlock = getLastBlock();
		Block block = miner.newBlock(lastBlock);
		transactionPool.getTransactions().forEach(e -> block.getBody().addTransaction(e));
		executor.run(block);
		transactionPool.clearTransactions();
		dbAccess.putLastBlockIndex(block.getHeader().getIndex());
		dbAccess.putBlock(block);
		logger.info("Find a New Block, {}", block);

		ApplicationContextProvider.publishEvent(new MineBlockEvent(block));
		return block;
	}

	/**
	 * @param credentials
	 * @param to
	 * @param amount
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public Transaction sendTransaction(Credentials credentials, String to, BigDecimal amount, String data) throws
			Exception {

		Preconditions.checkArgument(to.startsWith("0x"), "Incorrect payment address format");
		Preconditions.checkArgument(!credentials.getAddress().equals(to), "Incorrect collection address format Cannot be the same as the sending address");

		Transaction transaction = new Transaction(credentials.getAddress(), to, amount);
		transaction.setPublicKey(Keys.publicKeyEncode(credentials.getEcKeyPair().getPublicKey().getEncoded()));
		transaction.setStatus(TransactionStatusEnum.APPENDING);
		transaction.setData(data);
		transaction.setTxHash(transaction.hash());
		String sign = Sign.sign(credentials.getEcKeyPair().getPrivateKey(), transaction.toString());
		transaction.setSign(sign);

		if (!Sign.verify(credentials.getEcKeyPair().getPublicKey(), sign, transaction.toString())) {
			throw new RuntimeException("Private key signature verification failed, illegal private key");
		}

		transactionPool.addTransaction(transaction);

		ApplicationContextProvider.publishEvent(new SendTransactionEvent(transaction));
		return transaction;
	}

	/**
	 * @return
	 */
	public Optional<Block> getLastBlock() {
		return dbAccess.getLastBlock();
	}

	/**
	 * @param ip
	 * @param port
	 * @return
	 */
	public void addNode(String ip, int port) throws Exception {

		appClient.addNode(ip, port);
		Node node = new Node(ip, port);
		dbAccess.addNode(node);
	}
}
