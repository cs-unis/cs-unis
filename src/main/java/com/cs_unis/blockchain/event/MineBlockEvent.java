package com.hades_region.blockchain.event;

import com.hades_region.blockchain.core.Block;
import org.springframework.context.ApplicationEvent;

/**
 * @author Robert Gerard
 */
public class MineBlockEvent extends ApplicationEvent {

    public MineBlockEvent(Block block) {
        super(block);
    }
}
