*数据源工具
	配置：
	 1  可以使用环境参数定义jdbc.properties路径
     2 java ... -Dconfig.path=/etc/www/jdbc -jar executer.jar <br/>
     3 如果环境定义该参数，/etc/www/jdbc路径下必须有文件jdbc.properties
     4 否则需要将jdbc属性文件放置classpath
     5 jdbc密码加密见JdbcCfg.main 加密字符串前缀 PASSWORD_ENCRYPT_PREFIX