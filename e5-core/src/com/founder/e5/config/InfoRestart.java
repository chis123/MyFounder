package com.founder.e5.config;

/**
 * <p>�������ļ���Ӧ��һ������ʱ������</p>
 * <p>ϵͳ����ʱ���Ե���������ķ������г�ʼ��</p>
 * @author Gong Lijie
 * @version 1.0
 */
public class InfoRestart
{
    private String invokeClass;
    private String invokeMethod;
    public String getInvokeClass()
    {
        return invokeClass;
    }
    public void setInvokeClass(String invokeClass)
    {
        this.invokeClass = invokeClass;
    }
    public String getInvokeMethod()
    {
        return invokeMethod;
    }
    public void setInvokeMethod(String invokeMethod)
    {
        this.invokeMethod = invokeMethod;
    }
    public String toString(){
		return (new StringBuffer(100)
				.append("[invokeClass:").append(invokeClass)
				.append(",invokeMethod:").append(invokeMethod)
				.append("]")
				).toString();
	}
}
