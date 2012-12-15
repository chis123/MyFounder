package com.founder.e5.listpage;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.founder.e5.commons.Log;
import com.founder.e5.context.E5Exception;
import com.founder.e5.dom.DocTypeField;
import com.founder.e5.dom.DocTypeReader;
import com.founder.e5.listpage.ListPage;
import com.founder.e5.listpage.XMLParser;

/**
 * ListPage所涉及到的工具类
 * @author LuZuowei
 *
 */
public class ListPageUtil 
{	
	private DocTypeReader typeReader;
	private XMLParser parser;
	private Log	log;
	
	public ListPageUtil() 
	{
		super();
		parser = new XMLParser();
	}

	/**
	 * 临时数据转变为存储数据的过程
	 * @param data
	 * @return
	 */
	public ListPage toListPage(PreSettingBaseData data)
	{
		ListPage page = null;
		if (data != null)
		{
			//(1) 基础信息设置
			page = new ListPage();
			page.setListID(data.getId());
			page.setDocTypeID(data.getDocTypeID());
			page.setListBuilderName(data.getBuilderID());
			page.setListName(data.getName());
			page.setPathXSL(data.getPathxsl());
			page.setPathJS(data.getPathjs());
			page.setPathCSS(data.getPathcss());			
			//(2) XML信息的合成
			StringBuffer buf = new StringBuffer(100);
			buf.append("<root>\n");			
			if (data.getFieldDisplay().size() > 0)
			{				
				//(2.1) 显示字段
				List temp = data.getFieldDisplay();
				if (temp.size() > 0)
				{
					buf.append("<fields>\n");					
					for (int i = 0; i < temp.size(); i++)
					{
						PreFieldDisplayData pre = (PreFieldDisplayData)temp.get(i);
						buf.append("<field>\n");
						buf.append("<name>").append(pre.getCode()).append("</name>\n");
						buf.append("<status>").append(pre.getStatus()).append("</status>\n");
						buf.append("</field>\n");
					}
					buf.append("</fields>\n");
				}
				
				//(2.2) 排序字段
				temp = data.getSortFields();
				if (temp.size() > 0)
				{
					StringBuffer sorts = new StringBuffer(100);
					StringBuffer types = new StringBuffer(100);
					for (int i = 0; i < temp.size(); i++)
					{
						PreSortFieldData pre = (PreSortFieldData)temp.get(i);
						if (i > 0)
						{
							sorts.append(",");
							types.append(",");
						}
						sorts.append(pre.getCode());
						types.append(pre.getType());						
					}
					buf.append("<sort>\n");
					buf.append("<fields>").append(sorts.toString()).append("</fields>\n");
					buf.append("<types>").append(types.toString()).append("</types>\n");
					buf.append("</sort>\n");
				}
				//(2.3) XSL处理字段
				temp = data.getFieldXSL();
				if (temp.size() > 0)
				{
					buf.append("<xsl>\n");
					for (int i = 0; i < temp.size(); i++)
					{
						PreFieldXSLData xslData = (PreFieldXSLData)temp.get(i);
						buf.append("<field>\n");
						buf.append("<name>").append(xslData.getCode()).append("</name>\n");
						buf.append("<type>").append(xslData.getType()).append("</type>\n");
						switch(xslData.getType())
						{
							case 1:
								break;
							case 2:
								break;
							case 3:
								Hashtable hash = xslData.getHash();
								Enumeration iter = hash.keys();
								buf.append("<enums>\n");
								if (iter.hasMoreElements())
								{
									String key = (String)iter.nextElement();
									String value = (String)hash.get(key);
									buf.append("<enum>\n");
									buf.append("<key><![CDATA[").append(key).append("]]></key>\n");
									buf.append("<value><![CDATA[").append(value).append("]]></value>\n");
									buf.append("</enum>");
								}
								buf.append("</enums>\n");
								break;
							case 4:
								buf.append("<custom><![CDATA[").append(xslData.getData()).append("]]></custom>");
								break;
						}
						buf.append("</field>\n");
					}
					buf.append("</xsl>\n");
				}
			}
			buf.append("</root>\n");
			page.setListXML(buf.toString());
		}
		return page;
	}
	
	/**
	 * 存储数据转变为临时数据的过程
	 * @param page
	 * @return
	 */
	public PreSettingBaseData toPreSettingData(ListPage page)
	{
		PreSettingBaseData data = null;
		if (page != null)
		{
			data = new PreSettingBaseData();
			//(1) 基础信息
			data.setId(page.getListID());
			data.setBuilderID(page.getListBuilderName());
			data.setDocTypeID(page.getDocTypeID());
			data.setName(page.getListName());
			data.setPathxsl(page.getPathXSL());
			data.setPathjs(page.getPathJS());
			data.setPathcss(page.getPathCSS());			
			//(2) ListXML解析
			try
			{
				parser.load(page.getListXML());
				//(2.1) FieldDisplay
				int nCount = parser.getChildsCount("//root/fields/field");
				int k = 0;
				for (int i = 0; i < nCount; i++)
				{
					k = i + 1;
					String code = parser.getField("//root/fields/field[" + k + "]/name");
					String status = parser.getField("//root/fields/field[" + k + "]/status");
					DocTypeField field = typeReader.getField(data.getDocTypeID(),code);
					PreFieldDisplayData dispData = new PreFieldDisplayData();
					dispData.setCode(code);
					if (status == null)
						dispData.setStatus(0);
					else
						dispData.setStatus(Integer.parseInt(status));
					if (field != null)
					{
						dispData.setName(field.getColumnName());
						dispData.setValid(1);
					}
					else
					{
						dispData.setName("[None]");
						dispData.setValid(0);
					}
					
					data.addFieldDisplay(dispData);
				}
				//(2.2) SortField
				String sortfields = parser.getField("//root/sort/fields");
				String sorttypes = parser.getField("//root/sort/types");
				if (sortfields != null && sorttypes != null)
				{
					String[] fields = sortfields.split(",");
					String[] types = sorttypes.split(",");
					if (fields != null && types != null && fields.length == types.length)
					{
						for (int i = 0; i < fields.length; i++)
						{
							PreSortFieldData sortData = new PreSortFieldData();
							sortData.setCode(fields[i]);
							DocTypeField field = typeReader.getField(data.getDocTypeID(),fields[i]);
							if (field != null)
							{
								sortData.setName(field.getColumnName());
								sortData.setValid(1);
							}
							else
							{
								sortData.setName("[None]");
								sortData.setValid(0);								
							}
							sortData.setType(Integer.parseInt(types[i]));
							data.addSortField(sortData);
						}
					}
				}
				//(2.3) XSL数据
				nCount = parser.getChildsCount("//root/xsl/field");
				k = 0;
				for (int i = 0; i < nCount; i++)
				{
					k = i + 1;
					String code = parser.getField("//root/xsl/field[" + k + "]/name");
					int type = Integer.parseInt(parser.getField("//root/xsl/field[" + k + "]/type"));
					DocTypeField field = typeReader.getField(data.getDocTypeID(),code);
					PreFieldXSLData xslData = new PreFieldXSLData();
					xslData.setCode(code);
					xslData.setType(type);
					if (field != null)
					{
						xslData.setName(field.getColumnName());
						xslData.setValid(1);
					}
					else
					{
						xslData.setName("[None]");
						xslData.setValid(0);
					}
					switch(type)
					{
						case 1:
						case 2:
						case 3:
						case 4:
							break;
						case 5:
							int nChilds = parser.getChildsCount("//root/xsl/field[" + k + "]/enums/enum");
							if (nChilds > 0)
							{								
								Hashtable hash = xslData.getHash();
								int jj = 0;
								for (int j = 0; j < nChilds; j++)
								{
									jj = j + 1;
									String key = parser.getField("//root/xsl/field[" + k + "]/enums/enum[" + jj + "/key");
									String value = parser.getField("//root/xsl/field[" + k + "]/enums/enum[" + jj + "/value");
									hash.put(key,value);
								}
							}
							break;
						case 6:
							xslData.setData(parser.getField("//root/xsl/field[" + k + "]/custom"));
							break;
					}
					data.addFieldXSL(xslData);
				}
			}
			catch(Exception e)
			{
				log.error("ParseXML has error["+page.getListXML()+"]!",e);
			}
		}
		return data;
	}

	/**
	 * 
	 * @param buildertype
	 * @param data
	 * @return
	 */
	public String toXML(String buildertype,PreSpecialBaseData data) throws E5Exception
	{
		String xml = "";
		if ("table".equals(buildertype))
		{
			PreSpecialTableData data1 = (PreSpecialTableData)data;
			xml = data1.toXML();
		}
		else if ("tree".equals(buildertype))
		{
			PreSpecialTreeData data1 = (PreSpecialTreeData)data;
			xml = data1.toXML();			
		}
		else if ("grid".equals(buildertype))
		{
			PreSpecialGridData data1 = (PreSpecialGridData)data;
			xml = data1.toXML();
		}
		return xml;
	}
	
	/**
	 * 
	 * @param xml
	 * @return
	 */
	public PreSpecialBaseData toPreSettingSpecialData(String buildertype,String xml) throws E5Exception
	{
		PreSpecialBaseData data = null;
		if ("table".equals(buildertype))
		{
			PreSpecialTableData data1 = new PreSpecialTableData();
			data1.initXML(xml);
			data = data1;
		}
		else if ("tree".equals(buildertype))
		{
			PreSpecialTreeData data1 = new PreSpecialTreeData();
			data1.initXML(xml);
			data = data1;			
		}
		else if ("grid".equals(buildertype))
		{
			PreSpecialGridData data1 = new PreSpecialGridData();
			data1.initXML(xml);
			data = data1;
		}
		return data;
	}
	
	public void setLog(Log log) 
	{
		this.log = log;
	}

	public void setTypeReader(DocTypeReader typeReader) 
	{
		this.typeReader = typeReader;
	}
}
