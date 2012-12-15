/**
 * $Id: e5new com.founder.e5.db.dialect OracleDialect.java 
 * created on 2006-3-29 11:07:47
 * by liyanhui
 */
package com.founder.e5.db.dialect;

import java.sql.Types;

/**
 * An SQL dialect for Oracle, compatible with Oracle 8.
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-3-29 11:07:47
 */
public class OracleDialect extends Oracle9Dialect {

	public OracleDialect() {
		super();

		// Oracle8 and previous define only a "DATE" type which
		// is used to represent all aspects of date/time
		registerColumnType( Types.TIMESTAMP, "date" );
	}

}
