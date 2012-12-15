package com.founder.e5.sys;

import java.io.InputStream;
import com.founder.e5.context.E5Exception;

public interface FtpReader
{
	public boolean connect()throws E5Exception;
	public InputStream read(String path, String fileName)throws E5Exception;
	public void disconnect() throws E5Exception;
	public boolean deleteFile(String path, String fileName)throws E5Exception;
	public void setControlEncoding(String encoding);
}
