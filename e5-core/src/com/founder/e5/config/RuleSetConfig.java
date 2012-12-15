package com.founder.e5.config;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSetBase;

/**
 * 解析E5配置文件(<code>e5-config.xml</code>)所用的Digester RuleSet
 * @created on 2005-7-13
 * @author Gong Lijie
 */
public class RuleSetConfig extends RuleSetBase
{
    /**
	 * @param digester 用来增加规则的Digester实例.
	 */
	public void addRuleInstances(Digester digester) {
		//====restart-config======
        digester.addObjectCreate("e5-config/restart-config/action",
        		"com.founder.e5.config.InfoRestart");
	    digester.addSetProperties("e5-config/restart-config/action");
	    digester.addSetNext("e5-config/restart-config/action", 
	    		"addInfo", "com.founder.e5.config.InfoRestart");

		//====database-config======
	    digester.addObjectCreate("e5-config/database-config/central-db",
				"com.founder.e5.config.InfoCentralDB");
		digester.addSetProperties("e5-config/database-config/central-db");
		digester.addSetNext("e5-config/database-config/central-db",
				"addInfo", "com.founder.e5.config.InfoCentralDB");

	    //====locale-config=======
	    digester.addObjectCreate("e5-config/locale",
				"com.founder.e5.config.InfoLocale");
	    digester.addSetProperties("e5-config/locale");
	    digester.addSetNext("e5-config/locale",
	    		"addInfo", "com.founder.e5.config.InfoLocale");

	    //====factory-config=======
	    digester.addObjectCreate("e5-config/factory-config/bean",
				"com.founder.e5.config.InfoFactory");
		digester.addSetProperties("e5-config/factory-config/bean");
		digester.addSetNext("e5-config/factory-config/bean",
				"addInfo", "com.founder.e5.config.InfoFactory");
		
		//====platform-field=======
	    digester.addObjectCreate("e5-config/platform-field/field",
				"com.founder.e5.config.InfoField");
		digester.addSetProperties("e5-config/platform-field/field");
		digester.addSetNext("e5-config/platform-field/field",
				"addInfo", "com.founder.e5.config.InfoField");

		//====cache-config=======
	    digester.addObjectCreate("e5-config/cache-config",
				"com.founder.e5.config.CacheConfig");
		digester.addSetProperties("e5-config/cache-config");
		digester.addSetNext("e5-config/cache-config",
				"addInfo", "com.founder.e5.config.CacheConfig");
	    digester.addObjectCreate("e5-config/cache-config/action",
				"com.founder.e5.config.InfoCache");
		digester.addSetProperties("e5-config/cache-config/action");
		digester.addSetNext("e5-config/cache-config/action",
				"addInfo", "com.founder.e5.config.InfoCache");

		//====server-clone=======
//        digester.addCallMethod("e5-config/server-clone/server",
//                "addServer", 0);
	    digester.addObjectCreate("e5-config/server-clone/server",
				"com.founder.e5.config.InfoServer");
	    digester.addSetProperties("e5-config/server-clone/server");
	    digester.addSetNext("e5-config/server-clone/server",
	    		"addInfo", "com.founder.e5.config.InfoServer");

		//====customization-config=======
	    digester.addCallMethod("e5-config/customization-config/toolkit-customize",
	    		"addCustomizeToolkit", 0);
	    digester.addCallMethod("e5-config/customization-config/toolkit-buttonstyle",
	    		"addToolkitButtonStyle", 0);

	    digester.addObjectCreate("e5-config/customization-config/customization-item",
				"com.founder.e5.config.InfoCustomizeItem");
		digester.addSetProperties("e5-config/customization-config/customization-item");
		digester.addSetNext("e5-config/customization-config/customization-item",
				"addInfo", "com.founder.e5.config.InfoCustomizeItem");

	    
		//====system-load=======
	    digester.addObjectCreate("e5-config/system-load",
				"com.founder.e5.config.InfoSystemLoad");
		digester.addSetProperties("e5-config/system-load");
		digester.addSetNext("e5-config/system-load",
				"addInfo", "com.founder.e5.config.InfoSystemLoad");
		
		//====org=======
	    digester.addObjectCreate("e5-config/org/type",
				"com.founder.e5.config.InfoOrg");
		digester.addSetProperties("e5-config/org/type");
		digester.addSetNext("e5-config/org/type",
				"addInfo", "com.founder.e5.config.InfoOrg");

		//====dbsession-config=======
	    digester.addObjectCreate("e5-config/dbsession-config/dbdef",
				"com.founder.e5.config.InfoDBSession");
		digester.addSetProperties("e5-config/dbsession-config/dbdef");
		digester.addSetNext("e5-config/dbsession-config/dbdef",
				"addInfo", "com.founder.e5.config.InfoDBSession");

		//====id-config=======
	    digester.addObjectCreate("e5-config/id-config/id",
				"com.founder.e5.config.InfoID");
		digester.addSetProperties("e5-config/id-config/id");
		digester.addSetNext("e5-config/id-config/id",
				"addInfo", "com.founder.e5.config.InfoID");

		//====system permission page list=======
	    digester.addObjectCreate("e5-config/sys-permission-list/page",
				"com.founder.e5.config.InfoPermissionPage");
		digester.addSetProperties("e5-config/sys-permission-list/page");
		digester.addSetNext("e5-config/sys-permission-list/page",
				"addInfo", "com.founder.e5.config.InfoPermissionPage");

	}
}

