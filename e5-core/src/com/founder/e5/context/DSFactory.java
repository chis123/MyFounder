package com.founder.e5.context;

public class DSFactory {
	private static DSManager dsManager = null;
	private static DSReader dsReader = null;
	
	public static DSManager buildDSManager(){
		if (dsManager != null) return dsManager;
		dsManager = DSManagerImpl.getInstance();
		return dsManager;
	}

	public static DSReader buildDSReader(){
		if (dsReader != null) return dsReader;
		dsReader = DSReaderImpl.getInstance();
		return dsReader;
	}
}
