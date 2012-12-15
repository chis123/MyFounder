package com.founder.e5.config;

/**
 * <p>与配置文件对应的一个启动时运行项</p>
 * <p>系统启动时可以调用若干类的方法进行初始化</p>
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
