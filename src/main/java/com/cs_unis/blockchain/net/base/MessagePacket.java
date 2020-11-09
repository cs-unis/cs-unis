package com.hades_region.blockchain.net.base;

import org.tio.core.intf.Packet;

/**
 * @author Robert Gerard
 */
public class MessagePacket extends Packet {

	public static final int HEADER_LENGTH = 5;

	public static final String HELLO_MESSAGE = "Hello world.";
	public static final String FETCH_ACCOUNT_LIST_SYMBOL = "get_accounts_list";
	public static final String FETCH_NODE_LIST_SYMBOL = "get_nodes_list";
	private byte type;

	private byte[] body;

	public MessagePacket(byte[] body) {
		this.body = body;
	}

	public MessagePacket() {
	}

	public MessagePacket(byte type) {
		this.type = type;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	/**
	 * @return
	 */
	public byte[] getBody() {
		return body;
	}

	/**
	 * @param body
	 */
	public void setBody(byte[] body) {
		this.body = body;
	}

}
