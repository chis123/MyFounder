package com.founder.e5.context;

/**
 * E5数据源实体类
 * @author Gong Lijie
 */
public class E5DataSource
{
	// primary key
	private int dsID;
	/** 数据源登记的名称 */
	private String name;
	/** 数据源真实名称*/
	private String dataSource;
	/** 数据库类型，字符串，如oracle,sybase,sqlserver等*/
	private String dbType;
	/** 数据库服务器*/
	private String dbServer;
	/** 数据库名*/
	private String db;
	/** 数据库用户名，在dataSource中可配置用户和口令，所以该属性可空*/
	private String user;
	/** 数据库用户口令，在dataSource中可配置用户和口令，所以该属性可空*/
	private String password;

	public E5DataSource () {
		super();
	}

	/**
	 * Constructor for required fields
	 */
	public E5DataSource (
				String _name,
				String _dataSource,
				String _dbType,
				String _dbServer,
				String _db,
				String _user,
				String _password) 
	{
		this.setName(_name);
		this.setDataSource(_dataSource);
		this.setDbType(_dbType);
		this.setDbServer(_dbServer);
		this.setDb(_db);
		this.setUser(_user);
		this.setPassword(_password);
	}
	/**
	 * @return Returns the _dsID.
	 */
	public int getDsID()
	{
		return dsID;
	}
	/**
	 * @param _dsid The _dsID to set.
	 */
	void setDsID(int _dsid)
	{
		dsID = _dsid;
	}
	/**
	 * @return Returns the dataSource.
	 */
	public String getDataSource()
	{
		return dataSource;
	}
	/**
	 * @param dataSource The dataSource to set.
	 */
	public void setDataSource(String dataSource)
	{
		this.dataSource = dataSource;
	}
	/**
	 * @return Returns the db.
	 */
	public String getDb()
	{
		return db;
	}
	/**
	 * @param db The db to set.
	 */
	public void setDb(String db)
	{
		this.db = db;
	}
	/**
	 * @return Returns the dbServer.
	 */
	public String getDbServer()
	{
		return dbServer;
	}
	/**
	 * @param dbServer The dbServer to set.
	 */
	public void setDbServer(String dbServer)
	{
		this.dbServer = dbServer;
	}
	/**
	 * @return Returns the dbType.
	 */
	public String getDbType()
	{
		return dbType;
	}
	/**
	 * @param dbType The dbType to set.
	 */
	public void setDbType(String dbType)
	{
		this.dbType = dbType;
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
	/**
	 * @return Returns the password.
	 */
	public String getPassword()
	{
		return password;
	}
	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}
	/**
	 * @return Returns the user.
	 */
	public String getUser()
	{
		return user;
	}
	/**
	 * @param user The user to set.
	 */
	public void setUser(String user)
	{
		this.user = user;
	}
	public String toString()
	{
		return " datasource:" + dataSource
			+";\n db:" + db
			+";\n dbServer:" + dbServer
			+";\n dbType:" + dbType
			+";\n dsID:" + dsID
			+";\n name:" + name
			+";\n password:" + password
			+";\n user:" + user			
			;
	}
}