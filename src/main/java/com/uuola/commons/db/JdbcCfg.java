/*
 * @(#)JdbcCfg.java 2013-10-26
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.commons.db;

import com.uuola.commons.coder.DESede;
import com.uuola.commons.coder.MyBase64;


/**
 * <pre>
 * jdbc属性文件常量
 * @author tangxiaodong
 * 创建日期: 2013-10-26
 * </pre>
 */
public class JdbcCfg {

    /**
     * 可以使用环境参数定义jdbc.properties路径
     * java ... -Dconfig.path=/etc/www/jdbc -jar executer.jar <br/>
     * 如果环境定义该参数，/etc/www/jdbc路径下必须有文件jdbc.properties
     */
    public static final String ENV_JDBC_CONFIG_PATH = "config.path" ;
    
    /**
     * 属性文件名
     */
    public static final String ENV_JDBC_PROPERTIES_NAME = "jdbc.properties";
    
    /**
     * 多个数据源名称列表
     */
    public static final String JDBC_DATASOURCE_NAME_LIST = "jdbc.datasource.name_list";

    /**
     * 缺省数据源名称
     */
    public static final String DEFAULT_DATASOURCE_NAME = "#_DATASOURCE_NAME_DEFAULT";
    
    /**
     * 密码加密字符前缀，如果有此前缀，需要对后部分进行解密
     * 使用DESede算法
     */
    public static final String PASSWORD_ENCRYPT_PREFIX = "enc_";
    
    /**
     * 密钥hash后的key
     */
    public static final String HASH_KEY = "@$DGLuE14vblL3D1LSyQ==$@"; 
    
    /**
     * JDBC 连接参数
     */
    public static final String PARAMS_DRIVER_CLASS = "driver_class";
    public static final String PARAMS_CONNECTION_URL = "connection_url";
    public static final String PARAMS_USERNAME = "username";
    public static final String PARAMS_PASSWORD = "password";
    public static final String PARAMS_MIN_SIZE = "min_size";
    public static final String PARAMS_MIN_IDLE = "min_idle";
    public static final String PARAMS_MAX_WAIT = "max_wait";
    public static final String PARAMS_MAX_IDLE = "max_idle";
    public static final String PARAMS_MAX_ACTIVE = "max_active";
    public static final String PARAMS_VALIDATION_QUERY = "validation_query";
    public static final String PARAMS_VALIDATION_INTERVAL = "validation_interval";
    public static final String PARAMS_JDBCINTERCEPTORS = "jdbc_interceptors";
    public static final String PARAMS_REMOVEABANDONED = "remove_abandoned";
    public static final String PARAMS_REMOVEABANDONEDTIMEOUT = "remove_abandoned_timeout";
    public static final String PARAMS_LOGABANDONED = "log_abandoned";
    public static final String PARAMS_TESTONBORROW = "test_on_borrow";
    public static final String PARAMS_TESTWHILEIDLE = "test_while_idle";
    public static final String PARAMS_TESTONRETURN = "test_on_return";
    public static final String PARAMS_TIMEBETWEENEVICTIONRUNSMILLIS = "timeBetweenEvictionRunsMillis";
    public static final String PARAMS_MINEVICTABLEIDLETIMEMILLIS = "minEvictableIdleTimeMillis";
    
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        String enc = DESede.encrypt("root", HASH_KEY);
        System.out.println(enc);
        System.out.println(DESede.decrypt(enc, HASH_KEY));
        System.out.println(MyBase64.encode(enc));
    }
}
