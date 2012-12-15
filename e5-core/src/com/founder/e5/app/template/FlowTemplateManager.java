package com.founder.e5.app.template;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.hibernate.Session;

import com.founder.e5.commons.Log;
import com.founder.e5.commons.StringUtils;
import com.founder.e5.context.Context;
import com.founder.e5.context.E5Exception;
import com.founder.e5.dom.DocType;
import com.founder.e5.dom.DocTypeManager;
import com.founder.e5.flow.Flow;
import com.founder.e5.flow.FlowManager;
import com.founder.e5.flow.FlowNode;
import com.founder.e5.flow.Icon;
import com.founder.e5.flow.Operation;
import com.founder.e5.flow.Proc;
import com.founder.e5.flow.ProcFlow;
import com.founder.e5.flow.ProcManager;
import com.founder.e5.flow.ProcUnflow;

/**
 * @author slim
 * @version 1.0
 * @created 11-七月-2005 09:47:48
 */
public class FlowTemplateManager implements IAppTemplateManager {
	
	private Log log;
	private FlowManager fm;
	private ProcManager pm;
	private DocTypeManager dtm;

	public FlowTemplateManager() {
		fm = (FlowManager)Context.getBean("FlowManager");
		pm = (ProcManager)Context.getBean("ProcManager");
		dtm = (DocTypeManager)Context.getBean("DocTypeManager");
		log = Context.getLog("e5.sys");
	}

	void setSession(Session session) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.founder.e5.app.template.IAppTemplateManager#load(int,
	 *      java.lang.String)
	 */
	public int load(int appID, String templateFile) throws E5Exception {
		SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			doc = reader.read(new File(templateFile));
			return load(0, doc);
		} catch (DocumentException e) {
			throw new E5Exception("Invalid Template File", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.founder.e5.app.template.IAppTemplateManager#load(int,
	 *      org.dom4j.Document)
	 */
	public int load(int appID, Document doc) throws E5Exception {
		boolean iconImport = true;
		boolean opImport = true;
		boolean flowImport = true;

		try{
			Node icons = doc.selectSingleNode("//ICONS");
			if(icons!=null) iconImport = importIcon(icons);
			
			Node ops = doc.selectSingleNode("//OPERATIONS");
			if(ops!=null) opImport = importOp(ops);
			
			Node flows = doc.selectSingleNode("//WORKFLOWS");
			if(flows!=null) flowImport = importFlow(flows);

			//增加操作组/操作排序的模板加载
			FlowProcOrder orderTemplate = new FlowProcOrder();
			//操作排序加载
			Node node = doc.selectSingleNode("//PROCORDERS");
			if (node != null) orderTemplate.importProcOrders(node);
			//操作组加载
			node = doc.selectSingleNode("//PROCGROUPS");
			if (node != null) orderTemplate.importProcGroups(node);
			
		}catch(Exception ex){
			log.error("[FlowTemplate.Import]", ex);
			flowImport = false;
		}
		
		if (iconImport && opImport && flowImport) return 0;
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.founder.e5.app.template.IAppTemplateManager#exportTemplate(int)
	 */
	public String exportTemplate(int appID) {
		DocType[] docTypes = null;
		// 首先获取DocTypes
		try {
			docTypes = dtm.getTypes(appID);
		} catch (Exception ex) {
			log.error("[Flow Export]Exception when get doctypes!", ex);
			docTypes = null;
		}
		if (docTypes == null)
			return "<WORKFLOWTEMPLATE></WORKFLOWTEMPLATE>";

		StringBuffer flowsb = new StringBuffer("<WORKFLOWS>");
		StringBuffer opsb = new StringBuffer("<OPERATIONS>");
		for (int i = 0; i < docTypes.length; i++) 
		{
			opsb.append(this.exportOperations(docTypes[i]));
			flowsb.append(this.exportFlows(docTypes[i]));
		}
		opsb.append("</OPERATIONS>");
		flowsb.append("</WORKFLOWS>");

		String iconStr = this.exportIcons();
		
		//增加操作组/操作排序的导出
		FlowProcOrder orderTemplate = new FlowProcOrder();
		String procGroups = orderTemplate.exportProcGroups(appID);
		String procOrders = orderTemplate.exportProcOrders(appID);
		
		StringBuffer sb = new StringBuffer("<WORKFLOWTEMPLATE>");
		sb.append(iconStr).append(opsb).append(flowsb)
			//增加操作组/操作排序的导出
			.append(procOrders).append(procGroups)
			.append("</WORKFLOWTEMPLATE>");
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.founder.e5.app.template.IAppTemplateManager#unload(int)
	 */
	public void unload(int appID) {
		DocType[] docTypes = null;
		try{
			docTypes = dtm.getTypes(appID);
			for(int i=0;i<docTypes.length;i++)
			{
				try{
					fm.deleteDocType(docTypes[i].getDocTypeID());
					pm.deleteUnflows(docTypes[i].getDocTypeID());
					pm.deleteOperations(docTypes[i].getDocTypeID());
				}catch(Exception ex){
					log.error("[unload] Error when unload doctype:" + docTypes[i].getDocTypeID(),ex);
				}
			}
		}catch(Exception ex){
			log.error("[unload] Exception:",ex);
		}
	}
	/**
	 * 生成Icon部分的输出xml
	 * 
	 * @return
	 */
	private String exportIcons() {
		Icon[] icons = null;
		StringBuffer sb = new StringBuffer("");
		sb.append("<ICONS>");
		try {
			icons = pm.getIcons();
			if (icons != null)
				for (int i = 0; i < icons.length; i++) 
					sb.append("<ICON FILENAME=\"").append(parseStr(icons[i].getFileName()))
						.append("\" DESCRIPTION=\"").append(parseStr(icons[i].getDescription()))
						.append("\" FORMAT=\"").append(parseStr(icons[i].getFormat()))
						.append("\" SIZE=\"").append(parseStr(icons[i].getSize()))
						.append("\" />");
		} catch (Exception ex) {
			log.error("Export icons exception!", ex);
		}
		sb.append("</ICONS>");
		return sb.toString();
	}

	/**
	 * 组成操作部分
	 * @param docType
	 * @return
	 */
	private String exportOperations(DocType docType) {
		StringBuffer sb = new StringBuffer();
		sb.append("<DOCTYPE NAME=\"").append(docType.getDocTypeName())
			.append("\">");

		Operation[] ops = null;
		try {
			ops = pm.getOperations(docType.getDocTypeID());
			if (ops != null)
			for (int i = 0; i < ops.length; i++) {
				int var = ops[i].getShowType();
				int tt = var % 2;
				int tb = var / 2;
				if (tb >= 1) tb = 1;
				
				sb.append("<OPERATION><NAME>").append(parseStr(ops[i].getName())).append("</NAME>");
				sb.append("<DESCRIPTION>").append(parseStr(ops[i].getDescription())).append("</DESCRIPTION>");
				sb.append("<ID>").append(ops[i].getID()).append("</ID>");
				sb.append("<URL>").append(StringUtils.encode(parseStr(ops[i].getCodeURL()))).append("</URL>");
				sb.append("<CALLMODE>").append(ops[i].getCallMode()).append("</CALLMODE>");
				sb.append("<NEEDREFRESH>").append(parseInt(ops[i].isNeedRefresh())).append("</NEEDREFRESH>");
				sb.append("<NEEDLOCK>").append(parseInt(ops[i].isNeedLock())).append("</NEEDLOCK>");
				sb.append("<MULTIDOC>").append(ops[i].getDealCount() - 1).append("</MULTIDOC>");
				sb.append("<NEEDLOG>").append(parseInt(ops[i].isNeedLog())).append("</NEEDLOG>");
				sb.append("<SIZE WIDTH=\"").append(ops[i].getWidth())
					.append("\" HEIGHT=\"").append(ops[i].getHeight())
					.append("\" RESIZABLE=\"").append(parseInt(ops[i].isResizable()))
					.append("\" />");
				sb.append("<SHOWINTOOLBAR>").append(tt).append("</SHOWINTOOLBAR>")
					.append("<SHOWINPOPMENU>").append(tb).append("</SHOWINPOPMENU>");
				sb.append("<NEEDPROMPT>").append(parseInt(ops[i].isNeedPrompt())).append("</NEEDPROMPT>");
				sb.append("</OPERATION>");
			}
		} catch (Exception ex) {
			log.error("Export operations exception!", ex);
		}
		sb.append("</DOCTYPE>");
		return sb.toString();
	}
	//一个流程操作的输出
	private void exportProc(Proc proc, StringBuffer sb)
	{
		if (proc == null) return;
		
		String opName = "";
		String iconName = "";
		try{
			Operation op = pm.getOperation(proc.getOpID());
			if (op != null) opName = op.getName();
			
			Icon icon = pm.getIcon(proc.getIconID());
			if (icon != null) iconName = icon.getFileName();
		} catch (Exception ex){
			log.error("[ExportProc]Get operation or icon info error!",ex);
		}
		switch (proc.getProcType())
		{
			case Proc.PROC_DO: sb.append("<DO"); break;
			case Proc.PROC_GO: sb.append("<GO"); break;
			case Proc.PROC_BACK: sb.append("<BACK"); break;
			case Proc.PROC_JUMP: sb.append("<JUMP"); break;
			case Proc.PROC_UNFLOW: sb.append("<UNFLOW"); break;
		}
		sb.append(" NAME=\"").append(parseStr(proc.getProcName()))
			.append("\" OPERATIONNAME=\"").append(opName)
			.append("\" ICON=\"").append(parseStr(iconName));
		//如是跳转，需加输出信息
		if (proc.getProcType() == Proc.PROC_JUMP) {
			String flowName = "";
			String nodeName = "";
			try{
				Flow flow = fm.getFlow(((ProcFlow)proc).getNextFlowID());
				if (flow != null) flowName = flow.getName();
				
				FlowNode node = fm.getFlowNode(((ProcFlow)proc).getNextFlowNodeID());
				if (node != null) nodeName = node.getName();
			}catch(Exception ex){
				log.error("[ExportProc]Get flow info for jump error!",ex);
			}
			sb.append("\" JUMPTOFLOW=\"").append(parseStr(flowName))
				.append("\" JUMPTOFLOWNODE=\"").append(parseStr(nodeName));
		}
		sb.append("\"/>");
		return;
	}
	//输出流程节点
	private void exportNode(FlowNode node, StringBuffer sb) throws E5Exception
	{
		sb.append("<NODE NAME=\"").append(parseStr(node.getName()))
			.append("\" DESCRIPTION=\"").append(parseStr(node.getDescription()))
			.append("\" WAITINGNAME=\"").append(parseStr(node.getWaitingStatus()))
			.append("\" DOINGNAME=\"").append(parseStr(node.getDoingStatus()))
			.append("\" DONENAME=\"").append(parseStr(node.getDoneStatus()))
			.append("\" >");
		exportProc(pm.getDo(node.getID()), sb);
		exportProc(pm.getGo(node.getID()), sb);
		exportProc(pm.getBack(node.getID()), sb);
		
		ProcFlow[] jumpProcs = pm.getJumps(node.getID());
		if (jumpProcs != null)
		{
			for(int m = 0; m < jumpProcs.length;m++)
				exportProc(jumpProcs[m], sb);
		}
		sb.append("</NODE>");
	}
	//一个流程导出
	private void exportFlow(Flow flow, StringBuffer sb, boolean isDefault)
	throws E5Exception
	{
		if(isDefault) sb.append("<DEFAULTFLOW");
		else sb.append("<FLOW");
		
		sb.append(" NAME=\"").append(parseStr(flow.getName()))
				.append("\" DESCRIPTION=\"").append(parseStr(flow.getDescription())).append("\">");

		FlowNode[] nodes = fm.getFlowNodes(flow.getID());
		if (nodes != null)
			for (int n = 0; n < nodes.length; n++) 
				exportNode(nodes[n], sb);
		if (isDefault)
			sb.append("</DEFAULTFLOW>");
		else
			sb.append("</FLOW>");
	}
	/**
	 * 组成Flow部分的输出
	 * @param docType
	 * @return
	 */
	private String exportFlows(DocType docType) 
	{
		int defaultFlowID = docType.getDefaultFlow();
		StringBuffer sb = new StringBuffer();
		sb.append("<DOCTYPE NAME=\"").append(parseStr(docType.getDocTypeName()))
			.append("\">");

		Flow[] flows = null;
		try {
			flows = fm.getFlows(docType.getDocTypeID());
			if ( flows != null)
				for (int i = 0; i < flows.length; i++) 
					exportFlow(flows[i], sb, (defaultFlowID == flows[i].getID()));

			ProcUnflow[] unflows = pm.getUnflows(docType.getDocTypeID());
			if (unflows != null)
				for (int i = 0; i < unflows.length; i++)
					exportProc(unflows[i], sb);
		} catch (Exception ex) {
			log.error("Flow template export exception!", ex);
		}
		sb.append("</DOCTYPE>");
		return sb.toString();
	}

	/**
	 * 加载Flow部分
	 * 1、根据文档类型进行分类添加流程
	 * 2、再加入流程节点，同时添加节点上的操作
	 * 3、跳转操作由于其特殊性保存起来直到最后进行添加。
	 * 4、重名的流程和节点会影响到跳转的指向，所以需要被禁止
	 * @param doc
	 */
	private boolean importFlow(Node doc) 
	{
		//若模板中没有对应节点，则直接返回
		List docList = doc.selectNodes("//WORKFLOWS/DOCTYPE");
		if(docList == null)
		{
			log.info("[FlowTemplate.ImportFlow]: The template has no docType");
			return true;
		}
		
		boolean result = true;
		
		DocType docType;
		//jumpBoxs保存跳转操作的信息，稍后加载。因为跳转可能跨流程，所以不能当时加载
		ArrayList jumpBoxs = new ArrayList(); 
		ProcUnflow unflow = new ProcUnflow();
		
		//取出所有的图标，在加载流程操作时需要对应图标
		Icon[] icons = null;
		try{
			icons = pm.getIcons();
		}catch(Exception ex){
			log.error("[FlowTemplate.ImportFlow] First:get icon exception!", ex);
			result = false;
		}
		
		Iterator iter = docList.iterator();
		//按文档类型逐个加载其流程
		while(iter.hasNext())
		{
			jumpBoxs.clear();//跳转操作可跨流程但不能跨文档类型，所以不同文档类型时清空
			try
			{
				Node docNode = (Node)iter.next();
				if(docNode == null) continue;

				String docTypeName = getAttributeValue(docNode, "@NAME");
				docType = dtm.get(docTypeName);
				if (docType == null)
				{
					log.error("[FlowTemplate.ImportFlow]Cannot find the doctype by name:" + docTypeName);
					result = false;
					continue;
				}
				
				int docTypeID = docType.getDocTypeID();
				Operation[] operations = pm.getOperations(docTypeID);
				//缺省流程加载
				Node defaultFlowNode = docNode.selectSingleNode("descendant::DEFAULTFLOW");
				if(defaultFlowNode != null)
				{
					if (!importFlow(docTypeID, defaultFlowNode, 
							operations, icons, jumpBoxs, true))
						result = false;
					
				}
				//普通流程加载
				List flowList = docNode.selectNodes("descendant::FLOW");
				if (flowList == null) continue;
				
				Iterator flowIter = flowList.iterator();
				while(flowIter.hasNext())
				{
					if (!importFlow(docTypeID, (Node)flowIter.next(),  
							operations, icons, jumpBoxs, false))
						result = false;
				}
				//跳转操作的加载
				if (!importJumpNode(jumpBoxs,icons))
					result = false;

				//非流程操作的加载
				List unflowNodes = docNode.selectNodes("descendant::UNFLOW");
				if(unflowNodes != null)
				{
					Iterator unflowIter = unflowNodes.iterator();
					while(unflowIter.hasNext())
						try
						{
							fillProc(unflow, (Node)unflowIter.next(), operations, icons, docTypeID);
							pm.createUnflow(docTypeID, unflow);
						}catch(Exception ex){
							log.error("[FlowTemplate.ImportFlow]Add unflow operation error:", ex);
							result = false;
						}
				}
			}catch(E5Exception ex){
				log.error("[FlowTemplate.ImportFlow]Error when import flow",ex);
				result = false;
			}
		}
		return result;
	}
	/**
	 * 插入Icon部分
	 * @param doc
	 */
	private boolean importIcon(Node doc) 
	{
		List icons = doc.selectNodes("descendant::ICON");
		if (icons == null) return true;

		boolean result = true;
		
		List iconNameList = new ArrayList();
		try{
			Icon[] allIcons = pm.getIcons();
			if (allIcons!=null)
				for(int i=0;i<allIcons.length;i++)
					iconNameList.add(allIcons[i].getFileName());
		}catch(Exception ex){
			log.error("[FlowTemplate.ImportIcon]Get existed icons error.", ex);
			result = false;
		}
		Icon icon = new Icon();

		Iterator it = icons.iterator();
		while(it.hasNext())
		{
			Node node = (Node)it.next();
			Attribute attName = (Attribute)node.selectSingleNode("@FILENAME");
			String name = attName.getValue();
			//存在该图标
			if(iconNameList.contains(name)) continue;

			try{
				iconNameList.add(attName);
				icon.setDescription(getAttributeValue(node, "@DESCRIPTION"));
				icon.setFileName(name);
				icon.setFormat(getAttributeValue(node, "@FORMAT"));
				icon.setSize(getAttributeValue(node, "@SIZE"));
				pm.createIcon(icon);
			}catch(Exception ex){
				log.error("[FlowTemplate.ImportIcon]Create icon error.Icon filename:" + name,ex);
				result = false;
			}
		}
		return result;		
	}
	private String getAttributeValue(Node node, String name)
	{
		Attribute child = (Attribute)node.selectSingleNode(name);
		if (child != null)
			return child.getValue();
		else return null;
		
	}
	/**
	 * 加载Operation操作
	 * 重名操作被禁止使用
	 * @param doc
	 * @return boolean 表示加载过程中是否有错，有错时返回false
	 */
	private boolean importOp(Node doc) 
	{
		List docList = doc.selectNodes("//OPERATIONS/DOCTYPE");
		if (docList == null) return true;

		Operation[] allOps = null;
		List opList = null;
		List opNameList = new ArrayList(); //存放一个文档类型下已有的操作的名称
		Operation operation = new Operation();

		boolean result = true; //表示加载过程中是否有错，有错时返回false
		
		Iterator it = docList.iterator();
		while(it.hasNext())
		{
			Node docType = (Node)it.next();
			String docTypeName = ((Attribute)docType.selectSingleNode("@NAME")).getValue();

			opNameList.clear();
			//加载每个文档类型的操作
			try
			{
				opList = docType.selectNodes("descendant::OPERATION");
				//判断是否没有操作项
				if(opList == null) continue;

				int docTypeID;
				if ("".equals(docTypeName)) //独立功能条操作
					docTypeID = 0;
				else
				{
					//找出系统中文档类型下已有的操作，在加载过程中比较是否有同名操作
					docTypeID = dtm.get(docTypeName).getDocTypeID();
					try{
						allOps = pm.getOperations(docTypeID);
						if (allOps != null)
						{
							for (int i = 0;i < allOps.length; i++)
								opNameList.add(allOps[i].getName());
						}
					}catch(Exception ex){
						log.error("[FlowTemplate.ImportOperaton]Get doctype's old operations error.DocTypeName:"
								+ docTypeName, ex);
						result = false;
					}
				}
				Iterator nit = opList.iterator(); 
				//逐个加载操作
				while(nit.hasNext())
				{
					try{
						Node op = (Node)nit.next();
						String tmpOpName = getNodeValue(op, "descendant::NAME");
						if (tmpOpName == null)
						{
							log.error("[FlowTemplate.ImportOperaton]One operation is not added.No name");
							result = false;
							continue;
						}
						if(opNameList.contains(tmpOpName))
						{
							log.error("[FlowTemplate.ImportOperaton]One operation is not added.Same name:" + tmpOpName);
							result = false;
							continue;
						}
						operation.setDocTypeID(docTypeID);
						operation.setName(tmpOpName);
						operation.setDescription( getNodeValue(op, "descendant::DESCRIPTION") );
						operation.setCallMode(getNodeIntValue(op, "descendant::CALLMODE") );
						operation.setNeedLog(parseBool( getNodeValue(op,"descendant::NEEDLOG")));
						operation.setNeedRefresh(parseBool( getNodeValue(op,"descendant::NEEDREFRESH")));
						operation.setNeedLock(parseBool( getNodeValue(op,"descendant::NEEDLOCK")));
						operation.setNeedPrompt(parseBool( getNodeValue(op, "descendant::NEEDPROMPT")));
						operation.setResizable(parseBool( getNodeValue(op, "descendant::SIZE/@RESIZABLE")));
						operation.setDealCount(getNodeIntValue(op, "descendant::MULTIDOC") + 1);
						operation.setCodeURL( getNodeValue(op, "descendant::URL"));
						operation.setShowType(getNodeIntValue(op, "descendant::SHOWINPOPMENU") * 2 
									+ getNodeIntValue(op, "descendant::SHOWINTOOLBAR"));
						operation.setHeight(getNodeIntValue(op, "descendant::SIZE/@HEIGHT"));
						operation.setWidth(getNodeIntValue(op, "descendant::SIZE/@WIDTH"));

						//这里假设DocType已经生成完毕了
						pm.createOperation(docTypeID, operation);
					}catch(Exception ex){
						log.error("[FlowTemplate.ImportOperaton]Add operation error:" + operation.getName(), ex);
						result = false;
					}
				}
			}catch(Exception ex){
				log.error("[FlowTemplate.ImportOperaton]Import docType operations Error.DocTypeName:" + docTypeName, ex);
				result = false;
			}
		}
		return result;
	}
	//读取节点整数值
	private int getNodeIntValue(Node node, String name)
	{
		return parseInt(getNodeValue(node, name));
	}
	//读取节点字符串值
	private String getNodeValue(Node node, String name)
	{
		Node child = node.selectSingleNode(name);
		if (child != null)
			return child.getStringValue();
		else return null;
	}
	//加载一个流程
	private boolean importFlow(int docTypeID, Node nodeFlow, 
			Operation[] operations, Icon[] icons, List jumpBoxs, boolean isDefault)
	{
		boolean result = true;
		Flow flow = new Flow();
		try {
			String tmpName = getAttributeValue(nodeFlow, "@NAME");
			if (log.isInfoEnabled())
				log.info("[FlowTemplate.ImportFlow]: " + tmpName);
			//先增加流程
			flow.setName(tmpName);
			flow.setDescription((getAttributeValue(nodeFlow, "@DESCRIPTION")));
			fm.create(docTypeID,flow);

			int flowID = flow.getID();
			if (isDefault)//设置为缺省流程
				dtm.setDefaultFlow(docTypeID, flowID);

			//流程节点名称的List。一个流程里的节点不允许重名
			List flowNodeNameList = new ArrayList(); 
			//加入节点
			List nodeList = nodeFlow.selectNodes("descendant::NODE");

			if(nodeList!=null)
			{
				Iterator itNodes = nodeList.iterator();
				while(itNodes.hasNext())
				{
					Node node = (Node)itNodes.next();
					if (!importFlowNode(docTypeID, flowID, node, flowNodeNameList, 
							operations, icons, jumpBoxs))
						result = false;
				}
				
			}
		} catch (Exception e) {
			log.error("[FlowTemplate.ImportFlow]Error when import a flow:" + flow.getName(), e);
			result = false;
		}
		return result;
	}
	//加载流程节点
	private boolean importFlowNode(int docTypeID, int flowID, Node node, List flowNodeNameList, 
			Operation[] operations, Icon[] icons, List jumpBoxs)
	{
		String tmpNodeName = getAttributeValue(node, "@NAME");
		if (flowNodeNameList.contains(tmpNodeName))
		{
			log.error("[FlowTemplate.ImportFlowNode]Duplicated flownode name!" + tmpNodeName);
			return false;
		}
		else flowNodeNameList.add(tmpNodeName);
		
		boolean result = true;
		FlowNode flowNode = new FlowNode();
		try{
			flowNode.setName(tmpNodeName);

			flowNode.setDescription(getAttributeValue(node, "@DESCRIPTION"));
			flowNode.setWaitingStatus(getAttributeValue(node, "@WAITINGNAME"));
			flowNode.setDoingStatus(getAttributeValue(node, "@DOINGNAME"));
			flowNode.setDoneStatus(getAttributeValue(node, "@DONENAME"));
			flowNode.setNextNodeID(0);
			flowNode.setPreNodeID(0);
			
			if (log.isInfoEnabled())
				log.info("[FlowTemplate.ImportFlowNode]: " + tmpNodeName);

			fm.append(flowID, flowNode);
			
			int flowNodeID = flowNode.getID();
			//添加处理操作
			Node procNode = node.selectSingleNode("descendant::DO");
			Proc proc = new Proc();
			if (procNode!=null)
			{
				fillProc(proc, procNode, operations, icons, docTypeID);
				pm.addDo(flowNodeID,proc);
			}
			//添加下传操作
			procNode = node.selectSingleNode("descendant::GO");
			if (procNode!=null)
			{
				fillProc(proc, procNode, operations, icons, docTypeID);
				pm.addGo(flowNodeID,proc);
			}
			//添加回退操作
			procNode = node.selectSingleNode("descendant::BACK");
			if (procNode != null)
			{
				fillProc(proc, procNode, operations, icons, docTypeID);
				pm.addBack(flowNodeID,proc);
			}
			//jump
			List jumpProcs = node.selectNodes("descendant::JUMP");
			if (jumpProcs != null && jumpProcs.size() > 0)
			{
				jumpBoxs.add(new JumpBox(flowNodeID,node, flowID, docTypeID));
			}
		}catch(Exception ex){
			log.error("[FlowTemplate.ImportFlowNode]:" + flowNode.getName(),ex);
			result = false;
		}
		return result;
	}
	/**
	 * 为了避免二次遍历模板文件，存储所有的跳砖操作
	 * 在完成其他部分后进行
	 * @param jumpList
	 */
	private boolean importJumpNode(List jl,Icon[] icons)
	{
		if(jl == null || jl.size() == 0) return true;
		
		Operation[] operations = null;
		Flow[] flows = null;
		Object[] flowNodes = null;
		boolean result = true;
		try{
			operations = pm.getOperations(0);
			flows = fm.getFlows(0);
			flowNodes = new FlowNode[flows.length][];
			for(int i = 0;i< flows.length;i++)
				flowNodes[i] = fm.getFlowNodes(flows[i].getID());
			
		}catch(Exception ex){
			log.error("[importJumpNode]Get Operation Error:",ex);
			result = false;
		}
		ProcFlow jump = new ProcFlow();
		Iterator it = jl.iterator();
		while(it.hasNext())// 迭代检查跳转操作
		{
			JumpBox jumpBox = (JumpBox)it.next();
			int flowNodeID = jumpBox.flowNodeID;
			int docTypeID = jumpBox.docTypeID;
			Node nodeFlowNode = jumpBox.flowNode;

			List jumpList = nodeFlowNode.selectNodes("descendant::JUMP");

			if(jumpList == null) continue;
			
			Iterator jIter = jumpList.iterator();
			//处理跳转操作
			while(jIter.hasNext())
			{
				try
				{
					Node jumpNode = (Node)jIter.next();

					fillProc(jump, jumpNode, operations, icons, docTypeID);
					
					String nextFlowName = getAttributeValue(jumpNode, "@JUMPTOFLOW");
					int flowIndex = getFlowIndex(docTypeID, nextFlowName, flows);
					if (flowIndex < 0)
					{
						log.error("[FlowTemplate.ImportJumpNode]Cannot find the flow for jump:" + nextFlowName);
						result = false;
					}
					else
					{
						jump.setNextFlowID(flows[flowIndex].getID());
					
						String nextFlowNodeName = getAttributeValue(jumpNode, "@JUMPTOFLOWNODE");
						int nextNodeID = getFlowNodeID(flows[flowIndex].getID(), nextFlowNodeName, (FlowNode[])flowNodes[flowIndex]);
						if (nextNodeID > 0) 
							jump.setNextFlowNodeID(nextNodeID);
						else
						{
							log.error("[FlowTemplate.ImportJumpNode]Cannot find the flownode for jump:" + nextFlowNodeName);
							result = false;
						}
					}
					if (log.isInfoEnabled())
						log.info("Now import jump:" + jump.getProcName());
					pm.createJump(flowNodeID,jump);
				}catch(Exception ex){
					log.error("[FlowTemplate.ImportJumpNode]Create jump error:" + jump.getProcName(),ex);
					result = false;
				}
			}
		}
		return result;
	}
	
	private String parseStr(String str) 
	{
		if (str == null) return "";
		
		return StringUtils.encode(str);
	}
	private int parseInt(boolean bool) {
		if (bool == true) return 1;
		else return 0;
	}
	private int parseInt(String parser)
	{
		if (parser == null) return 0;
		
		try{
			return Integer.parseInt(parser);
		}catch(Exception ex){
			return 0;
		}
	}
	private boolean parseBool(String var)
	{
		if (var == null || "".equals(var.trim()) || "0".equals(var))
		{
			return false;
		}
		else if(var.equalsIgnoreCase("false")
				|| var.equalsIgnoreCase("off")
				|| var.equalsIgnoreCase("wrong"))
		{
			return false;
		}
		else{
			return true;
		}
	}
	//根据操作名称查找操作ID
	private int getOperationID(String operationName, Operation[] operations, int docTypeID)
	{
		if (operations == null) return 0;
		
		for(int o = 0; o < operations.length; o++)
			if (docTypeID == operations[o].getDocTypeID() 
					&& operations[o].getName().equals(operationName))
				return operations[o].getID();
		return 0;
	}
	//根据图标名称查找图标ID
	private int getIconID(String iconName, Icon[] icons)
	{
		if (icons == null) return 0;
		
		for(int o=0;o<icons.length;o++)
			if(icons[o].getFileName().equals(iconName))
				return icons[o].getID();
		return 0;
	}
	//根据流程名称查找流程在流程数组中的位置
	private int getFlowIndex(int docTypeID, String flowName, Flow[] flows)
	{
		if (flows == null) return -1;
		for(int m = 0; m < flows.length; m++)
			if(docTypeID == flows[m].getDocTypeID() && flows[m].getName().equals(flowName))
				return m;
		return -1;
	}
	//根据流程名称查找流程在流程数组中的位置
	private int getFlowNodeID(int flowID, String flowNodeName, FlowNode[] flowNodes)
	{
		if (flowNodes == null) return 0;
		for(int m = 0; m < flowNodes.length; m++)
			if(flowID == flowNodes[m].getFlowID() && flowNodes[m].getName().equals(flowNodeName))
				return flowNodes[m].getID();
		return 0;
	}
	//根据NODE，组装一个流程操作的名称、操作ID、图标ID
	private void fillProc(Proc proc, Node procNode, Operation[] operations, Icon[] icons, int docTypeID)
	{
		proc.setDescription("");
		proc.setProcName(getAttributeValue(procNode, "@NAME"));

		String operationName = getAttributeValue(procNode, "@OPERATIONNAME");
		int opID = getOperationID(operationName, operations, docTypeID);
		proc.setOpID(opID);
		
		String iconName = getAttributeValue(procNode, "@ICON");
		int iconID = getIconID(iconName, icons);
		proc.setIconID(iconID);
	}
}
   /////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 存储跳砖操作的包装类
	 * @author slim
	 *
	 */
	class JumpBox
	{
		JumpBox(int flowNodeID,Node flowNode,int flowID,int docTypeID)
		{
			this.flowNodeID =flowNodeID;
			this.flowNode = flowNode;
			this.flowID = flowID;
			this.docTypeID = docTypeID;
		}
		int flowNodeID;
		Node flowNode;
		int flowID;
		int docTypeID;
	}