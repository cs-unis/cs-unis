package com.hades_region.blockchain.utils;

import com.hades_region.blockchain.crypto.BtcAddress;
import com.hades_region.blockchain.crypto.ECKeyPair;
import com.hades_region.blockchain.crypto.Keys;
import com.hades_region.blockchain.crypto.Sign;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PublicKey;

/**
 * @author Robert Gerard
 * @since 18-4-9
 */
public class SignTest {

	static Logger logger = LoggerFactory.getLogger(SignTest.class);

	@Test
	public void sign() throws Exception {

		ECKeyPair ecKeyPair = Keys.createEcKeyPair();
		String btcAddress = BtcAddress.getAddress(ecKeyPair.getPublicKey().getEncoded());
		String ethAddress = ecKeyPair.getAddress();
		String data = "ppblock";
		String sign = Sign.sign(ecKeyPair.getPrivateKey(), data);
		logger.info("btc address: "+ btcAddress);
		logger.info("ether address: "+ ethAddress);
		logger.info("private key: "+ ecKeyPair.exportPrivateKey());
		logger.info("public key: "+ ecKeyPair.getPublicKey());
		logger.info("sign: "+ sign);
		logger.info("sign verify result: "+Sign.verify(ecKeyPair.getPublicKey(), sign, data));

		String publicKeyEncode = Keys.publicKeyEncode(ecKeyPair.getPublicKey().getEncoded());
		logger.info("sign verify result: "+Sign.verify(Keys.publicKeyDecode(publicKeyEncode), sign, data));

		PublicKey publicKey = Sign.publicKeyFromPrivate(ecKeyPair.getPrivateKeyValue());
		logger.info("address: "+ BtcAddress.getAddress(publicKey.getEncoded()));
	}

}
