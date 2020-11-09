package com.hades_region.blockchain.listener;

import com.google.common.base.Optional;
import com.hades_region.blockchain.core.Block;
import com.hades_region.blockchain.event.FetchNextBlockEvent;
import com.hades_region.blockchain.event.MineBlockEvent;
import com.hades_region.blockchain.utils.SerializeUtils;
import com.hades_region.blockchain.db.DBAccess;
import com.hades_region.blockchain.net.base.MessagePacket;
import com.hades_region.blockchain.net.base.MessagePacketType;
import com.hades_region.blockchain.net.client.AppClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Robert Gerard
 * @since 18-4-19
 */
@Component
public class BlockEventListener {

	@Autowired
	private AppClient appClient;
	@Autowired
	private DBAccess dbAccess;
	private static Logger logger = LoggerFactory.getLogger(BlockEventListener.class);

	/**
	 * @param event
	 */
	@EventListener(MineBlockEvent.class)
	public void mineBlock(MineBlockEvent event) {

		logger.info("++++++++++++++ Start broadcasting the new block +++++++++++++++++++++");
		Block block = (Block) event.getSource();
		MessagePacket messagePacket = new MessagePacket();
		messagePacket.setType(MessagePacketType.REQ_NEW_BLOCK);
		messagePacket.setBody(SerializeUtils.serialize(block));
		appClient.sendGroup(messagePacket);
	}

	/**
	 * @param event
	 */
	@EventListener(FetchNextBlockEvent.class)
	public void fetchNextBlock(FetchNextBlockEvent event) {

		logger.info("++++++++++++++++++++++++++++++ Start sending messages in groups next Block +++++++++++++++++++++++++++++++++");
		Integer blockIndex = (Integer) event.getSource();
		if (blockIndex == 0) {
			Optional<Object> lastBlockIndex = dbAccess.getLastBlockIndex();
			if (lastBlockIndex.isPresent()) {
				blockIndex = (Integer) lastBlockIndex.get();
			}
		}
		MessagePacket messagePacket = new MessagePacket();
		messagePacket.setType(MessagePacketType.REQ_SYNC_NEXT_BLOCK);
		messagePacket.setBody(SerializeUtils.serialize(blockIndex+1));
		appClient.sendGroup(messagePacket);
	}

}
