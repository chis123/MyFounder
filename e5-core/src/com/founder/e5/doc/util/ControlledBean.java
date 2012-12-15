/*
 * Created on 2005-7-11 12:51:19
 *
 */
package com.founder.e5.doc.util;

/**
 * @author liyanhui
 * @version 1.0
 * @created 2005-7-11 12:51:19
 */
public interface ControlledBean {
    
    int STATUS_TRANSIENT = 0;		//¡Ÿ ±Ã¨
    int STATUS_PERSISTENT = 1;		//≥÷æ√Ã¨
    
    int getStatus();
    void setStatus(int status);
    
    boolean isDirty();
    void setDirty(boolean dirty);
    
    String[] getDirtyFields();
    void clearDirtyFields();

}
