package com.hades_region.blockchain.event;

import com.hades_region.blockchain.core.Transaction;
import org.springframework.context.ApplicationEvent;

/**
 * @author Robert Gerard
 */
public class SendTransactionEvent extends ApplicationEvent {

    public SendTransactionEvent(Transaction transaction) {
        super(transaction);
    }

}
