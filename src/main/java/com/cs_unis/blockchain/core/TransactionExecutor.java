package com.hades_region.blockchain.core;

import com.google.common.base.Optional;
import com.hades_region.blockchain.enums.TransactionStatusEnum;
import com.hades_region.blockchain.account.Account;
import com.hades_region.blockchain.crypto.Keys;
import com.hades_region.blockchain.crypto.Sign;
import com.hades_region.blockchain.db.DBAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author Robert Gerard
 * @since 18-4-23
 */
@Component
public class TransactionExecutor {

	@Autowired
	private DBAccess dbAccess;

	@Autowired
	private TransactionPool transactionPool;

	/**
	 * @param block
	 */
	public void run(Block block) throws Exception {

		for (Transaction transaction : block.getBody().getTransactions()) {
			synchronized (this) {

				Optional<Account> recipient = dbAccess.getAccount(transaction.getTo());
				//如果收款地址账户不存在，则创建一个新账户
				if (!recipient.isPresent()) {
					recipient = Optional.of(new Account(transaction.getTo(), BigDecimal.ZERO));
				}
				//挖矿奖励
				if (null == transaction.getFrom()) {
					recipient.get().setBalance(recipient.get().getBalance().add(transaction.getAmount()));
					dbAccess.putAccount(recipient.get());
					continue;
				}
				Optional<Account> sender = dbAccess.getAccount(transaction.getFrom());
				boolean verify = Sign.verify(
						Keys.publicKeyDecode(transaction.getPublicKey()),
						transaction.getSign(),
						transaction.toString());
				if (!verify) {
					transaction.setStatus(TransactionStatusEnum.FAIL);
					transaction.setErrorMessage("Transaction signature error");
					continue;
				}
				if (sender.get().getBalance().compareTo(transaction.getAmount()) == -1) {
					transaction.setStatus(TransactionStatusEnum.FAIL);
					transaction.setErrorMessage("Insufficient account balance");
					continue;
				}

				sender.get().setBalance(sender.get().getBalance().subtract(transaction.getAmount()));
				recipient.get().setBalance(recipient.get().getBalance().add(transaction.getAmount()));
				dbAccess.putAccount(sender.get());
				dbAccess.putAccount(recipient.get());
			}//end synchronize
		}// end for

		transactionPool.clearTransactions();
	}
}
