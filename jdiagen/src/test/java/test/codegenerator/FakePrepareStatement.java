/*
 * jDialects, a tiny SQL dialect tool 
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package test.codegenerator;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * This is not a unit test class, it's a code generator tool to create source
 * code for jDialects
 *
 * @author Yong Zhu 
 * @since 1.0.0
 */
public class FakePrepareStatement implements PreparedStatement {
	private String limits = "";

	public FakePrepareStatement() {
	}

	public String getLimits() {
		return limits;
	}

	public void setLimits(String limits) {
		this.limits = limits;
	}

	@Override
	public void addBatch(String sql) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void cancel() throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void clearBatch() throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void clearWarnings() throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void close() throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void closeOnCompletion() throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public boolean execute(String sql) throws SQLException {
		// Auto-generated method stub
		return false;
	}

	@Override
	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		// Auto-generated method stub
		return false;
	}

	@Override
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		// Auto-generated method stub
		return false;
	}

	@Override
	public boolean execute(String sql, String[] columnNames) throws SQLException {
		// Auto-generated method stub
		return false;
	}

	@Override
	public int[] executeBatch() throws SQLException {
		// Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
		// Auto-generated method stub
		return null;
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		// Auto-generated method stub
		return 0;
	}

	@Override
	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		// Auto-generated method stub
		return 0;
	}

	@Override
	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		// Auto-generated method stub
		return 0;
	}

	@Override
	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		// Auto-generated method stub
		return 0;
	}

	@Override
	public Connection getConnection() throws SQLException {
		// Auto-generated method stub
		return null;
	}

	@Override
	public int getFetchDirection() throws SQLException {
		// Auto-generated method stub
		return 0;
	}

	@Override
	public int getFetchSize() throws SQLException {
		// Auto-generated method stub
		return 0;
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		// Auto-generated method stub
		return null;
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		// Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxRows() throws SQLException {
		// Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		// Auto-generated method stub
		return false;
	}

	@Override
	public boolean getMoreResults(int current) throws SQLException {
		// Auto-generated method stub
		return false;
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		// Auto-generated method stub
		return 0;
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		// Auto-generated method stub
		return null;
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		// Auto-generated method stub
		return 0;
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		// Auto-generated method stub
		return 0;
	}

	@Override
	public int getResultSetType() throws SQLException {
		// Auto-generated method stub
		return 0;
	}

	@Override
	public int getUpdateCount() throws SQLException {
		// Auto-generated method stub
		return 0;
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		// Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		// Auto-generated method stub
		return false;
	}

	@Override
	public boolean isClosed() throws SQLException {
		// Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPoolable() throws SQLException {
		// Auto-generated method stub
		return false;
	}

	@Override
	public void setCursorName(String name) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setEscapeProcessing(boolean enable) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setMaxFieldSize(int max) throws SQLException {
		limits+="MaxFieldSize"+max;
	}

	@Override
	public void setMaxRows(int max) throws SQLException { 
		limits+="MaxRows"+max; 
	}

	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setQueryTimeout(int seconds) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// Auto-generated method stub
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void addBatch() throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void clearParameters() throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public boolean execute() throws SQLException {
		// Auto-generated method stub
		return false;
	}

	@Override
	public ResultSet executeQuery() throws SQLException {
		// Auto-generated method stub
		return null;
	}

	@Override
	public int executeUpdate() throws SQLException {
		// Auto-generated method stub
		return 0;
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		// Auto-generated method stub
		return null;
	}

	@Override
	public ParameterMetaData getParameterMetaData() throws SQLException {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void setArray(int parameterIndex, Array x) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setBlob(int parameterIndex, Blob x) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setByte(int parameterIndex, byte x) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setClob(int parameterIndex, Clob x) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setDate(int parameterIndex, Date x) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setDouble(int parameterIndex, double x) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setFloat(int parameterIndex, float x) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setInt(int parameterIndex, int x) throws SQLException {
		this.limits += parameterIndex +"="+ x + ",";
	}

	@Override
	public void setLong(int parameterIndex, long x) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setNString(int parameterIndex, String value) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setObject(int parameterIndex, Object x) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setRef(int parameterIndex, Ref x) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setShort(int parameterIndex, short x) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setString(int parameterIndex, String x) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setTime(int parameterIndex, Time x) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setURL(int parameterIndex, URL x) throws SQLException {
		// Auto-generated method stub

	}

	@Override
	public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
		// Auto-generated method stub

	}
}