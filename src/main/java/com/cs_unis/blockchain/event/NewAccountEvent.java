package com.hades_region.blockchain.event;

import com.hades_region.blockchain.account.Account;
import org.springframework.context.ApplicationEvent;

/**
 * @author Robert Gerard
 */
public class NewAccountEvent extends ApplicationEvent {

    public NewAccountEvent(Account account) {
        super(account);
    }
}
