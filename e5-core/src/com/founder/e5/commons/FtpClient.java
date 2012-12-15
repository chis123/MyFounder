package com.founder.e5.commons;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.BindException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * FTP�ͻ��˶��󣬿��������ϴ��������ļ�<br><br>
 * �����Ƕ�apache org.apache.commons.net.ftp.FTPClient�ķ�װ���ṩ������ʹ�õĽӿ�
 * <br><br>
 * e5core��ֱ�ӽ���FTP�������������࣬һ����FtpReaderImpl��һ����FtpClient��
 * ���в��죬���û�кϲ�Ϊһ����
 * @author liyanhui 2004-6-17 17:25:46
 */
public class FtpClient {
	//ȱʡ���룬ʹ��GBK���ɴ�������Ŀ¼��Ӣ��Ŀ¼
	private final static String DEFAULT_ENCODING = "GBK";
	
	/** the underlying org.apache.commons.net.ftp.FTPClient */
	private FTPClient client;

	/**
	 * ����һ��FTPClient���󣬲����ݸ�����Ϣ����ftp����
	 * 
	 * @param host ��������IP
	 * @param port �˿�
	 * @param username �û���
	 * @param password ����
	 * @throws SocketException
	 * @throws IOException
	 * @throws FtpException
	 */
	public FtpClient( String host, int port, String user, String pass )
		throws SocketException, IOException, FtpException {

		client = new FTPClient();
		client.setDefaultPort( port );
		client.connect( host );

		if ( !client.login( user, pass ) )
			throw new FtpException( "login failed: user=" + user + " password="
					+ pass );

		if ( !client.setFileType( FTP.BINARY_FILE_TYPE ) )
			throw new FtpException( "set binary file type failed" );

		if ( !client.setFileTransferMode( FTP.STREAM_TRANSFER_MODE ) )
			throw new FtpException( "set stream transfer mode failed" );

		if ( !client.setFileStructure( FTP.FILE_STRUCTURE ) )
			throw new FtpException( "set file structure failed" );
		try {
	    	client.listFiles();
	    }catch(BindException ex)
	    {
	    	System.out.println("FtpClient: turn to passive mode!");
	    	client.enterLocalPassiveMode();
	    }
	    
	    client.setControlEncoding(DEFAULT_ENCODING);
	}

	/**
	 * ����һ��FTPClient���󣬲����ݸ�����Ϣ����ftp���ӣ�ʹ��21�˿ڣ�
	 * 
	 * @param host ��������IP
	 * @param user �û���
	 * @param pass ����
	 * @throws SocketException
	 * @throws IOException
	 * @throws FtpException
	 */
	public FtpClient( String host, String user, String pass )
		throws SocketException, IOException, FtpException {
		this( host, 21, user, pass );
	}

	/**
	 * ���ñ��롣Ĭ�ϱ�����GBK������Ҫ�仯���ڴ˴��޸�
	 * @param encoding
	 */
	public void setControlEncoding(String encoding) {
		client.setControlEncoding(encoding);
	}
	/**
	 * �Ͽ�����
	 * 
	 * @throws java.io.IOException
	 */
	public void disconnect() throws IOException {
		client.disconnect();
	}

	/**
	 * �ر�FTP���ӣ�ͬdisconnect��
	 * 
	 * @throws IOException
	 * @created 2005-9-15 17:12:13
	 */
	public void close() throws IOException {
		client.disconnect();
	}

	// --------------------------------------------------------------------------

	/**
	 * �ı�Ftp�Ự�ĵ�ǰ����Ŀ¼<br>
	 * <br>
	 * ע�⣺��·����"/"��ͷ������Ϊ����·����������Ϊ���·����<br>
	 * ���⣬·�������"/"�ָ�<br>
	 * <br>
	 * �÷����ڲ�����org.apache.commons.net.ftp.FTPClient��������false����ֵת��Ϊ�쳣�׳�
	 * 
	 * @param destPath Ŀ��·����\��/���ɣ�
	 * @param createWhenNotExist ��Ϊ�棬��·��������ʱ�ʹ���Ŀ¼
	 * @throws IOException
	 */
	public void cd( String destPath, boolean createWhenNotExist )
			throws IOException {
		if ( destPath == null || "".equals( destPath ) )
			throw new IllegalArgumentException();

		if ( !createWhenNotExist ) {
			if ( !client.changeWorkingDirectory( destPath ) )
				throw new IOException( "�л���Ŀ¼ʧ�ܣ�" + destPath );
		}

		String path = destPath.replace( '\\', '/' );

		// ����·�������е���Ŀ¼
		if ( path.startsWith( "/" ) ) {
			if ( !client.changeWorkingDirectory( "/" ) )
				throw new IOException( "�л�����·��ʧ��" );

			path = path.substring( 1 );
		}

		String[] paths = StringUtils.split( path, "/" );
		for ( int i = 0; i < paths.length; i++ ) {
			String dirName = paths[i];
			if ( !client.changeWorkingDirectory( dirName ) ) {
				client.mkd( dirName );

				if ( !client.changeWorkingDirectory( dirName ) ) {
					String wd = client.printWorkingDirectory();
					String msg = "��Ŀ¼[" + wd + "]�´���Ŀ¼[" + dirName + "]ʧ�ܣ�";
					throw new IOException( msg );
				}
			}

		}

	}

	/**
	 * ���汾���ļ���Զ��·��. ��Զ��·�������ڣ��򴴽���Ӧ·��
	 * 
	 * @param localFile �����ļ�·����
	 * @param remotePath Զ��ftp·��
	 * @return
	 * @throws IOException
	 */
	public boolean storeFile( String localFile, String remotePath )
			throws IOException {
		cd( remotePath, true );

		String remoteFile = remotePath + "/" + new File( localFile ).getName();
		BufferedInputStream in = new BufferedInputStream( new FileInputStream(
				localFile ) );
		try {
			return client.storeFile( remoteFile, in );
		} finally {
			ResourceMgr.closeQuietly( in );
		}
	}

	/**
	 * �������ݵ�Զ���ļ�
	 * 
	 * @param data �ļ�����
	 * @param remoteFile ��ʽ��/dir1/dir2/dir3/file.txt
	 * @return �Ƿ�ɹ�
	 * @throws IOException
	 * @created 2005-9-2 14:54:01
	 */
	public void storeFile( byte[] data, String remoteFile ) throws IOException {
		storeFile( new ByteArrayInputStream( data ), remoteFile );
	}

	/**
	 * �������ݵ�Զ���ļ�
	 * 
	 * @param stream �ļ�������
	 * @param remoteFile ��ʽ��/dir1/dir2/dir3/file.txt
	 * @return �Ƿ�ɹ�
	 * @throws IOException
	 * @created 2005-9-2 14:54:01
	 */
	public void storeFile( InputStream stream, String remoteFile )
			throws IOException {
		File file = new File( remoteFile );
		String remotePath = file.getParent();
		String filename = file.getName();

		cd( remotePath, true );

		try {
			boolean result = client.storeFile( filename, stream );
			if ( !result ) {
				String msg = "�洢�ļ�ʧ�ܣ�" + remoteFile;
				throw new IOException( msg );
			}
		} finally {
			ResourceMgr.closeQuietly( stream );
		}
	}

	// --------------------------------------------------------------------------

	/**
	 * �ϴ������ļ���Զ���ļ�
	 * 
	 * @param localFile �����ļ�·����
	 * @param remoteFile Զ���ļ���ftp·���� ��/upload/software/winrar.exe
	 * @throws IOException
	 */
	public void upload( String localFile, String remoteFile )
			throws IOException {
		upload( new File( localFile ), remoteFile );
	}

	/**
	 * �ϴ������ļ���Զ���ļ�
	 * 
	 * @param localFile �����ļ�·����
	 * @param remoteFile Զ���ļ���ftp·���� ��/upload/software/winrar.exe
	 * @throws IOException
	 */
	public void upload( File localFile, String remoteFile ) throws IOException {
		File file = new File( remoteFile );
		uploadToDir( localFile, file.getParent(), file.getName() );
	}

	/**
	 * �ϴ������ļ���ָ����ftpĿ¼��
	 * 
	 * @param localFile
	 * @param ftpPath ftp·�� ��/upload/software
	 * @return �Ƿ��ϴ��ɹ�
	 * @throws IOException
	 */
	public void uploadToDir( String localFile, String ftpPath )
			throws IOException {
		uploadToDir( new File( localFile ), ftpPath, null );
	}

	/**
	 * �ϴ������ļ���ָ����ftpĿ¼�²�������
	 * 
	 * @param localFile �����ļ�·����
	 * @param ftpPath ָ����ftpĿ¼ ��/upload/software
	 * @param newName ���ļ���
	 * @throws IOException
	 */
	public void uploadToDir( String localFile, String ftpPath, String newName )
			throws IOException {
		uploadToDir( new File( localFile ), ftpPath, newName );
	}

	/**
	 * �ϴ������ļ���ָ����ftpĿ¼��
	 * 
	 * @param localFile �����ļ�·����
	 * @param ftpPath ָ����ftpĿ¼ ��/upload/software
	 * @throws IOException
	 */
	public void uploadToDir( File localFile, String ftpPath )
			throws IOException {
		uploadToDir( localFile, ftpPath, null );
	}

	/**
	 * �ϴ������ļ���ָ����ftpĿ¼�²�������
	 * 
	 * @param localFile �����ļ�·����
	 * @param ftpPath ָ����ftpĿ¼ ��/upload/software
	 * @param newName ���ļ���
	 * @throws IOException
	 */
	public void uploadToDir( File localFile, String ftpPath, String newName )
			throws IOException {
		if ( ftpPath == null )
			throw new NullPointerException( "ftpPath" );

		cd( ftpPath, true );

		String remoteName = ( newName == null ) ? localFile.getName() : newName;
		BufferedInputStream in = new BufferedInputStream( new FileInputStream(
				localFile ) );
		try {
			boolean result = client.storeFile( remoteName, in );
			if ( !result ) {
				String msg = "�ϴ��ļ�ʧ�ܣ�localFile=" + localFile + " ftpPath="
						+ ftpPath + " newName=" + newName;
				throw new IOException( msg );
			}
		} finally {
			ResourceMgr.closeQuietly( in );
		}
	}

	// -------------------------------------------------------------------------

	/**
	 * @param ftpFile ftp�ϵ��ļ��ľ���·����
	 * @param localDir ����Ŀ¼��·����
	 * @return
	 * @throws IOException
	 */
	public File downloadToDir( String ftpFile, File localDir )
			throws IOException {
		if ( !localDir.exists() )
			localDir.mkdirs();

		File localFile = new File( localDir, new File( ftpFile ).getName() );

		boolean result = false;
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream( localFile ) );
		try {
			result = client.retrieveFile( ftpFile, bos );
		} finally {
			ResourceMgr.closeQuietly( bos );
		}

		if ( !result )
			throw new IOException( "�����ļ�����ftpFile=" + ftpFile + " localFile="
					+ localFile );
		return localFile;
	}

	// -------------------------------------------------------------------------

	/**
	 * ��ȡ��ǰ����Ŀ¼
	 * 
	 * @return
	 * @throws java.io.IOException
	 */
	public String printWorkingDirectory() throws IOException {
		return client.printWorkingDirectory();
	}

	/**
	 * �ı䵱ǰ����Ŀ¼
	 * 
	 * @param pathname
	 * @return
	 * @throws java.io.IOException
	 */
	public boolean changeWorkingDirectory( String pathname ) throws IOException {
		return client.changeWorkingDirectory( pathname );
	}

	/**
	 * �г���ǰĿ¼�µ��ļ�
	 * 
	 * @return FTPFile[]
	 * @throws java.io.IOException
	 */
	public FTPFile[] listFiles() throws IOException {
		return client.listFiles();
	}
	
	/**
	 * ɾ��ָ����ftp·���ϵ��ļ�
	 * 
	 * @param ftpPath
	 * @return
	 * @throws IOException
	 */
	public boolean delete(String ftpPath) throws IOException
	{
		return client.deleteFile(ftpPath);
	}

}
