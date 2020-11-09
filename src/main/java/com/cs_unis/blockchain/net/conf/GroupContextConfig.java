package com.hades_region.blockchain.net.conf;

import com.hades_region.blockchain.net.client.AppClientAioHandler;
import com.hades_region.blockchain.net.client.AppClientAioListener;
import com.hades_region.blockchain.net.server.AppServerAioHandler;
import com.hades_region.blockchain.net.server.AppServerAioListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.server.ServerGroupContext;

/**
 * @author Robert Gerard
 * @since 18-4-18
 */
@Configuration
public class GroupContextConfig {

	@Autowired
	TioProperties tioProperties;

	@Autowired
	AppClientAioHandler clientAioHandler;

	@Autowired
	AppClientAioListener clientAioListener;

	@Autowired
	AppServerAioHandler serverAioHandler;

	@Autowired
	AppServerAioListener serverAioListener;

	/**
	 * @return
	 */
	@Bean
	public ClientGroupContext clientGroupContext() {

		ReconnConf reconnConf = new ReconnConf(5000L, 20);
		ClientGroupContext clientGroupContext = new ClientGroupContext(clientAioHandler, clientAioListener, reconnConf);
		clientGroupContext.setHeartbeatTimeout(tioProperties.getHeartTimeout());
		return clientGroupContext;
	}

	/**
	 * @return
	 */
	@Bean
	public ServerGroupContext serverGroupContext() {

		ServerGroupContext serverGroupContext = new ServerGroupContext(
				tioProperties.getServerGroupContextName(),
				serverAioHandler,
				serverAioListener);
		serverGroupContext.setHeartbeatTimeout(tioProperties.getHeartTimeout());

		return serverGroupContext;
	}

}
