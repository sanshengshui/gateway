package com.aiyolo.common;

import net.sf.json.JSONArray;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class ArrayHelper {

    public static String[] getStringArray(String arrayString) {
        return validJsonArray(arrayString) ?
                getStringArray(JSONArray.fromObject(arrayString)) : null;
    }

    /**
     * 将JSONArray转化为String[]
     * @param jsonArray
     * @return
     */
    public static String[] getStringArray(JSONArray jsonArray) {
        String[] stringArray = new String[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            stringArray[i] = jsonArray.getString(i);
        }
        return stringArray;
    }

    public static String getArrayString(String[] stringArray) {
        if (stringArray == null) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for (int i = 0; i < stringArray.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append("\"" + stringArray[i] + "\"");
        }
        sb.append("]");

        return sb.toString();
    }

    public static boolean validJsonArray(String jsonArray) {
        if (jsonArray != null && jsonArray.length() > 0 &&
                "[".equals(jsonArray.substring(0, 1)) &&
                "]".equals(jsonArray.substring(jsonArray.length() - 1))) {
            return true;
        }
        return false;
    }

    public static Object[] removeNullElement(Object[] anArray) {
        List<Object> _list = new ArrayList<Object>();
        for (Object element : anArray) {
            if (element != null) {
                _list.add(element);
            }
        }
        return _list.toArray(new String[0]);
    }

    public static Object[] removeDuplicateElement(Object[] anArray) {
        return removeDuplicateElement(anArray, true);
    }

    public static Object[] removeDuplicateElement(Object[] anArray, Boolean removeEmpty) {
        if (anArray == null) {
            return null;
        }

        List<Object> _list = new ArrayList<Object>();
        for (Object element : anArray) {
            if (removeEmpty && ObjectUtils.isEmpty(element)) {
                continue;
            }
            if (!_list.contains(element)) {
                _list.add(element);
            }
        }
        return _list.toArray(new String[0]);
    }

}
