package com.founder.e5.commons;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.net.ftp.FTPClient;

import com.founder.e5.context.E5DataSource;
import com.founder.e5.db.DBType;

/**
 * ��⹤���࣬���ڼ��FTP�洢�豸������Դ�Ƿ����
 * 
 * @author wanghc
 * @created 2009-5-6 ����03:30:49
 */
public class VerifyUtil
{

	/**
	 * ����FTP�����Ƿ����
	 * 
	 * @param url - ftp url
	 * @param userName - ftp ��½�û���
	 * @param pwd - ftp��½����
	 * @return �ɹ���ʧ��
	 */
	public static boolean testFTPDevice(String url, String userName, String pwd)
	{
		FTPClient client = new FTPClient();
		int replyCode = 0;
		try
		{
			int port = getPort(url);
			client.setDefaultPort(port);
			String hostName = getHostName(url);
			client.connect(hostName);
			client.login(userName, pwd);
			replyCode = client.getReplyCode();
			if (replyCode == 230)
				return true;
			else
				return false;
		} 
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
		finally
		{
			try	{
				if (client != null)
					if (replyCode == 230)
						client.disconnect();
			} catch (Exception e){}
		}
	}
	


	/**
	 * ��������Դ�����Ƿ����
	 * 
	 * @param e5ds - ����Դ����
	 * @return �Ƿ����
	 */
	public static boolean testDataSource(E5DataSource e5ds)
	{
		javax.naming.Context ctx;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		try
		{
			ctx = new InitialContext();
			DataSource ds = (javax.sql.DataSource) ctx.lookup(e5ds.getDataSource());
			conn = ds.getConnection();
			stmt = conn.createStatement();
			rset = stmt.executeQuery(getTestSQL(e5ds.getDbType()));
			if (rset.next())
				return true;
			else
				return false;

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		} 
		finally
		{
			ResourceMgr.closeQuietly(rset);
			ResourceMgr.closeQuietly(stmt);
			ResourceMgr.closeQuietly(conn);
		}
	}
	
	/**
	 * ��ȡָ�����ݵĲ���SQL���
	 * 
	 * @param dbType
	 * @see DBType
	 * @return test sql
	 */
	public static String getTestSQL(String dbType)
	{
		if(DBType.ORACLE.equals(dbType))
			return "SELECT 11 FROM DUAL";
		else
			return "SELECT 11";
	}
	
	private static int getPort(String url)
	{
		int port = 21;
		String urlBody = removeFTPUrlHead(url);
		int tmp = urlBody.indexOf(":");
		if (tmp > 0)
		{
			int pos = urlBody.indexOf("/");
			String portStr = urlBody.substring(tmp + 1, pos);
			if (!portStr.equals(""))
				port = Integer.parseInt(portStr);
		}
		return port;
	}
	
	private static String getHostName(String url)
	{
		String urlBody = removeFTPUrlHead(url);
		int tmp = urlBody.indexOf(":");
		if (tmp <= 0)
			tmp = urlBody.indexOf("/");
		String host = urlBody.substring(0, tmp);

		return host;
	}

	private static String removeFTPUrlHead(String url)
	{
		return url.substring(6, url.length());

	}
}
