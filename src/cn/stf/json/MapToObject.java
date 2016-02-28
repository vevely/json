package cn.stf.json;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by javen on 2016/2/17.
 */
public class MapToObject {

    /**
     * 将map转换成Javabean
     *
     * @param javabean javaBean
     * @param data     map数据
     */
    public static Object format(Map data, Class javabean) {
        Object obj = null;
        try {
            obj = javabean.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Method[] methods = javabean.getDeclaredMethods();

        for (Method method : methods) {
            try {
                if (method.getName().startsWith("set")) {

                    String field = method.getName();

                    field = field.substring(field.indexOf("set") + 3);

                    field = field.toLowerCase().charAt(0) + field.substring(1);

                    Object[] innerObj = new Object[1];

                    innerObj[0] = data.get(field);

                    method.invoke(obj, innerObj);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return obj;
    }
}
