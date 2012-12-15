/*
 * Created on 2005-6-23 9:33:45
 *
 */
package com.founder.e5.doc;

import java.lang.reflect.Field;

/**
 * 文档关联对象，代表一条文档关联关系
 * @author liyanhui
 * @version 1.0
 * @created 2005-6-23 9:33:45
 */
public class DocAssociation {
    
    private int associationID;
    private int rootLibID;
    private long rootID;
    private int srcLibID;
    private long srcID;
    private int destLibID;
    private long destID;    
    private int associationCode;
    private int order;

    /**
     * 
     */
    public DocAssociation() {
        super();
    }
    
    /**
     * @return Returns the associationID.
     */
    public int getAssociationID() {
        return associationID;
    }
    /**
     * @param associationID The associationID to set.
     */
    public void setAssociationID(int associationID) {
        this.associationID = associationID;
    }
    /**
     * @return Returns the associationCode.
     */
    public int getAssociationCode() {
        return associationCode;
    }
    /**
     * @param associationType The associationCode to set.
     */
    public void setAssociationCode(int associationType) {
        this.associationCode = associationType;
    }
    /**
     * @return Returns the destID.
     */
    public long getDestID() {
        return destID;
    }
    /**
     * @param destID The destID to set.
     */
    public void setDestID(long destID) {
        this.destID = destID;
    }
    /**
     * @return Returns the destLibID.
     */
    public int getDestLibID() {
        return destLibID;
    }
    /**
     * @param destLibID The destLibID to set.
     */
    public void setDestLibID(int destLibID) {
        this.destLibID = destLibID;
    }
    /**
     * @return Returns the order.
     */
    public int getOrder() {
        return order;
    }
    /**
     * @param order The order to set.
     */
    public void setOrder(int order) {
        this.order = order;
    }
    /**
     * @return Returns the rootID.
     */
    public long getRootID() {
        return rootID;
    }
    /**
     * @param rootID The rootID to set.
     */
    public void setRootID(long rootID) {
        this.rootID = rootID;
    }
    /**
     * @return Returns the rootLibID.
     */
    public int getRootLibID() {
        return rootLibID;
    }
    /**
     * @param rootLibID The rootLibID to set.
     */
    public void setRootLibID(int rootLibID) {
        this.rootLibID = rootLibID;
    }
    /**
     * @return Returns the srcID.
     */
    public long getSrcID() {
        return srcID;
    }
    /**
     * @param srcID The srcID to set.
     */
    public void setSrcID(long srcID) {
        this.srcID = srcID;
    }
    /**
     * @return Returns the srcLibID.
     */
    public int getSrcLibID() {
        return srcLibID;
    }
    /**
     * @param srcLibID The srcLibID to set.
     */
    public void setSrcLibID(int srcLibID) {
        this.srcLibID = srcLibID;
    }

	public boolean equals(Object obj) {
		if (obj instanceof DocAssociation) {
			return (associationID == ((DocAssociation) obj).associationID);
		}
		return false;
	}

	public int hashCode() {
		return associationID;
	}
	public String toString() {
        StringBuffer bf = new StringBuffer();
        Field[] fields = this.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            Object value = null;
            try {
                value = field.get(this);
            } catch (Exception e) {
            }
            bf.append(field.getName()).append("=").append(value).append("\n");
        }
        return bf.toString();
    }
}
