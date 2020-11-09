package com.hades_region.blockchain.net.client;

import com.google.common.base.Optional;
import com.hades_region.blockchain.account.Account;
import com.hades_region.blockchain.core.Block;
import com.hades_region.blockchain.core.Transaction;
import com.hades_region.blockchain.db.DBAccess;
import com.hades_region.blockchain.event.FetchNextBlockEvent;
import com.hades_region.blockchain.net.ApplicationContextProvider;
import com.hades_region.blockchain.net.base.*;
import com.hades_region.blockchain.utils.SerializeUtils;
import com.radarcenter.blockchain.net.base.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;

import java.util.List;

/**
 * @author Robert Gerard
 */
@Component
public class AppClientAioHandler extends BaseAioHandler implements ClientAioHandler {

	private static Logger logger = LoggerFactory.getLogger(AppClientAioHandler.class);
	@Autowired
	private DBAccess dbAccess;
	@Autowired
	private AppClient appClient;
	private static MessagePacket heartbeatPacket = new MessagePacket(MessagePacketType.STRING_MESSAGE);

	@Override
	public void handler(Packet packet, ChannelContext channelContext) throws Exception {

		MessagePacket messagePacket = (MessagePacket) packet;
		byte[] body = messagePacket.getBody();
		byte type = messagePacket.getType();
		if (body != null) {
			logger.info("Response node information， {}", channelContext.getServerNode());
			switch (type) {

				case MessagePacketType.STRING_MESSAGE:
					String str = (String) SerializeUtils.unSerialize(body);
					logger.info("Receive a server confirmation message："+str);
					break;

				case MessagePacketType.RES_CONFIRM_TRANSACTION:
					this.confirmTransaction(body);
					break;

				case MessagePacketType.RES_SYNC_NEXT_BLOCK:
					this.fetchNextBlock(body);
					break;

				case MessagePacketType.RES_NEW_BLOCK:
					this.newBlock(body);
					break;

				case MessagePacketType.RES_NEW_ACCOUNT:
					this.newAccount(body);
					break;

				case MessagePacketType.RES_ACCOUNTS_LIST:
					this.getAccountList(body);
					break;

				case MessagePacketType.RES_NODE_LIST:
					this.getNodeList(body);
					break;

			} //end of switch

		}

		return;
	}

	/**
	 * @param body
	 */
	public void confirmTransaction(byte[] body) {

		logger.info("Transaction confirmation response received");
		ServerResponseVo responseVo = (ServerResponseVo) SerializeUtils.unSerialize(body);
		Transaction tx = (Transaction) responseVo.getItem();
		if (responseVo.isSuccess()) {
			logger.info("Transaction confirmation successful， {}", tx);
		} else {
			logger.error("Transaction confirmation failure, {}", tx);
		}
	}

	/**
	 * @param body
	 */
	public void fetchNextBlock(byte[] body) {

		ServerResponseVo responseVo = (ServerResponseVo) SerializeUtils.unSerialize(body);
		if (!responseVo.isSuccess()) {
			logger.error("Block synchronization failure, "+responseVo.getMessage());
			return;
		}
		Block block = (Block) responseVo.getItem();
		if (dbAccess.getBlock(block.getHeader().getIndex()).isPresent()) {
			return;
		}
		if (checkBlock(block, dbAccess)) {
			Optional<Object> lastBlockIndex = dbAccess.getLastBlockIndex();
			if (lastBlockIndex.isPresent()) {
				Integer blockIndex = (Integer) lastBlockIndex.get();
				if (blockIndex  < block.getHeader().getIndex()) {
					dbAccess.putBlock(block);
					dbAccess.putLastBlockIndex(block.getHeader().getIndex());
				}
			} else {
				dbAccess.putBlock(block);
				dbAccess.putLastBlockIndex(block.getHeader().getIndex());
			}
			logger.info("Block synchronization successful， {}", block.getHeader());
			ApplicationContextProvider.publishEvent(new FetchNextBlockEvent(0));
		} else {
			logger.error("Block synchronization failure，{}", block.getHeader());
			//ApplicationContextProvider.publishEvent(new FetchNextBlockEvent(block.getHeader().getIndex()-1));
		}
	}

	/**
	 * @param body
	 */
	public void newBlock(byte[] body) {
		ServerResponseVo responseVo = (ServerResponseVo) SerializeUtils.unSerialize(body);
		Block newBlock = (Block) responseVo.getItem();
		if (responseVo.isSuccess()) {
			logger.info("Block confirmation successful, {}", newBlock);
		} else {
			logger.error("Block confirmation failure, {}, {}", responseVo.getMessage(), newBlock);
		}
	}

	/**
	 * @param body
	 */
	public void newAccount(byte[] body) {
		ServerResponseVo responseVo = (ServerResponseVo) SerializeUtils.unSerialize(body);
		Account account = (Account) responseVo.getItem();
		if (responseVo.isSuccess()) {
			logger.info("New account synchronization account successful， {}", account);
		} else {
			logger.error("New account synchronization account failure, {}", account);
		}
	}

	/**
	 * @param body
	 */
	public void getAccountList(byte[] body) {

		ServerResponseVo responseVo = (ServerResponseVo) SerializeUtils.unSerialize(body);
		if (!responseVo.isSuccess()) {
			return;
		}
		List<Account> accounts = (List<Account>) responseVo.getItem();
		for (Account e : accounts) {
			Optional<Account> acc = dbAccess.getAccount(e.getAddress());
			if (acc.isPresent()) {
				logger.info("Account already in existence：{}", e);
				continue;
			}
			if (dbAccess.putAccount(e)) {
				logger.info("Sync account successful：{}", e);
			}

		}
	}

	/**
	 * @param body
	 */
	public void getNodeList(byte[] body) throws Exception {

		ServerResponseVo responseVo = (ServerResponseVo) SerializeUtils.unSerialize(body);
		if (!responseVo.isSuccess()) {
			return;
		}
		List<Node> nodes = (List<Node>) responseVo.getItem();
		for (Node node : nodes) {
			dbAccess.addNode(node);
			appClient.addNode(node.getIp(), node.getPort());
		}

	}

	/**
	 * @return
	 */
	@Override
	public MessagePacket heartbeatPacket() {
		return heartbeatPacket;
	}
}
