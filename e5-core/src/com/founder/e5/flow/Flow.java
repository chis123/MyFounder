package com.founder.e5.flow;

/**
 * Á÷³ÌÀà
 * @created 04-8-2005 14:15:37
 * @author Gong Lijie
 * @version 1.0
 */
public class Flow {

	private int ID;
	private String name;
	private int docTypeID;
	private int firstFlowNodeID;
	private String description;

	/**
	 * @return Returns the description.
	 */
	public String getDescription()
	{
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	/**
	 * @return Returns the docTypeID.
	 */
	public int getDocTypeID()
	{
		return docTypeID;
	}
	/**
	 * @param docTypeID The docTypeID to set.
	 */
	void setDocTypeID(int docTypeID)
	{
		this.docTypeID = docTypeID;
	}
	/**
	 * @return Returns the firstFlowNodeID.
	 */
	public int getFirstFlowNodeID()
	{
		return firstFlowNodeID;
	}
	/**
	 * @param firstFlowNodeID The firstFlowNodeID to set.
	 */
	void setFirstFlowNodeID(int firstFlowNodeID)
	{
		this.firstFlowNodeID = firstFlowNodeID;
	}
	/**
	 * @return Returns the iD.
	 */
	public int getID()
	{
		return ID;
	}
	/**
	 * @param id The iD to set.
	 */
	void setID(int id)
	{
		ID = id;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}
}