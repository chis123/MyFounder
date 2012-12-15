package com.founder.e5.config;

import java.net.InetAddress;
public class Test {

	public static void main(String[] args)
	{
		try
		{
			String ip = InetAddress.getLocalHost().getHostAddress();
			String plainText = "3des:e5platform:"+ip+":"+"2016-4-31";
			
			//Encrypt encrypt = new Encrypt();
			//System.out.println(encrypt.encrypt(plainText));
			
			//System.out.println(encrypt.decrypt("EBF64ACDA3E2EC0489202AA74DBA7470C7E8FD23AD500423AAAFA889C8C9255BCCA95936455D7289"));
		}
		catch(Exception e)		
		{
			e.printStackTrace();
		}
	}
}
