package com.founder.e5.cat;

/**
 * Created on 2005-7-29
 * @author Gong Lijie
 */
public class Test
{
	public static String rep(String s)
	{
		StringBuffer ret = new StringBuffer(s.length());
		for (int i = 0; i < s.length(); i++)
		{
			ret.append(s.charAt(i));
			if (s.charAt(i) == '\'')
				ret.append('\'');
		}
		return ret.toString();
	}
}
