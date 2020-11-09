package com.hades_region.blockchain.net.server;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.hades_region.blockchain.account.Account;
import com.hades_region.blockchain.core.Block;
import com.hades_region.blockchain.core.Transaction;
import com.hades_region.blockchain.core.TransactionExecutor;
import com.hades_region.blockchain.core.TransactionPool;
import com.hades_region.blockchain.crypto.Keys;
import com.hades_region.blockchain.crypto.Sign;
import com.hades_region.blockchain.crypto.WalletUtils;
import com.hades_region.blockchain.db.DBAccess;
import com.hades_region.blockchain.net.base.*;
import com.hades_region.blockchain.utils.SerializeUtils;
import com.radarcenter.blockchain.net.base.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioHandler;

import java.util.List;

/**
 * @author Robert Gerard
 */
@Component
public class AppServerAioHandler extends BaseAioHandler implements ServerAioHandler {

	private static Logger logger = LoggerFactory.getLogger(AppServerAioHandler.class);
	@Autowired
	private DBAccess dbAccess;
	@Autowired
	private TransactionPool transactionPool;
	@Autowired
	private TransactionExecutor executor;
	@Override
	public void handler(Packet packet, ChannelContext channelContext) throws Exception {

		MessagePacket messagePacket = (MessagePacket) packet;
		byte type = messagePacket.getType();
		byte[] body = messagePacket.getBody();

		if (body != null) {
			logger.info("Request node information， {}", channelContext.getClientNode());
			if (body != null) {
				MessagePacket resPacket = null;
				switch (type) {

					case MessagePacketType.STRING_MESSAGE:
						resPacket = this.stringMessage(body);
						break;

					case MessagePacketType.REQ_CONFIRM_TRANSACTION:
						resPacket = this.confirmTransaction(body);
						break;

					case MessagePacketType.REQ_SYNC_NEXT_BLOCK:
						resPacket = this.fetchNextBlock(body);
						break;

					case MessagePacketType.REQ_NEW_BLOCK:
						resPacket = this.newBlock(body);
						break;

					case MessagePacketType.REQ_NEW_ACCOUNT:
						resPacket = this.newAccount(body);
						break;

					case MessagePacketType.REQ_ACCOUNTS_LIST:
						resPacket = this.getAccountList(body);
						break;

					case MessagePacketType.REQ_NODE_LIST:
						resPacket = this.getNodeList(body);
						break;

				} //end of switch

				Aio.send(channelContext, resPacket);
			}
		}
		return;
	}

	/**
	 * @param body
	 * @return
	 */
	public MessagePacket stringMessage(byte[] body) {

		MessagePacket resPacket = new MessagePacket();
		String str = (String) SerializeUtils.unSerialize(body);
		logger.info("Receive a client request message："+str);
		resPacket.setType(MessagePacketType.STRING_MESSAGE);
		resPacket.setBody(SerializeUtils.serialize("I got your message. Your message is:" + str));

		return resPacket;
	}
	/**
	 * @param body
	 */
	public MessagePacket confirmTransaction(byte[] body) throws Exception {

		ServerResponseVo responseVo = new ServerResponseVo();
		MessagePacket resPacket = new MessagePacket();
		Transaction tx = (Transaction) SerializeUtils.unSerialize(body);
		logger.info("Transaction confirmation request received， {}", tx);
		responseVo.setItem(tx);
		if (Sign.verify(Keys.publicKeyDecode(tx.getPublicKey()), tx.getSign(), tx.toString())) {
			responseVo.setSuccess(true);
			transactionPool.addTransaction(tx);
		} else {
			responseVo.setSuccess(false);
			responseVo.setMessage("Transaction signature error");
			logger.info("Transaction confirmation fails, transaction signature is wrong, {}", tx);
		}
		resPacket.setType(MessagePacketType.RES_CONFIRM_TRANSACTION);
		resPacket.setBody(SerializeUtils.serialize(responseVo));

		return resPacket;
	}

	/**
	 * @param body
	 * @return
	 */
	public MessagePacket fetchNextBlock(byte[] body) {

		ServerResponseVo responseVo = new ServerResponseVo();
		MessagePacket resPacket = new MessagePacket();
		Integer blockIndex = (Integer) SerializeUtils.unSerialize(body);
		logger.info("Block synchronization request received, the height of block synchronization is, {}", blockIndex);
		Optional<Block> block = dbAccess.getBlock(blockIndex);
		if (block.isPresent()) {
			responseVo.setItem(block.get());
			responseVo.setSuccess(true);
		} else {
			responseVo.setSuccess(false);
			responseVo.setItem(null);
			responseVo.setMessage("The block to synchronize does not exist.{"+blockIndex+"}");
		}
		resPacket.setType(MessagePacketType.RES_SYNC_NEXT_BLOCK);
		resPacket.setBody(SerializeUtils.serialize(responseVo));

		return resPacket;
	}

	public MessagePacket newBlock(byte[] body) throws Exception {

		ServerResponseVo responseVo = new ServerResponseVo();
		MessagePacket resPacket = new MessagePacket();
		Block newBlock = (Block) SerializeUtils.unSerialize(body);
		logger.info("New block confirmation request received： {}", newBlock);
		if (checkBlock(newBlock, dbAccess)) {
			dbAccess.putLastBlockIndex(newBlock.getHeader().getIndex());
			dbAccess.putBlock(newBlock);
			responseVo.setSuccess(true);
			executor.run(newBlock);
		} else {
			logger.error("Block confirmation failed：{}", newBlock);
			responseVo.setSuccess(false);
			responseVo.setMessage("Block check failed, invalid block.");
		}
		responseVo.setItem(newBlock);
		resPacket.setType(MessagePacketType.RES_NEW_BLOCK);
		resPacket.setBody(SerializeUtils.serialize(responseVo));

		return resPacket;
	}

	/**
	 * @param body
	 * @return
	 */
	public MessagePacket newAccount(byte[] body) {

		ServerResponseVo responseVo = new ServerResponseVo();
		MessagePacket resPacket = new MessagePacket();
		Account account = (Account) SerializeUtils.unSerialize(body);
		logger.info("Receive a new account synchronization request： {}", account);
		if (WalletUtils.isValidAddress(account.getAddress())) {
			dbAccess.putAccount(account);
			responseVo.setSuccess(true);
		} else {
			responseVo.setSuccess(false);
			responseVo.setMessage("Illegal wallet address");
			logger.error("New account synchronization confirmation failed, invalid wallet address, {}", account);
		}
		responseVo.setItem(account);
		resPacket.setType(MessagePacketType.RES_NEW_ACCOUNT);
		resPacket.setBody(SerializeUtils.serialize(responseVo));

		return resPacket;
	}

	/**
	 * @return
	 */
	public MessagePacket getAccountList(byte[] body) {

		String message = (String) SerializeUtils.unSerialize(body);
		ServerResponseVo responseVo = new ServerResponseVo();
		MessagePacket resPacket = new MessagePacket();
		logger.info("Received a request for an account list");
		if (Objects.equal(message, MessagePacket.FETCH_ACCOUNT_LIST_SYMBOL)) {
			List<Account> accounts = dbAccess.listAccounts();
			responseVo.setSuccess(true);
			responseVo.setItem(accounts);
		} else {
			responseVo.setSuccess(false);
		}
		resPacket.setType(MessagePacketType.RES_ACCOUNTS_LIST);
		resPacket.setBody(SerializeUtils.serialize(responseVo));

		return  resPacket;
	}

	/**
	 * @param body
	 * @return
	 */
	public MessagePacket getNodeList(byte[] body) {
		String message = (String) SerializeUtils.unSerialize(body);
		ServerResponseVo responseVo = new ServerResponseVo();
		MessagePacket resPacket = new MessagePacket();
		logger.info("A request for a node list was received");
		if (Objects.equal(message, MessagePacket.FETCH_NODE_LIST_SYMBOL)) {
			Optional<List<Node>> nodes = dbAccess.getNodeList();
			if (nodes.isPresent()) {
				responseVo.setSuccess(true);
				responseVo.setItem(nodes.get());
			}
		} else {
			responseVo.setSuccess(false);
		}
		resPacket.setType(MessagePacketType.RES_NODE_LIST);
		resPacket.setBody(SerializeUtils.serialize(responseVo));

		return  resPacket;
	}
}
