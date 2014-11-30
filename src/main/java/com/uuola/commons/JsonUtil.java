
package com.uuola.commons;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 *
 * @author txdnet
 */
public  abstract  class JsonUtil {

    // 键名过滤为小写
    private static NameFilter lowerNameFilter = new NameFilter() {
        @Override
        public String process(Object source, String name, Object value) {
            return name.toLowerCase();
        }
    };

    // 对象转为JSON字串,并禁用了循环引用检测
    public static String toJSONString(Object obj) {
        return JSON.toJSONString(obj,
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat);
    }

    // 对象转为JSON字串，键名全为小写,并禁用了循环引用检测
    public static String toLowerKeyJSONString(Object obj) {
        return JSON.toJSONString(obj, lowerNameFilter, 
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat);
    }
    
    // 将json写入输出流
    public static void toJSONString(Object obj, OutputStream output, String encoding) throws IOException{
        SerializeWriter out = new SerializeWriter();
        try {
            JSONSerializer serializer = new JSONSerializer(out);
            serializer.config(SerializerFeature.DisableCircularReferenceDetect, true);
            serializer.config(SerializerFeature.WriteDateUseDateFormat, true);
            serializer.write(obj);
            out.writeTo(output, encoding);
        } finally {
            out.close();
        }
    }

    // JSON字符串转为对象数组列表
    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }

    // JSON字符串转为对象
    public static <T> T toJsonObject(String str, Class<T> type) {
        return JSON.parseObject(str, type);
    }

    // jsonp 返回值
    public static StringBuilder getJsonpStr(String jsonpcall, Object obj) {
        return new StringBuilder(jsonpcall)
                .append("(")
                .append(JsonUtil.toLowerKeyJSONString(obj))
                .append(");");
    }
}
