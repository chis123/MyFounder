package com.founder.e5.sys.org;

/**
 * @version 1.0
 * @created 08-����-2005 15:08:26
 */
public class OrgType {

	/**
	 * ��֯����ID
	 */
	private String id;
	/**
	 * ��֯��������
	 */
	private String name;

	public OrgType(){

	}

	

    /**
     * @return ���� id��
     */
    public String getId()
    {
        return id;
    }
    /**
     * @return ���� name��
     */
    public String getName()
    {
        return name;
    }
    /**
     * @param id Ҫ���õ� id��
     */
    public void setId(String id)
    {
        this.id = id;
    }
    /**
     * @param name Ҫ���õ� name��
     */
    public void setName(String name)
    {
        this.name = name;
    }
}