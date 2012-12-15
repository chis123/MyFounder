package com.founder.enp.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileMover implements Runnable {
	
	private static Log log = LogFactory.getLog(FileMover.class);
	private Object lock = new Object();
	private FileSaver fs;
	//private File[] nameList;
	
	public FileMover(FileSaver fs)
	{
		this.fs = fs;
		//this.nameList = fs.nameList;
	}

	public void run() {
		//syn
		while(true)
		{
			if(fs.nameQueue.size() > 0)
			{
				if(log.isDebugEnabled())
					log.debug("queue size:" + fs.nameQueue.size());
				File file = (File)fs.nameQueue.poll();
				String destFilePath = calOutputFile(file);
				createDestDir(destFilePath);
				if(copyFile(file, destFilePath))
					clear(file);
			}
			/*
			else
			{
				if(log.isDebugEnabled())
					log.debug("queue empty!");
			}
			*/
		}
	}

	/**
	 * ����Ŀ��Ŀ¼����dest֮�µ���Ŀ¼ֱ���ļ������֣�
	 * @param nameFile
	 * @return
	 */
	private String calOutputFile(File nameFile)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(calFileNamePath(nameFile));
		sb.append(calFileContentPath(nameFile));

		return sb.toString();
	}
	
	/**
	 * �������ļ��������ֳ�����Ŀ¼�ṹ
	 * ��Ҫ�Ƿֳ�home��news��img����Ŀ¼
	 * @param nameFile
	 * @return
	 */
	private String calFileNamePath(File nameFile)
	{
		String ret = null;
		if(nameFile.getName().startsWith("home"))
		{
			ret = "/home";
		}
		else if(nameFile.getName().startsWith("news"))
		{
			ret = "/news";
		}
		else if(nameFile.getName().startsWith("img"))
		{
			ret = "/img";
		}
		return ret;
	}
	
	/**
	 * �������ļ�����������·����Ϣ
	 * �磺xin_0021005182033062184075.jpg~/politics/leaders/zhouyongkang~htm~http~#$%
	 * @param nameFile
	 * @return /politics/leaders/zhouyongkang/xin_0021005182033062184075.jpg
	 */
	private String calFileContentPath(File nameFile)
	{
		StringBuilder sb = new StringBuilder();
		
		FileInputStream fis = null;
		List lines;
		String line;
		try
		{
			fis = new FileInputStream(nameFile);
			lines = IOUtils.readLines(fis);
			if(log.isDebugEnabled())
			{
				log.debug(lines);
			}
			if(lines.size() != 0)
			{
				line = (String)lines.get(0);
				String[] items = StringUtils.split(line, "~");
				if(items.length > 0)
				{
					sb.append(items[1]);
					sb.append("/");
					sb.append(items[0]);
				}
				else
				{
					log.error("[" + nameFile.getAbsolutePath() + "] content error!");
				}
			}
			else
			{
				log.error("[" + nameFile.getAbsolutePath() + "] is empty!");
			}
		}
		catch(IOException ex)
		{
			log.error("[" + nameFile.getAbsolutePath() + "] io error!", ex);
		}
		finally
		{
			IOUtils.closeQuietly(fis);
		}
		return sb.toString();
	}
	
	/**
	 * ����Ŀ����Ŀ¼
	 * ������������Ŀ��Ŀ¼�����ھʹ���һ��
	 * @param path
	 */
	private void createDestDir(String path)
	{
		String dir = path.substring(0, path.lastIndexOf("/"));
		File destDir = new File(dir);
		if(!destDir.exists())
		{
			destDir.mkdirs();
		}
	}
	
	/**
	 * �����ļ���Ŀ��Ŀ¼
	 * @param file
	 * @param destFilePath
	 */
	private boolean copyFile(File file, String destFilePath)
	{
		boolean ret = false;
		String filename = file.getName();
		File textFile = new File(this.fs.srcText + "/" + filename);
		if(!textFile.exists())
		{
			log.error("textFile[" + textFile.getAbsolutePath() + "] no exist");
			return false;
		}
		File destFile = new File(this.fs.dest + destFilePath);
		try {
			FileUtils.copyFile(textFile, destFile);
			ret = true;
			log.info("copy file success completed! [" + textFile.getAbsolutePath() + "], [" + destFile.getAbsolutePath() + "]");
		} catch (IOException e) {
			log.error("copy file fail, [" + textFile.getAbsolutePath() + "], [" + destFile.getAbsolutePath() + "]", e);
		}
		return ret;
	}
	
	/**
	 * ��������
	 * @param file
	 */
	private void clear(File file)
	{
		String filename = file.getName();
		boolean flag = false;
		
		//����name
		flag = file.delete();
		if(log.isDebugEnabled())
			log.debug("ɾ����־�ļ�:" + file.getAbsolutePath());
		//����map
		Object obj = this.fs.hashMap.remove(file.getAbsolutePath());
		if(log.isDebugEnabled())
		{
			log.debug(obj);
			log.debug(fs.hashMap.size());
		}
		//����text
		File textFile = new File(this.fs.srcText + "/" + filename);
		flag = textFile.delete();
		if(log.isDebugEnabled())
			log.debug("ɾ��ʵ���ļ�:" + textFile.getAbsolutePath());
	}
}
