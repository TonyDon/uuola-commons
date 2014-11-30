/*
 * @(#)JsonUtilTest.java 2014年11月30日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txcms.util.test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import com.uuola.commons.JsonUtil;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2014年11月30日
 * </pre>
 */
public class JsonUtilTest {

    /**
     * @param args
     * @throws FileNotFoundException 
     */
    public static void main(String[] args) throws FileNotFoundException {
        // TODO Auto-generated method stub
        // StringReader strReader = new StringReader("[{name:'aa',age:12}]");
        Reader reader = new InputStreamReader(JsonUtilTest.class.getResourceAsStream("/json.txt"));
        List<Men> mens = JsonUtil.parseReader(reader);
        System.out.println(JsonUtil.toJSONString(mens));
    }

    class Men{
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public Integer getAge() {
            return age;
        }
        
        public void setAge(Integer age) {
            this.age = age;
        }
        
        private String name;
        private Integer age;
        private String[] jobs;
        private House house;
        
        public String[] getJobs() {
            return jobs;
        }

        
        public void setJobs(String[] jobs) {
            this.jobs = jobs;
        }

        
        public House getHouse() {
            return house;
        }

        
        public void setHouse(House house) {
            this.house = house;
        }
        
    }
    
    class House{
        private String name;

        
        public String getName() {
            return name;
        }

        
        public void setName(String name) {
            this.name = name;
        }
        
    }
}
