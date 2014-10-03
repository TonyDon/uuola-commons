/*
 * @(#)JdbcFactory.java 2013-10-26
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.commons.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.tomcat.jdbc.pool.DataSourceProxy;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uuola.commons.StringUtil;
import com.uuola.commons.coder.DESede;
import com.uuola.commons.coder.MyBase64;
import com.uuola.commons.constant.CST_CHAR;


/**
 * <pre>
 * 加载JDBC配置工厂类, 该工厂使用tomcat-jdbc创建数据源
 * @author tangxiaodong
 * 创建日期: 2013-10-26
 * </pre>
 */
public class JdbcFactory {
    
    private static Logger logger = LoggerFactory.getLogger(JdbcFactory.class);


    /**
     * 多数据源MAP
     */
    private static Map<String, DataSourceProxy> dataSourceProxyMap = initDataSourceProxyMap();
    
    /**
     * 初始化TOMCAT JDBC POOL PROXY 配置
     */
    private static Map<String, DataSourceProxy> initDataSourceProxyMap() {
        logger.info("JdbcFactory.initDataSourceProxyMap() ...");
        // 加载配置文件
        Properties jdbcProperties = loadJdbcProperties();
        if (null == jdbcProperties) {
            logger.info("JdbcFactory init() jdbc properties is null.");
            return Collections.emptyMap();
        }
        String dsNameList = jdbcProperties.getProperty(JdbcCfg.JDBC_DATASOURCE_NAME_LIST);
        if(StringUtil.isEmpty(dsNameList)){
            dsNameList = JdbcCfg.DEFAULT_DATASOURCE_NAME;
        }
        String[] dsNameArray = StringUtil.split(dsNameList, CST_CHAR.CHAR_COMMA);
        Map<String, DataSourceProxy> dataSourceProxyMap = new HashMap<String, DataSourceProxy>();
        for(String name : dsNameArray){
            if(StringUtil.isNotEmpty(name)){
                String dsName = name.trim();
                dataSourceProxyMap.put(dsName, buildDataSourceProxy(dsName, jdbcProperties));
            }
        }
        return dataSourceProxyMap;
    }
    
    private static DataSourceProxy buildDataSourceProxy(String name, Properties jdbcProperties) {
        PoolProperties poolProperties = new PoolProperties();
        boolean isDefautName = JdbcCfg.DEFAULT_DATASOURCE_NAME.equals(name);

        /**
         * 设置驱动类名
         */
        String driverClass = jdbcProperties.getProperty(getJdbcParamName(isDefautName, name,
                JdbcCfg.PARAMS_DRIVER_CLASS));
        if (StringUtil.isNotEmpty(driverClass)) {
            poolProperties.setDriverClassName(driverClass);
        }

        /**
         * 设置连接字符
         */
        String url = jdbcProperties
                .getProperty(getJdbcParamName(isDefautName, name, JdbcCfg.PARAMS_CONNECTION_URL));
        if (StringUtil.isNotEmpty(url)) {
            poolProperties.setUrl(url);
        }

        /**
         * 设置用户名
         */
        String username = jdbcProperties
                .getProperty(getJdbcParamName(isDefautName, name, JdbcCfg.PARAMS_USERNAME));
        if (StringUtil.isNotEmpty(username)) {
            poolProperties.setUsername(username);
        }

        /**
         * 设置连接密码
         */
        String password = jdbcProperties
                .getProperty(getJdbcParamName(isDefautName, name, JdbcCfg.PARAMS_PASSWORD));
        if (StringUtil.isNotEmpty(password)) {
            poolProperties.setPassword(decrypt(password));
        }

        /**
         * 设置初始化连接数
         */
        String minSize = jdbcProperties
                .getProperty(getJdbcParamName(isDefautName, name, JdbcCfg.PARAMS_MIN_SIZE));
        if (StringUtil.isNotEmpty(minSize)) {
            poolProperties.setInitialSize(Integer.parseInt(minSize));
        }
        
        /**
         * 设置最小空闲数
         */
        String minIdle = jdbcProperties
                .getProperty(getJdbcParamName(isDefautName, name, JdbcCfg.PARAMS_MIN_IDLE));
        if(StringUtil.isNotEmpty(minIdle)){
            poolProperties.setMinIdle(Integer.parseInt(minIdle));
        }
        
        /**
         * 设置最大等待时间
         */
        String maxWait = jdbcProperties
                .getProperty(getJdbcParamName(isDefautName, name, JdbcCfg.PARAMS_MAX_WAIT));
        if(StringUtil.isNotEmpty(maxWait)){
            poolProperties.setMaxWait(Integer.parseInt(maxWait));
        }
        
        /**
         * 最大活动链接数
         */
        String maxActive = jdbcProperties
                .getProperty(getJdbcParamName(isDefautName, name, JdbcCfg.PARAMS_MAX_ACTIVE));
        if(StringUtil.isNotEmpty(maxActive)){
            poolProperties.setMaxActive(Integer.parseInt(maxActive));
        }
        
        /**
         * 设置连接验证查询SQL
         */
        String validationQuery = jdbcProperties
                .getProperty(getJdbcParamName(isDefautName, name, JdbcCfg.PARAMS_VALIDATION_QUERY));
        if(StringUtil.isNotEmpty(validationQuery)){
            poolProperties.setValidationQuery(validationQuery);
        }
        
        /**
         * 设置连接验证时间间隔
         */
        String validationInterval = jdbcProperties
                .getProperty(getJdbcParamName(isDefautName, name, JdbcCfg.PARAMS_VALIDATION_INTERVAL));
        if(StringUtil.isNotEmpty(validationInterval)){
            poolProperties.setValidationInterval(Long.parseLong(validationInterval));
        }
        
        /**
         * tomcat-jdbc拦截器
         */
        String jdbcInterceptors = jdbcProperties
                .getProperty(getJdbcParamName(isDefautName, name, JdbcCfg.PARAMS_JDBCINTERCEPTORS));
        if(StringUtil.isNotEmpty(jdbcInterceptors)){
            poolProperties.setJdbcInterceptors(jdbcInterceptors);
        }
        
        /**
         * 是否自动终结连接
         */
        String removeAbandoned = jdbcProperties
                .getProperty(getJdbcParamName(isDefautName, name, JdbcCfg.PARAMS_REMOVEABANDONED));
        if(StringUtil.isNotEmpty(removeAbandoned)){
            poolProperties.setRemoveAbandoned(Boolean.parseBoolean(removeAbandoned));
        }
        
        /**
         * 在超时多少秒后执行连接终结
         */
        String removeAbandonedTimeout = jdbcProperties
                .getProperty(getJdbcParamName(isDefautName, name, JdbcCfg.PARAMS_REMOVEABANDONEDTIMEOUT));
        if(StringUtil.isNotEmpty(removeAbandonedTimeout)){
            poolProperties.setRemoveAbandonedTimeout(Integer.parseInt(removeAbandonedTimeout));
        }
        
        /**
         * 是否打印终结连接日志
         */
        String logAbandoned = jdbcProperties
                .getProperty(getJdbcParamName(isDefautName, name, JdbcCfg.PARAMS_LOGABANDONED));
        if(StringUtil.isNotEmpty(logAbandoned)){
            poolProperties.setLogAbandoned(Boolean.parseBoolean(logAbandoned));
        }
        
        /**
         * 在从池中取出连接前进行检验
         */
        String testOnBorrow = jdbcProperties
                .getProperty(getJdbcParamName(isDefautName, name, JdbcCfg.PARAMS_TESTONBORROW));
        if(StringUtil.isNotEmpty(testOnBorrow)){
            poolProperties.setTestOnBorrow(Boolean.parseBoolean(testOnBorrow));
        }
        
        /**
         * 是否在空闲时检验
         */
        String testWhileIdle = jdbcProperties
                .getProperty(getJdbcParamName(isDefautName, name, JdbcCfg.PARAMS_TESTWHILEIDLE));
        if(StringUtil.isNotEmpty(testWhileIdle)){
            poolProperties.setTestWhileIdle(Boolean.parseBoolean(testWhileIdle));
        }
        
        /**
         * 是否在返回连接时检验
         */
        String testOnReturn = jdbcProperties
                .getProperty(getJdbcParamName(isDefautName, name, JdbcCfg.PARAMS_TESTONRETURN));
        if(StringUtil.isNotEmpty(testOnReturn)){
            poolProperties.setTestOnReturn(Boolean.parseBoolean(testOnReturn));
        }
        
        /**
         *  timeBetweenEvictionRunsMillis 和 minEvictableIdleTimeMillis，可以持续更新连接池中的连接对象，
         *  当 timeBetweenEvictionRunsMillis 大于0时，
         *  每过timeBetweenEvictionRunsMillis 时间，就会启动一个线程，
         *  校验连接池中闲置时间超过minEvictableIdleTimeMillis的连接对象
         */
        String timeBetweenEvictionRunsMillis = jdbcProperties
                .getProperty(getJdbcParamName(isDefautName, name, JdbcCfg.PARAMS_TIMEBETWEENEVICTIONRUNSMILLIS));
        if(StringUtil.isNotEmpty(timeBetweenEvictionRunsMillis)){
            poolProperties.setTimeBetweenEvictionRunsMillis(Integer.parseInt(timeBetweenEvictionRunsMillis));
        }
        
        String minEvictableIdleTimeMillis = jdbcProperties
                .getProperty(getJdbcParamName(isDefautName, name, JdbcCfg.PARAMS_MINEVICTABLEIDLETIMEMILLIS));
        if(StringUtil.isNotEmpty(minEvictableIdleTimeMillis)){
            poolProperties.setMinEvictableIdleTimeMillis(Integer.parseInt(minEvictableIdleTimeMillis));
        }
        
        return new DataSourceProxy(poolProperties);
    }

    private static String decrypt(String password) {
        String encPass = password.trim();
        int encPrefixIdx = encPass.indexOf(JdbcCfg.PASSWORD_ENCRYPT_PREFIX);
        if (encPrefixIdx >= 0) {
            try{
            encPass = encPass.substring(JdbcCfg.PASSWORD_ENCRYPT_PREFIX.length() + encPrefixIdx);
            encPass = DESede.decrypt(MyBase64.decode(encPass), JdbcCfg.HASH_KEY);
            }catch(Exception e){
                logger.error("decrypt error.", e);
                throw new RuntimeException(e);
            }
        }
        return encPass;
    }

    /**
     * 如果是缺省数据源名，则只使用jdbc参数名，否则为 ： 数据源名.参数名
     * @param isDefautName
     * @param name
     * @param jdbcParamName
     * @return
     */
    private static String getJdbcParamName(boolean isDefautName, String name, String jdbcParamName) {
        return isDefautName ? jdbcParamName : name.concat(CST_CHAR.STR_DOT).concat(jdbcParamName) ;
    }

    private static Properties loadJdbcProperties(){
        String jdbcCfgPath = System.getProperty(JdbcCfg.ENV_JDBC_CONFIG_PATH);
        Properties cfg = new Properties();
        InputStream is = null;
        try {
            if (StringUtil.isNotEmpty(jdbcCfgPath)) {
                is = new FileInputStream(new File(jdbcCfgPath, JdbcCfg.ENV_JDBC_PROPERTIES_NAME));
                logger.info(JdbcCfg.ENV_JDBC_PROPERTIES_NAME + " found in " + jdbcCfgPath);
                cfg.load(is);
            } else {
                is = JdbcFactory.class.getClassLoader().getResourceAsStream(JdbcCfg.ENV_JDBC_PROPERTIES_NAME);
                logger.info(JdbcCfg.ENV_JDBC_PROPERTIES_NAME + " found in Classpath .");
                cfg.load(is);
            }
        } catch (Exception e) {
            logger.error("load jdbc.properties fail.", e);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return cfg;
    }

    /**
     * 根据数据源名称获取dataSource代理对象
     * @param dataSourceName
     * @return
     */
    public static DataSourceProxy getDataSourceProxy(String dataSourceName) {
        DataSourceProxy proxy = dataSourceProxyMap.get(dataSourceName);
        if (null == proxy) {
            throw new RuntimeException(JdbcFactory.class.getCanonicalName()
                    + ".getDataSourceProxy("+ dataSourceName + ")- DataSourceProxy Is Null!");
        }
        return proxy;
    }
    
    /**
     * 得到缺省数据源代理对象
     * @return
     */
    public static DataSourceProxy getDataSourceProxy(){
        return getDataSourceProxy(JdbcCfg.DEFAULT_DATASOURCE_NAME);
    }
    
    public static String getFactoryInfo(){
        return dataSourceProxyMap.toString();
    }
}
