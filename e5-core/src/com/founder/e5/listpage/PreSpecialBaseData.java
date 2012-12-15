package com.founder.e5.listpage;

import com.founder.e5.context.E5Exception;

public abstract class PreSpecialBaseData 
{
	public PreSpecialBaseData() 
	{
		super();
	}

	public abstract String toXML() throws E5Exception;
	
	public abstract void initXML(String xml) throws E5Exception;
}
