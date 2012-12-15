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
 * @modified 2009-11-2 ����ftpд�����Ի���
 * 
 * @version 1.0
 * @created 11-����-2005 13:06:55
 */
class StorageDeviceManagerImpl implements StorageDeviceManager {

	private Log log = Context.getLog("e5.sys");
	
	/* ftp д�����Դ���,0 ��ʾ������ */
	private static int    writeRetryCount = 0;
	private static String configFile = "e5-config.xml";
	
	static{
		//��e5-config.xmlװ�����õ����Դ���
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
	 * ������еĴ洢�豸
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
	 * �����洢�豸
	 * @param storageDivice    �洢�豸����
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
	 * �����豸����ô洢�豸
	 * @param deviceName    �洢�豸��
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
	 * ���´洢�豸
	 * @param storageDevice    �洢�豸
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
	 * ɾ���洢�豸
	 * @param deviceName    �洢�豸��
	 * 
	 */
	public void delete(String deviceName) throws E5Exception{
	    DAOHelper.delete("delete from com.founder.e5.sys.StorageDevice as device where device.DeviceName = :deviceName", 
	    		deviceName, Hibernate.STRING);
	}

	/**
	 * ��ָ������д���豸��ָ�����豸�ϵ����·��
	 * 
	 * @param deviceName �豸��
	 * @param rltPath ���·����
	 * @param in ������
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
	 * ��ָ������д���豸��ָ�����豸�ϵ����·��
	 * 
	 * @param deviceName �豸��
	 * @param rltPath ���·����
	 * @param in ������
	 * @param deviceType �豸���� �ο�StorageDevice�Ķ���
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
	 * ��ָ������д��nfs��ntfs�ļ�·��
	 * 
	 * @param filePath nfs��ntfs�ļ�·��
	 * @param in  ������
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
	 * ��ָ����nfs��ntfs�ļ���ȡ������
	 * 
	 * @param filePath nfs��ntfs�ļ�
	 * @return ������
	 * @throws Exception
	 */
	private InputStream read(String filePath) throws Exception
	{
	    InputStream in = new FileInputStream(filePath);
		return in;
	}
	
	/**
	 * ��ָ�������������ص�ftp�洢��
	 * ���fileName�е����·�����������Զ�����(wanghc)
	 * 
	 * @param hostName ������
	 * @param port   �˿ں�
	 * @param userName ftp�û���
	 * @param password ftp�û�����
	 * @param path ftp����·��
	 * @param fileName �ļ���
	 * @param in  ������
	 * @throws Exception
	 */
	private void ftpUpload(String hostName, int port, String userName, String password, String path, String fileName, InputStream in) throws Exception
	{
	    path = path.replaceAll("//" + hostName, "/");//�ƺ�����?
		path = path.replaceAll("\\\\", "/");
		
		if ("".equals(path) || path.endsWith("/")) path += fileName;
		else path += "/" + fileName;
		
		   //ֻ����һ��д��
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
			 * ��FTPд�����ݷ���SocketExceptionͬʱ������Ϣ����recv failed...ʱ�����Զ��д������
			 * ͨ������������FTP Server�ܾ�������
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
	     * ����FtpClient���ͬ������
	     * ����ദʹ��FTPClient�ͽ����������ã���passiveMode����ȣ����е�FtpClient�
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
		    
		    path = path.replaceAll("//"+hostName, "/");//�ƺ�����?
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
	 * ���fileName�е����·���������򴴽�
	 * 
	 * @throws IOException
	 * @author wanghc
	 */
	/*
     * 2009-7-7 Gong Lijie
     * ����ķ�������FtpClient���ͬ����������˲�����Ҫ�˷���
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
	 * ��ȡһ��ftpReader����
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
	 * ��ftp�洢�豸�ϻ����
	 * 
	 * @param hostName ������
	 * @param port   �����˿�
	 * @param userName  �û���
	 * @param password  �û�����
	 * @param path     ftp����·��
	 * @param fileName �ļ���
	 * @return ������
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
	 * ��ָ�����豸����ȡ������
	 * 
	 * @param deviceName  �洢�豸��
	 * @param rltPath   ���·����
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
	 * ��ָ�����豸����ȡ������
	 * 
	 * @param deviceName  �洢�豸��
	 * @param rltPath   ���·����
	 * @param deviceType �豸���� �ο�StorageDevice�Ķ���
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
		 * ��URLָ����Http��ַ���������
		 * 
		 * @param httpURL
		 *            URL��Ϣ
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
	   * ��������д��URLָ����Http��ַ
	   * 
	   * @param httpURL http��ַ��Ϣ
	   * @param in ������
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