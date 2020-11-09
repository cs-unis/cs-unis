package com.hades_region.blockchain.net.server;

import com.hades_region.blockchain.net.conf.TioProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tio.server.AioServer;
import org.tio.server.ServerGroupContext;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author Robert Gerard
 */
@Component
public class AppServer {

	@Resource
	private ServerGroupContext serverGroupContext;
	@Autowired
	private TioProperties properties;

	@PostConstruct
	public void serverStart() throws IOException {

		AioServer aioServer = new AioServer(serverGroupContext);
		aioServer.start(properties.getServerIp(), properties.getServerPort());
	}
}