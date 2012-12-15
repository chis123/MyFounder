package com.founder.e5.listpage;

import com.founder.e5.context.E5Exception;
import com.founder.e5.listpage.XMLParser;

public class PreSpecialGridData extends PreSpecialBaseData
{
	private int 			rows;
	private int				cols;
	private int 			caches;
	private boolean			autoOrder;
	private boolean 		autoCheck;
	private boolean			autoWrap;
	private String			table;
	
	public boolean isAutoWrap() {
		return autoWrap;
	}

	public void setAutoWrap(boolean autoWrap) {
		this.autoWrap = autoWrap;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public PreSpecialGridData() 
	{
		super();
		rows = 5;
		cols = 5;
		caches = 5;
		autoOrder = false;
		autoCheck = false;
		autoWrap = false;
	}

	public boolean isAutoCheck() 
	{
		return autoCheck;
	}

	public void setAutoCheck(boolean autoCheck) 
	{
		this.autoCheck = autoCheck;
	}

	public boolean isAutoOrder() 
	{
		return autoOrder;
	}

	public void setAutoOrder(boolean autoOrder) 
	{
		this.autoOrder = autoOrder;
	}

	public int getCaches() 
	{
		return caches;
	}

	public void setCaches(int caches) 
	{
		this.caches = caches;
	}

	public int getCols() 
	{
		return cols;
	}

	public void setCols(int cols) 
	{
		this.cols = cols;
	}

	public int getRows() 
	{
		return rows;
	}

	public void setRows(int rows) 
	{
		this.rows = rows;
	}

	public String toXML() throws E5Exception 
	{
		StringBuffer buf = new StringBuffer(200);
		buf.append("<custom>\n");
		buf.append("<para>\n");
		buf.append("<rows>").append(rows).append("</rows>\n");
		buf.append("<cols>").append(cols).append("</cols>\n");
		buf.append("<caches>").append(caches).append("</caches>\n");
		if (autoWrap)
			buf.append("<auto_wrap>1</auto_wrap>\n");
		else
			buf.append("<auto_wrap>0</auto_wrap>\n");		
		if (autoOrder)
			buf.append("<auto_seq>1</auto_seq>\n");
		else
			buf.append("<auto_seq>0</auto_seq>\n");
		if (autoCheck)
			buf.append("<auto_check>1</auto_check>\n");
		else
			buf.append("<auto_check>0</auto_check>\n");		
		buf.append("</para>\n");
		buf.append("<slice><![CDATA[");
		buf.append(table);
		buf.append("]]></slice>\n");
		buf.append("</custom>\n");
		return buf.toString();
	}

	public void initXML(String xml) throws E5Exception 
	{
		XMLParser parser = new XMLParser();
		if (xml != null && !"".equals(xml))
		{
			try
			{
				parser.load(xml);
				//(1) Para
				String temp = parser.getField("//custom/para/rows");
				if (temp != null)
					rows = Integer.parseInt(temp);
				temp = parser.getField("//custom/para/cols");
				if (temp != null)
					cols = Integer.parseInt(temp);				
				temp = parser.getField("//custom/para/caches");
				if (temp != null)
					caches = Integer.parseInt(temp);
				temp = parser.getField("//custom/para/auto_seq");
				if ("1".equals(temp))
					autoOrder = true;
				else
					autoOrder = false;
				temp = parser.getField("//custom/para/auto_check");
				if ("1".equals(temp))
					autoCheck = true;
				else
					autoCheck = false;
				temp = parser.getField("//custom/para/auto_wrap");
				if ("1".equals(temp))
					autoWrap = true;
				else
					autoWrap = false;
				
				//(2) template
				table = parser.getField("//custom/slice");
			}
			catch(Exception e)
			{
				throw new E5Exception("parser xml has error["+xml+"]!",e);
			}
		}		
	}	
}
