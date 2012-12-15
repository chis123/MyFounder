package com.founder.e5.listpage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.founder.e5.context.E5Exception;
import com.founder.e5.listpage.XMLParser;

/**
 * Tree型的特殊设置
 * @author LuZuowei 
 */
public class PreSpecialTreeData extends PreSpecialBaseData
{
	private List layerList;
	private List template;
	
	public PreSpecialTreeData() 
	{
		super();
		layerList = Collections.synchronizedList(new ArrayList(10));
		template = Collections.synchronizedList(new ArrayList(10));
	}

	protected void finalize() throws Throwable 
	{
		layerList.clear();
		layerList = null;
		template.clear();
		template = null;
		super.finalize();
	}

	public void clearLayer()
	{
		layerList.clear();
	}
	
	public void clearTemplate()
	{
		template.clear();
	}
	
	public void add(PreLayerFieldData data)
	{
		layerList.add(data);
	}
	
	public void add(ColumnTemplate data)
	{
		template.add(data);
	}
	
	public List getLayerList()
	{
		return layerList;
	}
	
	public List getTemplate()
	{
		return template;
	}
	
	public String toXML() throws E5Exception
	{
		StringBuffer buf = new StringBuffer(200);
		buf.append("<custom>\n");
		buf.append("<para>\n");
		for (int i = 0; i < layerList.size(); i++)
		{
			PreLayerFieldData data = (PreLayerFieldData)layerList.get(i);
			if (data != null)
			{
				buf.append("<layer>\n");
				buf.append("<code>").append(data.getCode()).append("</code>\n");
				buf.append("<name><![CDATA[").append(data.getName()).append("]]></name>\n");
				buf.append("</layer>\n");
			}
		}
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
				int nCount = parser.getChildsCount("//custom/para/layer");
				int k = 0;
				clearLayer();
				for (int i = 0; i < nCount; i++)
				{
					k = i + 1;
					PreLayerFieldData data = new PreLayerFieldData();
					data.setCode(parser.getField("//custom/para/layer["+k+"]/code"));
					data.setName(parser.getField("//custom/para/layer["+k+"]/name"));
					add(data);
				}
				//(2) template
				nCount = parser.getChildsCount("//custom/slice/col");
				k = 0;
				clearTemplate();
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
