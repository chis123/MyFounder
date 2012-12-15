package com.founder.e5.doc;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.founder.e5.commons.Log;
import com.founder.e5.context.Context;
import com.founder.e5.db.BfileType;
import com.founder.e5.db.IBfile;
import com.founder.e5.db.IBlob;
import com.founder.e5.db.IClob;
import com.founder.e5.db.LobHelper;
import com.founder.e5.doc.util.E5docHelper;

/**
 * 通用文档类型，对应文档实体表中的一条记录。<br>
 * <br>
 * get/set时传入的属性名大小写均可，内部转化为大写；<br>
 * 宽泛的类型转换支持，Number、String、Date均可转换为整型 <br>
 * 不允许通过set(String,Object)传入主键列名的方式修改主键值（以及DocLib值）<br>
 * <br>
 * 注意：对于可变对象属性值（如java.sql.Date），用户不可通过直接修改属性对象本身（如调用
 * java.sql.Date.setTime）来改变文档相应属性的值，这种方式造成的改变不会被检测到，因此
 * 不会被DocumentManager.update更新到数据库
 * 
 * @author liyanhui
 * @version 1.3
 * @created 2005-6-21 16:40:02
 */
public class Document implements Cloneable {

	protected static Log log = Context.getLog( "e5.doc" );

	/**
	 * 文档类型ID
	 */
	protected int docTypeID;

	/**
	 * 保存文档动态属性：key（属性名：字符串对象）- value（属性值：Object对象）
	 */
	protected Map properties = new HashMap();

	// -------------------------------------------------------------------------
	// ------------- 实体bean属性

	static final int STATUS_TRANSIENT = 0; // 临时态

	static final int STATUS_PERSISTENT = 1; // 持久态

	/**
	 * 保存"脏"字段，属性名（String对象）的集合
	 */
	private HashSet dirtyColumns = new HashSet();

	/**
	 * "脏"标记
	 */
	private boolean dirty = false;

	/**
	 * 文档的持久化状态
	 */
	private int status = STATUS_TRANSIENT;

	// -------------------------------------------------------------------------
	// --------- 状态管理方法

	/**
	 * 返回当前持久化状态
	 */
	int getStatus() {
		return status;
	}

	/**
	 * 设置文档持久化状态
	 */
	void setStatus( int status ) {
		this.status = status;
	}

	/**
	 * 查询"脏"标记
	 */
	boolean isDirty() {
		return dirty;
	}

	/**
	 * 设置"脏"标记
	 */
	void setDirty( boolean flag ) {
		this.dirty = flag;
	}

	/**
	 * 返回"脏"字段集合
	 */
	Collection getDirtyColumns() {
		// 考虑到性能，且是包内操作，就直接返回了
		return dirtyColumns;
		// return Collections.unmodifiableCollection(dirtyColumns);
	}

	/**
	 * 清空"脏"字段集合
	 */
	void clearDirtyColumns() {
		dirtyColumns.clear();
	}

	// 如果调用set方法时处于持久态，则置更新标记为真并记录更新字段
	// 注意：传过来的propertyName应该是大写
	private void fireDirty( String propertyName ) {
		if ( !dirty ) {
			dirty = true;
		}
		dirtyColumns.add( propertyName );
	}

	// -------------------------------------------------------------------------
	// --------- constructor

	/**
	 * 根据给定文档类型创建空文档实例
	 */
	Document( int docTypeID ) {
		this.docTypeID = docTypeID;

		// 预置值
		setDeleteFlag( 0 );
		setCreated( new Timestamp( System.currentTimeMillis() ) );
		setLocked( false );
	}

	// -------------------------------------------------------------------------
	// ---------- basic access methods

	/**
	 * 根据属性名取属性值
	 * 
	 * @param propertyName 属性名(即表字段名)
	 * @return 属性值
	 */
	public Object get( String propertyName ) {
		return properties.get( propertyName.toUpperCase() );
	}

	/**
	 * 设置属性值并返回属性被更新前的值（有的话）
	 * 
	 * @param propertyName 属性名(即表字段名)
	 * @param propertyValue 属性值（可以为空）
	 * @return 属性被更新前的值
	 */
	public Object set( String propertyName, Object propertyValue ) {
		propertyName = propertyName.toUpperCase();

		if ( propertyName.equals( E5docHelper.DOCUMENTID ) )
			throw new IllegalArgumentException( "Can't modify DocumentID!" );

		if ( propertyName.equals( E5docHelper.DOCLIBID ) )
			throw new IllegalArgumentException( "Can't modify DocLibID!" );

		// 如果目前是持久态则记录修改过的字段
		if ( status == STATUS_PERSISTENT
				&& !_propertyEquals( propertyName, propertyValue ) ) {
			fireDirty( propertyName );
		}

		return properties.put( propertyName, propertyValue );
	}

	/**
	 * 判断给定属性值是否与当前属性值相等
	 */
	private boolean _propertyEquals( String propertyName, Object newValue ) {
		Object oldValue = properties.get( propertyName );
		if ( newValue == null )
			return oldValue == null;
		else
			return newValue.equals( oldValue );
	}

	// -------------------------------------------------------------------------
	// ------------------- 类型转换方法

	/**
	 * 取boolean型值。<br>
	 * <br>
	 * 若属性值为Boolean类型，则返回布尔值；若为Number类型，则当值不为0时返回true；若为其他类型，则当值不为null时返回true
	 * 
	 * @param propertyName
	 * @return
	 */
	public boolean getBoolean( String propertyName ) {
		Object obj = get( propertyName );

		if ( obj instanceof Boolean ) {
			return ( ( Boolean ) obj ).booleanValue();
		}
		if ( obj instanceof Number ) {
			return ( ( Number ) obj ).intValue() != 0;
		}

		return obj != null;
	}

	/**
	 * 取int型值。<br>
	 * <br>
	 * 该方法会把Number、String、java.util.Date类型值转化为int型值<br>
	 * 若相应属性值为null，则返回Integer.MIN_VALUE<br>
	 * 
	 * @param propertyName
	 * @return
	 */
	public int getInt( String propertyName ) {
		Object obj = get( propertyName );

		if ( obj instanceof Number ) {
			return ( ( Number ) obj ).intValue();
		}
		if ( obj instanceof String ) {
			try {
				return Integer.parseInt( ( String ) obj );
			} catch ( NumberFormatException e ) {
				// ignore
			}
		}
		if ( obj instanceof java.util.Date ) {
			return ( int ) ( ( java.util.Date ) obj ).getTime();
		}

		return Integer.MIN_VALUE;
	}

	/**
	 * 取long型值。<br>
	 * <br>
	 * 该方法会把Number、String、java.util.Date类型值转化为long型值<br>
	 * 若相应属性值为null，则返回Long.MIN_VALUE<br>
	 * 
	 * @param propertyName
	 * @return
	 */
	public long getLong( String propertyName ) {
		Object obj = get( propertyName );

		if ( obj instanceof Number ) {
			return ( ( Number ) obj ).longValue();
		}
		if ( obj instanceof String ) {
			try {
				return Long.parseLong( ( String ) obj );
			} catch ( NumberFormatException e ) {
				// ignore
			}
		}
		if ( obj instanceof java.util.Date ) {
			return ( ( java.util.Date ) obj ).getTime();
		}

		return Long.MIN_VALUE;
	}

	/**
	 * 取float型值。<br>
	 * <br>
	 * 该方法会把Number、String、java.util.Date类型值转化为float型值<br>
	 * 若相应属性值为null，则返回Float.MIN_VALUE<br>
	 * 
	 * @param propertyName
	 * @return
	 */
	public float getFloat( String propertyName ) {
		Object obj = get( propertyName );

		if ( obj instanceof Number ) {
			return ( ( Number ) obj ).floatValue();
		}
		if ( obj instanceof String ) {
			try {
				return Float.parseFloat( ( String ) obj );
			} catch ( NumberFormatException e ) {
				// ignore
			}
		}
		if ( obj instanceof java.util.Date ) {
			return ( ( java.util.Date ) obj ).getTime();
		}

		return Float.MIN_VALUE;
	}

	/**
	 * 取double型值。<br>
	 * <br>
	 * 该方法会把Number、String、java.util.Date类型值转化为double型值<br>
	 * 若相应属性值为null，则返回Double.MIN_VALUE<br>
	 * 
	 * @param propertyName
	 * @return
	 */
	public double getDouble( String propertyName ) {
		Object obj = get( propertyName );

		if ( obj instanceof Number ) {
			return ( ( Number ) obj ).doubleValue();
		}
		if ( obj instanceof String ) {
			try {
				return Double.parseDouble( ( String ) obj );
			} catch ( NumberFormatException e ) {
				// ignore
			}
		}
		if ( obj instanceof java.util.Date ) {
			return ( ( java.util.Date ) obj ).getTime();
		}

		return Double.MIN_VALUE;
	}

	/**
	 * 取String型值。<br>
	 * <br>
	 * 该方法把任何类型的值转化为其String形式<br>
	 * 注意：若实际属性值为null，则返回空字符串(09:35 2006-04-29)<br>
	 * 
	 * @param propertyName
	 * @return
	 */
	public String getString( String propertyName ) {
		Object value = get( propertyName );
		return ( value == null ? "" : value.toString() );
	}

	/**
	 * 取java.sql.Date型值。<br>
	 * <br>
	 * 该方法把java.util.Date类型的值转化为java.sql.Date，其他类型则抛出IllegalStateException
	 * 
	 * @param propertyName
	 * @return
	 */
	public java.sql.Date getDate( String propertyName ) {
		Object obj = get( propertyName );
		if ( obj instanceof java.util.Date ) {
			java.util.Date date = ( java.util.Date ) obj;
			return new java.sql.Date( date.getTime() );
		}
		if (obj == null) return null;
		
		String realType = obj.getClass().getName();
		throw new IllegalStateException( "Can't convert " + realType
				+ " to Date." );
	}

	/**
	 * 取java.sql.Time型值。<br>
	 * <br>
	 * 该方法把java.util.Date类型的值转化为java.sql.Time，其他类型则抛出IllegalStateException
	 * 
	 * @param propertyName
	 * @return
	 */
	public java.sql.Time getTime( String propertyName ) {
		Object obj = get( propertyName );
		if ( obj instanceof java.util.Date ) {
			java.util.Date date = ( java.util.Date ) obj;
			return new java.sql.Time( date.getTime() );
		}
		if (obj == null) return null;
		
		String realType = obj.getClass().getName();
		throw new IllegalStateException( "Can't convert " + realType
				+ " to Time." );
	}

	/**
	 * 取Timestamp型值。<br>
	 * <br>
	 * 该方法把java.util.Date类型的值转化为java.sql.Timestamp，其他类型则抛出IllegalStateException
	 * 
	 * @param propertyName
	 * @return
	 */
	public java.sql.Timestamp getTimestamp( String propertyName ) {
		Object obj = get( propertyName );
		if ( obj instanceof java.util.Date ) {
			java.util.Date date = ( java.util.Date ) obj;
			return new java.sql.Timestamp( date.getTime() );
		}
		if (obj == null) return null;
		
		String realType = obj.getClass().getName();
		throw new IllegalStateException( "Can't convert " + realType
				+ " to Timestamp." );
	}

	/**
	 * 取IClob型值。<br>
	 * <br>
	 * 如果该属性的值不是IClob类型的，则抛出IllegalStateException
	 * 
	 * @param propertyName
	 * @return
	 */
	public IClob getClob( String propertyName ) {
		Object obj = get( propertyName );
		if ( obj instanceof IClob ) {
			return ( IClob )obj;
		}
		if (obj == null) return null;
		
		String realType = obj.getClass().getName();
		throw new IllegalStateException( propertyName + "[" + realType
				+ "] isn't a IClob!" );
	}

	/**
	 * 取IBlob型值。<br>
	 * <br>
	 * 如果该属性的值不是IBlob类型的，则抛出IllegalStateException
	 * 
	 * @param propertyName
	 * @return
	 */
	public IBlob getBlob( String propertyName ) {
		Object obj = get( propertyName );
		if ( obj instanceof IBlob ) {
			return ( IBlob )obj;
		}
		if (obj == null) return null;

		String realType = obj.getClass().getName();
		throw new IllegalStateException( propertyName + "[" + realType
				+ "] isn't a IBlob!" );
	}

	/**
	 * 取IBfile型值。<br>
	 * <br>
	 * 如果该属性的值不是IBfile类型的，则抛出IllegalStateException
	 * 
	 * @param propertyName
	 * @return
	 */
	public IBfile getBfile( String propertyName ) {
		Object obj = get( propertyName );
		if ( obj instanceof IBfile ) {
			return ( IBfile )obj;
		}
		if (obj == null) return null;

		String realType = obj.getClass().getName();
		throw new IllegalStateException( propertyName + "[" + realType
				+ "] isn't a IBfile!" );
	}

	// -------------------------------------------------------------------------

	/**
	 * 设置属性值
	 * 
	 * @param propertyName
	 * @param propertyValue
	 */
	public void set( String propertyName, boolean propertyValue ) {
		set( propertyName, new Boolean( propertyValue ) );
	}

	/**
	 * 设置属性值
	 * 
	 * @param propertyName
	 * @param propertyValue
	 */
	public void set( String propertyName, byte propertyValue ) {
		set( propertyName, new Byte( propertyValue ) );
	}

	/**
	 * 设置属性值
	 * 
	 * @param propertyName
	 * @param propertyValue
	 */
	public void set( String propertyName, char propertyValue ) {
		set( propertyName, new Character( propertyValue ) );
	}

	/**
	 * 设置属性值
	 * 
	 * @param propertyName
	 * @param propertyValue
	 */
	public void set( String propertyName, int propertyValue ) {
		set( propertyName, new Integer( propertyValue ) );
	}

	/**
	 * 设置属性值
	 * 
	 * @param propertyName
	 * @param propertyValue
	 */
	public void set( String propertyName, long propertyValue ) {
		set( propertyName, new Long( propertyValue ) );
	}

	/**
	 * 设置属性值
	 * 
	 * @param propertyName
	 * @param propertyValue
	 */
	public void set( String propertyName, float propertyValue ) {
		set( propertyName, new Float( propertyValue ) );
	}

	/**
	 * 设置属性值
	 * 
	 * @param propertyName
	 * @param propertyValue
	 */
	public void set( String propertyName, double propertyValue ) {
		set( propertyName, new Double( propertyValue ) );
	}

	/**
	 * 设置Clob类型属性值，自动把String转化为IClob
	 * 
	 * @param propertyName
	 * @param propertyValue
	 */
	public void setClob( String propertyName, String propertyValue ) {
		IClob clob = LobHelper.createClob( propertyValue );
		set( propertyName, clob );
	}

	/**
	 * 设置Blob类型属性值，自动把byte[]转化为IBlob
	 * 
	 * @param propertyName
	 * @param propertyValue
	 */
	public void setBlob( String propertyName, byte[] propertyValue ) {
		IBlob blob = LobHelper.createBlob( propertyValue );
		set( propertyName, blob );
	}

	/**
	 * 设置Bfile类型属性值，自动把{directory，file，stream}转化为IBfile。<br>
	 * <br>
	 * 注意：采用该方法设置的bfile是外部文件型bfile。
	 * 
	 * @param propertyName 属性名
	 * @param directory bfile目录
	 * @param file bfile文件
	 * @param stream bfile数据
	 */
	public void setBfile( String propertyName, String directory, String file,
			InputStream stream ) {
		IBfile bfile = LobHelper.createBfile(
				directory,
				file,
				stream,
				BfileType.EXTFILE );
		set( propertyName, bfile );
	}

	// -------------------------------------------------------------------------
	// --------- fixed properties access methods

	/**
	 * 取文档作者
	 * 
	 * @return Returns the authors.
	 */
	public String getAuthors() {
		return getString( E5docHelper.AUTHORS );
	}

	/**
	 * 设置文档作者
	 * 
	 * @param authors The authors to set.
	 */
	public void setAuthors( String authors ) {
		set( E5docHelper.AUTHORS, authors );
	}

	/**
	 * 取文档记录创建时间
	 * 
	 * @return Returns the created.
	 */
	public Timestamp getCreated() {
		return getTimestamp( E5docHelper.SYSCREATED );
	}

	/**
	 * 设置文档记录创建时间
	 * 
	 * @param created The created to set.
	 */
	public void setCreated( Timestamp created ) {
		set( E5docHelper.SYSCREATED, created );
	}

	/**
	 * 取当前流程ID
	 * 
	 * @return Returns the currentFlow.
	 */
	public int getCurrentFlow() {
		return getInt( E5docHelper.CURRENTFLOW );
	}

	/**
	 * 设置当前流程ID
	 * 
	 * @param currentFlow The currentFlow to set.
	 */
	public void setCurrentFlow( int currentFlow ) {
		set( E5docHelper.CURRENTFLOW, new Integer( currentFlow ) );
	}

	/**
	 * 取当前节点ID
	 * 
	 * @return Returns the currentNode.
	 */
	public int getCurrentNode() {
		return getInt( E5docHelper.CURRENTNODE );
	}

	/**
	 * 设置当前节点ID
	 * 
	 * @param currentNode The currentNode to set.
	 */
	public void setCurrentNode( int currentNode ) {
		set( E5docHelper.CURRENTNODE, new Integer( currentNode ) );
	}

	/**
	 * 取当前状态
	 * 
	 * @return Returns the currentStatus.
	 */
	public String getCurrentStatus() {
		return getString( E5docHelper.CURRENTSTATUS );
	}

	/**
	 * 设置当前状态
	 * 
	 * @param currentStatus The currentStatus to set.
	 */
	public void setCurrentStatus( String currentStatus ) {
		set( E5docHelper.CURRENTSTATUS, currentStatus );
	}

	/**
	 * 取当前用户ID
	 * 
	 * @return Returns the currentUserID.
	 */
	public int getCurrentUserID() {
		return getInt( E5docHelper.CURRENTUSERID );
	}

	/**
	 * 设置当前用户ID
	 * 
	 * @param currentUserID The currentUserID to set.
	 */
	public void setCurrentUserID( int currentUserID ) {
		set( E5docHelper.CURRENTUSERID, new Integer( currentUserID ) );
	}

	/**
	 * 取当前用户名
	 * 
	 * @return Returns the currentUsername.
	 */
	public String getCurrentUserName() {
		return getString( E5docHelper.CURRENTUSERNAME );
	}

	/**
	 * 设置当前用户名
	 * 
	 * @param currentUsername The currentUsername to set.
	 */
	public void setCurrentUserName( String currentUsername ) {
		set( E5docHelper.CURRENTUSERNAME, currentUsername );
	}

	/**
	 * 取删除标记
	 * 
	 * @return Returns the deleteFlag.
	 */
	public int getDeleteFlag() {
		return getInt( E5docHelper.DELETEFLAG );
	}

	/**
	 * 设置删除标记
	 * 
	 * @param deleteFlag The deleteFlag to set.
	 */
	public void setDeleteFlag( int deleteFlag ) {
		set( E5docHelper.DELETEFLAG, new Integer( deleteFlag ) );
	}

	/**
	 * 取文档库ID
	 * 
	 * @return Returns the docLibID.
	 */
	public int getDocLibID() {
		return getInt( E5docHelper.DOCLIBID );
	}

	/**
	 * 设置文档库ID. 用户只能通过DocumentManager.moveTo()方法完成文档移库操作
	 * 
	 * @param doclibID The docLibID to set.
	 */
	void internalSetLibID( int docLibID ) {
		properties.put( E5docHelper.DOCLIBID, new Integer( docLibID ) );
	}

	/**
	 * 取文档ID
	 * 
	 * @return Returns the docID.
	 */
	public long getDocID() {
		return getLong( E5docHelper.DOCUMENTID );
	}

	/**
	 * 设置文档ID；该方法仅供DocumentManager调用
	 * 
	 * @param docID The docID to set.
	 */
	void internalSetDocID( long docID ) {
		properties.put( E5docHelper.DOCUMENTID, new Long( docID ) );
	}

	/**
	 * 取文档所在文件夹ID
	 * 
	 * @return Returns the folderID.
	 */
	public int getFolderID() {
		return getInt( E5docHelper.FOLDERID );
	}

	/**
	 * 设置文档所在文件夹ID
	 * 
	 * @param folderID The folderID to set.
	 */
	public void setFolderID( int folderID ) {
		set( E5docHelper.FOLDERID, new Integer( folderID ) );
	}

	/**
	 * 查询文档是否带附件
	 * 
	 * @return Returns the haveAttach.
	 */
	public int getHaveAttach() {
		return getInt( E5docHelper.HAVEATTACH );
	}

	/**
	 * 设置文档是否带附件标记
	 * 
	 * @param haveAttach The haveAttach to set.
	 */
	public void setHaveAttach( int haveAttach ) {
		set( E5docHelper.HAVEATTACH, new Integer( haveAttach ) );
	}

	/**
	 * 查询文档是否有关联文档
	 * 
	 * @return Returns the haveRelation.
	 */
	public int getHaveRelation() {
		return getInt( E5docHelper.HAVERELATION );
	}

	/**
	 * 设置文档是否有关联文档标记
	 * 
	 * @param haveRelation The haveRelation to set.
	 */
	public void setHaveRelation( int haveRelation ) {
		set( E5docHelper.HAVERELATION, new Integer( haveRelation ) );
	}

	/**
	 * 查询文档保存标记
	 * 
	 * @return Returns the keep.
	 */
	public boolean isKeep() {
		int i = getInt( E5docHelper.ISKEEP ); // 数据库中存的是整型值
		return ( i != 0 );
	}

	/**
	 * 设置文档保存标记
	 * 
	 * @param keep The keep to set.
	 */
	public void setKeep( boolean keep ) {
		int i = ( keep ? 1 : 0 );
		set( E5docHelper.ISKEEP, new Integer( i ) );
	}

	/**
	 * 取文档最后更新时间
	 * 
	 * @return Returns the lastmodified.
	 */
	public Timestamp getLastmodified() {
		return getTimestamp( E5docHelper.LASTMODIFIED );
	}

	/**
	 * 设置文档最后更新时间
	 * 
	 * @param lastmodified The lastmodified to set.
	 */
	public void setLastmodified( Timestamp lastmodified ) {
		set( E5docHelper.LASTMODIFIED, lastmodified );
	}

	/**
	 * 取文档锁定标记
	 * 
	 * @return Returns the locked.
	 */
	public boolean isLocked() {
		int i = getInt( E5docHelper.ISLOCKED ); // 数据库中存的是整型值
		return ( i != 0 );
	}

	/**
	 * 设置文档锁定标记
	 * 
	 * @param locked The locked to set.
	 */
	public void setLocked( boolean locked ) {
		int i = ( locked ? 1 : 0 );
		set( E5docHelper.ISLOCKED, new Integer( i ) );
	}

	/**
	 * 取文档标题
	 * 
	 * @return Returns the topic.
	 */
	public String getTopic() {
		return getString( E5docHelper.TOPIC );
	}

	/**
	 * 设置文档标题
	 * 
	 * @param topic The topic to set.
	 */
	public void setTopic( String topic ) {
		set( E5docHelper.TOPIC, topic );
	}

	// -------------------------------------------------------------------------

	/**
	 * 取得文档类型ID
	 * 
	 * @return Returns the docTypeID.
	 */
	public int getDocTypeID() {
		return docTypeID;
	}

	// -------------------------------------------------------------------------

	/**
	 * 两个文档的ID相等即认为相等
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals( Object obj ) {
		if ( obj == this )
			return true;
		if ( !( obj instanceof Document ) )
			return false;

		Document another = ( Document ) obj;
		return ( getDocID() == another.getDocID() );
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return ( int ) getDocID();
	}

	/**
	 * 注意：浅拷贝克隆，如更改克隆对象中的可变对象属性（如java.sql.Date），则原对象<br>
	 * 的属性也相应改变<br>
	 * 
	 * 另外，不复制"脏"标记、"脏"字段集合和状态信息，克隆出的对象为临时态
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone() throws CloneNotSupportedException {
		Document clone = new Document( docTypeID );
		for ( Iterator i = properties.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry me = ( Map.Entry ) i.next();
			clone.properties.put( me.getKey(), me.getValue() );
		}
		return clone;
	}

	/**
	 * 从另一文档拷贝属性（文档ID、文档库ID除外）
	 * 
	 * @param another
	 */
	public void copyPropertiesFrom( Document another ) {
		for ( Iterator i = another.properties.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry me = ( Map.Entry ) i.next();
			String propertyName = ( String ) me.getKey();
			if ( !( E5docHelper.DOCUMENTID.equals( propertyName ) || E5docHelper.DOCLIBID.equals( propertyName ) ) ) {
				properties.put( propertyName, me.getValue() );
			}
		}
	}

	/**
	 * 输出当前文档所有字段
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for ( Iterator i = properties.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry me = ( Map.Entry ) i.next();
			sb.append( me.getKey() ).append( "=" ).append( me.getValue() ).append(
					"\n" );
		}
		return sb.toString();
	}

	/**
	 * 返回该文档的标识字符串（例：docLibID=#, docID=###）
	 * 
	 * @return
	 */
	public String idString() {
		return new StringBuffer().append( "docLibID=" ).append( getDocLibID() ).append(
				", docID=" ).append( getDocID() ).toString();
	}

}
