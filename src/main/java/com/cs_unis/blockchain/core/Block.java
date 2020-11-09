package com.hades_region.blockchain.core;

import java.io.Serializable;

/**
 * @author Robert Gerard
 * @since 18-4-6
 */
public class Block implements Serializable {

	private BlockHeader header;
	private BlockBody body;

	public Block(BlockHeader header, BlockBody body) {
		this.header = header;
		this.body = body;
	}

	public Block() {
	}

	public BlockHeader getHeader() {
		return header;
	}

	public void setHeader(BlockHeader header) {
		this.header = header;
	}

	public BlockBody getBody() {
		return body;
	}

	public void setBody(BlockBody body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "Block{" +
				"header=" + header +
				", body=" + body.toString() +
				'}';
	}
}
