package com.android.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Kinson on 2018/8/28.
 */

public class JsonHelper {
    public static final String TAG = JsonHelper.class.getName();
    static Gson mGson;
    static {
        mGson = new Gson();
    }

    /**
     * 获取返回的业务实体信息
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getData(String json, Class<T> cls) {
        T t = null;
        try {
            t = mGson.fromJson(json, cls);
        } catch (Exception e) {
            if (e != null) {
                LogUtil.w(TAG, StringUtils.getExceptionMsg(e));
            }
        }
        return t;
    }

    /**
     * 从Json中获取某个List
     *
     * @param json
     * @param type
     *            java.lang.reflect.Type type = new
     *            com.google.gson.reflect.TypeToken<List<cls>>() { }.getType();
     * @param parameter
     *            List对应的参数名
     * @return
     */
    @SuppressWarnings("unchecked")
    public static ArrayList getList(String json, Type type, String parameter) {
        // 创建一个JsonParser
        JsonParser parser = new JsonParser();
        JsonObject jsonObj = null;
        ArrayList result = null;
        try {
            JsonElement jsonEl = parser.parse(json);
            jsonObj = jsonEl.getAsJsonObject();// 转换成Json对象
            JsonArray paraStr = jsonObj.get(parameter).getAsJsonArray();// parameter节点
            result = mGson.fromJson(paraStr, type);
        } catch (Exception e) {
            if (e != null) {
                LogUtil.w(TAG, StringUtils.getExceptionMsg(e));
            }
        }
        return result;
    }
    /**
     * 从Json中获取某个List
     *
     * @param json
     * @param type
     *            java.lang.reflect.Type type = new
     *            com.google.gson.reflect.TypeToken<List<cls>>() { }.getType();
     * @return
     */
    @SuppressWarnings("unchecked")
    public static ArrayList getList(String json, Type type) {
        // 创建一个JsonParser
        JsonParser parser = new JsonParser();
        ArrayList result = null;
        try {
            JsonElement jsonEl = parser.parse(json);
            JsonArray paraStr = jsonEl.getAsJsonArray();// parameter节点
            result = mGson.fromJson(paraStr, type);
        } catch (Exception e) {
            if (e != null) {
                LogUtil.w(TAG, StringUtils.getExceptionMsg(e));
            }
        }
        return result;
    }

    /**
     * List[Object] to Json str
     * */
    public static <PK> String getJsonStrByListObj2(List<PK> lst) {
        StringBuilder strJson = new StringBuilder("[");
        Gson gson = new Gson();
        for (int i = 0; i < lst.size(); i++) {
            if (i != lst.size() - 1) {
                strJson.append(gson.toJson(lst.get(i)) + ",");
            } else {
                strJson.append(gson.toJson(lst.get(i)));
            }
        }
        strJson = strJson.append("]");
        return strJson.toString();
    }

    /**
     * Json str to Object
     * */
    public <PK> PK getObjByJsonStr(String jsonStr, Class<PK> clazz) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        try {
            if (jsonStr != null) {
                PK pk = gson.fromJson(jsonStr.toString(), clazz);
                return pk;
            }
        } catch (JsonSyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String parserMap(Map<String, String> map) {
        return mGson.toJson(map);
    }

    /***************************************************************************
     * 解析Json到Map V2，支持潜入基本类型及数组
     *
     * @param json
     * @return
     */
    public static Object parserJsonToMapV2(String json) {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(json);
        if (jsonElement.isJsonArray()) {
            return parserArrays(jsonElement.getAsJsonArray());
        } else if (jsonElement.isJsonNull()) {
            return null;
        } else if (jsonElement.isJsonObject()) {
            return parserJsonObjecet(jsonElement.getAsJsonObject());
        } else if (jsonElement.isJsonPrimitive()) {
            return jsonElement.getAsString();
        }
        return null;
    }

    private static List<Object> parserArrays(JsonArray jsonArray) {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonElement jsonElement = jsonArray.get(i);
            if (jsonElement.isJsonNull()) {
                list.add("null");
            } else if (jsonElement.isJsonArray()) {
                list.add(parserArrays(jsonElement.getAsJsonArray()));
            } else if (jsonElement.isJsonObject()) {
                list.add(parserArrays(jsonElement.getAsJsonArray()));
            } else if (jsonElement.isJsonPrimitive()) {
                list.add(jsonElement.getAsString());
            }
        }
        return list;
    }

    private static Map<String, Object> parserJsonObjecet(JsonObject jsonObject) {
        Map<String, Object> map = new HashMap<String, Object>();
        Set<Map.Entry<String, JsonElement>> set = jsonObject.entrySet();
        for (Map.Entry<String, JsonElement> entry : set) {
            if (entry.getValue().isJsonPrimitive()) {
                map.put(entry.getKey(), entry.getValue().getAsString());
            } else if (entry.getValue().isJsonNull()) {
                map.put(entry.getKey(), "null");
            } else if (entry.getValue().isJsonArray()) {
                map.put(entry.getKey(), parserArrays(entry.getValue().getAsJsonArray()));
            } else if (entry.getValue().isJsonObject()) {
                map.put(entry.getKey(), parserJsonObjecet(entry.getValue().getAsJsonObject()));
            }
        }
        return map;
    }

    /**
     * 从Json中获取code
     * @param statusCode
     * @param json
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String getCode(String statusCode,String json) {
        // 创建一个JsonParser
        JsonParser parser = new JsonParser();
        String result = "";
        try {
            JsonElement jsonEl = parser.parse(json);
            JsonObject jsonObj = null;
            jsonObj = jsonEl.getAsJsonObject();// 转换成Json对象
            result = jsonObj.get(statusCode).getAsString();// parameter节点
        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }

    /**
     * 从Json中获取某个String
     *
     * @param json
     * @param parameter
     *            String对应的参数名
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String getString(String json, String parameter) {
        // 创建一个JsonParser
        JsonParser parser = new JsonParser();
        String result = "";
        try {
            JsonElement jsonEl = parser.parse(json);
            JsonObject jsonObj = null;
            jsonObj = jsonEl.getAsJsonObject();// 转换成Json对象
            result = jsonObj.get(parameter).getAsString();// parameter节点
        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }

    /**
     * 从Json中获取parameter对应实体信息
     *
     * @param json
     *            服务端返回的JSON
     * @param cls
     * @param parameter
     *            实体的参数名
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getData(String json, Class<T> cls, String parameter) {
        // 创建一个JsonParser
        JsonParser parser = new JsonParser();
        JsonObject jsonObj = null;
        T t = null;
        try {
            JsonElement jsonEl = parser.parse(json);
            jsonObj = jsonEl.getAsJsonObject();// 转换成Json对象
            JsonObject result = jsonObj.get(parameter).getAsJsonObject();// parameter节点
            t = mGson.fromJson(result, cls);
        } catch (Exception e) {
            if (e != null) {
                LogUtil.w(TAG, StringUtils.getExceptionMsg(e));
            }
        }
        return t;
    }

    /**
     * 对象集合转为json字符串
     * @param dataList
     * @return
     */
    public static <T> String convertList2Json(List<T> dataList) {
        try {
            return mGson.toJson(dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对象转为json字符串
     * @param t
     * @param <T>
     * @return
     */
    public static <T> String convertObject2Json(T t) {
        try {
            return new Gson().toJson(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
