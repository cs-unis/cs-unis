package com.hades_region.blockchain.wallet;

import com.hades_region.blockchain.crypto.Bip39Wallet;
import com.hades_region.blockchain.crypto.WalletUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author Robert Gerard
 * @since 18-4-9
 */
public class WalletTest {

	static Logger logger = LoggerFactory.getLogger(WalletTest.class);
	public static final File WALLET_DIR = new File("./keystore");
	public static final String WALLET_PASS = "123456";

	static {
		if (!WALLET_DIR.exists()) {
			WALLET_DIR.mkdir();
		}
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void generateNewWalletFile() throws Exception {

		String filename = WalletUtils.generateFullNewWalletFile(WALLET_PASS, WALLET_DIR);
		logger.info("wallet name: " + filename);
	}

	@Test
	public void generateLightNewWalletFile() throws Exception {
		String filename = WalletUtils.generateLightNewWalletFile(WALLET_PASS, WALLET_DIR);
		logger.info("wallet name: " + filename);
	}

	@Test
	public void generateBip39Wallet() throws Exception {

		Bip39Wallet wallet = WalletUtils.generateBip39Wallet();
		logger.info("memorizing word: "+ wallet.getMnemonic());
		logger.info("address: " + wallet.getKeyPair().getAddress());
	}

	@Test
	public void generateBip39WalletFileWithPass() throws Exception {

		Bip39Wallet wallet = WalletUtils.generateBip39Wallet(WALLET_PASS, WALLET_DIR);
		logger.info("memorizing word: "+ wallet.getMnemonic());
		logger.info("wallet file: "+ wallet.getFilename());
		logger.info("address: " + wallet.getKeyPair().getAddress());
	}

	@Test
	public void generateBip39WalletWithPass() throws Exception {

		Bip39Wallet wallet = WalletUtils.generateBip39Wallet(WALLET_PASS);
		logger.info("memorizing word: "+ wallet.getMnemonic());
		logger.info("address: " + wallet.getKeyPair().getAddress());
	}

}
