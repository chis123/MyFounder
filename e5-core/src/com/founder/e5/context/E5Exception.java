package com.founder.e5.context;

/**
 * E5平台的异常基类
 * 可以给自己创建的异常标识错误码，便于使用异常文件更详细地描述异常内容
 * @created on 2005-7-12
 * @author Gong Lijie
 * @version 1.0
 */
public class E5Exception extends Exception
{
	private static final long serialVersionUID = 1L;
	private int errorCode;
	
	public E5Exception(String message)
	{
		super(message);
	}

	public E5Exception(Throwable cause)
	{
		super(cause);
	}

	public E5Exception(String message, Throwable cause)
	{
		super(message, cause);
	}
	public E5Exception(int _errorCode, String message)
	{
		super(message);
		errorCode = _errorCode;
	}

	public E5Exception(int _errorCode, Throwable cause)
	{
		super(cause);
		errorCode = _errorCode;
	}

	public E5Exception(int _errorCode, String message, Throwable cause)
	{
		super(message, cause);
		errorCode = _errorCode;
	}
	
	public int getErrorCode()
	{
		return errorCode;
	}
	
	public String toString(){
		return errorCode + super.toString();
	}
}
