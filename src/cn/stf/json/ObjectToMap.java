package cn.stf.json;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by javen on 2016/2/17.
 */
public class ObjectToMap {

    /**
     * 将javaBean转换成Map
     * @param javaBean
     * @return Map对象
     */
    public static Map<String, Object> format(Object javaBean) {

        Map<String, Object> result = new HashMap();
        Method[] methods = javaBean.getClass().getDeclaredMethods();

        for (Method method : methods) {
            try {
                if (method.getName().startsWith("get")) {
                    String field = method.getName();
                    field = field.substring(field.indexOf("get") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);
                    Object value = method.invoke(javaBean, (Object[]) null);
                    result.put(field, null == value ? null : value);
                }
            } catch (Exception e) {
            }
        }

        return result;
    }

}
