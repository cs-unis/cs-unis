package com.hades_region.blockchain.net.conf;

import com.hades_region.blockchain.net.base.Node;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Robert Gerard
 * @since 18-4-18
 */
@Configuration
@ConfigurationProperties(prefix = "tio")
public class TioProperties {

	@NotNull
	private int heartTimeout;
	@NotNull
	private String clientGroupName;
	@NotNull
	private String serverGroupContextName;
	@NotNull
	private int serverPort;
	private String serverIp;

	private List<Node> nodes;

	public int getHeartTimeout() {
		return heartTimeout;
	}

	public void setHeartTimeout(int heartTimeout) {
		this.heartTimeout = heartTimeout;
	}

	public String getClientGroupName() {
		return clientGroupName;
	}

	public void setClientGroupName(String clientGroupName) {
		this.clientGroupName = clientGroupName;
	}

	public String getServerGroupContextName() {
		return serverGroupContextName;
	}

	public void setServerGroupContextName(String serverGroupContextName) {
		this.serverGroupContextName = serverGroupContextName;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}
}
