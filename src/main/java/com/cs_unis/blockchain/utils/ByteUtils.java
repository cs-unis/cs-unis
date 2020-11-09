package com.hades_region.blockchain.utils;

import org.apache.commons.lang3.ArrayUtils;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author Robert Gerard
 * @since 18-4-9
 */
public class ByteUtils {

	/**
	 * @param data1
	 * @param data2
	 * @return
	 */
	public static byte[] add(byte[] data1, byte[] data2) {

		byte[] result = new byte[data1.length + data2.length];
		System.arraycopy(data1, 0, result, 0, data1.length);
		System.arraycopy(data2, 0, result, data1.length, data2.length);

		return result;
	}

	/**
	 * @param bytes
	 * @return
	 */
	public static byte[] merge(byte[]... bytes) {
		Stream<Byte> stream = Stream.of();
		for (byte[] b : bytes) {
			stream = Stream.concat(stream, Arrays.stream(ArrayUtils.toObject(b)));
		}
		return ArrayUtils.toPrimitive(stream.toArray(Byte[]::new));
	}

	/**
	 * @param val
	 * @return
	 */
	public static byte[] toBytes(long val) {
		return ByteBuffer.allocate(Long.BYTES).putLong(val).array();
	}


}
