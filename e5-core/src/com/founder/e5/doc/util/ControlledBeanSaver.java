/*
 * Created on 2005-7-11 12:56:19
 *
 */
package com.founder.e5.doc.util;

/**
 * @author liyanhui
 * @version 1.0
 * @created 2005-7-11 12:56:19
 */
public abstract class ControlledBeanSaver {
    
    /**
     * ±£¥Ê µÃÂbean
     * @param bean
     */
    public void save(ControlledBean bean) {
        switch (bean.getStatus()) {
            case ControlledBean.STATUS_TRANSIENT:{
                insert(bean);
                bean.setStatus(ControlledBean.STATUS_PERSISTENT);
                break;
            }
            case ControlledBean.STATUS_PERSISTENT: {
                if (bean.isDirty()) {
                    update(bean, bean.getDirtyFields());
                    bean.clearDirtyFields();
                    bean.setDirty(false);
                }
                break;
            }

            default:
                break;
        }
    }
    
    protected abstract void insert(ControlledBean bean);
    
    protected abstract void update(ControlledBean bean, String[] fields);

}
