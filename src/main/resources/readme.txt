*数据源工具
	配置：
	 1  可以使用环境参数定义jdbc.properties路径
     2 java ... -Dconfig.path=/etc/www/jdbc -jar executer.jar <br/>
     3 如果环境定义该参数，/etc/www/jdbc路径下必须有文件jdbc.properties
     4 否则需要将jdbc属性文件放置classpath
     5 jdbc密码加密见JdbcCfg.main 加密字符串前缀 PASSWORD_ENCRYPT_PREFIX
     
     
 <pre>
 public class App 
{
    public static void main( String[] args ) throws Exception
 {
        DataSourceProxy proxy = JdbcFactory.getDataSourceProxy("txweb_db");
        Connection conn = proxy.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("select * from infobase limit 12");
            rs = stmt.executeQuery();
            List<InfoBase> list = JdbcUtil.parseListByResultSets(rs, InfoBase.class);
            System.out.println(JsonUtil.toJSONString(list));
        } catch (Throwable t) {

        } finally {
            JdbcUtil.close(rs, stmt, conn);
        }
        System.out.println(JdbcFactory.getFactoryInfo());
    }

}
 </pre>