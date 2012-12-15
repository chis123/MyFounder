package com.founder.e5.dom.template;

import org.apache.commons.lang.StringUtils;

import com.founder.e5.db.ColumnInfo;
import com.founder.e5.db.DBType;

/**
 * Oracle类型的触发器模板管理器
 * <br/>用于创建分类关联表和文档库的对应时
 * @created 2008-1-30
 * @author Gong Lijie
 * @version 1.0
 */
public class OracleTriggerTemplateMgr extends TriggerTemplateManager{       
    public OracleTriggerTemplateMgr() {
       this.dbType = DBType.ORACLE;
    }

    protected String replaceDocLibFields(String triggerTemplate, String[] fields) 
    throws Exception {
    	ColumnInfo[] sysFields = getSysFields();

        //fields[1]是文档库的字段，之间用，分割的            
        String[] doclibFields = fields[1].split(",");
        
        //6%
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < sysFields.length; i++) {
            buf.append("\"").append(sysFields[i].getName()).append("\"").append(",");
        }
        for( int i = 0; i < doclibFields.length; i++){
            if(doclibFields[i].length() >0)
                buf.append("\"").append(doclibFields[i]).append("\"").append(",");
        }
        if(buf.length() > 0)
            buf.deleteCharAt(buf.length()-1);
        
        triggerTemplate = StringUtils.replace(triggerTemplate,"%6",buf.toString());
        
        //5%
        StringBuffer temp = new StringBuffer();
        for (int i = 0; i < sysFields.length; i++) {
            temp.append(":New.").append(sysFields[i].getName()).append(",");
        }
        for( int i = 0; i < doclibFields.length; i++){
            if(doclibFields[i].length() > 0)
                temp.append(":New.").append(doclibFields[i]).append(",");
        }
        if(temp.length()>0)
            temp.deleteCharAt(temp.length()-1);
        
        return triggerTemplate = StringUtils.replace(triggerTemplate,"%5",temp.toString());
    }
}
