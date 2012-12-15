package com.founder.e5.sys.org;

import java.io.Serializable;


/**
 * @version 1.0
 * @created 08-七月-2005 15:11:18
 */
public class UserFolder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int hashCode = Integer.MIN_VALUE;
	/**
	 * 用户ID
	 */
	private int userID;
	/**
	 * 文档类型ID
	 */
	private int docTypeID;
	/**
	 * 文件夹ID
	 */
	private int folderID;
	/**
	 * 库ID
	 */
	private int libID;

	
	public UserFolder(){

	}

    /**
     * @return 返回 docTypeID。
     */
    public int getDocTypeID()
    {
        return docTypeID;
    }
    /**
     * @return 返回 folderID。
     */
    public int getFolderID()
    {
        return folderID;
    }
    /**
     * @return 返回 libID。
     */
    public int getLibID()
    {
        return libID;
    }
    /**
     * @return 返回 userID。
     */
    public int getUserID()
    {
        return userID;
    }
    /**
     * @param docTypeID 要设置的 docTypeID。
     */
    public void setDocTypeID(int docTypeID)
    {
        this.docTypeID = docTypeID;
    }
    /**
     * @param folderID 要设置的 folderID。
     */
    public void setFolderID(int folderID)
    {
        this.folderID = folderID;
    }
    /**
     * @param libID 要设置的 libID。
     */
    public void setLibID(int libID)
    {
        this.libID = libID;
    }
    /**
     * @param userID 要设置的 userID。
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