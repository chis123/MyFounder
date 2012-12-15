package com.founder.e5.flow;

import java.util.Observable;

/**
 * 流程操作的基础类<br/>
 * ProcFlow/ProcUnflow都从这个类继承<br/>
 * 
 * @created 2005-8-4
 * @updated 2006-8-1
 * @author Gong Lijie
 * @version 2.0
 */
public class Proc 
extends Observable
//该类继承了Observable，用途是当操作有增、删、改动作时，通知排序操作进行相应修改
{
	public static final int PROC_DO = 1;
	public static final int PROC_GO = 2;
	public static final int PROC_BACK = 3;
	public static final int PROC_JUMP = 4;
	public static final int PROC_UNFLOW = 5;

	/**
	 * 操作类型：DO/GO/BACK/JUMP/UNFLOW
	 */
	protected int procType;
	protected int procID;
	protected String procName;
	protected int opID;
	protected int iconID;
	protected String description;

	public String toString () {
		return (new StringBuffer()
				.append("[procType:").append(procType)
				.append(",procID:").append(procID)
				.append(",procName:").append(procName)
				.append(",opID:").append(opID)
				.append(",iconID:").append(iconID)
				.append(",description:").append(description)
				.append("]")
				).toString();
	}
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
	 * @return Returns the iconID.
	 */
	public int getIconID()
	{
		return iconID;
	}
	/**
	 * @param iconID The iconID to set.
	 */
	public void setIconID(int iconID)
	{
		this.iconID = iconID;
	}
	/**
	 * @return Returns the opID.
	 */
	public int getOpID()
	{
		return opID;
	}
	/**
	 * @param opID The opID to set.
	 */
	public void setOpID(int opID)
	{
		this.opID = opID;
	}
	/**
	 * @return Returns the procID.
	 */
	public int getProcID()
	{
		return procID;
	}
	
	//因为ProcOrder继承Proc类，所以无法把setProcID方法设置只包内可见
	/**
	 * @param procID The procID to set.
	 */
	public void setProcID(int procID)
	{
		this.procID = procID;
	}
	/**
	 * @return Returns the procName.
	 */
	public String getProcName()
	{
		return procName;
	}
	/**
	 * @param procName The procName to set.
	 */
	public void setProcName(String procName)
	{
		this.procName = procName;
	}
	/**
	 * @return Returns the procType.
	 */
	public int getProcType()
	{
		return procType;
	}
	/**
	 * @param procType The procType to set.
	 */
	public void setProcType(int procType)
	{
		this.procType = procType;
	}
	/* (non-Javadoc)
	 * @see java.util.Observable#notifyObservers(java.lang.Object)
	 */
	public void notifyObservers(Object arg)
	{
		/**
		 * 当要通知时才注册Observer，而不是在构造方法中注册，
		 * 避免一般性操作中的注册行为
		 * 这里ProcOrderObserver必须是单例模式，
		 * 否则多次调用时会变成多个Observer，从而多次调用，导致出错
		 */
		addObserver(ProcOrderObserver.getInstance());
		setChanged();
		super.notifyObservers(arg);
	}
}