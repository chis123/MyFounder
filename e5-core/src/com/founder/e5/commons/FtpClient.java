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
 * FTP客户端对象，可以用来上传、下载文件<br><br>
 * 该类是对apache org.apache.commons.net.ftp.FTPClient的封装，提供更易于使用的接口
 * <br><br>
 * e5core里直接进行FTP操作的有两个类，一个是FtpReaderImpl，一个是FtpClient。
 * 稍有差异，因此没有合并为一个类
 * @author liyanhui 2004-6-17 17:25:46
 */
public class FtpClient {
	//缺省编码，使用GBK，可处理中文目录和英文目录
	private final static String DEFAULT_ENCODING = "GBK";
	
	/** the underlying org.apache.commons.net.ftp.FTPClient */
	private FTPClient client;

	/**
	 * 创建一个FTPClient对象，并根据给定信息建立ftp连接
	 * 
	 * @param host 主机名或IP
	 * @param port 端口
	 * @param username 用户名
	 * @param password 密码
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
	 * 创建一个FTPClient对象，并根据给定信息建立ftp连接（使用21端口）
	 * 
	 * @param host 主机名或IP
	 * @param user 用户名
	 * @param pass 密码
	 * @throws SocketException
	 * @throws IOException
	 * @throws FtpException
	 */
	public FtpClient( String host, String user, String pass )
		throws SocketException, IOException, FtpException {
		this( host, 21, user, pass );
	}

	/**
	 * 设置编码。默认编码是GBK，如需要变化，在此处修改
	 * @param encoding
	 */
	public void setControlEncoding(String encoding) {
		client.setControlEncoding(encoding);
	}
	/**
	 * 断开连接
	 * 
	 * @throws java.io.IOException
	 */
	public void disconnect() throws IOException {
		client.disconnect();
	}

	/**
	 * 关闭FTP连接（同disconnect）
	 * 
	 * @throws IOException
	 * @created 2005-9-15 17:12:13
	 */
	public void close() throws IOException {
		client.disconnect();
	}

	// --------------------------------------------------------------------------

	/**
	 * 改变Ftp会话的当前工作目录<br>
	 * <br>
	 * 注意：若路径以"/"开头，则认为绝对路径；否则认为相对路径。<br>
	 * 另外，路径最好以"/"分隔<br>
	 * <br>
	 * 该方法内部采用org.apache.commons.net.ftp.FTPClient，并把其false返回值转换为异常抛出
	 * 
	 * @param destPath 目的路径（\和/均可）
	 * @param createWhenNotExist 如为真，则当路径不存在时就创建目录
	 * @throws IOException
	 */
	public void cd( String destPath, boolean createWhenNotExist )
			throws IOException {
		if ( destPath == null || "".equals( destPath ) )
			throw new IllegalArgumentException();

		if ( !createWhenNotExist ) {
			if ( !client.changeWorkingDirectory( destPath ) )
				throw new IOException( "切换到目录失败：" + destPath );
		}

		String path = destPath.replace( '\\', '/' );

		// 绝对路径，先切到根目录
		if ( path.startsWith( "/" ) ) {
			if ( !client.changeWorkingDirectory( "/" ) )
				throw new IOException( "切换到根路径失败" );

			path = path.substring( 1 );
		}

		String[] paths = StringUtils.split( path, "/" );
		for ( int i = 0; i < paths.length; i++ ) {
			String dirName = paths[i];
			if ( !client.changeWorkingDirectory( dirName ) ) {
				client.mkd( dirName );

				if ( !client.changeWorkingDirectory( dirName ) ) {
					String wd = client.printWorkingDirectory();
					String msg = "在目录[" + wd + "]下创建目录[" + dirName + "]失败！";
					throw new IOException( msg );
				}
			}

		}

	}

	/**
	 * 保存本地文件到远端路径. 如远端路径不存在，则创建相应路径
	 * 
	 * @param localFile 本地文件路径名
	 * @param remotePath 远程ftp路径
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
	 * 保存数据到远程文件
	 * 
	 * @param data 文件数据
	 * @param remoteFile 格式：/dir1/dir2/dir3/file.txt
	 * @return 是否成功
	 * @throws IOException
	 * @created 2005-9-2 14:54:01
	 */
	public void storeFile( byte[] data, String remoteFile ) throws IOException {
		storeFile( new ByteArrayInputStream( data ), remoteFile );
	}

	/**
	 * 保存数据到远程文件
	 * 
	 * @param stream 文件输入流
	 * @param remoteFile 格式：/dir1/dir2/dir3/file.txt
	 * @return 是否成功
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
				String msg = "存储文件失败：" + remoteFile;
				throw new IOException( msg );
			}
		} finally {
			ResourceMgr.closeQuietly( stream );
		}
	}

	// --------------------------------------------------------------------------

	/**
	 * 上传本地文件到远程文件
	 * 
	 * @param localFile 本地文件路径名
	 * @param remoteFile 远程文件的ftp路径名 如/upload/software/winrar.exe
	 * @throws IOException
	 */
	public void upload( String localFile, String remoteFile )
			throws IOException {
		upload( new File( localFile ), remoteFile );
	}

	/**
	 * 上传本地文件到远程文件
	 * 
	 * @param localFile 本地文件路径名
	 * @param remoteFile 远程文件的ftp路径名 如/upload/software/winrar.exe
	 * @throws IOException
	 */
	public void upload( File localFile, String remoteFile ) throws IOException {
		File file = new File( remoteFile );
		uploadToDir( localFile, file.getParent(), file.getName() );
	}

	/**
	 * 上传本地文件到指定的ftp目录下
	 * 
	 * @param localFile
	 * @param ftpPath ftp路径 如/upload/software
	 * @return 是否上传成功
	 * @throws IOException
	 */
	public void uploadToDir( String localFile, String ftpPath )
			throws IOException {
		uploadToDir( new File( localFile ), ftpPath, null );
	}

	/**
	 * 上传本地文件到指定的ftp目录下并重命名
	 * 
	 * @param localFile 本地文件路径名
	 * @param ftpPath 指定的ftp目录 如/upload/software
	 * @param newName 新文件名
	 * @throws IOException
	 */
	public void uploadToDir( String localFile, String ftpPath, String newName )
			throws IOException {
		uploadToDir( new File( localFile ), ftpPath, newName );
	}

	/**
	 * 上传本地文件到指定的ftp目录下
	 * 
	 * @param localFile 本地文件路径名
	 * @param ftpPath 指定的ftp目录 如/upload/software
	 * @throws IOException
	 */
	public void uploadToDir( File localFile, String ftpPath )
			throws IOException {
		uploadToDir( localFile, ftpPath, null );
	}

	/**
	 * 上传本地文件到指定的ftp目录下并重命名
	 * 
	 * @param localFile 本地文件路径名
	 * @param ftpPath 指定的ftp目录 如/upload/software
	 * @param newName 新文件名
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
				String msg = "上传文件失败：localFile=" + localFile + " ftpPath="
						+ ftpPath + " newName=" + newName;
				throw new IOException( msg );
			}
		} finally {
			ResourceMgr.closeQuietly( in );
		}
	}

	// -------------------------------------------------------------------------

	/**
	 * @param ftpFile ftp上的文件的绝对路径名
	 * @param localDir 本地目录的路径名
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
			throw new IOException( "下载文件出错：ftpFile=" + ftpFile + " localFile="
					+ localFile );
		return localFile;
	}

	// -------------------------------------------------------------------------

	/**
	 * 获取当前工作目录
	 * 
	 * @return
	 * @throws java.io.IOException
	 */
	public String printWorkingDirectory() throws IOException {
		return client.printWorkingDirectory();
	}

	/**
	 * 改变当前工作目录
	 * 
	 * @param pathname
	 * @return
	 * @throws java.io.IOException
	 */
	public boolean changeWorkingDirectory( String pathname ) throws IOException {
		return client.changeWorkingDirectory( pathname );
	}

	/**
	 * 列出当前目录下的文件
	 * 
	 * @return FTPFile[]
	 * @throws java.io.IOException
	 */
	public FTPFile[] listFiles() throws IOException {
		return client.listFiles();
	}
	
	/**
	 * 删除指定的ftp路径上的文件
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
