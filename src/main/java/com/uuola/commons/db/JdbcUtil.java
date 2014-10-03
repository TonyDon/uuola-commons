/*
 * @(#)JdbcUtil.java 2013-10-26
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.commons.db;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uuola.commons.StringUtil;
import com.uuola.commons.reflect.FieldUtil;
/**
 * <pre>
 * JDBC 工具类
 * @author tangxiaodong
 * 创建日期: 2013-10-26
 * </pre>
 */
public class JdbcUtil {

    private static Logger logger = LoggerFactory.getLogger(JdbcUtil.class);

    /*
     * 将ResultSet单个结果集包装到Map<String,Object>对应值 return map;
     */
    public static Map<String, Object> parseMapBySingleResultSet(ResultSet rs) throws SQLException {
        if (null == rs || rs.isClosed() || !rs.next()) {
            logger.info("JDBC ResultSet is empty or closed.");
            return Collections.emptyMap();
        }
        ResultSetMetaData rsmd = rs.getMetaData();
        int colnum = rsmd.getColumnCount();
        Map<String, Object> m = new HashMap<String, Object>(colnum);
        for (int i = 1; i <= colnum; i++) {
            m.put(lookupColumnName(rsmd, i), rs.getObject(i));
        }
        return m;
    }

    /*
     * 将ResultSet多个结果集包装到List<Map<String,Object>>对应值 return
     * List<Map<String,Object>>;
     */
    public static List<Map<String, Object>> parseListByResultSets(ResultSet rs) throws SQLException {
        if (null == rs || rs.isClosed()) {
            logger.info("JDBC ResultSet is null or closed.");
            return Collections.emptyList();
        }
        ResultSetMetaData rsmd = rs.getMetaData();
        int colnum = rsmd.getColumnCount();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        while (rs.next()) {
            Map<String, Object> m = new HashMap<String, Object>(colnum);
            for (int i = 1; i <= colnum; i++) {
                m.put(lookupColumnName(rsmd, i), rs.getObject(i));
            }
            list.add(m);
        }
        return list;

    }

    /**
     * 将单一结果行设置倒具体类型并返回
     * 
     * @param rs
     * @param clazz
     * @return
     * @throws Exception
     */
    public <T> T parseBeanBySingleResultSet(ResultSet rs, Class<T> clazz) throws Exception {
        if (null == rs || rs.isClosed() || !rs.next()) {
            logger.info("JDBC ResultSet is empty or closed.");
            return null;
        }
        Map<String, Field> fieldNameMap = FieldUtil.getAllAccessibleFieldNameMap(clazz);
        if (fieldNameMap.isEmpty()) {
            return null;
        }
        ResultSetMetaData rsmd = rs.getMetaData();
        int colnum = rsmd.getColumnCount();
        T obj = clazz.newInstance();
        for (int i = 1; i <= colnum; i++) {
            // user_name -> userName
            String fieldName = StringUtil.parseUnderscoreName(lookupColumnName(rsmd, i));
            Field targetField = fieldNameMap.get(fieldName);
            if (null != targetField) {
                Object value = getResultSetValue(rs, i, targetField.getType());
                if (null != value) {
                    targetField.set(obj, value);
                }
            }
        }
        return obj;
    }

    /**
     * 将ResultSet 包装到 Class POJO 的List 中
     * 
     * @param rs
     * @param clazz
     * @return
     * @throws Exception
     */
    public static <T> List<T> parseListByResultSets(ResultSet rs, Class<T> clazz) throws Exception {
        if (null == rs || rs.isClosed()) {
            logger.info("JDBC ResultSet is null or closed.");
            return Collections.emptyList();
        }
        Map<String, Field> fieldNameMap = FieldUtil.getAllAccessibleFieldNameMap(clazz);
        if (fieldNameMap.isEmpty()) {
            return Collections.emptyList();
        }
        ResultSetMetaData rsmd = rs.getMetaData();
        int colnum = rsmd.getColumnCount();
        List<T> list = new ArrayList<T>();
        while (rs.next()) {
            T obj = clazz.newInstance();
            for (int i = 1; i <= colnum; i++) {
                // user_name -> userName
                String fieldName = StringUtil.parseUnderscoreName(lookupColumnName(rsmd, i));
                Field targetField = fieldNameMap.get(fieldName);
                if (null != targetField) {
                    Object value = getResultSetValue(rs, i, targetField.getType());
                    if (null != value) {
                        targetField.set(obj, value);
                    }
                }
            }
            list.add(obj);
        }
        return list;
    }

    /**
     * 得到列名，如果有标签名则取列标签名， 否则取表列名
     * @param resultSetMetaData
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    public static String lookupColumnName(ResultSetMetaData resultSetMetaData, int columnIndex) throws SQLException {
        String name = resultSetMetaData.getColumnLabel(columnIndex);
        return (name != null && name.length() >= 1) ? name : 
            resultSetMetaData.getColumnName(columnIndex);
    }
    
    /**
     * 判断 sql给出的对象类型是否为数值类型
     * 
     * @param sqlType
     *            the SQL type to be checked
     * @return whether the type is numeric
     */
    public static boolean isNumeric(int sqlType) {
        return Types.BIT == sqlType || Types.BIGINT == sqlType || Types.DECIMAL == sqlType || Types.DOUBLE == sqlType
                || Types.FLOAT == sqlType || Types.INTEGER == sqlType || Types.NUMERIC == sqlType
                || Types.REAL == sqlType || Types.SMALLINT == sqlType || Types.TINYINT == sqlType;
    }

    /**
     * 得到ResultSet 值的具体对象
     * 
     * @param rs
     * @param index
     * @return
     * @throws SQLException
     */
    public static Object getResultSetValue(ResultSet rs, int index) throws SQLException {
        Object obj = rs.getObject(index);
        String className = null;
        if (obj != null) {
            className = obj.getClass().getName();
        }
        if (obj instanceof Blob) {
            obj = rs.getBytes(index);
        } else if (obj instanceof Clob) {
            obj = rs.getString(index);
        } else if (className != null
                && ("oracle.sql.TIMESTAMP".equals(className) || "oracle.sql.TIMESTAMPTZ".equals(className))) {
            obj = rs.getTimestamp(index);
        } else if (className != null && className.startsWith("oracle.sql.DATE")) {
            String metaDataClassName = rs.getMetaData().getColumnClassName(index);
            if ("java.sql.Timestamp".equals(metaDataClassName) || "oracle.sql.TIMESTAMP".equals(metaDataClassName)) {
                obj = rs.getTimestamp(index);
            } else {
                obj = rs.getDate(index);
            }
        } else if (obj != null && obj instanceof java.sql.Date) {
            if ("java.sql.Timestamp".equals(rs.getMetaData().getColumnClassName(index))) {
                obj = rs.getTimestamp(index);
            }
        }
        return obj;
    }

    /**
     * 将一行记录集值在具体类型对象中设置
     * 
     * @param rs
     * @param index
     * @param requiredType
     * @return
     * @throws SQLException
     */
    public static Object getResultSetValue(ResultSet rs, int index, Class<?> requiredType) throws SQLException {
        if (requiredType == null) {
            return getResultSetValue(rs, index);
        }

        Object value = null;
        boolean wasNullCheck = false;

        // Explicitly extract typed value, as far as possible.
        if (String.class.equals(requiredType)) {
            value = rs.getString(index);
        } else if (boolean.class.equals(requiredType) || Boolean.class.equals(requiredType)) {
            value = rs.getBoolean(index);
            wasNullCheck = true;
        } else if (byte.class.equals(requiredType) || Byte.class.equals(requiredType)) {
            value = rs.getByte(index);
            wasNullCheck = true;
        } else if (short.class.equals(requiredType) || Short.class.equals(requiredType)) {
            value = rs.getShort(index);
            wasNullCheck = true;
        } else if (int.class.equals(requiredType) || Integer.class.equals(requiredType)) {
            value = rs.getInt(index);
            wasNullCheck = true;
        } else if (long.class.equals(requiredType) || Long.class.equals(requiredType)) {
            value = rs.getLong(index);
            wasNullCheck = true;
        } else if (float.class.equals(requiredType) || Float.class.equals(requiredType)) {
            value = rs.getFloat(index);
            wasNullCheck = true;
        } else if (double.class.equals(requiredType) || Double.class.equals(requiredType)
                || Number.class.equals(requiredType)) {
            value = rs.getDouble(index);
            wasNullCheck = true;
        } else if (byte[].class.equals(requiredType)) {
            value = rs.getBytes(index);
        } else if (java.sql.Date.class.equals(requiredType)) {
            value = rs.getDate(index);
        } else if (java.sql.Time.class.equals(requiredType)) {
            value = rs.getTime(index);
        } else if (java.sql.Timestamp.class.equals(requiredType) || java.util.Date.class.equals(requiredType)) {
            value = rs.getTimestamp(index);
        } else if (BigDecimal.class.equals(requiredType)) {
            value = rs.getBigDecimal(index);
        } else if (Blob.class.equals(requiredType)) {
            value = rs.getBlob(index);
        } else if (Clob.class.equals(requiredType)) {
            value = rs.getClob(index);
        } else {
            // Some unknown type desired -> rely on getObject.
            value = getResultSetValue(rs, index);
        }

        // Perform was-null check if demanded (for results that the
        // JDBC driver returns as primitives).
        if (wasNullCheck && value != null && rs.wasNull()) {
            value = null;
        }
        return value;
    }

    /**
     * 关闭jdbc 连接
     * 
     * @param c
     */
    public static void close(Connection c) {
        if (c != null) {
            try {
                c.close();
            } catch (SQLException e) {
                logger.warn("Could not close JDBC Connection.", e);
            } catch (Throwable t) {
                logger.warn("Unexpected exception on closing JDBC Connection.", t);
            }
        }
    }

    /**
     * 关闭jdbc SQL处理对象
     * 
     * @param stmt
     */
    public static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                logger.warn("Could not close JDBC Statement.", e);
            } catch (Throwable t) {
                logger.warn("Unexpected exception on closing JDBC Statement", t);
            }
        }
    }

    /**
     * 关闭jdbc 结果集
     * 
     * @param rs
     */
    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.warn("Could not close JDBC ResultSet", e);
            } catch (Throwable t) {
                logger.warn("Unexpected exception on closing JDBC ResultSet", t);
            }
        }
    }

    /**
     * 关闭jdbc 记录集，处理器 及连接
     * 
     * @param rs
     * @param stmt
     * @param conn
     */
    public static void close(ResultSet rs, Statement stmt, Connection conn) {
        close(rs);
        close(stmt);
        close(conn);
    }
}
