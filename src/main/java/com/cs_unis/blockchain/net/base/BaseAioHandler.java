package com.hades_region.blockchain.net.base;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.hades_region.blockchain.core.Block;
import com.hades_region.blockchain.mine.pow.ProofOfWork;
import com.hades_region.blockchain.db.DBAccess;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;

import java.nio.ByteBuffer;

/**
 * @author Robert Gerard
 * @since 18-4-17
 */
public abstract class BaseAioHandler {

	public MessagePacket decode(ByteBuffer buffer, ChannelContext channelContext) throws AioDecodeException {

		int readableLength = buffer.limit() - buffer.position();
		if (readableLength < MessagePacket.HEADER_LENGTH) {
			return null;
		}
		byte messageType = buffer.get();
		int bodyLength = buffer.getInt();

		if (bodyLength < 0) {
			throw new AioDecodeException("bodyLength [" + bodyLength + "] is not right, remote:" + channelContext.getClientNode());
		}
		int neededLength = MessagePacket.HEADER_LENGTH + bodyLength;
		int isDataEnough = readableLength - neededLength;
		if (isDataEnough < 0) {
			return null;
		} else
		{
			MessagePacket imPacket = new MessagePacket();
			imPacket.setType(messageType);
			if (bodyLength > 0) {
				byte[] dst = new byte[bodyLength];
				buffer.get(dst);
				imPacket.setBody(dst);
			}
			return imPacket;
		}
	}

	public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {

		MessagePacket messagePacket = (MessagePacket) packet;
		byte[] body = messagePacket.getBody();
		int bodyLen = 0;
		if (body != null) {
			bodyLen = body.length;
		}

		int allLen = MessagePacket.HEADER_LENGTH + bodyLen;
		ByteBuffer buffer = ByteBuffer.allocate(allLen);
		buffer.order(groupContext.getByteOrder());

		buffer.put(messagePacket.getType());
		buffer.putInt(bodyLen);

		if (body != null) {
			buffer.put(body);
		}
		return buffer;
	}

	/**
	 * @param block
	 * @param dbAccess
	 * @return
	 */
	public boolean checkBlock(Block block, DBAccess dbAccess) {

		if (block.getHeader().getIndex() == 1) {
			return Objects.equal(block.getHeader().getHash(), block.getHeader().hash());
		}

		boolean blockValidate = false;
		if (block.getHeader().getIndex() > 1) {
			Optional<Block> prevBlock = dbAccess.getBlock(block.getHeader().getIndex()-1);
			if (prevBlock.isPresent()
					&& prevBlock.get().getHeader().getHash().equals(block.getHeader().getPreviousHash())) {
				blockValidate = true;
			}
		}
		ProofOfWork proofOfWork = ProofOfWork.newProofOfWork(block);
		if (!proofOfWork.validate()) {
			blockValidate = false;
		}

		return blockValidate;
	}

}
