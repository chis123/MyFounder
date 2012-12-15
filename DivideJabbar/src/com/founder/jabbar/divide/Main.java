/**
 * 用于转换天钩生成的XML，将其按站点和日期分拣到不同的目录，每做完一次休息1分钟
 *   新浪
 *   	20080605
 *   	20080606
 *   新华网
 *   	20080605
 *   	20080606
 */
package com.founder.jabbar.divide;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class Main {
	
	private String newsid;
	private String title;
	private String url;
	private String from;
	private String pDate;
	private String gDate;
	
	//当前目录下的文件数
	private int fileCount = 0;

	/**
	 * 程序入口
	 * @param args
	 * 第一个参数为源路径字符串
	 * 第二个参数为目标路径字符串
	 */
	public static void main(String[] args) {
		if(args.length != 2)
			System.out.print("参数不正确，正确格式：DivideJabbar src desc");
		
		Main main = new Main();
		while(true)
		{
			//main.start(args[0], args[1]);
			main.start("g:/2008-06-13", "g:/dest");
			try {
				Thread.sleep(1000 * 60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void start(String src, String dest)
	{
		long begin = System.currentTimeMillis();
		File srcPath = new File(src);
		if(!srcPath.isDirectory())
		{
			System.out.println("输入的源路径不正确！");
		}
		File destPath = new File(dest);
		if(!destPath.isDirectory())
		{
			System.out.println("输入的目标路径不正确");
		}
		
		int fileCount = run(srcPath, destPath);
		long end = System.currentTimeMillis();
		System.out.println("本次操作用时[" + (end-begin)+"]毫秒，操作文件数["+fileCount+"]");
	}
	
	private int run(File srcPath, File destPath)
	{
		File[] srcFiles = srcPath.listFiles();
		File srcFile = null;
		SAXReader xmlReader = new SAXReader();
		Document doc = null;
		String pubtime = null;
		String site = null;
		File lastDestDir = null;
		int fileCount = srcFiles.length;
		
		for(int i = 0; i < fileCount; i++)
		{
			srcFile = srcFiles[i];
			//System.out.println(srcFile.getAbsolutePath());
			//必须是XML文件才处理
			if(srcFile.isDirectory())
				continue;
			if(!srcFile.getAbsolutePath().endsWith(".xml"))
				continue;
			
			try {
				doc = xmlReader.read(srcFile);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
			
			this.newsid = doc.valueOf("//news/NewsID");
			this.title = doc.valueOf("//news/title");
			this.url = doc.valueOf("//news/UrlAddress");
			this.from = doc.valueOf("//news/from");
			this.pDate = doc.valueOf("//news/pubtime");
			this.gDate = doc.valueOf("//news/gettime");
			
			pubtime = this.gDate;
			pubtime = pubtime.substring(0, 10);
			pubtime = replace(pubtime, "-", "/");
			//System.out.println(pubtime);
			site = this.from;
			//System.out.println(site);
			lastDestDir = createDestPath(destPath, site, pubtime);
			
			System.out.println("拷贝文件["+srcFile.getAbsolutePath()+"] 到 ["+lastDestDir.getAbsolutePath()+"]");
			moveFileToDest(srcFile, lastDestDir);
			System.out.println("处理config.xml");
			makeConfigFile(lastDestDir);
			System.out.println("处理spider.xml");
			makeSpiderFile(lastDestDir);
		}
		return fileCount;
	}
	
	private void makeConfigFile(File dir)
	{
		File configFile = new File(dir.getAbsoluteFile() + "/config.xml");
		if(!configFile.isFile())
		{
			createConfigFile(configFile);
		}
		else
		{
			modifyConfigFile(configFile);
		}
	}
	
	private void modifyConfigFile(File file)
	{
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		SAXReader xmlReader = new SAXReader();
		Document doc = null;
		
		//读
		try
		{
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, "UTF-8");
			br = new BufferedReader(isr);
			doc = xmlReader.read(br);
			String count = doc.valueOf("//parameters/files");
			int fileCount = Integer.parseInt(count);
			this.fileCount = ++fileCount;
			Element files = (Element)doc.selectSingleNode("//parameters/files");
			files.setText(""+fileCount);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(isr);
			IOUtils.closeQuietly(fis);
		}
		
		//写
		saveXML(file, doc);
	}
	
	private void createConfigFile(File file)
	{
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement( "parameters" );
		root.addElement( "files" ).addText("1");
		this.fileCount = 1;
		
		saveXML(file, document);
	}
	
	private void makeSpiderFile(File dir)
	{
		File sFile = new File(dir.getAbsoluteFile() + "/spider.xml");
		if(!sFile.isFile())
		{
			createSpiderFile(sFile);
		}
		else
		{
			modifySpiderFile(sFile);
		}
	}
	
	private void modifySpiderFile(File file)
	{
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		SAXReader xmlReader = new SAXReader();
		Document doc = null;
		
		//读
		try
		{
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, "UTF-8");
			br = new BufferedReader(isr);
			doc = xmlReader.read(br);
			Element root = doc.getRootElement();
			Element linkElement = root.addElement("link");
			linkElement.addElement("no").addText(""+this.fileCount);
			linkElement.addElement("newsID").addText(this.newsid);
			linkElement.addElement("title").addText(this.title);
			linkElement.addElement("url_d").addText(this.url);
			linkElement.addElement("url_s").addText(this.url);
			linkElement.addElement("site").addText(this.from);
			linkElement.addElement("from").addText(this.from);
			linkElement.addElement("p_date").addText(this.pDate);
			linkElement.addElement("g_date").addText(this.gDate);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(isr);
			IOUtils.closeQuietly(fis);
		}
		
		saveXML(file, doc);
	}
	
	private void createSpiderFile(File file)
	{
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement( "links_root" );
		Element linkElement = root.addElement("link");
		linkElement.addElement("no").addText(""+this.fileCount);
		linkElement.addElement("newsID").addText(this.newsid);
		linkElement.addElement("title").addText(this.title);
		linkElement.addElement("url_d").addText(this.url);
		linkElement.addElement("url_s").addText(this.url);
		linkElement.addElement("site").addText(this.from);
		linkElement.addElement("from").addText(this.from);
		linkElement.addElement("p_date").addText(this.pDate);
		linkElement.addElement("g_date").addText(this.gDate);

		saveXML(file, document);
	}
	
	private void saveXML(File file, Document document)
	{
		OutputFormat of = new OutputFormat("", false, "UTF-8");
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		XMLWriter writer = null;
		
		try
		{
			fos = new FileOutputStream(file);
			osw = new OutputStreamWriter(fos, "UTF-8");
			bw = new BufferedWriter(osw);
			writer = new XMLWriter(bw, of);
			writer.write(document);
			writer.flush();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			IOUtils.closeQuietly(bw);
			IOUtils.closeQuietly(osw);
			IOUtils.closeQuietly(fos);
		}
	}
	
	private File createDestPath(File destPath, String site, String pubtime)
	{
		String lastDest = destPath.getAbsolutePath() + "/spider/" + site + "/" + pubtime;
		File lastDestPath = new File(lastDest);
		if(!lastDestPath.isDirectory())
			lastDestPath.mkdirs();
		return lastDestPath;
	}
	
	private void moveFileToDest(File srcFile, File destPath)
	{
		try {
			FileUtils.copyFileToDirectory(srcFile, destPath, true);
		} catch (IOException e) {
			System.out.println("移动文件过程中出现异常");
			e.printStackTrace();
		}
		srcFile.delete();
	}
	
	private String replace(
			String line,
			String oldString,
			String newString)
		{
			if (line == null)
			{
				return null;
			}
			if (oldString == null || newString == null)
			{
				return line;
			}

			if (oldString.equals(""))
			{
				return line;
			}

			int i = 0;
			if ((i = line.indexOf(oldString, i)) >= 0)
			{
				char[] line2 = line.toCharArray();
				char[] newString2 = newString.toCharArray();
				int oLength = oldString.length();
				StringBuffer buf = new StringBuffer(line2.length);
				buf.append(line2, 0, i).append(newString2);
				i += oLength;
				int j = i;
				while ((i = line.indexOf(oldString, i)) > 0)
				{
					buf.append(line2, j, i - j).append(newString2);
					i += oLength;
					j = i;
				}
				buf.append(line2, j, line2.length - j);
				return buf.toString();
			}
			return line;
		}


}
