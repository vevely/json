package cn.stf.json;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 对象转json字符串，支持List、Map、javabean
 * Created by javen on 2016/1/18.
 */
public class ObjectToJson {


    /**
     * 对外发布json格式化方法
     *
     * @param obj Object
     * @return String
     */
    public static String format(Object obj) {
        StringBuilder json = new StringBuilder();
        format(obj, json);
        return json.toString();
    }


    /**
     * List、Map、Object类型转json格式
     *
     * @param obj
     * @param json
     * @return
     */
    private static StringBuilder format(Object obj, StringBuilder json) {

        if (obj instanceof Map) {
            Map map = (Map) obj;
            //Map 解析
            json.append("{");
            Iterator it = map.keySet().iterator();
            while (it.hasNext()) {
                Object key = it.next();
                //toStringBuilder(key, json);
                //Map中键值一致视为String类型
                json.append("\"");
                json.append(key);
                json.append("\":");
                if (toStringBuilder(map.get(key), json)) {
                    //递归调用
                    format(map.get(key), json);
                }
                json.append(",");
            }
            if (json.lastIndexOf(",") == json.length() - 1) {
                json.deleteCharAt(json.length() - 1);
            }
            json.append("}");
        } else if (obj instanceof List) {
            List list = (List) obj;
            //List 解析
            json.append("[");
            for (int i = 0; i < list.size(); i++) {
                if (toStringBuilder(list.get(i), json)) {
                    //递归调用
                    format(list.get(i), json);
                }
                json.append(",");
            }
            if (json.lastIndexOf(",") == json.length() - 1) {
                json.deleteCharAt(json.length() - 1);
            }
            json.append("]");
        } else if (obj instanceof Object) {
            //JavaBean 解析
            json.append("{");
            Method[] method = obj.getClass().getDeclaredMethods();
            for (int i = 0; i < method.length; i++) {
                String methodName = method[i].getName();
                if (methodName.startsWith("get")) {
                    methodName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
                    json.append("\"");
                    json.append(methodName);
                    json.append("\":");
                    try {
                        Object value = method[i].invoke(obj, null);
                        if (toStringBuilder(value, json)) {
                            //递归调用
                            format(value, json);
                        }
                        json.append(",");
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new StringBuilder("Error");
                    }
                }
            }
            if (json.lastIndexOf(",") == json.length() - 1) {
                json.deleteCharAt(json.length() - 1);
            }
            json.append("}");
        } else {
            json.append(obj);
            json.append(",");
        }
        return json;
    }


    /**
     * 包装类型、字符类型转json格式
     *
     * @param obj
     * @param json
     * @return
     */
    private static boolean toStringBuilder(Object obj, StringBuilder json) {
        boolean flag = false;
        if (obj instanceof Number || obj instanceof Boolean || obj == null) {
            json.append(obj);
        } else if (obj instanceof String) {
            json.append("\"");
            //将字符串中的双引号转为单引号
            json.append(((String) obj).indexOf("\"") > 0 ? ((String) obj).replaceAll("\"", "'") : obj);
            json.append("\"");
        } else {
            flag = true;
        }
        return flag;
    }


}
