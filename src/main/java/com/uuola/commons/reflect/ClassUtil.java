/*
 * @(#)ClassUtil.java 2013-7-27
 * 
 * Copy Right@ uuola
 */

package com.uuola.commons.reflect;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uuola.commons.StringUtil;
import com.uuola.commons.constant.CST_ENCODING;

/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2013-7-27
 * </pre>
 */
public  abstract  class ClassUtil {
    
    private static Logger log = LoggerFactory.getLogger(ClassUtil.class);

    /**
     * 通过实体获取表名
     * 
     * @return
     */
    public static String getTableName(Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        if (null == table || StringUtil.isEmpty(table.name())) {
            throw new RuntimeException(String.format("Entity Class:%s , not set table name!",
                    clazz.getCanonicalName()));
        }
        return table.name();
    }
    
    /**
     * 加载包路径下的class
     * @param pack 包路径 : eg. com.uuola.commons or com/uuola/commons
     * @param recursive 是否递归
     * @return
     */
    public static Set<Class<?>> getClasses(String pack, boolean recursive) {
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        String packageName = pack;
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        ClassLoader classLoader = ClassUtil.getDefaultClassLoader();
        try {
            dirs = classLoader.getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), CST_ENCODING.UTF8);
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    findClassAndSetByFile(classLoader, packageName, filePath, recursive, classes);
                } else if ("jar".equals(protocol)) {
                    JarFile jar;
                    try {
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        // 从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            System.out.println(name);
                            // 如果是以/开头的
                            if (name.charAt(0) == '/') {
                                // 获取后面的字符串
                                name = name.substring(1);
                            }
                            if (name.startsWith(packageDirName) && name.endsWith(".class")) {
                                name = name.replace('/', '.');
                                int lastExtIdx = name.length() - 6;
                                String className = name.substring(0, lastExtIdx);
                                if (!recursive && className.indexOf('.', packageName.length() + 1) != -1) {
                                    continue;
                                }
                                try {
                                    classes.add(classLoader.loadClass(className));
                                } catch (Throwable e) {
                                    log.error("", e);
                                }
                            }
                        }
                    } catch (Throwable e) {
                        log.error("", e);
                    }
                }
            }
        } catch (Throwable e) {
            log.error("", e);
        }
        return classes;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     * 
     * @param packageName
     * @param packagePath
     * @param recursive
     * @return Class Set
     */
    public static void findClassAndSetByFile(ClassLoader classLoader, String packageName, String packagePath, final boolean recursive, Set<Class<?>> classes) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return ;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        // 循环所有文件
        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findClassAndSetByFile(classLoader, packageName + "." + file.getName(), 
                        file.getAbsolutePath(), recursive, classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    classes.add(classLoader.loadClass(packageName + '.' + className));
                } catch (Exception e) {
                    log.error("", e);
                }
            }
        }
    }

    /**
     * 得到缺省类加载器
     * @return
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        }
        catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassUtil.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                }
                catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }
}
