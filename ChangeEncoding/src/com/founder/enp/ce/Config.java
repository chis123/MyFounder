package com.founder.enp.ce;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Config {
	
	private static final Log log = LogFactory.getLog(Config.class);

	private Configuration configuration;
	
	public Config()
	{
		String workdir = System.getProperty("user.dir");
		String configFile = workdir + "/config.xml";
		try {
			configuration = new XMLConfiguration(configFile);
		} catch (ConfigurationException ex) {
			log.error("读取配置文件出现异常", ex);
		}
	}
	
	public String getConfig(String param) {
		return configuration.getString(param);
	}
}
