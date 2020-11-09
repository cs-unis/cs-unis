package com.hades_region.blockchain.crypto;

import com.hades_region.blockchain.utils.ByteUtils;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author Robert Gerard
 * @since 18-4-8
 */
public class BtcAddress {

	private static final String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";

	/**
	 * @param publicKey
	 * @return
	 */
	public static String getAddress(byte[] publicKey) {

		byte[] sha256Bytes = Hash.sha3(publicKey);
		RIPEMD160Digest digest = new RIPEMD160Digest();
		digest.update(sha256Bytes, 0, sha256Bytes.length);
		byte[] ripemd160Bytes = new byte[digest.getDigestSize()];
		digest.doFinal(ripemd160Bytes, 0);
		byte[] networkID = new BigInteger("00", 16).toByteArray();
		byte[] extendedRipemd160Bytes = ByteUtils.add(networkID, ripemd160Bytes);
		byte[] oneceSha256Bytes = Hash.sha3(extendedRipemd160Bytes);
		byte[] twiceSha256Bytes = Hash.sha3(oneceSha256Bytes);
		byte[] checksum = new byte[4];
		System.arraycopy(twiceSha256Bytes, 0, checksum, 0, 4);
		byte[] binaryAddressBytes = ByteUtils.add(extendedRipemd160Bytes, checksum);
		return Base58.encode(binaryAddressBytes);
	}

	/**
	 * @param address
	 * @return
	 */
	public static boolean verifyAddress(String address) {

		if (address.length() < 26 || address.length() > 35) {
			return false;
		}
		byte[] decoded = decodeBase58To25Bytes(address);
		if (null == decoded) {
			return false;
		}
		// 验证校验码
		byte[] hash1 = Hash.sha3(Arrays.copyOfRange(decoded, 0, 21));
		byte[] hash2 = Hash.sha3(hash1);

		return Arrays.equals(Arrays.copyOfRange(hash2, 0, 4), Arrays.copyOfRange(decoded, 21, 25));
	}

	/**
	 * @param input
	 * @return
	 */
	private static byte[] decodeBase58To25Bytes(String input) {

		BigInteger num = BigInteger.ZERO;
		for (char t : input.toCharArray()) {
			int p = ALPHABET.indexOf(t);
			if (p == -1) {
				return null;
			}
			num = num.multiply(BigInteger.valueOf(58)).add(BigInteger.valueOf(p));
		}

		byte[] result = new byte[25];
		byte[] numBytes = num.toByteArray();
		System.arraycopy(numBytes, 0, result, result.length - numBytes.length, numBytes.length);
		return result;
	}
}
