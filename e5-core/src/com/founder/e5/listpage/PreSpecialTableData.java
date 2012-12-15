package com.founder.e5.listpage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.founder.e5.context.E5Exception;
import com.founder.e5.listpage.XMLParser;

public class PreSpecialTableData extends PreSpecialBaseData
{
	private int 			rows;
	private int 			caches;
	private boolean			autoOrder;
	private boolean 		autoCheck;
	private List			template;
	
	public PreSpecialTableData() 
	{
		super();
		template = Collections.synchronizedList(new ArrayList(10));
		
		rows = 25;
		caches = 5;
		autoOrder = false;
		autoCheck = false;
	}

	protected void finalize() throws Throwable 
	{
		super.finalize();
		template.clear();
		template = null;
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

	public int getRows() 
	{
		return rows;
	}

	public void setRows(int rows) 
	{
		this.rows = rows;
	}
	
	public void clear()
	{
		template.clear();
	}
	
	public void add(ColumnTemplate temp)
	{
		template.add(temp);
	}
	
	public List getList()
	{
		return template;
	}
	
	public String toXML() throws E5Exception
	{
		StringBuffer buf = new StringBuffer(200);
		buf.append("<custom>\n");
		buf.append("<para>\n");
		buf.append("<rows>").append(rows).append("</rows>\n");
		buf.append("<caches>").append(caches).append("</caches>\n");
		if (autoOrder)
			buf.append("<auto_seq>1</auto_seq>\n");
		else
			buf.append("<auto_seq>0</auto_seq>\n");
		if (autoCheck)
			buf.append("<auto_check>1</auto_check>\n");
		else
			buf.append("<auto_check>0</auto_check>\n");		
		buf.append("</para>\n");
		buf.append("<slice>\n");
		for (int i = 0; i < template.size(); i++)
		{
			ColumnTemplate data = (ColumnTemplate)template.get(i);
			if (data != null)
			{
				buf.append("<col>\n");
				buf.append("<name><![CDATA[").append(data.getName()).append("]]></name>\n");
				buf.append("<head><![CDATA[").append(data.getHeader()).append("]]></head>\n");
				buf.append("<width>").append(data.getWidth()).append("</width>\n");
				buf.append("<template><![CDATA[").append(data.getTemplate()).append("]]></template>\n");
				buf.append("</col>\n");
			}
		}
		buf.append("</slice>\n");
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
				//(2) template
				int nCount = parser.getChildsCount("//custom/slice/col");
				int k = 0;
				clear();
				for (int i = 0; i < nCount; i++)
				{
					k = i + 1;
					ColumnTemplate data = new ColumnTemplate();
					data.setName(parser.getField("//custom/slice/col["+k+"]/name"));
					data.setHeader(parser.getField("//custom/slice/col["+k+"]/head"));
					data.setWidth(parser.getField("//custom/slice/col["+k+"]/width"));
					data.setTemplate(parser.getField("//custom/slice/col["+k+"]/template"));
					add(data);
				}
			}
			catch(Exception e)
			{
				throw new E5Exception("parser xml has error["+xml+"]!",e);
			}
		}
	}
}
