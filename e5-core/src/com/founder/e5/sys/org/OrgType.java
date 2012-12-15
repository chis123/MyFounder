package com.founder.e5.sys.org;

/**
 * @version 1.0
 * @created 08-七月-2005 15:08:26
 */
public class OrgType {

	/**
	 * 组织类型ID
	 */
	private String id;
	/**
	 * 组织类型名称
	 */
	private String name;

	public OrgType(){

	}

	

    /**
     * @return 返回 id。
     */
    public String getId()
    {
        return id;
    }
    /**
     * @return 返回 name。
     */
    public String getName()
    {
        return name;
    }
    /**
     * @param id 要设置的 id。
     */
    public void setId(String id)
    {
        this.id = id;
    }
    /**
     * @param name 要设置的 name。
     */
    public void setName(String name)
    {
        this.name = name;
    }
}