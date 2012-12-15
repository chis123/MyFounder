package com.founder.e5.permission;

/**
 * 权限管理器的创建工厂
 * 统一提供对外的接口
 * 接口分系统管理部分和操作主界面部分
 * @created 14-7-2005 16:25:35
 * @author Gong Lijie
 * @version 1.0
 */
public class Factory {

	private static FVPermissionManager fvpManager;
	private static FVPermissionReader fvpReader;

	private static FlowPermissionManager flowpManager;
	private static FlowPermissionReader flowpReader;
	
	private static PermissionManager pManager;
	private static PermissionReader pReader;
	
	private static CatPermissionManager catpManager;
	private static CatPermissionReader catpReader;
	
	private static DomPermissionManager dompManager;
	private static DomPermissionReader dompReader;

	public static FVPermissionManager buildFVPermissionManager(){
		if (fvpManager == null) fvpManager = new FVPermissionManagerImpl();
		return fvpManager;
	}

	public static FVPermissionReader buildFVPermissionReader(){
		if (fvpReader == null) fvpReader = new FVPermissionReaderImpl();
		return fvpReader;
	}

	public static FlowPermissionManager buildFlowPermissionManager(){
		if (flowpManager == null) flowpManager = new FlowPermissionManagerImpl();
		return flowpManager;
	}

	public static FlowPermissionReader buildFlowPermissionReader(){
		if (flowpReader == null) flowpReader = new FlowPermissionReaderImpl();
		return flowpReader;
	}

	public static PermissionManager buildPermissionManager(){
		if (pManager == null) pManager = new PermissionManagerImpl();
		return pManager;
	}

	public static PermissionReader buildPermissionReader(){
		if (pReader == null) pReader = new PermissionReaderImpl();
		return pReader;
	}

	public static CatPermissionManager buildCatPermissionManager(){
		if (catpManager == null) catpManager = new CatPermissionManagerImpl();
		return catpManager;
	}

	public static CatPermissionReader buildCatPermissionReader(){
		if (catpReader == null) catpReader = new CatPermissionReaderImpl();
		return catpReader;
	}
	
	public static DomPermissionManager buildDomPermissionManager(){
		if (dompManager == null) dompManager = new DomPermissionManagerImpl();
		return dompManager;
	}

	public static DomPermissionReader buildDomPermissionReader(){
		if (dompReader == null) dompReader = new DomPermissionReaderImpl();
		return dompReader;
	}
}