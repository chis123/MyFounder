package com.founder.e5.commons;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/***
 * http协议客户端调用工具
 * 支持通过通过GET方法的方式访问URL,并返回response的内容
 * 可以自动识别内容的编码
 * 
 * @author wanghc
 * @created 2006-6-22
 */
public class HttpClient
{
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args)
//		throws Exception
//	{
//		String url = "http://localhost:8080/e5new/test.jsp";
//		//String url = "http://news.163.com";
//		String r = HttpClient.doGet(url);		
//		
//		System.out.print(r);
//			
//	}	
	
	//默认字符的编码
	private static final String DEFAULT_ENCODE = "UTF-8";
	
	/**
	 * 通过 HTTP GET 方法方式访问URL,返回网页内容
	 * 
	 * @param url - 完整的HTTP URL ,如:http://192.168.0.1:8080/e5new/SysLogin.jsp
	 * @return String url返回的网页内容
	 * @throws E5Exception
	 */
	static public String doGet(String url)
		throws Exception
	{
		String result = null;	
		
		//检查协议
		if(!url.toLowerCase().startsWith("http://"))
			throw new IllegalArgumentException("URL地址错误,必须以http://开始");
		
		URL urltool = new URL(url);
		HttpURLConnection http = (HttpURLConnection)urltool.openConnection();
		
		InputStream inputStream = http.getInputStream();
		
		//获取content的编码
		String encode = getEncode(http);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,encode));  
		
		StringBuffer sb = new StringBuffer();
		String s = null;
		while (((s=reader.readLine())!=null))
		{
			sb.append(s+"\r\n");
		}			
		inputStream.close();			
		result = sb.toString();
		
		return result;
	}
	
	/**
	 * 取得HTTP的contentType的编码,默认编码为UTF-8
	 * @param http
	 * @return
	 */
	private static String getEncode(HttpURLConnection http)
	{
		String encode = DEFAULT_ENCODE;
		//get content-type from http header,the sender will give charset
		String contentType = http.getContentType();
		if(contentType!=null)
		{
			int index = contentType.toUpperCase().indexOf("CHARSET=");
			if(index!=-1)
			{
				encode = contentType.substring(index+8,contentType.length());
			}
		}		
		return encode;
	}
}
