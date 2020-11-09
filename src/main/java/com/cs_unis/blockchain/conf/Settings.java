package com.hades_region.blockchain.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Robert Gerard
 * @since 18-7-14
 */
@Configuration
@ConfigurationProperties(prefix = "settings")
public class Settings {

	private boolean nodeDiscover;

	private boolean autoMining;

	public boolean isNodeDiscover() {
		return nodeDiscover;
	}

	public void setNodeDiscover(boolean nodeDiscover) {
		this.nodeDiscover = nodeDiscover;
	}

	public boolean isAutoMining() {
		return autoMining;
	}

	public void setAutoMining(boolean autoMining) {
		this.autoMining = autoMining;
	}
}
