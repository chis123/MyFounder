package com.founder.e5.dom.template;

import org.apache.commons.lang.StringUtils;

import com.founder.e5.db.ColumnInfo;
import com.founder.e5.db.DBType;

/**
 * Sybase类型的触发器模板管理器
 * <br/>用于创建分类关联表和文档库的对应时
 * @created 2008-1-30
 * @author Gong Lijie
 * @version 1.0
 */
public class SybaseTriggerTemplateManager extends TriggerTemplateManager{
    public SybaseTriggerTemplateManager() {
        this.dbType = DBType.SYBASE;
    }

    protected String replaceDocLibFields(String triggerTemplate, String[] fields) throws Exception 
    {
    	ColumnInfo[] sysFields = getSysFields();
        
        String[] doclibFields = fields[1].split(",");
        StringBuffer temp = new StringBuffer();
        for (int i = 0; i < sysFields.length; i++) {
            temp.append(sysFields[i].getName()).append(",");
        }
        for( int i = 0; i < doclibFields.length; i++){
            if(doclibFields[i].length() > 0)
                temp.append(doclibFields[i]).append(",");
        }
        if(temp.length()>0)
            temp.deleteCharAt(temp.length()-1);
        
        return triggerTemplate = StringUtils.replace(triggerTemplate,"%5",temp.toString());
    }
    
    public String[] getTriggers(String relTableName, int docLibID)
    {
    	String[] names = super.getTriggers(relTableName, docLibID);
    	
        return new String[]{names[0] + "_insert", names[0] + "_update", names[0] + "_delete" };
    }
}
