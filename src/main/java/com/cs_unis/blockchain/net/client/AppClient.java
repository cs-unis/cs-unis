package com.hades_region.blockchain.net.client;

import com.google.common.base.Optional;
import com.hades_region.blockchain.conf.Settings;
import com.hades_region.blockchain.db.DBAccess;
import com.hades_region.blockchain.event.FetchNextBlockEvent;
import com.hades_region.blockchain.net.ApplicationContextProvider;
import com.hades_region.blockchain.net.base.MessagePacket;
import com.hades_region.blockchain.net.base.MessagePacketType;
import com.hades_region.blockchain.net.base.Node;
import com.hades_region.blockchain.net.conf.TioProperties;
import com.hades_region.blockchain.utils.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.tio.client.AioClient;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.core.Aio;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author Robert Gerard
 *
 */
@Component
public class AppClient {

	@Resource
	private ClientGroupContext clientGroupContext;
	@Autowired
	private TioProperties tioProperties;

	private AioClient aioClient;
	@Autowired
	private DBAccess dbAccess;
	@Autowired
    Settings settings;

	private static Logger logger = LoggerFactory.getLogger(AppClient.class);

	@PostConstruct
	public void clientStart() throws Exception {

		if (!settings.isNodeDiscover()) {
			return;
		}

		aioClient = new AioClient(clientGroupContext);
		Optional<List<Node>> nodeList = dbAccess.getNodeList();
		List<Node> nodes = null;
		if (nodeList.isPresent()) {
			nodes = nodeList.get();

		} else if (null != tioProperties.getNodes()) {
			nodes = tioProperties.getNodes();
		}
		for (Node node : nodes) {
			addNode(node.getIp(), node.getPort());
		}
	}

	/**
	 * @param messagePacket
	 */
	public void sendGroup(MessagePacket messagePacket) {

		if (!settings.isNodeDiscover()) {
			return;
		}

		Aio.sendToGroup(clientGroupContext, tioProperties.getClientGroupName(), messagePacket);
	}

	/**
	 * @param serverIp
	 * @param port
	 */
	public void addNode(String serverIp, int port) throws Exception {

		if (!settings.isNodeDiscover()) {
			return;
		}

		Node node = new Node(serverIp, port);
		ClientChannelContext channelContext = aioClient.connect(node);
		Aio.send(channelContext, new MessagePacket(SerializeUtils.serialize(MessagePacket.HELLO_MESSAGE)));
		Aio.bindGroup(channelContext, tioProperties.getClientGroupName());
		dbAccess.addNode(node);
		logger.info("Add node successfully, {}", node);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void fetchNextBlock() {
		ApplicationContextProvider.publishEvent(new FetchNextBlockEvent(0));

	}

	@EventListener(ApplicationReadyEvent.class)
	public void fetchAccounts() {

		MessagePacket packet = new MessagePacket();
		packet.setType(MessagePacketType.REQ_ACCOUNTS_LIST);
		packet.setBody(SerializeUtils.serialize(MessagePacket.FETCH_ACCOUNT_LIST_SYMBOL));
		sendGroup(packet);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void fetchNodeList() {

		logger.info("++++++++++++++++++++++++++ Start getting online nodes +++++++++++++++++++++++++++");
		MessagePacket packet = new MessagePacket();
		packet.setType(MessagePacketType.REQ_NODE_LIST);
		packet.setBody(SerializeUtils.serialize(MessagePacket.FETCH_NODE_LIST_SYMBOL));
		sendGroup(packet);
	}
}
