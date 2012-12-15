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
 * ͨ���ĵ����ͣ���Ӧ�ĵ�ʵ����е�һ����¼��<br>
 * <br>
 * get/setʱ�������������Сд���ɣ��ڲ�ת��Ϊ��д��<br>
 * ��������ת��֧�֣�Number��String��Date����ת��Ϊ���� <br>
 * ������ͨ��set(String,Object)�������������ķ�ʽ�޸�����ֵ���Լ�DocLibֵ��<br>
 * <br>
 * ע�⣺���ڿɱ��������ֵ����java.sql.Date�����û�����ͨ��ֱ���޸����Զ����������
 * java.sql.Date.setTime�����ı��ĵ���Ӧ���Ե�ֵ�����ַ�ʽ��ɵĸı䲻�ᱻ��⵽�����
 * ���ᱻDocumentManager.update���µ����ݿ�
 * 
 * @author liyanhui
 * @version 1.3
 * @created 2005-6-21 16:40:02
 */
public class Document implements Cloneable {

	protected static Log log = Context.getLog( "e5.doc" );

	/**
	 * �ĵ�����ID
	 */
	protected int docTypeID;

	/**
	 * �����ĵ���̬���ԣ�key�����������ַ�������- value������ֵ��Object����
	 */
	protected Map properties = new HashMap();

	// -------------------------------------------------------------------------
	// ------------- ʵ��bean����

	static final int STATUS_TRANSIENT = 0; // ��ʱ̬

	static final int STATUS_PERSISTENT = 1; // �־�̬

	/**
	 * ����"��"�ֶΣ���������String���󣩵ļ���
	 */
	private HashSet dirtyColumns = new HashSet();

	/**
	 * "��"���
	 */
	private boolean dirty = false;

	/**
	 * �ĵ��ĳ־û�״̬
	 */
	private int status = STATUS_TRANSIENT;

	// -------------------------------------------------------------------------
	// --------- ״̬������

	/**
	 * ���ص�ǰ�־û�״̬
	 */
	int getStatus() {
		return status;
	}

	/**
	 * �����ĵ��־û�״̬
	 */
	void setStatus( int status ) {
		this.status = status;
	}

	/**
	 * ��ѯ"��"���
	 */
	boolean isDirty() {
		return dirty;
	}

	/**
	 * ����"��"���
	 */
	void setDirty( boolean flag ) {
		this.dirty = flag;
	}

	/**
	 * ����"��"�ֶμ���
	 */
	Collection getDirtyColumns() {
		// ���ǵ����ܣ����ǰ��ڲ�������ֱ�ӷ�����
		return dirtyColumns;
		// return Collections.unmodifiableCollection(dirtyColumns);
	}

	/**
	 * ���"��"�ֶμ���
	 */
	void clearDirtyColumns() {
		dirtyColumns.clear();
	}

	// �������set����ʱ���ڳ־�̬�����ø��±��Ϊ�沢��¼�����ֶ�
	// ע�⣺��������propertyNameӦ���Ǵ�д
	private void fireDirty( String propertyName ) {
		if ( !dirty ) {
			dirty = true;
		}
		dirtyColumns.add( propertyName );
	}

	// -------------------------------------------------------------------------
	// --------- constructor

	/**
	 * ���ݸ����ĵ����ʹ������ĵ�ʵ��
	 */
	Document( int docTypeID ) {
		this.docTypeID = docTypeID;

		// Ԥ��ֵ
		setDeleteFlag( 0 );
		setCreated( new Timestamp( System.currentTimeMillis() ) );
		setLocked( false );
	}

	// -------------------------------------------------------------------------
	// ---------- basic access methods

	/**
	 * ����������ȡ����ֵ
	 * 
	 * @param propertyName ������(�����ֶ���)
	 * @return ����ֵ
	 */
	public Object get( String propertyName ) {
		return properties.get( propertyName.toUpperCase() );
	}

	/**
	 * ��������ֵ���������Ա�����ǰ��ֵ���еĻ���
	 * 
	 * @param propertyName ������(�����ֶ���)
	 * @param propertyValue ����ֵ������Ϊ�գ�
	 * @return ���Ա�����ǰ��ֵ
	 */
	public Object set( String propertyName, Object propertyValue ) {
		propertyName = propertyName.toUpperCase();

		if ( propertyName.equals( E5docHelper.DOCUMENTID ) )
			throw new IllegalArgumentException( "Can't modify DocumentID!" );

		if ( propertyName.equals( E5docHelper.DOCLIBID ) )
			throw new IllegalArgumentException( "Can't modify DocLibID!" );

		// ���Ŀǰ�ǳ־�̬���¼�޸Ĺ����ֶ�
		if ( status == STATUS_PERSISTENT
				&& !_propertyEquals( propertyName, propertyValue ) ) {
			fireDirty( propertyName );
		}

		return properties.put( propertyName, propertyValue );
	}

	/**
	 * �жϸ�������ֵ�Ƿ��뵱ǰ����ֵ���
	 */
	private boolean _propertyEquals( String propertyName, Object newValue ) {
		Object oldValue = properties.get( propertyName );
		if ( newValue == null )
			return oldValue == null;
		else
			return newValue.equals( oldValue );
	}

	// -------------------------------------------------------------------------
	// ------------------- ����ת������

	/**
	 * ȡboolean��ֵ��<br>
	 * <br>
	 * ������ֵΪBoolean���ͣ��򷵻ز���ֵ����ΪNumber���ͣ���ֵ��Ϊ0ʱ����true����Ϊ�������ͣ���ֵ��Ϊnullʱ����true
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
	 * ȡint��ֵ��<br>
	 * <br>
	 * �÷������Number��String��java.util.Date����ֵת��Ϊint��ֵ<br>
	 * ����Ӧ����ֵΪnull���򷵻�Integer.MIN_VALUE<br>
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
	 * ȡlong��ֵ��<br>
	 * <br>
	 * �÷������Number��String��java.util.Date����ֵת��Ϊlong��ֵ<br>
	 * ����Ӧ����ֵΪnull���򷵻�Long.MIN_VALUE<br>
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
	 * ȡfloat��ֵ��<br>
	 * <br>
	 * �÷������Number��String��java.util.Date����ֵת��Ϊfloat��ֵ<br>
	 * ����Ӧ����ֵΪnull���򷵻�Float.MIN_VALUE<br>
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
	 * ȡdouble��ֵ��<br>
	 * <br>
	 * �÷������Number��String��java.util.Date����ֵת��Ϊdouble��ֵ<br>
	 * ����Ӧ����ֵΪnull���򷵻�Double.MIN_VALUE<br>
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
	 * ȡString��ֵ��<br>
	 * <br>
	 * �÷������κ����͵�ֵת��Ϊ��String��ʽ<br>
	 * ע�⣺��ʵ������ֵΪnull���򷵻ؿ��ַ���(09:35 2006-04-29)<br>
	 * 
	 * @param propertyName
	 * @return
	 */
	public String getString( String propertyName ) {
		Object value = get( propertyName );
		return ( value == null ? "" : value.toString() );
	}

	/**
	 * ȡjava.sql.Date��ֵ��<br>
	 * <br>
	 * �÷�����java.util.Date���͵�ֵת��Ϊjava.sql.Date�������������׳�IllegalStateException
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
	 * ȡjava.sql.Time��ֵ��<br>
	 * <br>
	 * �÷�����java.util.Date���͵�ֵת��Ϊjava.sql.Time�������������׳�IllegalStateException
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
	 * ȡTimestamp��ֵ��<br>
	 * <br>
	 * �÷�����java.util.Date���͵�ֵת��Ϊjava.sql.Timestamp�������������׳�IllegalStateException
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
	 * ȡIClob��ֵ��<br>
	 * <br>
	 * ��������Ե�ֵ����IClob���͵ģ����׳�IllegalStateException
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
	 * ȡIBlob��ֵ��<br>
	 * <br>
	 * ��������Ե�ֵ����IBlob���͵ģ����׳�IllegalStateException
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
	 * ȡIBfile��ֵ��<br>
	 * <br>
	 * ��������Ե�ֵ����IBfile���͵ģ����׳�IllegalStateException
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
	 * ��������ֵ
	 * 
	 * @param propertyName
	 * @param propertyValue
	 */
	public void set( String propertyName, boolean propertyValue ) {
		set( propertyName, new Boolean( propertyValue ) );
	}

	/**
	 * ��������ֵ
	 * 
	 * @param propertyName
	 * @param propertyValue
	 */
	public void set( String propertyName, byte propertyValue ) {
		set( propertyName, new Byte( propertyValue ) );
	}

	/**
	 * ��������ֵ
	 * 
	 * @param propertyName
	 * @param propertyValue
	 */
	public void set( String propertyName, char propertyValue ) {
		set( propertyName, new Character( propertyValue ) );
	}

	/**
	 * ��������ֵ
	 * 
	 * @param propertyName
	 * @param propertyValue
	 */
	public void set( String propertyName, int propertyValue ) {
		set( propertyName, new Integer( propertyValue ) );
	}

	/**
	 * ��������ֵ
	 * 
	 * @param propertyName
	 * @param propertyValue
	 */
	public void set( String propertyName, long propertyValue ) {
		set( propertyName, new Long( propertyValue ) );
	}

	/**
	 * ��������ֵ
	 * 
	 * @param propertyName
	 * @param propertyValue
	 */
	public void set( String propertyName, float propertyValue ) {
		set( propertyName, new Float( propertyValue ) );
	}

	/**
	 * ��������ֵ
	 * 
	 * @param propertyName
	 * @param propertyValue
	 */
	public void set( String propertyName, double propertyValue ) {
		set( propertyName, new Double( propertyValue ) );
	}

	/**
	 * ����Clob��������ֵ���Զ���Stringת��ΪIClob
	 * 
	 * @param propertyName
	 * @param propertyValue
	 */
	public void setClob( String propertyName, String propertyValue ) {
		IClob clob = LobHelper.createClob( propertyValue );
		set( propertyName, clob );
	}

	/**
	 * ����Blob��������ֵ���Զ���byte[]ת��ΪIBlob
	 * 
	 * @param propertyName
	 * @param propertyValue
	 */
	public void setBlob( String propertyName, byte[] propertyValue ) {
		IBlob blob = LobHelper.createBlob( propertyValue );
		set( propertyName, blob );
	}

	/**
	 * ����Bfile��������ֵ���Զ���{directory��file��stream}ת��ΪIBfile��<br>
	 * <br>
	 * ע�⣺���ø÷������õ�bfile���ⲿ�ļ���bfile��
	 * 
	 * @param propertyName ������
	 * @param directory bfileĿ¼
	 * @param file bfile�ļ�
	 * @param stream bfile����
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
	 * ȡ�ĵ�����
	 * 
	 * @return Returns the authors.
	 */
	public String getAuthors() {
		return getString( E5docHelper.AUTHORS );
	}

	/**
	 * �����ĵ�����
	 * 
	 * @param authors The authors to set.
	 */
	public void setAuthors( String authors ) {
		set( E5docHelper.AUTHORS, authors );
	}

	/**
	 * ȡ�ĵ���¼����ʱ��
	 * 
	 * @return Returns the created.
	 */
	public Timestamp getCreated() {
		return getTimestamp( E5docHelper.SYSCREATED );
	}

	/**
	 * �����ĵ���¼����ʱ��
	 * 
	 * @param created The created to set.
	 */
	public void setCreated( Timestamp created ) {
		set( E5docHelper.SYSCREATED, created );
	}

	/**
	 * ȡ��ǰ����ID
	 * 
	 * @return Returns the currentFlow.
	 */
	public int getCurrentFlow() {
		return getInt( E5docHelper.CURRENTFLOW );
	}

	/**
	 * ���õ�ǰ����ID
	 * 
	 * @param currentFlow The currentFlow to set.
	 */
	public void setCurrentFlow( int currentFlow ) {
		set( E5docHelper.CURRENTFLOW, new Integer( currentFlow ) );
	}

	/**
	 * ȡ��ǰ�ڵ�ID
	 * 
	 * @return Returns the currentNode.
	 */
	public int getCurrentNode() {
		return getInt( E5docHelper.CURRENTNODE );
	}

	/**
	 * ���õ�ǰ�ڵ�ID
	 * 
	 * @param currentNode The currentNode to set.
	 */
	public void setCurrentNode( int currentNode ) {
		set( E5docHelper.CURRENTNODE, new Integer( currentNode ) );
	}

	/**
	 * ȡ��ǰ״̬
	 * 
	 * @return Returns the currentStatus.
	 */
	public String getCurrentStatus() {
		return getString( E5docHelper.CURRENTSTATUS );
	}

	/**
	 * ���õ�ǰ״̬
	 * 
	 * @param currentStatus The currentStatus to set.
	 */
	public void setCurrentStatus( String currentStatus ) {
		set( E5docHelper.CURRENTSTATUS, currentStatus );
	}

	/**
	 * ȡ��ǰ�û�ID
	 * 
	 * @return Returns the currentUserID.
	 */
	public int getCurrentUserID() {
		return getInt( E5docHelper.CURRENTUSERID );
	}

	/**
	 * ���õ�ǰ�û�ID
	 * 
	 * @param currentUserID The currentUserID to set.
	 */
	public void setCurrentUserID( int currentUserID ) {
		set( E5docHelper.CURRENTUSERID, new Integer( currentUserID ) );
	}

	/**
	 * ȡ��ǰ�û���
	 * 
	 * @return Returns the currentUsername.
	 */
	public String getCurrentUserName() {
		return getString( E5docHelper.CURRENTUSERNAME );
	}

	/**
	 * ���õ�ǰ�û���
	 * 
	 * @param currentUsername The currentUsername to set.
	 */
	public void setCurrentUserName( String currentUsername ) {
		set( E5docHelper.CURRENTUSERNAME, currentUsername );
	}

	/**
	 * ȡɾ�����
	 * 
	 * @return Returns the deleteFlag.
	 */
	public int getDeleteFlag() {
		return getInt( E5docHelper.DELETEFLAG );
	}

	/**
	 * ����ɾ�����
	 * 
	 * @param deleteFlag The deleteFlag to set.
	 */
	public void setDeleteFlag( int deleteFlag ) {
		set( E5docHelper.DELETEFLAG, new Integer( deleteFlag ) );
	}

	/**
	 * ȡ�ĵ���ID
	 * 
	 * @return Returns the docLibID.
	 */
	public int getDocLibID() {
		return getInt( E5docHelper.DOCLIBID );
	}

	/**
	 * �����ĵ���ID. �û�ֻ��ͨ��DocumentManager.moveTo()��������ĵ��ƿ����
	 * 
	 * @param doclibID The docLibID to set.
	 */
	void internalSetLibID( int docLibID ) {
		properties.put( E5docHelper.DOCLIBID, new Integer( docLibID ) );
	}

	/**
	 * ȡ�ĵ�ID
	 * 
	 * @return Returns the docID.
	 */
	public long getDocID() {
		return getLong( E5docHelper.DOCUMENTID );
	}

	/**
	 * �����ĵ�ID���÷�������DocumentManager����
	 * 
	 * @param docID The docID to set.
	 */
	void internalSetDocID( long docID ) {
		properties.put( E5docHelper.DOCUMENTID, new Long( docID ) );
	}

	/**
	 * ȡ�ĵ������ļ���ID
	 * 
	 * @return Returns the folderID.
	 */
	public int getFolderID() {
		return getInt( E5docHelper.FOLDERID );
	}

	/**
	 * �����ĵ������ļ���ID
	 * 
	 * @param folderID The folderID to set.
	 */
	public void setFolderID( int folderID ) {
		set( E5docHelper.FOLDERID, new Integer( folderID ) );
	}

	/**
	 * ��ѯ�ĵ��Ƿ������
	 * 
	 * @return Returns the haveAttach.
	 */
	public int getHaveAttach() {
		return getInt( E5docHelper.HAVEATTACH );
	}

	/**
	 * �����ĵ��Ƿ���������
	 * 
	 * @param haveAttach The haveAttach to set.
	 */
	public void setHaveAttach( int haveAttach ) {
		set( E5docHelper.HAVEATTACH, new Integer( haveAttach ) );
	}

	/**
	 * ��ѯ�ĵ��Ƿ��й����ĵ�
	 * 
	 * @return Returns the haveRelation.
	 */
	public int getHaveRelation() {
		return getInt( E5docHelper.HAVERELATION );
	}

	/**
	 * �����ĵ��Ƿ��й����ĵ����
	 * 
	 * @param haveRelation The haveRelation to set.
	 */
	public void setHaveRelation( int haveRelation ) {
		set( E5docHelper.HAVERELATION, new Integer( haveRelation ) );
	}

	/**
	 * ��ѯ�ĵ�������
	 * 
	 * @return Returns the keep.
	 */
	public boolean isKeep() {
		int i = getInt( E5docHelper.ISKEEP ); // ���ݿ��д��������ֵ
		return ( i != 0 );
	}

	/**
	 * �����ĵ�������
	 * 
	 * @param keep The keep to set.
	 */
	public void setKeep( boolean keep ) {
		int i = ( keep ? 1 : 0 );
		set( E5docHelper.ISKEEP, new Integer( i ) );
	}

	/**
	 * ȡ�ĵ�������ʱ��
	 * 
	 * @return Returns the lastmodified.
	 */
	public Timestamp getLastmodified() {
		return getTimestamp( E5docHelper.LASTMODIFIED );
	}

	/**
	 * �����ĵ�������ʱ��
	 * 
	 * @param lastmodified The lastmodified to set.
	 */
	public void setLastmodified( Timestamp lastmodified ) {
		set( E5docHelper.LASTMODIFIED, lastmodified );
	}

	/**
	 * ȡ�ĵ��������
	 * 
	 * @return Returns the locked.
	 */
	public boolean isLocked() {
		int i = getInt( E5docHelper.ISLOCKED ); // ���ݿ��д��������ֵ
		return ( i != 0 );
	}

	/**
	 * �����ĵ��������
	 * 
	 * @param locked The locked to set.
	 */
	public void setLocked( boolean locked ) {
		int i = ( locked ? 1 : 0 );
		set( E5docHelper.ISLOCKED, new Integer( i ) );
	}

	/**
	 * ȡ�ĵ�����
	 * 
	 * @return Returns the topic.
	 */
	public String getTopic() {
		return getString( E5docHelper.TOPIC );
	}

	/**
	 * �����ĵ�����
	 * 
	 * @param topic The topic to set.
	 */
	public void setTopic( String topic ) {
		set( E5docHelper.TOPIC, topic );
	}

	// -------------------------------------------------------------------------

	/**
	 * ȡ���ĵ�����ID
	 * 
	 * @return Returns the docTypeID.
	 */
	public int getDocTypeID() {
		return docTypeID;
	}

	// -------------------------------------------------------------------------

	/**
	 * �����ĵ���ID��ȼ���Ϊ���
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
	 * ע�⣺ǳ������¡������Ŀ�¡�����еĿɱ�������ԣ���java.sql.Date������ԭ����<br>
	 * ������Ҳ��Ӧ�ı�<br>
	 * 
	 * ���⣬������"��"��ǡ�"��"�ֶμ��Ϻ�״̬��Ϣ����¡���Ķ���Ϊ��ʱ̬
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
	 * ����һ�ĵ��������ԣ��ĵ�ID���ĵ���ID���⣩
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
	 * �����ǰ�ĵ������ֶ�
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
	 * ���ظ��ĵ��ı�ʶ�ַ���������docLibID=#, docID=###��
	 * 
	 * @return
	 */
	public String idString() {
		return new StringBuffer().append( "docLibID=" ).append( getDocLibID() ).append(
				", docID=" ).append( getDocID() ).toString();
	}

}
