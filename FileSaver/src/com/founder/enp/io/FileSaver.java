package com.founder.enp.io;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileSaver {
	
	//系统配置
	private PropertiesConfiguration config;
	//系统日志
	private static Log log = LogFactory.getLog(FileSaver.class);;
	//源名称目录
	protected String srcName;
	protected File srcNameDir;
	//源实体目录
	protected String srcText;
	protected File srcTextDir;
	//目标目录
	protected String dest;
	protected File destDir;
	//源名称目录下的文件列表
	protected File[] nameFiles;
	protected Queue nameQueue = new ConcurrentLinkedQueue();
	protected Map hashMap = new HashMap();
	//扫描的间隔时间，缺省1000毫秒
	private int scanTime = 1000;

	/**
	 * 程序主入口
	 */
	public static void main(String[] args) {
		log.info("********************************");
		log.info("FileSaver start...");
		log.info("********************************");
		FileSaver fs = new FileSaver();
		try {
			fs.getConfig();
		} catch (ConfigurationException e) {
			log.fatal("Can not read config file!", e);
			System.exit(1);
		}
		fs.checkDir();
		
		FileMover fm = new FileMover(fs);
		Thread thread = new Thread(fm);
		thread.start();
		
		fs.scan();
	}
	
	/**
	 * 提取程序配置参数
	 * @throws ConfigurationException
	 */
	private void getConfig() throws ConfigurationException
	{
		this.config = new PropertiesConfiguration(getClass().getResource("/config.properties"));
		this.srcName = config.getString("src.name.dir");
		this.srcText = config.getString("src.text.dir");
		this.dest = config.getString("dest.dir");
		this.scanTime = config.getInt("scan.time");
		if(log.isDebugEnabled())
		{
			log.debug("src.name.dir = " + srcName);
			log.debug("src.text.dir = " + srcText);
			log.debug("dest.dir = " + dest);
		}
	}
	
	/**
	 * 校验config配置中指定的目录是否正确
	 */
	private void checkDir()
	{
		this.srcNameDir = new File(this.srcName);
		if(!srcNameDir.isDirectory())
		{
			log.error("src.name.dir is invalid!");
			System.exit(2);
		}
		this.srcTextDir = new File(this.srcText);
		if(!srcTextDir.isDirectory())
		{
			log.error("src.text.dir is invalid!");
			System.exit(3);
		}
		this.destDir = new File(this.dest);
		if(!destDir.isDirectory())
		{
			log.info("dest.dir is invalid! auto create it.");
			if(!destDir.mkdirs())
			{
				log.error("fail to create dest dir!");
				System.exit(4);
			}
		}
	}
	
	/**
	 * 扫描名称目录下的文件
	 */
	private void scan()
	{
		while(true)
		{
			log.info("scan [" + this.srcName + "]...");
			nameFiles = this.srcNameDir.listFiles();
			File file;
			if(nameFiles.length > 0)
			{
				for (int i = 0; i < nameFiles.length; i++) {
					file = (File)nameFiles[i];
					String filepath = file.getAbsolutePath();
					if(!hashMap.containsKey(filepath))
					{
						nameQueue.add(file);
						hashMap.put(filepath, filepath);
					}
				}
				if(log.isDebugEnabled())
					log.info(nameQueue.size());
			}
			else
			{
				log.info("empty!");
			}
			try {
				Thread.sleep(this.scanTime);
			} catch (InterruptedException e) {
				log.error(e);
				System.exit(5);
			}
		}
	}

}
