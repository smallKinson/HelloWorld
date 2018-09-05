package com.android.volley.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class VolleyHelper {

    private static final SparseArray<String> errorStatus = new SparseArray<String>();

    private static final Gson gson = new Gson();

    public static final String SERVER_ERROR = "服务器累坏了:";

    public static final String UNKNOWN_ERROR = SERVER_ERROR;

    public static final String NETWORK_ERROR = "网络不给力";

    public static final int LOGIN_SESSION_ERROR = 604;

    public static final int ACCESS_TOKEN_EXPIRED = 666;

    /*
     * 常用接口状态错误码
     */
    static {
        errorStatus.put(600, "账号为空");
        errorStatus.put(601, "密码为空");
        errorStatus.put(602, "账号或密码错误");
        errorStatus.put(603, "账号已存在");
        errorStatus.put(604, "登录超时或sid不正确");
        errorStatus.put(605, "手机号码为空");
        errorStatus.put(606, "邮箱为空");
        errorStatus.put(607, "手机号码已使用");
        errorStatus.put(608, "邮箱已使用");
        errorStatus.put(609, "用户名已使用");
        errorStatus.put(610, "账号不存在");
        errorStatus.put(611, "来源不存在");
        errorStatus.put(612, "第三方平台类型为空");
        errorStatus.put(613, "unionId为空");
        errorStatus.put(614, "昵称为空");
        errorStatus.put(615, "锁定");
        errorStatus.put(630, "sid或vid都为空");

        errorStatus.put(401, "用户未进行登录");
        errorStatus.put(403, "用户没有操作权限");
        errorStatus.put(500, "服务器发生未知错误");
    }

    /**
     * 解析接口状态码
     *
     * @param jsonData
     * @return
     */
    public static int parseStatusCode(String jsonData) {
        int statusCode = -1;
        try {
            statusCode = Integer.parseInt(parseString(jsonData,
                    IResponse.STATUS_CODE));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusCode;
    }

    /**
     * 解析接口状态描述信息
     *
     * @param jsonData
     * @return
     */
    public static String parseStatusMsg(String jsonData) {
        return parseString(jsonData, IResponse.STATUS_MSG);
    }

    /**
     * 解析jsonObject对象
     *
     * @param json
     * @param parameter
     * @return
     */
    public static JsonObject parseJsonObject(String json, String parameter) {
        // 创建一个JsonParser
        JsonParser parser = new JsonParser();
        JsonObject result = null;
        try {
            JsonElement jsonEl = parser.parse(json);
            JsonObject jsonObj = jsonEl.getAsJsonObject();// 转换成Json对象
            result = jsonObj.get(parameter).getAsJsonObject();// parameter节点
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return result;
    }

    /**
     * 解析jsonArray数组
     *
     * @param json
     * @param parameter
     * @return
     */
    public static JsonArray parseJsonArray(String json, String parameter) {
        // 创建一个JsonParser
        JsonParser parser = new JsonParser();
        JsonArray result = null;
        try {
            JsonElement jsonEl = parser.parse(json);
            JsonObject jsonObj = jsonEl.getAsJsonObject();// 转换成Json对象
            result = jsonObj.get(parameter).getAsJsonArray();// parameter节点
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return result;
    }

    /**
     * 解析json字符串
     *
     * @param json
     * @param parameter
     * @return
     */
    public static String parseString(String json, String parameter) {
        // 创建一个JsonParser
        JsonParser parser = new JsonParser();
        String result = "";
        try {
            JsonElement jsonEl = parser.parse(json);
            JsonObject jsonObj = null;
            jsonObj = jsonEl.getAsJsonObject();// 转换成Json对象
            result = jsonObj.get(parameter).getAsString();// parameter节点
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return result;
    }

    /**
     * 获取返回的业务实体信息
     *
     * @param <T>
     * @param
     * @return
     */
    public static <T> T getData(String json, Class<T> cls) {
        T t = null;
        try {
            t = gson.fromJson(json, cls);
        } catch (Exception e) {
            e.printStackTrace();
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
    public static <T> ArrayList<T> getList(String json, Type type,
                                           String parameter) {
        // 创建一个JsonParser
        JsonParser parser = new JsonParser();
        JsonObject jsonObj = null;
        ArrayList<T> result = null;
        try {
            JsonElement jsonEl = parser.parse(json);
            jsonObj = jsonEl.getAsJsonObject();// 转换成Json对象
            JsonArray paraStr = jsonObj.get(parameter).getAsJsonArray();// parameter节点
            result = gson.fromJson(paraStr, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 网络是否正常
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkAvailable(Context context) {
        try {
            ConnectivityManager nw = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = nw.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isAvailable();

        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }

    }

    /**
     * 拼接接口url及参数,编码
     *
     * @param url
     * @param paramsMap
     * @return
     */
    public static String concatGetUrlParams(String url,
                                            Map<String, String> paramsMap) {
        if (paramsMap != null) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            for (Entry<String, String> key : paramsMap.entrySet()) {
                if (key.getKey() != null && key.getValue() != null) {
                    NameValuePair nameValuePair = new BasicNameValuePair(key
                            .getKey().toString(), key.getValue().toString());
                    params.add(nameValuePair);
                }
            }
            String pStr = URLEncodedUtils.format(params, HTTP.UTF_8);
            if (TextUtils.isEmpty(pStr)) {
                return url;
            }
            return url += "?" + pStr;
        }
        return url;
    }

    /**
     * 拼接接口url及参数,不编码
     * @param url
     * @param paramsMap
     * @return
     */
    public static String concatGetUrlUnEncodingParams(String url,
                                                      Map<String, String> paramsMap) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(url).append("?");
        if (paramsMap != null) {
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                strBuilder.append(entry.getKey()).append("=")
                        .append(entry.getValue()).append("&");
            }
        }
        strBuilder.deleteCharAt(strBuilder.length() - 1);
        return strBuilder.toString();
    }

}
