package com.founder.e5.dom.util;

import com.founder.e5.db.ColumnInfo;
import com.founder.e5.db.DataType;

/**
 * @created 2005-7-20 17:26:04
 * @author Zhang Kaifeng
 * @version
 */
public class FlowField {

    public static final ColumnInfo FLOWRECORDID;

    public static final ColumnInfo DOCUMENTID;

    public static final ColumnInfo DOCLIBID;

    public static final ColumnInfo OPERATORID;

    public static final ColumnInfo OPERATOR;

    public static final ColumnInfo OPERATION;

    public static final ColumnInfo STARTTIME;

    public static final ColumnInfo ENDTIME;

    public static final ColumnInfo FROMPOSITION;

    public static final ColumnInfo TOPOSITION;

    public static final ColumnInfo LASTFLOWNODE;

    public static final ColumnInfo CURFLOWNODE;

    public static final ColumnInfo DETAIL;

    static {
        FLOWRECORDID = new ColumnInfo("FLOWRECORDID", 	null, DataType.INTEGER,   false,38, 0, null);
        DOCUMENTID 	 = new ColumnInfo("DOCUMENTID", 	null, DataType.INTEGER,   true, 38, 0, null);
        DOCLIBID 	 = new ColumnInfo("DOCLIBID", 		null, DataType.INTEGER,   true, 38, 0, null);
        OPERATORID 	 = new ColumnInfo("OPERATORID", 	null, DataType.INTEGER,   true, 38, 0, null);
        OPERATOR 	 = new ColumnInfo("OPERATOR", 		null, DataType.VARCHAR,   true, 20, 0, null);
        OPERATION 	 = new ColumnInfo("OPERATION", 		null, DataType.VARCHAR,   true, 40, 0, null);
        STARTTIME 	 = new ColumnInfo("STARTTIME", 		null, DataType.TIMESTAMP, true, 0,  0, null);
        ENDTIME 	 = new ColumnInfo("ENDTIME", 		null, DataType.TIMESTAMP, true, 0,  0, null);
        FROMPOSITION = new ColumnInfo("FROMPOSITION", 	null, DataType.VARCHAR,   true, 40, 0, null);
        TOPOSITION 	 = new ColumnInfo("TOPOSITION", 	null, DataType.VARCHAR,   true, 40, 0, null);
        LASTFLOWNODE = new ColumnInfo("LASTFLOWNODE", 	null, DataType.INTEGER,   true, 38, 0, null);
        CURFLOWNODE  = new ColumnInfo("CURFLOWNODE", 	null, DataType.INTEGER,   true, 38, 0, null);
        DETAIL 		 = new ColumnInfo("DETAIL", 		null, DataType.VARCHAR,   true, 255,0, null);
    }
}
