package com.founder.e5.sys;

import java.io.InputStream;

import com.founder.e5.context.CacheReader;
import com.founder.e5.context.E5Exception;

/**
 * @version 1.0
 * @created 11-七月-2005 12:43:46
 */
class StorageDeviceReaderImpl implements StorageDeviceReader {

    private StorageDeviceManager storageDeviceManager = SysFactory.getStorageDeviceManager();
    
	public StorageDeviceReaderImpl(){

	}

	public StorageDevice[] get() throws E5Exception{
		//SysCache是后加的缓存，若已有系统没增加此缓存，则直接从Manager读取。
		SysCache cache = getCache();
		if (cache == null)
			return storageDeviceManager.get();
		else {
			StorageDevice[] devices = cache.getDevices();
			if (devices != null) return devices;
			
			return storageDeviceManager.get();
		}
	}
	private SysCache getCache() {
		return (SysCache)CacheReader.find(SysCache.class);
	}
	
	/* (non-Javadoc)
	 * @see com.founder.e5.sys.StorageDeviceReader#getByName(java.lang.String)
	 */
	public StorageDevice getByName(String deviceName) throws E5Exception{
		StorageDevice[] devices = get();
		if (devices == null) return null;
		
		for (int i = 0; i < devices.length; i++) {
			if (deviceName.equals(devices[i].getDeviceName()))
				return devices[i];
		}
		return null;
	}

    /* (non-Javadoc)
     * @see com.founder.e5.sys.StorageDeviceReader#write(java.lang.String, java.lang.String, java.io.InputStream)
     */
    public void write(String deviceName, String rltPath, InputStream in) throws E5Exception
    {
	    StorageDevice device = getByName(deviceName);
	    
	    write(device, rltPath, in);
    }
	public void write(StorageDevice device, String rltPath, InputStream in) throws E5Exception {
		storageDeviceManager.write(device, rltPath, in);
	}

    /* （非 Javadoc）
     * @see com.founder.e5.sys.StorageDeviceReader#write(java.lang.String, java.lang.String, java.io.InputStream, int)
     */
    public void write(String deviceName, String rltPath, InputStream in, int deviceType) throws E5Exception
    {
	    StorageDevice device = getByName(deviceName);
	    
	    write(device, rltPath, in, deviceType);
    }
	public void write(StorageDevice device, String rltPath, InputStream in, int deviceType) throws E5Exception {
		storageDeviceManager.write(device, rltPath, in, deviceType);
	}

    /* （非 Javadoc）
     * @see com.founder.e5.sys.StorageDeviceReader#read(java.lang.String, java.lang.String)
     */
    public InputStream read(String deviceName, String rltPath) throws E5Exception
    {
	    StorageDevice device = getByName(deviceName);
	    
       return read(device, rltPath);
    }

	public InputStream read(StorageDevice device, String rltPath) throws E5Exception {
        return storageDeviceManager.read(device, rltPath);
	}

   /* （非 Javadoc）
     * @see com.founder.e5.sys.StorageDeviceReader#read(java.lang.String, java.lang.String, int)
     */
    public InputStream read(String deviceName, String rltPath, int deviceType) throws E5Exception
    {
    	StorageDevice device = getByName(deviceName);
    	
    	return read(device, rltPath, deviceType);
    }

	public InputStream read(StorageDevice device, String rltPath, int deviceType) throws E5Exception {
    	return storageDeviceManager.read(device, rltPath, deviceType);
	}

    public FtpReader getFtpReader(String deviceName)throws E5Exception
    {
    	return storageDeviceManager.getFtpReader(deviceName);
    }
}