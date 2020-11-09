package com.hades_region.blockchain.wallet;

import com.hades_region.blockchain.crypto.Credentials;
import com.hades_region.blockchain.crypto.ECKeyPair;
import com.hades_region.blockchain.crypto.Keys;
import com.hades_region.blockchain.crypto.WalletUtils;
import com.radarcenter.blockchain.crypto.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Robert Gerard
 * @since 18-7-14
 */
public class CredentialTest {

	static Logger logger = LoggerFactory.getLogger(CredentialTest.class);

	/**
	 * @throws Exception
	 */
	@Test
	public void createByPrivateKey() throws Exception {

		String privateKey = "bc3da6fa7ab05c21a1087e93206ce7635bc4be0a23340211174662441862217e";
		Credentials credentials = Credentials.crdeate(privateKey);
		logger.info("ether address: "+ credentials.getAddress());
		logger.info("btc address: "+ credentials.getBtcAddress());
		logger.info("privateKey: "+ credentials.getEcKeyPair().exportPrivateKey());
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void createByKeypair() throws Exception {

		ECKeyPair keyPair = Keys.createEcKeyPair();
		Credentials credentials = Credentials.create(keyPair);
		logger.info("ether address: "+ credentials.getAddress());
		logger.info("btc address: "+ credentials.getBtcAddress());
		logger.info("privateKey: "+ credentials.getEcKeyPair().exportPrivateKey());
	}

	@Test
	public void loadCredentialsFromWallet() throws Exception {

		String walletFile = WalletTest.WALLET_DIR+"/UTC--2018-07-14T06-22-58.622000000Z" +
				"--0x74704f8be564c681e042e37f33efb12fc631b87c.json";
		Credentials credentials = WalletUtils.loadCredentials(WalletTest.WALLET_PASS, walletFile);
		logger.info("ether address: "+ credentials.getAddress());
		logger.info("btc address: "+ credentials.getBtcAddress());
		logger.info("privateKey: "+ credentials.getEcKeyPair().exportPrivateKey());
	}

	@Test
	public void loadCredentialsFromMemorizingWords() throws Exception {
		//educate bread attract theme obey squirrel busy food finish segment sell audit
		//0xce7d01da2b1cfe5b65f35924127fa8f746a00050
		String memorizingWords = "educate bread attract theme obey squirrel busy food finish segment sell audit";
		Credentials credentials = WalletUtils.loadBip39Credentials(memorizingWords);
		logger.info("ether address: "+ credentials.getAddress());
		logger.info("btc address: "+ credentials.getBtcAddress());
		logger.info("privateKey: "+ credentials.getEcKeyPair().exportPrivateKey());
	}

	/**
	 * test datas:
	 * memorizing word: worth flush raise credit unable very easily edge near nuclear video vicious
	 * address: 0x7154dbe7a2f9f1a9632f886201efdf996627b387
	 * password: 123456
	 */
	@Test
	public void loadCredentialsWithWordsAndPass() throws Exception {

		String words = "worth flush raise credit unable very easily edge near nuclear video vicious";
		String password = "123456";

		Credentials credentials = WalletUtils.loadBip39Credentials(password, words);
		logger.info("ether address: "+ credentials.getAddress());
		logger.info("btc address: "+ credentials.getBtcAddress());
		logger.info("privateKey: "+ credentials.getEcKeyPair().exportPrivateKey());
	}
}
