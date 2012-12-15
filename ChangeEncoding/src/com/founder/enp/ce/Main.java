/*
 * $Logfile$
 * $Revision$
 * $Date$
 * $Author$
 * $History$
 *
 * Copyright (c) 2006, 北大方正电子有限公司数字媒体开发部
 * All rights reserved.
 */
package com.founder.enp.ce;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Main {
	
	private static Log log = LogFactory.getLog(Main.class);
	private Config config = new Config();
	protected static FileQueue queue = new FileQueue();

	public static void main(String[] args)
	{
		log.info("程序开始运行...");
		long begin = System.currentTimeMillis();
		
		Main main = new Main();
		
		main.run();
		main.test();
		
		long end = System.currentTimeMillis();
		log.info("程序运行结束，用时：" + (end - begin));
	}
	
	private void run() {
		String inputDir = config.getConfig("input.dir");
		log.debug(inputDir);
		File dir = new File(inputDir);
		if(!dir.isDirectory()) {
			log.info("指定的输入目录[" + inputDir + "]不正确");
			return;
		}
		listFile(dir);
	}
	
	private void listFile(File dir) {
		File[] subs = dir.listFiles();
		for(int i = 0; i < subs.length; i++) {
			File sub = subs[i];
			if(sub.isFile()) {
				queue.push(sub.getAbsolutePath());
			}
			else {
				listFile(sub);
			}
		}
	}
	
	private void runtask() {
		int threadCount = 1;
		try {
			threadCount = Integer.parseInt(config.getConfig("threadCount"));
		}
		catch(Exception ex) {
			log.error("配置文件中线程数参数不正确");
		}
		//ThreadGroup group = 
	}
	
	private void test() {
		while(queue.size() > 0)
			System.out.println(queue.pull());
	}
}
