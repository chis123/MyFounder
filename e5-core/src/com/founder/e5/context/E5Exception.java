package com.founder.e5.context;

/**
 * E5ƽ̨���쳣����
 * ���Ը��Լ��������쳣��ʶ�����룬����ʹ���쳣�ļ�����ϸ�������쳣����
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
