package com.founder.e5.listpage;
import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import com.founder.e5.context.E5Exception;
import com.founder.e5.listpage.ShowField;

/**
 * 配置信息使用的XML解析器,ListXSL
 * 以DOM4J作为基础
 * @author LuZuowei
 *
 */
public class XMLParser 
{
	Document document = null;
	
	public XMLParser() 
	{
		super();
	}
	
	/**
	 * 加载，字符串方式
	 * @param xml
	 * @return
	 */
	public void load(String xml) throws E5Exception
	{
		try
		{
			SAXReader reader = new SAXReader();
			InputSource input = new InputSource();
			input.setCharacterStream((Reader)(new StringReader(xml)));
		    document = reader.read(input);
		}
		catch(Exception e)
		{
			throw new E5Exception("parse xml failed![" + xml + "]",e);
		}
	}
	
	/**
	 * 文件方式
	 * @param file
	 * @return
	 */
	public void load(File file) throws E5Exception
	{
		try
		{
			SAXReader reader = new SAXReader();
			document = reader.read(file);
		}
		catch(Exception e)
		{
			throw new E5Exception("parse xml failed!["+file.getAbsolutePath()+"]",e);
		}
	}
	
	
	/**
	 * 获取当前设置所有字段名称
	 * @return
	 */
	public ShowField[] getFields() throws E5Exception
	{
		ShowField[] ret = null;
		try
		{
			List retlist = Collections.synchronizedList(new ArrayList(5));
			List list = document.selectNodes("//root/fields/field");
			if (list != null && list.size() > 0)
			{
				Node node = null;
				Node child = null;
				for (int i = 0; i < list.size(); i++)
				{
					ShowField field = new ShowField();
					node = (Node)list.get(i);
					child = node.selectSingleNode("name");
					if (child != null) field.setName(child.getText());
					child = node.selectSingleNode("type");
					if (child != null) field.setType(Integer.parseInt(child.getText()));
					child = node.selectSingleNode("url");
					if (child != null) field.setUrl(child.getText());
					child = node.selectSingleNode("clazz");
					if (child != null) field.setClazz(child.getText());
					child = node.selectSingleNode("method");
					if (child != null) field.setMethod(child.getText());
					retlist.add(field);
				}
			}
			if (retlist.size() > 0)
			{
				ret = new ShowField[retlist.size()];
				retlist.toArray(ret);
			}
			retlist.clear();
			retlist = null;
		}
		catch(Exception e)
		{
			throw new E5Exception("XMLParse::getFields has error!",e);
		}
		return ret;
	}
	
	/**
	 * 获取特定字段的值
	 */
	public String getField(String xpath) throws E5Exception
	{
		String ret = null; 
		try
		{
			Node node = document.selectSingleNode(xpath);
			if (node != null)
				ret = node.getText();
		}
		catch(Exception e)
		{
			throw new E5Exception("XMLParse::getField has error!",e);
		}
		return ret;
	}
	
	/**
	 * 返回xpath下的XML片断
	 * @param xpath
	 * @return
	 * @throws E5Exception
	 */
	public String getChildXML(String xpath) throws E5Exception
	{
		String ret = null;
		Node node = document.selectSingleNode(xpath);
		if (node != null)
			ret = node.asXML();
		return ret;
	}
	/**
	 * 返回一个节点子的个数
	 * @param xpath
	 * @return
	 */
	public int getChildsCount(String xpath) throws E5Exception
	{
		int nCount = 0;
		try
		{
			List list = document.selectNodes(xpath);
			if (list != null)
				nCount = list.size();
		}
		catch(Exception e)
		{
			throw new E5Exception("XMLParse::getChildsCount has error!",e);
		}		
		return nCount;
	}
}
