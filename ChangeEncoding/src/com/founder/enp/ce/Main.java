/*
 * $Logfile$
 * $Revision$
 * $Date$
 * $Author$
 * $History$
 *
 * Copyright (c) 2006, �������������޹�˾����ý�忪����
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
		log.info("����ʼ����...");
		long begin = System.currentTimeMillis();
		
		Main main = new Main();
		
		main.run();
		main.test();
		
		long end = System.currentTimeMillis();
		log.info("�������н�������ʱ��" + (end - begin));
	}
	
	private void run() {
		String inputDir = config.getConfig("input.dir");
		log.debug(inputDir);
		File dir = new File(inputDir);
		if(!dir.isDirectory()) {
			log.info("ָ��������Ŀ¼[" + inputDir + "]����ȷ");
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
			log.error("�����ļ����߳�����������ȷ");
		}
		//ThreadGroup group = 
	}
	
	private void test() {
		while(queue.size() > 0)
			System.out.println(queue.pull());
	}
}
