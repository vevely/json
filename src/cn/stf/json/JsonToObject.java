package cn.stf.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by javen on 2016/2/17.
 */
public class JsonToObject {

    public static Object format(String json) {

        if(json == null){
            return null;
        }
        json = json.trim();

        List outer = new ArrayList();
        char[] c = json.toCharArray();

        if(c[0]=='{'){
            outer.add(new HashMap());
        }else if(c[0]=='['){
            outer.add(new ArrayList());
        }else{
            return json;
        }
        List values = new ArrayList();

        int start = 0;
        boolean flag = false;
        StringBuilder number = new StringBuilder();

        int floor = 0; //记录集合包含关系
        for (int i = 1; i < c.length-1; i++) {
            if (c[i] == '"') {
                flag = !flag;
                if (flag) {
                    start = i + 1;
                } else {
                    values.add(json.substring(start, i));
                }
                continue;
            }
            if (flag) {
                continue;
            }
            switch (c[i]) {
                case '{':
                    outer.add(new HashMap());
                    if (outer.get(floor) instanceof Map && values.size() == 1) {
                        ((Map) outer.get(floor)).put(values.get(0), outer.get(floor + 1));
                        values = new ArrayList();
                        floor++;
                    } else if (outer.get(floor) instanceof List && values.size() == 0) {
                        ((List) outer.get(floor)).add(outer.get(floor + 1));
                        floor++;
                    } else {
                        System.err.println("({)匹配异常！");
                        return "Error";
                    }
                    break;
                case '[':
                    outer.add(new ArrayList());
                    if (outer.get(floor) instanceof Map && values.size() == 1) {
                        ((Map) outer.get(floor)).put(values.get(0), outer.get(floor + 1));
                        values = new ArrayList();
                        floor++;
                    } else if (outer.get(floor) instanceof List && values.size() == 0) {
                        ((List) outer.get(floor)).add(outer.get(floor + 1));
                        floor++;
                    } else {
                        System.err.println("([)匹配异常！");
                        return "Error";
                    }
                    break;
                case '}':
                    if (outer.get(floor) instanceof Map && values.size() == 2) {
                        ((Map) outer.get(floor)).put(values.get(0), values.get(1));
                        outer.remove(floor);
                        values = new ArrayList();
                        floor--;
                    } else if (outer.get(floor) instanceof List && values.size() == 1) {
                        ((List) outer.get(floor)).add(values.get(0));
                        outer.remove(floor);
                        values = new ArrayList();
                        floor--;
                    } else {
                        System.err.println("(})匹配异常！");
                        return "Error";
                    }
                    break;
                case ']':
                    if (outer.get(floor) instanceof Map && values.size() == 2) {
                        ((Map) outer.get(floor)).put(values.get(0), values.get(1));
                        outer.remove(floor);
                        values = new ArrayList();
                        floor--;
                    } else if (outer.get(floor) instanceof List && values.size() == 1) {
                        ((List) outer.get(floor)).add(values.get(0));
                        outer.remove(floor);
                        values = new ArrayList();
                        floor--;
                    } else {
                        System.err.println("(])匹配异常！");
                        return "Error";
                    }
                    break;
                case ',':
                    if (values.size() == 2 && outer.get(floor) instanceof Map) {
                        ((Map) outer.get(floor)).put(values.get(0), values.get(1));
                        values = new ArrayList();
                    } else if (values.size() == 1 && outer.get(floor) instanceof List) {
                        ((List) outer.get(floor)).add(values.get(0));
                        values = new ArrayList();
                    } else if (values.size() > 0) {
                        return "Error";
                    }
                    break;

                case ':': break;
                //过滤特殊字符
                case '\b': break;
                case '\f': break;
                case '\n': break;
                case '\r': break;
                case '\t': break;
                case ' ' : break;
                default:
                    number.append(c[i]);//数值添加
                    if ((c[i + 1] < 46) || (c[i + 1] > 57)) {
                        String sn = number.toString();
                        if (sn.indexOf(".") > 0) {
                            values.add(Double.valueOf(sn));
                        } else {
                            if ((Integer.MIN_VALUE <= Long.valueOf(sn)) && (Long.valueOf(sn) <= Integer.MAX_VALUE)) {
                                values.add(Integer.valueOf(sn));
                            } else {
                                values.add(Long.valueOf(sn));
                            }
                        }
                        number = new StringBuilder();
                    }
            }
        }

        return outer.get(0);
    }

    public static Object format(String json,Class clazz) {
        return MapToObject.format((Map)(JsonToObject.format(json)),clazz);
    }

}
