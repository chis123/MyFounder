package com.founder.e5.sys.org;

import java.io.Serializable;


/**
 * @version 1.0
 * @created 08-����-2005 15:11:18
 */
public class UserFolder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int hashCode = Integer.MIN_VALUE;
	/**
	 * �û�ID
	 */
	private int userID;
	/**
	 * �ĵ�����ID
	 */
	private int docTypeID;
	/**
	 * �ļ���ID
	 */
	private int folderID;
	/**
	 * ��ID
	 */
	private int libID;

	
	public UserFolder(){

	}

    /**
     * @return ���� docTypeID��
     */
    public int getDocTypeID()
    {
        return docTypeID;
    }
    /**
     * @return ���� folderID��
     */
    public int getFolderID()
    {
        return folderID;
    }
    /**
     * @return ���� libID��
     */
    public int getLibID()
    {
        return libID;
    }
    /**
     * @return ���� userID��
     */
    public int getUserID()
    {
        return userID;
    }
    /**
     * @param docTypeID Ҫ���õ� docTypeID��
     */
    public void setDocTypeID(int docTypeID)
    {
        this.docTypeID = docTypeID;
    }
    /**
     * @param folderID Ҫ���õ� folderID��
     */
    public void setFolderID(int folderID)
    {
        this.folderID = folderID;
    }
    /**
     * @param libID Ҫ���õ� libID��
     */
    public void setLibID(int libID)
    {
        this.libID = libID;
    }
    /**
     * @param userID Ҫ���õ� userID��
     */
    public void setUserID(int userID)
    {
        this.userID = userID;
    }
    
    public boolean equals (Object obj) 
    {
        if(obj == null) return false;
        if(! (obj instanceof UserFolder))
            return false;
        else
        {
            UserFolder userFolder = (UserFolder)obj;
            if(userFolder.getUserID() == this.userID && userFolder.getDocTypeID() == this.docTypeID)
                return true;
        }
        return false;
    }
    
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		if (Integer.MIN_VALUE == this.hashCode) 
		{
			StringBuffer sb = new StringBuffer(100);
			sb.append(userID).append(':').append(docTypeID);
			this.hashCode = sb.toString().hashCode();
		}
		return this.hashCode;
	}
}