package com.hades_region.blockchain.account;

import com.google.common.base.Optional;
import com.hades_region.blockchain.crypto.ECKeyPair;
import com.hades_region.blockchain.event.NewAccountEvent;
import com.hades_region.blockchain.db.DBAccess;
import com.hades_region.blockchain.net.ApplicationContextProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author Robert Gerard
 * @since 18-4-6
 */
@Component
public class Personal {

	@Autowired
	private DBAccess dbAccess;

	/**
	 * @param keyPair
	 * @return
	 */
	public Account newAccount(ECKeyPair keyPair) throws Exception {

		Account account = new Account(keyPair.getAddress(), BigDecimal.ZERO);
		dbAccess.putAccount(account);
		ApplicationContextProvider.publishEvent(new NewAccountEvent(account));
		Optional<Account> coinBaseAccount = dbAccess.getCoinBaseAccount();
		if (!coinBaseAccount.isPresent()) {
			dbAccess.putCoinBaseAccount(account);
		}
		return account;
	}
}
