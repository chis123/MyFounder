package com.founder.e5.dom.template;

import org.apache.commons.lang.StringUtils;

import com.founder.e5.db.ColumnInfo;
import com.founder.e5.db.DBType;

/**
 * PostgreSQL 类型的触发器模板管理器
 * <br/>用于创建分类关联表和文档库的对应时
 * @author wanghc
 * @created 2009-3-19 下午02:39:19
 */
public class PostgreSQLTriggerTemplateMgr extends TriggerTemplateManager
{
	public PostgreSQLTriggerTemplateMgr(){
		this.dbType = DBType.POSTGRESQL;
	}
	
    protected String replaceDocLibFields(String triggerTemplate, String[] fields) 
    throws Exception {
    	ColumnInfo[] sysFields = getSysFields();

        //fields[1]是文档库的字段，之间用，分割的            
        String[] doclibFields = fields[1].split(",");
        
        //5%
        StringBuffer temp = new StringBuffer();
        for (int i = 0; i < sysFields.length; i++) {
            temp.append("New.").append(sysFields[i].getName()).append(",");
        }
        for( int i = 0; i < doclibFields.length; i++){
            if(doclibFields[i].length() > 0)
                temp.append("New.").append(doclibFields[i]).append(",");
        }
        if(temp.length()>0)
            temp.deleteCharAt(temp.length()-1);
        
        return triggerTemplate = StringUtils.replace(triggerTemplate,"%5",temp.toString());
    }
}
