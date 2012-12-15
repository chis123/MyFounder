package com.founder.e5.sys;

import java.io.IOException;
import java.io.InputStream;
import java.net.BindException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import com.founder.e5.context.E5Exception;

/**
 * e5core��ֱ�ӽ���FTP�������������࣬һ����FtpReaderImpl��һ����FtpClient��
 * ���в��죬���û�кϲ�Ϊһ����
 * @version 1.0
 */
public class FtpReaderImpl implements FtpReader {
	//ȱʡ���룬ʹ��GBK���ɴ�������Ŀ¼��Ӣ��Ŀ¼
	private final static String DEFAULT_ENCODING = "GBK";

	private FTPClient client;
	private String host;
	private int port;
	private String user;
	private String pass; 
	
	private FtpReaderImpl() {
		super();
		client = new FTPClient();
		client.setControlEncoding(DEFAULT_ENCODING);
	}
	public FtpReaderImpl(String host, int port, String user, String pass)
	{
		this.host=host;
		this.port=port;
		this.user=user;
		this.pass=pass;
		client = new FTPClient();
		client.setControlEncoding(DEFAULT_ENCODING);
	}
	public void setControlEncoding(String encoding) {
		client.setControlEncoding(encoding);
	}
	
	public boolean connect()throws E5Exception
	{
		//����һ������
		if (!bareConnect()) return false;
		//ͨ��listFiles����ж��Ƿ�PassiveMode
		try {
	    	client.listFiles();
	    } catch(BindException ex) {
	    	//����Bind�쳣����ʹ��Passiveģʽ
	    	System.out.println("FtpReader: turn to passive mode!");
	    	client.enterLocalPassiveMode();
	    } catch(SocketException ex) {
	    	//��IIS FTP������22�˿�ʱ��listFiles���쳣����ʱ������һ������
			try {client.disconnect();} catch (Exception e){}
			bareConnect();
		} catch(Exception ex) {
			throw new E5Exception("connect failed",ex);
		}
		return true;
	}
	private boolean bareConnect()throws E5Exception
	{
		try
		{
			client.setDefaultPort( port );
			client.connect( host );

			if ( !client.login( user, pass ) ) {
				return false;
			}
			if ( !client.setFileType( FTP.BINARY_FILE_TYPE ) ) {
				return false;
			}
			if ( !client.setFileTransferMode( FTP.STREAM_TRANSFER_MODE ) ) {
				return false;
			}
			if ( !client.setFileStructure( FTP.FILE_STRUCTURE ) ) {
				return false;
			}
		}
		catch(Exception ex)
		{
			throw new E5Exception("connect failed",ex);
		}
		return true;
	}
	public InputStream read(String path, String fileName)throws E5Exception
	{
		try
		{
			boolean bCd =client.changeWorkingDirectory(path);
			if(bCd)
			{
				InputStream in = client.retrieveFileStream(fileName);
				if( in == null )
					client.disconnect();
				else
					return in;
			}
			else
			{
				client.disconnect();
			}
		}
		catch(Exception ex)
		{
			try
			{
				client.disconnect();
			}
			catch ( IOException e )
			{
			}
			throw new E5Exception("read failed",ex);
		}
		return null;
	}
	public void disconnect() throws E5Exception
	{
		try
		{
			client.disconnect();
		}
		catch(Exception ex)
		{
			throw new E5Exception("disconnect failed",ex);
		}
	}
	
	public boolean deleteFile(String path, String fileName)throws E5Exception
	{
		boolean bDelete=false;
		try
		{
			bDelete =client.changeWorkingDirectory(path);
			if(bDelete)
			{
				bDelete = client.deleteFile(fileName);
			}
			else
			{
				throw new E5Exception("deleteFile failed,file not exist,path:"+path+" filename:"+fileName);
				
			}
		}
		catch(Exception ex)
		{
			throw new E5Exception("deleteFile failed",ex);
		}
		return bDelete;
	}
}
