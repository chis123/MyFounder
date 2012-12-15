package com.founder.e5.job;


import java.util.List;

import com.founder.e5.context.E5Exception;
import com.founder.e5.scheduler.BaseJob;

/**
 * 某些情况下，一个系统会需要在后台一直运行某个服务程序。<br/>
 * 该服务程序每隔一段时间重复执行一次，通过检查某个表获得它要执行的任务。<br/>
 * 每个需执行的任务作为表的一条记录而存在。<br/>
 * 当服务执行任务时，先把表中的任务做一个在执行标记，执行完后删除该记录。<br/>
 * 若任务执行不成功，则把表中的任务做不成功标记。<br/>
 * <br/>
 * job包就是为了这种情况而做的抽象。<br/>
 * <br/>
 * 
 * BaseService是符合E5系统的任务调度接口的服务基础类。<br/>
 * 
 * 它继承了BaseJob，调用BaseManager进行实际的操作。<br/>
 * 若无特殊需要，子类只要继承本基类即可，无需进行任何方法实现，除了给manager变量赋值。<br/>
 * 它使用manager的日志进行记录。<br/>
 * @created 2006-7-27
 * @author Gong Lijie
 * @version 1.0
 */
public abstract class BaseService extends BaseJob
{
	protected BaseManager manager;
	
	protected void execute() throws E5Exception
	{
		List datas = manager.getAllData(BaseData.STATUS_WAITING);		
        if (datas == null) return;

        for (int i = 0; i < datas.size(); i++)
        {
        	if (isInterrupt()) break;//若设置了中断标志，则立刻退出。
        	BaseData data = (BaseData)datas.get(i);
        	
            //设置在处理标志。若不成功，则可能是被其他服务修改了。不作处理
        	try {
        		manager.setStatus(data, BaseData.STATUS_DOING);
			} catch (Exception e) {
                continue;
			}
			//开始做具体的业务处理
        	try {
        		manager.handleData(data);
			} catch (Exception e) {
				//使用manager的日志进行记录
				manager.getLog().error("[Service Running]", e);
                try {
                	//处理异常时设置失败标志
                	manager.setStatus(data, BaseData.STATUS_FAILED);
				} catch (Exception e1) {
				}
				continue;
			}
			//处理完毕后删除任务表中的记录
            try {
            	manager.delete(data.getDocID());
			} catch (Exception e1) {
			}
        }
	}
}
