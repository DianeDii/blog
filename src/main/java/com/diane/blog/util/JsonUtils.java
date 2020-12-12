package com.diane.blog.util;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mysql.cj.xdevapi.JsonArray;

import java.util.*;
/**
 * @author dianedi
 * @date 2020/12/12 20:15
 * @Destription List / Set /String /map  <--> JSON /JsonObject /JsonArray 互转工具类
 */
public class JsonUtils {
    /**
     * JSON字符串转换成对象
     *
     * @param jsonString
     *            需要转换的字符串
     * @param type
     *            需要转换的对象类型
     * @return 对象
     */
    public static <T> T fromJson(String jsonString, Class<T> type) {
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        return (T) JSONObject.toJavaObject(jsonObject, type);
    }

    /**
     * 对象转换为json字符串
     * @param obj
     * @return
     */
    public static String logObjToString(Object obj){
        return JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 将JSONArray对象转换成list集合
     *
     * @param jsonArr
     * @return
     */
    public static List<Object> jsonToList(JSONArray jsonArr) {
        List<Object> list = new ArrayList<Object>();
        for (Object obj : jsonArr) {
            if (obj instanceof JSONArray) {
                list.add(jsonToList((JSONArray) obj));
            } else if (obj instanceof JSONObject) {
                list.add(jsonToMap((JSONObject) obj));
            } else {
                list.add(obj);
            }
        }
        return list;
    }

    /**
     * 将list集合转换成JSONArray对象
     * @param list
     * @param <T>
     * @return
     */
    public static<T> JSONArray listToJsonArray(List<T> list){

        JSONArray array= JSONArray.parseArray(JSON.toJSONString(list));
        return array;
    }

    /**
     * 将json字符串转换成map对象
     *
     * @param json
     * @return
     */
    public static Map<String, Object> jsonToMap(String json) {
        JSONObject obj = JSONObject.parseObject(json);
        return jsonToMap(obj);
    }

    /**
     * 将JSONObject转换成map对象
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> jsonToMap(JSONObject obj) {
        Set<?> set = obj.keySet();
        Map<String, Object> map = new HashMap<String, Object>(set.size());
        for (Object key : obj.keySet()) {
            Object value = obj.get(key);
            if (value instanceof JSONArray) {
                map.put(key.toString(), jsonToList((JSONArray) value));
            } else if (value instanceof JSONObject) {
                map.put(key.toString(), jsonToMap((JSONObject) value));
            } else {
                map.put(key.toString(), obj.get(key));
            }

        }
        return map;
    }

    /**
     * map转JSONObject
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static<K,V> JSONObject mapToJSONObject(Map<K,V> map){
        return JSONObject.parseObject(JSON.toJSONString(map));
    }

    /**
     * JSONObject转map
     * @return
     */
    public static Map JSONObjectToMap(JSONObject jsonObject){
        return JSONObject.toJavaObject(jsonObject, Map.class);
    }
}
