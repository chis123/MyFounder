package com.founder.e5.flow;

import java.util.Observable;

/**
 * ���̲����Ļ�����<br/>
 * ProcFlow/ProcUnflow���������̳�<br/>
 * 
 * @created 2005-8-4
 * @updated 2006-8-1
 * @author Gong Lijie
 * @version 2.0
 */
public class Proc 
extends Observable
//����̳���Observable����;�ǵ�����������ɾ���Ķ���ʱ��֪ͨ�������������Ӧ�޸�
{
	public static final int PROC_DO = 1;
	public static final int PROC_GO = 2;
	public static final int PROC_BACK = 3;
	public static final int PROC_JUMP = 4;
	public static final int PROC_UNFLOW = 5;

	/**
	 * �������ͣ�DO/GO/BACK/JUMP/UNFLOW
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
	
	//��ΪProcOrder�̳�Proc�࣬�����޷���setProcID��������ֻ���ڿɼ�
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
		 * ��Ҫ֪ͨʱ��ע��Observer���������ڹ��췽����ע�ᣬ
		 * ����һ���Բ����е�ע����Ϊ
		 * ����ProcOrderObserver�����ǵ���ģʽ��
		 * �����ε���ʱ���ɶ��Observer���Ӷ���ε��ã����³���
		 */
		addObserver(ProcOrderObserver.getInstance());
		setChanged();
		super.notifyObservers(arg);
	}
}