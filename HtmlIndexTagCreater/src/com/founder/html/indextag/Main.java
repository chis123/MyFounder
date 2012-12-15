/**
 * 为网站的HTML页面文件添加海量索引的标签
 * 程序参数中需要指出要处理的网站WEB的一个源目录，程序会递归处理子孙目录
 */
package com.founder.html.indextag;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.IOUtils;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(args.length != 1)
		{
			System.out.println("需要提供源目录作为参数");
			System.exit(1);
		}
		
		
		Main main = new Main();
		main.start(args[0]);
		//main.start("G:/html/20031231");
		//main.start("G:/html/20040102");
		//main.start("G:/html/20080605");
	}
	
	private void start(String src)
	{
		File srcDir = new File(src);
		if(!srcDir.isDirectory())
			System.out.println("参数不正确，指定的不是正常目录");
		
		run(srcDir);
	}
	
	private void run(File dir)
	{
		File[] subFiles = dir.listFiles();
		int fileCount = subFiles.length;
		File subFile = null;
		
		for(int i = 0; i < fileCount; i++)
		{
			subFile = subFiles[i];
			if(subFile.isDirectory())
				run(subFile);
			else if(subFile.isFile())
			{
				String fileName = subFile.getName();
				if(fileName.startsWith("news_")
						&& fileName.endsWith(".html"))
					opFile(subFile);
				else
					continue;
			}
		}
	}
	
	private void opFile(File file)
	{
		FileInputStream fis = null;
		List lines = null;
		String content = null;
		try
		{
			fis = new FileInputStream(file);
			lines = IOUtils.readLines(fis, "GBK");
			content = operate(lines, convertPubtime(file.lastModified()),
					getArticleId(file.getName()));
			//System.out.println(content);
			rewrite(file, content);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			IOUtils.closeQuietly(fis);
		}
	}
	
	private String getArticleId(String fileName)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(fileName.substring(5, 7));
		sb.append(fileName.substring(8, fileName.indexOf(".")));
		return sb.toString();
	}
	
	private String convertPubtime(long time)
	{
		Date date = new Date(time);
		SimpleDateFormat formatter = null;
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0", Locale.getDefault());
		return formatter.format(date);
	}
	
	private String getEnpProperty(String articleId, String pubtime, String title)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<!--enpproperty ");
		sb.append("<articleid>").append(articleId).append("</articleid>");
		sb.append("<date>").append(pubtime).append("</date>");
		sb.append("<author></author>");
		sb.append("<title>").append(title).append("</title>");
		sb.append("<keyword></keyword>");
		sb.append("<subtitle></subtitle>");
		sb.append("<introtitle></introtitle>");
		sb.append("<siteid>2</siteid>");
		sb.append("<nodeid>1140</nodeid>");
		sb.append("<nodename>云南新闻</nodename>");
		sb.append("<nodesearchname></nodesearchname>");
		sb.append("/enpproperty-->");
		return sb.toString();
	}
	
	private String operate(List lines, String pubtime, String articleId)
	{
		StringBuffer ret = new StringBuffer();
		int flag = 0;
		int contentFlag = 0;
		int i = 0;
		String title = null;
		String enpproperty = null;
		
		for (Iterator iterator = lines.iterator(); iterator.hasNext();) {
			String line = (String) iterator.next();
			i++;
			
			if(line.startsWith("<title>")
					&& line.indexOf("管理平台") > 0)
			{
				flag = 1;
			}
			else if(line.startsWith("<title>"))
			{
				flag = 2;
				title = ltrim(line, "<title>");
				title = rtrim(title, "</title>");
			}
			
			int p1 = line.indexOf("<font color=#000000  size=+1><br>");
			if(p1 > 0 && flag == 1)
			{
				int p2 = line.indexOf("</font>");
				title = line.substring(p1 + 33, p2);
			}
			if(title != null)
			{
				if(flag == 1)
				{
					title = clearTitleLink(title);
				}
				enpproperty = getEnpProperty(articleId, pubtime, title);
				title = null;
			}
			
			//处理html中的编码
			if(line.indexOf("gb2312") > 0)
			{
				line = replace(line, "gb2312", "UTF-8");
			}
			
			if(line.startsWith("<table width=100%  border=0 cellpadding=8 cellspacing=0 class=md1>")
					&& flag == 1)
			{
				ret.append(enpproperty);
				ret.append(" <!--enpcontent-->");
				ret.append(rtrim(line, "</td></tr></table>"));
				ret.append("<!--/enpcontent--></td></tr></table>\n");
			}
			else if(line.startsWith("</script>"))
			{
				contentFlag = i;
				ret.append(line).append("\n");
			}
			else if(line.startsWith("<p>")
					&& flag == 2 && contentFlag > 0
					&& contentFlag == i - 1)
			{
				ret.append(enpproperty);
				ret.append(" <!--enpcontent-->");
				ret.append(line);
				ret.append("<!--/enpcontent-->\n");
				contentFlag = 0;
			}
			else
			{
				ret.append(line).append("\n");
			}
		}
		//System.out.println(enpproperty);
		return ret.toString();
	}
	
	private void rewrite(File file, String data)
	{
		System.out.println("开始给["+file.getAbsolutePath()+"]加注海量检索标签...");
		FileOutputStream fos = null;
		try
		{
			fos = new FileOutputStream(file);
			IOUtils.write(data, fos, "UTF-8");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			IOUtils.closeQuietly(fos);
		}
	}
	
	private String clearTitleLink(String title)
	{
		int p1, p2;
		p1 = title.indexOf("<");
		if(p1 < 0)
			return title;
		else
		{
			p2 = title.indexOf(">");
			title = title.substring(p2 + 1, title.length());
			title = rtrim(title, "</A><BR>"); 
		}
		return title;
	}
	
	private String rtrim(String string, String flag)
	{
		int lenSrc = 0;
		int lenFlag = 0;
		String tmpStr = null;
		boolean hasNext = true;
		if (string == null)
		{
			return null;
		}
		if (flag == null || flag.equals(""))
		{
			return string;
		}
		if (string.equals(""))
		{
			return "";
		}
		lenSrc = string.length();
		lenFlag = flag.length();
		if (lenFlag > lenSrc)
		{
			return "";
		}
		while (lenSrc >= lenFlag && hasNext)
		{
			tmpStr = string.substring(lenSrc - lenFlag);
			if (tmpStr.equals(flag))
			{
				hasNext = true;
				string = string.substring(0, lenSrc - lenFlag);
				lenSrc = string.length();
			}
			else
			{
				hasNext = false;
			}
		}
		return string;
	}
	
	private String ltrim(String src, String flag)
	{
		int lenSrc = 0;
		int lenFlag = 0;
		String tmpStr = null;
		boolean hasNext = true;
		if (src == null)
		{
			return null;
		}
		if (flag == null || flag.equals(""))
		{
			return src;
		}
		if (src.equals(""))
		{
			return "";
		}
		lenSrc = src.length();
		lenFlag = flag.length();
		if (lenFlag > lenSrc)
		{
			return "";
		}
		while (lenSrc >= lenFlag && hasNext)
		{
			tmpStr = src.substring(0, lenFlag);
			if (tmpStr.equals(flag))
			{
				hasNext = true;
				src = src.substring(lenFlag);
				lenSrc = src.length();
			}
			else
			{
				hasNext = false;
			}
		}
		return src;
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
