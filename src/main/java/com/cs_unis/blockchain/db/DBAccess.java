package com.hades_region.blockchain.db;

import com.google.common.base.Optional;
import com.hades_region.blockchain.account.Account;
import com.hades_region.blockchain.core.Block;
import com.hades_region.blockchain.net.base.Node;

import java.util.List;

/**
 * @author Robert Gerard
 * @since 18-4-10
 */
public interface DBAccess {

	/**
	 * @param lastBlock
	 * @return
	 */
	boolean putLastBlockIndex(Object lastBlock);

	/**
	 * @return
	 */
	Optional<Object> getLastBlockIndex();

	/**
	 * @param block
	 * @return
	 */
	boolean putBlock(Block block);

	/**
	 * @param blockIndex
	 * @return
	 */
	Optional<Block> getBlock(Object blockIndex);

	/**
	 * @return
	 */
	Optional<Block> getLastBlock();

	/**
	 * @param account
	 * @return
	 */
	boolean putAccount(Account account);

	/**
	 * @param address
	 * @return
	 */
	Optional<Account> getAccount(String address);

	/**
	 * @param address
	 * @return
	 */
	boolean putCoinBaseAddress(String address);

	/**
	 * @return
	 */
	Optional<String> getCoinBaseAddress();

	/**
	 * @return
	 */
	Optional<Account> getCoinBaseAccount();

	/**
	 * @param account
	 * @return
	 */
	boolean putCoinBaseAccount(Account account);

	/**
	 * @return
	 */
	Optional<List<Node>> getNodeList();

	/**
	 * @param nodes
	 * @return
	 */
	boolean putNodeList(List<Node> nodes);

	/**
	 * @param node
	 * @return
	 */
	boolean addNode(Node node);

	void clearNodes();

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	boolean put(String key, Object value);

	/**
	 * @param key
	 * @return
	 */
	Optional<Object> get(String key);

	/**
	 * @param key
	 * @return
	 */
	boolean delete(String key);

	/**
	 * @param keyPrefix
	 * @return
	 */
	<T> List<T> seekByKey(String keyPrefix);

	/**
	 * @return
	 */
	List<Account> listAccounts();

	void closeDB();
}
