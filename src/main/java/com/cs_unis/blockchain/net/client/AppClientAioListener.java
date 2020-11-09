package com.hades_region.blockchain.net.client;

import com.hades_region.blockchain.net.conf.TioProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;

/**
 *
 * @author Robert Gerard
 */
@Component
public class AppClientAioListener implements ClientAioListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    TioProperties tioProperties;

    @Override
    public void onAfterClose(ChannelContext channelContext, Throwable throwable, String s, boolean b) throws Exception {
        logger.info("Connection is closed：server address is-" + channelContext.getServerNode());
        Aio.unbindGroup(channelContext);
    }

    @Override
    public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception {
        if (isConnected || isReconnect) {
            logger.info("Connection successful：server address is-" + channelContext.getServerNode());
            Aio.bindGroup(channelContext, tioProperties.getClientGroupName());
        } else {
            logger.info("Connection fail：server address is-" + channelContext.getServerNode());
        }
    }

    @Override
    public void onAfterReceived(ChannelContext channelContext, Packet packet, int i) throws Exception {

    }

    @Override
    public void onAfterSent(ChannelContext channelContext, Packet packet, boolean b) throws Exception {

    }

    @Override
    public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String s, boolean b) {

    }
}
