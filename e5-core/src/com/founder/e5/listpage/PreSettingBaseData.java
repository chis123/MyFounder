package com.founder.e5.listpage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * ListPage设置过程中Wizard记录的临时结果记录和简单逻辑处理的部分
 * 抽象类中存在的共存的部分
 * @author LuZuowei
 */
public class PreSettingBaseData 
{
	//(1) 基本信息
	private int		id;
	private int	 	docTypeID;
	private String	builderID;
	private String	name;
	private String  pathxsl;
	private String  pathjs;
	private String  pathcss;
	//(2) 配置信息	
	private List 	fieldDisplay;
	private List 	fieldXSL;
	private List 	sortFields;
	
	public PreSettingBaseData() 
	{
		super();
		fieldDisplay = Collections.synchronizedList(new ArrayList(10));
		fieldXSL = Collections.synchronizedList(new ArrayList(10));
		sortFields = Collections.synchronizedList(new ArrayList(10));
	}

	protected void finalize() throws Throwable 
	{
		super.finalize();
		fieldDisplay.clear();
		fieldDisplay = null;
		fieldXSL.clear();
		fieldXSL = null;
		sortFields.clear();
		sortFields = null;
	}
	
	public void clearFieldDisplay()
	{
		fieldDisplay.clear();
	}
	
	public void clearFieldXSL()
	{
		fieldXSL.clear();
	}
	
	public void clearSortFields()
	{
		sortFields.clear();
	}
	
	public void addFieldDisplay(PreFieldDisplayData data)
	{
		fieldDisplay.add(data);
	}
	
	public void addFieldXSL(PreFieldXSLData data)
	{
		fieldXSL.add(data);
	}
	
	public void addSortField(PreSortFieldData data)
	{
		sortFields.add(data);
	}
	
	public List getFieldDisplay()
	{
		return fieldDisplay;
	}

	public List getFieldXSL() 
	{
		return fieldXSL;
	}

	public List getSortFields() 
	{
		return sortFields;
	}

	public String getBuilderID() 
	{
		return builderID;
	}

	public void setBuilderID(String builderID) 
	{
		this.builderID = builderID;
	}

	public int getDocTypeID() 
	{
		return docTypeID;
	}

	public void setDocTypeID(int docTypeID) 
	{
		this.docTypeID = docTypeID;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getPathcss() 
	{
		return pathcss;
	}

	public void setPathcss(String pathcss) 
	{
		this.pathcss = pathcss;
	}

	public String getPathjs() 
	{
		return pathjs;
	}

	public void setPathjs(String pathjs) 
	{
		this.pathjs = pathjs;
	}

	public String getPathxsl() 
	{
		return pathxsl;
	}

	public void setPathxsl(String pathxsl) 
	{
		this.pathxsl = pathxsl;
	}	
	
	public boolean isOld()
	{
		if (pathxsl != null && !"".equals(pathxsl))
			return true;
		else
			return false;
	}

	public int getId() 
	{
		return id;
	}

	public void setId(int id) 
	{
		this.id = id;
	}
}