package com.founder.e5.sys;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.hibernate.Hibernate;

import com.founder.e5.commons.FtpClient;
import com.founder.e5.commons.Log;
import com.founder.e5.commons.ResourceMgr;
import com.founder.e5.context.BaseDAO;
import com.founder.e5.context.Context;
import com.founder.e5.context.DAOHelper;
import com.founder.e5.context.E5Exception;

/**
 * @modified 2009-11-2 增加ftp写入重试机制
 * 
 * @version 1.0
 * @created 11-七月-2005 13:06:55
 */
class StorageDeviceManagerImpl implements StorageDeviceManager {

	private Log log = Context.getLog("e5.sys");
	
	/* ftp 写入重试次数,0 表示不重试 */
	private static int    writeRetryCount = 0;
	private static String configFile = "e5-config.xml";
	
	static{
		//从e5-config.xml装载配置的重试次数
		InputStream in = null;
		try{
	        in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(configFile);
	        SAXReader reader = new SAXReader();
	        Document doc = reader.read(in);
	        
	        Element ftpretry = (Element)doc.getRootElement().selectSingleNode("//storage-device/ftpRetryCount");
	        if(ftpretry!=null)
	        	writeRetryCount = Integer.parseInt(ftpretry.getText());
	        if(writeRetryCount < 0) writeRetryCount = 0;
		}
		catch(Exception ex){
			//ignore
			writeRetryCount = 0;
		}finally{
			ResourceMgr.closeQuietly(in);
		}
	}
	
	public StorageDeviceManagerImpl(){
	}

	/**
	 * 获得所有的存储设备
	 */
	public StorageDevice[] get() throws E5Exception{
	    StorageDevice[] devices = null;
        List list = DAOHelper.find("from com.founder.e5.sys.StorageDevice as device");
        if(list.size() > 0)
        {
            devices = new StorageDevice[list.size()];
            list.toArray(devices);
        }
        return devices;
	}

	/**
	 * 创建存储设备
	 * @param storageDivice    存储设备对象
	 * 
	 */
	public void create(StorageDevice storageDevice) throws E5Exception{
	    try
        {
		    BaseDAO dao = new BaseDAO();
		    StorageDevice device = getByName(storageDevice.getDeviceName());
		    if(device != null)
		        throw new E5Exception("StorageDevice has exist.");
            dao.save(storageDevice);
        }
        catch (Exception e)
        {
            throw new E5Exception("create storage device error.", e);
        }
	}

	
	/**
	 * 根据设备名获得存储设备
	 * @param deviceName    存储设备名
	 * 
	 */
	public StorageDevice getByName(String deviceName) throws E5Exception
	{
        List list = DAOHelper.find("from com.founder.e5.sys.StorageDevice as device where device.DeviceName = :deviceName", 
        		deviceName, Hibernate.STRING);
        if(list.size() == 1)
        {
            return (StorageDevice)list.get(0);
        }
        else
            return null;
	}

	/**
	 * 更新存储设备
	 * @param storageDevice    存储设备
	 * 
	 */
	public void update(StorageDevice StorageDevice) throws E5Exception{
	    try
        {
		    BaseDAO dao = new BaseDAO();
		    dao.update(StorageDevice);
        }
        catch (Exception e)
        {
            throw new E5Exception("update storage device error.", e);
        }
	}

	/**
	 * 删除存储设备
	 * @param deviceName    存储设备名
	 * 
	 */
	public void delete(String deviceName) throws E5Exception{
	    DAOHelper.delete("delete from com.founder.e5.sys.StorageDevice as device where device.DeviceName = :deviceName", 
	    		deviceName, Hibernate.STRING);
	}

	/**
	 * 将指定的流写到设备名指定的设备上的相对路径
	 * 
	 * @param deviceName 设备名
	 * @param rltPath 相对路径名
	 * @param in 输入流
	 */
	public void write(String deviceName, String rltPath, InputStream in) throws E5Exception
	{
	    StorageDevice device = getByName(deviceName);
	    write(device, rltPath, in);
	}

	public void write(StorageDevice device, String rltPath, InputStream in) throws E5Exception {
	    write(device, rltPath, in, device.getDeviceType());
	}

	
	/**
	 * 将指定的流写到设备名指定的设备上的相对路径
	 * 
	 * @param deviceName 设备名
	 * @param rltPath 相对路径名
	 * @param in 输入流
	 * @param deviceType 设备类型 参考StorageDevice的定义
	 */
	public void write(String deviceName, String rltPath, InputStream in, int deviceType) throws E5Exception
	{
	    StorageDevice device = getByName(deviceName);
	    
	    write(device, rltPath, in, deviceType);
	}

	public void write(StorageDevice device, String rltPath, InputStream in, int deviceType) throws E5Exception
	{
	    if(in == null)
	        throw new NullPointerException("input stream is null.");
	    if(device == null)
	        throw new E5Exception("device not found.");
	    switch(deviceType)
	    {
	    	case StorageDevice.DEVICE_TYPE_NFS:
	    	    try {
                    write(device.getNfsDevicePath() + File.separator + rltPath, in);
                } catch (Exception e) {
                    throw new E5Exception("", e);
                }
                break;
	        case StorageDevice.DEVICE_TYPE_NTFS:
	            try {
                    write(device.getNtfsDevicePath() + File.separator + rltPath, in);
                } catch (Exception e) {
                    throw new E5Exception("", e);
                }
                break;
	        case StorageDevice.DEVICE_TYPE_FTP:
	        	writeFtp(device,rltPath,in);
                break;
	        case StorageDevice.DEVICE_TYPE_HTTP:
	            try {
                    writeHttp(device.getHttpDeviceURL() + "/" + rltPath, in);
                } catch (Exception e2) {
                    throw new E5Exception("", e2);
                }
	            break;
	    }
	}
	private void writeFtp(StorageDevice device,String rltPath, InputStream in) throws E5Exception
	{
        String hostName = StorageDeviceHelper.getHostName(device);
        int nPort = StorageDeviceHelper.getFtpPort(device);
        String fileName = StorageDeviceHelper.getFileName(rltPath);
        String filePath =StorageDeviceHelper.getWholeFtpPath(device,rltPath);
        try {
            ftpUpload(hostName, nPort, device.getUserName(), device.getUserPassword(), filePath, fileName, in);
        } catch (Exception e1) {
            throw new E5Exception("", e1);
        }
	}
	/**
	 * 将指定的流写到nfs或ntfs文件路径
	 * 
	 * @param filePath nfs或ntfs文件路径
	 * @param in  输入流
	 * @throws Exception
	 */
	private void write(String filePath, InputStream in) throws Exception
	{
	    FileOutputStream out = null;
	    try {
		    out = new FileOutputStream(filePath);
		    BufferedInputStream bin = new BufferedInputStream(in);
		    byte[] content = new byte[in.available()];
		    bin.read(content);
		    out.write(content);
		    out.flush();
	    } finally {
	        if(out != null)
	            out.close();
	    }
	}
	
	/**
	 * 从指定的nfs或ntfs文件读取输入流
	 * 
	 * @param filePath nfs或ntfs文件
	 * @return 输入流
	 * @throws Exception
	 */
	private InputStream read(String filePath) throws Exception
	{
	    InputStream in = new FileInputStream(filePath);
		return in;
	}
	
	/**
	 * 将指定的输入流上载到ftp存储上
	 * 如果fileName中的相对路径不存在则自动创建(wanghc)
	 * 
	 * @param hostName 主机名
	 * @param port   端口号
	 * @param userName ftp用户名
	 * @param password ftp用户口令
	 * @param path ftp绝对路径
	 * @param fileName 文件名
	 * @param in  输入流
	 * @throws Exception
	 */
	private void ftpUpload(String hostName, int port, String userName, String password, String path, String fileName, InputStream in) throws Exception
	{
	    path = path.replaceAll("//" + hostName, "/");//似乎无用?
		path = path.replaceAll("\\\\", "/");
		
		if ("".equals(path) || path.endsWith("/")) path += fileName;
		else path += "/" + fileName;
		
		   //只尝试一次写入
		if(writeRetryCount <= 0){
			FtpClient client = new FtpClient(hostName, port, userName, password);
		    try {
			    client.storeFile(in, path);
		    } finally {
		        if(client != null)
		            client.disconnect();
		    }
		}else{
			/**
			 * 在FTP写入数据返回SocketException同时错误消息包含recv failed...时，尝试多次写入数据
			 * 通常此类问题由FTP Server拒绝服务导致
			 */
			int retry = 0;
			while(retry <= writeRetryCount){
				try	{
					FtpClient client = new FtpClient(hostName, port, userName, password);
				    try {
					    client.storeFile(in, path);
					    break;
				    } finally {
				        if(client != null)
				            client.disconnect();
				    }
			    }
			    catch(SocketException ex){
					String msg = ex.getMessage();
					if(msg!=null && msg.toLowerCase().indexOf("recv failed")>=0){
						retry ++ ;
						if(retry > writeRetryCount)
							throw ex;
						
						if(log.isWarnEnabled())
							log.warn("ftp upload error,hostName="+hostName+",retry["+retry+"]");
						
						continue;
					}
					
					throw ex;
			    }
			}
		}
	    
	    /*
	     * 2009-7-7 Gong Lijie
	     * 改用FtpClient类的同样方法
	     * 避免多处使用FTPClient和进行特殊设置，如passiveMode检验等（集中到FtpClient里）
	    FTPClient client = new FTPClient();
	    try
	    {
		    client.setDefaultPort(port);
		    client.connect(hostName);
		    client.login(userName, password);
		    client.setFileType(FTP.BINARY_FILE_TYPE);
		    try {
		    	client.listFiles();
		    }catch(BindException ex)
		    {
		    	System.out.println("StorageDeviceManager:turn to passive mode!");
		    	client.enterLocalPassiveMode();
		    }
		    
		    path = path.replaceAll("//"+hostName, "/");//似乎无用?
		    createDir(client,path);
		    client.changeWorkingDirectory(path);
		    client.storeFile(fileName, in);
	    }
	    finally
	    {
	        //client.logout();
	        if(client != null)
	            client.disconnect();
	    }
	    */
	}
	/**
	 * 如果fileName中的相对路径不存在则创建
	 * 
	 * @throws IOException
	 * @author wanghc
	 */
	/*
     * 2009-7-7 Gong Lijie
     * 上面的方法改用FtpClient类的同样方法，因此不再需要此方法
	private void createDir(FTPClient client,String remotePath) throws IOException
	{
		String path = remotePath.replaceAll("\\\\", "/");

		String pathArray[] = path.split("/");
		for(int i=0;i<pathArray.length;i++)
		{
			if(pathArray[i].trim().equals("")) continue;
			if (!client.changeWorkingDirectory(pathArray[i]))
			{	
				client.makeDirectory(pathArray[i]);
				if (!client.changeWorkingDirectory(pathArray[i]))
					throw new IOException("Failed to create directory:" + pathArray[i]);
			}
		}
	}
	*/
	
	/**
	 * 获取一个ftpReader对象
	 * 
	 * 
	 */
	public FtpReader getFtpReader(String deviceName)throws E5Exception
	{
	    StorageDevice device = getByName(deviceName);
        String hostName = StorageDeviceHelper.getHostName(device);
        int nPort = StorageDeviceHelper.getFtpPort(device);
		return new FtpReaderImpl(hostName,nPort, device.getUserName(), device.getUserPassword());
	}
	/**
	 * 从ftp存储设备上获得流
	 * 
	 * @param hostName 主机名
	 * @param port   主机端口
	 * @param userName  用户名
	 * @param password  用户口令
	 * @param path     ftp绝对路径
	 * @param fileName 文件名
	 * @return 输入流
	 * @throws Exception
	 */
	private InputStream ftpDownload(String hostName, int port, String userName, String password, String path, String fileName) throws Exception
	{
		FtpReader ftpReader = new FtpReaderImpl(hostName,port, userName, password);
		FtpInputStream in = new FtpInputStream(ftpReader,path,fileName);
		if (in == null || in.isNull()) return null;
		return in;
	}
	
	/**
	 * 从指定的设备名读取输入流
	 * 
	 * @param deviceName  存储设备名
	 * @param rltPath   相对路径名
	 */
	public InputStream read(String deviceName, String rltPath) throws E5Exception
	{
	    StorageDevice device = getByName(deviceName);
	    if(device == null)
	        throw new E5Exception("device not found.");
	    
	    return read(device, rltPath);
	}
	
	public InputStream read(StorageDevice device, String rltPath) throws E5Exception {
		return read(device, rltPath, device.getDeviceType());
	}

	/**
	 * 从指定的设备名读取输入流
	 * 
	 * @param deviceName  存储设备名
	 * @param rltPath   相对路径名
	 * @param deviceType 设备类型 参考StorageDevice的定义
	 */
	public InputStream read(String deviceName, String rltPath, int deviceType) throws E5Exception
	{
	    StorageDevice device = getByName(deviceName);
	    if(device == null)
	        throw new E5Exception("device not found.");
	    
	    return read(device, rltPath, deviceType);
	}
	
	public InputStream read(StorageDevice device, String rltPath, int deviceType) throws E5Exception 
	{
		switch (deviceType) {
			case StorageDevice.DEVICE_TYPE_NFS:
				try {
					return read(device.getNfsDevicePath() + File.separator + rltPath);
				} catch (Exception e) {
					throw new E5Exception("", e);
				}
			case StorageDevice.DEVICE_TYPE_NTFS:
				try {
					return read(device.getNtfsDevicePath() + File.separator + rltPath);
				} catch (Exception e) {
					throw new E5Exception("", e);
				}
			case StorageDevice.DEVICE_TYPE_FTP:
				String hostName = StorageDeviceHelper.getHostName(device);
	
				int nPort = StorageDeviceHelper.getFtpPort(device);
				String path = StorageDeviceHelper.getWholeFtpPath(device, rltPath);
				String fileName = StorageDeviceHelper.getFileName(rltPath);
				try {
					return ftpDownload(hostName, nPort, device.getUserName(),
							device.getUserPassword(), path, fileName);
				} catch (Exception e1) {
					throw new E5Exception("", e1);
				}
			case StorageDevice.DEVICE_TYPE_HTTP:
				try {
					return readHttp(device.getHttpDeviceURL() + "/" + rltPath);
				} catch (Exception e2) {
					throw new E5Exception("", e2);
				}
			default:
				return null;
		}
	}

	
	 /**
		 * 从URL指定的Http地址获得输入流
		 * 
		 * @param httpURL
		 *            URL信息
		 * @throws java.lang.Exception
		 */
	  private InputStream readHttp(String httpURL) throws Exception
	  {
	      URL url = new URL(httpURL);
	      URLConnection conn = url.openConnection();
	      HttpURLConnection httpConn = (HttpURLConnection) conn;

	      httpConn.setRequestMethod("POST");
	      httpConn.setDoOutput(true);
	      httpConn.setDoInput(true);
	      
	      ByteArrayOutputStream bos = null;
	      BufferedInputStream bis = null;
	      
	      try
	      {
		      httpConn.connect();
		      bis = new BufferedInputStream(httpConn.getInputStream());
		      bos = new ByteArrayOutputStream();
		      byte[] buf = new byte[1024];
		      int len;
		      while ( (len = bis.read(buf, 0, buf.length)) != -1)
		      {
		          bos.write(buf, 0, len);
		      }
		      bos.flush();
		      return new ByteArrayInputStream(bos.toByteArray());
	      }
	      finally
	      {
		      bos.close();
		      bis.close();
		      httpConn.disconnect();
	      }
	  }
	  
	  
	  /**
	   * 将输入流写到URL指定的Http地址
	   * 
	   * @param httpURL http地址信息
	   * @param in 输入流
	   * @throws java.lang.Exception
	   */
	  private void writeHttp(String httpURL, InputStream in) throws Exception
	  {
	      URL url = new URL(httpURL);
	      URLConnection conn = url.openConnection();
	      HttpURLConnection httpConn = (HttpURLConnection) conn;

	      httpConn.setRequestProperty("Content-Type", "text/xml;charset=GBK");
	      httpConn.setRequestMethod("POST");
	      httpConn.setDoOutput(true);
	      httpConn.setDoInput(true);
	      
	      BufferedOutputStream httpOut = null;
	      BufferedInputStream httpIn = null;
	      
	      try
	      {
		      httpConn.connect();
		      httpOut = new BufferedOutputStream(httpConn.getOutputStream());
		      byte[] buf = new byte[1024];
		      int len;
		      while ( (len = in.read(buf, 0, buf.length)) != -1)
		      {
		          httpOut.write(buf, 0, len);
		      }
		      httpOut.flush();
		      httpIn = new BufferedInputStream(httpConn.getInputStream());
		      StringBuffer bufResponse = new StringBuffer();
		      while((len = httpIn.read(buf, 0, buf.length)) != -1)
		      {
		            bufResponse.append(new String(buf, 0, len));
		      }
//		      String response = null;
//		      response = bufResponse.toString();
	      }
	      finally
	      {
	          if(httpOut != null)
	              httpOut.close();
		      if(httpIn != null)
		          httpIn.close();
		      httpConn.disconnect();
	      }
	  }
	  /*
	  public static void main(String[] args) throws Exception {
		FTPClient client = new FTPClient();
		try {
//			StorageDeviceManagerImpl ftp = new StorageDeviceManagerImpl();
			FileInputStream fin = new FileInputStream("d:/2.jpg");
//			ftp.ftpUpload("172.16.197.18", 21, "attach", "attach", "/data/ftpdata/Attach/2007/10/", "photo.jpg",fin );
//			String root = "/2007/10/test.jpg";
			client.connect("172.16.197.18");
			client.setDefaultPort(21);
			client.login("share", "share");
			client.setFileType(FTP.BINARY_FILE_TYPE);
			client.listNames();
			client.changeWorkingDirectory("/data/ftpdata/PhotoDev");
			client.makeDirectory("2007");
			client.changeWorkingDirectory("2007");
			client.makeDirectory("09");
			client.changeWorkingDirectory("09");
			boolean ss= client.storeFile("a.jog", fin);
			System.out.println("result:"+ss);
			fin.close();
//			client.changeWorkingDirectory(root);
//			//client.storeFile(fileName, in);
		} finally {
//			client.logout();
			if (client != null)
				client.disconnect();
		}
	}
	*/

}