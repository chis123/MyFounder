package com.founder.e5.dom.template;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import com.founder.e5.commons.FileUtils;
import com.founder.e5.context.BaseField;
import com.founder.e5.db.ColumnInfo;
import com.founder.e5.db.DBType;

/**
 * 触发器模板的管理器抽象类
 * <br/>用于创建分类关联表和文档库的对应时
 * <br/>为了以后可以扩展对数据库类型的支持，各自提取成单独的类。
 * <br/>因为需要访问平台定义的系统字段，因此不能放在e5db包中
 * @author Gong Lijie
 * @created 2008-01-30
 */
public abstract class TriggerTemplateManager
{
	protected String templateRoot = "e5template";
    protected String templatePathPrefixIgnore = "/trigger_ignore.";
    protected String templatePathPrefixZero = "/trigger_zero.";
    protected String dbType = "";
    
    protected static HashMap managers = new HashMap(4);
    static
    {
    	managers.put(DBType.ORACLE, OracleTriggerTemplateMgr.class);
    	managers.put(DBType.MYSQL, MySqlTriggerTemplateMgr.class);
    	managers.put(DBType.SQLSERVER, SqlServerTriggerTemplateMgr.class);
    	managers.put(DBType.SYBASE, SybaseTriggerTemplateManager.class);
    	managers.put(DBType.POSTGRESQL, PostgreSQLTriggerTemplateMgr.class);
    }
    
	/**
     * 获取指定数据库类型的触发器模板管理器
     * @param dbType
     * @return
     */
    public static TriggerTemplateManager getTemplateManager(String dbType) 
    {
    	Class managerClass = (Class)managers.get(dbType);
    	try {
			if (managerClass != null)
				return (TriggerTemplateManager)managerClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	/*
        if(dbType.equals(DBType.ORACLE))
            return new OracleTriggerTemplateMgr();
        
        if(dbType.equals(DBType.MYSQL))
            return new MySqlTriggerTemplateMgr();
        
        if(dbType.equals(DBType.SQLSERVER))
            return new SqlServerTriggerTemplateMgr();

        if(dbType.equals(DBType.SYBASE))
            return new SybaseTriggerTemplateMgr();
        */

        return null;
    }
    
    /**
     * 注册新的数据库类型的模板管理器
     * @param dbType
     * @param managerClass
     */
    public static void register(String dbType, Class managerClass)
    {
    	managers.put(dbType, managerClass);
    }

    /**
     * 获取触发器内容
     * 2006-3-30 11:17:02 by Zhang Kaifeng
     * @param docLibId 
     * @param suffix 
     * @param categoryField 
     * @param fields
     * @param ignoreFlag 
     * @return
     * @throws Exception 
     */
    public String getTriggerContent(int docLibId, String suffix, String categoryField, 
    		String[] fields,int ignoreFlag) 
    throws Exception 
    {
        String triggerFilePath = this.getTriggerTemplatePath(ignoreFlag);
        String triggerTemplate = null;
        try{
            triggerTemplate = FileUtils.readClassPathFile(triggerFilePath);
        } catch (IOException e){
        	e.printStackTrace();
            return triggerTemplate;
        }
        
        triggerTemplate = StringUtils.replace(triggerTemplate,"%1",String.valueOf(docLibId));
        triggerTemplate = StringUtils.replace(triggerTemplate,"%2",suffix);
        triggerTemplate = StringUtils.replace(triggerTemplate,"%3",categoryField);
        
        ColumnInfo[] sysFields = getSysFields();
        StringBuffer temp = new StringBuffer();
        for (int i = 0; i < sysFields.length; i++) {
            temp.append(sysFields[i].getName()).append(",");
        }
        
        if(fields[0].length() > 0)  temp.append(fields[0]);
        else if(temp.length()>0) temp.deleteCharAt(temp.length()-1);
        
        triggerTemplate = StringUtils.replace(triggerTemplate,"%4",temp.toString());
        triggerTemplate = this.replaceDocLibFields(triggerTemplate,fields);
        
        return triggerTemplate;
    }

    /**
     * 取所有的系统平台字段
     * @return
     * @throws Exception
     */
    protected ColumnInfo[] getSysFields() throws Exception
    {
        Field[] sysFields = BaseField.class.getFields();
        if (sysFields == null) return null;
        
        ColumnInfo[] columns = new ColumnInfo[sysFields.length];
        for (int i = 0; i < sysFields.length; i++) {
            Field f = sysFields[i];
            ColumnInfo ci = (ColumnInfo) f.get(f);
            columns[i] = ci;
        }
    	return columns;
    }
    
    /**
     * 把模板中的字段参数替换成实际的库字段名
     * @param triggerTemplate
     * @param fields
     * @return
     * @throws Exception
     */
    protected String replaceDocLibFields(String triggerTemplate,String[] fields) 
    throws Exception 
    {
        return null;
    }

    /**
     * 取得触发器模板文件的路径
     * @param ignoreFlag
     * @return
     */
    protected String getTriggerTemplatePath(int ignoreFlag) 
    {
        if(ignoreFlag == 1)
            return templateRoot + this.templatePathPrefixIgnore + this.dbType;
		return templateRoot + this.templatePathPrefixZero + this.dbType;
    }

    public void setTemplateRoot(String templateRoot)
    {
    	this.templateRoot = templateRoot;
    }
    /**
	 * @param dbType The dbType to set.
	 */
    protected void setDbType(String dbType) 
    {
        this.dbType = dbType;
        //设置数据库类型时自动注册
        //不一样有用
        register(dbType, this.getClass());
    }
    
    /**
     * 获取某文档库上针对某分类关联表的触发器名称。
     * 有的数据库类型下，触发器会是多个，因此返回值的类型为String[]
     * @param relTableName
     * @param docLibID
     * @return String[]
     */
    public String[] getTriggers(String relTableName, int docLibID)
    {
        String suffix = relTableName.substring(8);
        String triggerName = "AUTO_SYN_" + docLibID + "_" + suffix;
        return new String[]{triggerName};
    }
}
