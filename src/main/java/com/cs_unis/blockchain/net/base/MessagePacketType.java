package com.hades_region.blockchain.net.base;

/**
 * @author Robert Gerard
 * @since 18-4-19
 */
public interface MessagePacketType {

	byte STRING_MESSAGE = 0;

	byte REQ_NEW_BLOCK = 1;

	byte RES_NEW_BLOCK = -1;

	byte REQ_CONFIRM_TRANSACTION = 2;

	byte RES_CONFIRM_TRANSACTION = -2;

	byte REQ_SYNC_NEXT_BLOCK = 3;

	byte RES_SYNC_NEXT_BLOCK = -3;

	byte REQ_NEW_ACCOUNT = 4;

	byte RES_NEW_ACCOUNT = -4;

	byte REQ_ACCOUNTS_LIST = 5;

	byte RES_ACCOUNTS_LIST = -5;

	byte REQ_NODE_LIST = 6;

	byte RES_NODE_LIST = -6;

}
