package com.founder.e5.commons;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/***
 * httpЭ��ͻ��˵��ù���
 * ֧��ͨ��ͨ��GET�����ķ�ʽ����URL,������response������
 * �����Զ�ʶ�����ݵı���
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
	
	//Ĭ���ַ��ı���
	private static final String DEFAULT_ENCODE = "UTF-8";
	
	/**
	 * ͨ�� HTTP GET ������ʽ����URL,������ҳ����
	 * 
	 * @param url - ������HTTP URL ,��:http://192.168.0.1:8080/e5new/SysLogin.jsp
	 * @return String url���ص���ҳ����
	 * @throws E5Exception
	 */
	static public String doGet(String url)
		throws Exception
	{
		String result = null;	
		
		//���Э��
		if(!url.toLowerCase().startsWith("http://"))
			throw new IllegalArgumentException("URL��ַ����,������http://��ʼ");
		
		URL urltool = new URL(url);
		HttpURLConnection http = (HttpURLConnection)urltool.openConnection();
		
		InputStream inputStream = http.getInputStream();
		
		//��ȡcontent�ı���
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
	 * ȡ��HTTP��contentType�ı���,Ĭ�ϱ���ΪUTF-8
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
