package com.hades_region.blockchain.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Robert Gerard
 * @since 2018-04-21 下午4:14.
 */
@Configuration
@ConfigurationProperties(prefix = "rocksdb")
public class RocksDbProperties {

	private String dataDir;

	public String getDataDir() {
		return dataDir;
	}

	public void setDataDir(String dataDir) {
		this.dataDir = dataDir;
	}
}
