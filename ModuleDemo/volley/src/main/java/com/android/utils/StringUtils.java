package com.android.utils;

import android.text.TextUtils;


import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigInteger;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StringUtils {

    public static boolean isEmpty(Object obj) {

        return null == obj || "".equals(obj.toString().trim())
                || "null".equalsIgnoreCase(obj.toString().trim());
    }

    public static boolean isBlank(Object obj) {

        return null == obj || "".equals(obj.toString());
    }

    public static boolean isNotEmpty(Object obj) {

        return !isEmpty(obj);
    }

    public static String getSequenceId() {
        String mark = String.valueOf(System.currentTimeMillis());
        return mark;
    }

    public static String getCurrentlyDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date());
    }

    public static String transformDateTime(long t) {
        Date date = new Date(t);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    public static String transformDateTime(long t, String fmt) {
        Date date = new Date(t);
        SimpleDateFormat dateFormat = new SimpleDateFormat(fmt);
        return dateFormat.format(date);
    }

    public static String transformDateTime(String t, String fmt) {
        Date date = new Date(Long.valueOf(t));
        SimpleDateFormat dateFormat = new SimpleDateFormat(fmt);
        return dateFormat.format(date);
    }

    public static String getCurrentlyDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(new Date());
    }

    public static String getUUID() {

        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String generate(String imageUri) {
        byte[] md5 = getMD5(imageUri.getBytes());
        BigInteger bi = new BigInteger(md5).abs();
        return bi.toString(10 + 26);
    }

    private static byte[] getMD5(byte[] data) {
        byte[] hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(data);
            hash = digest.digest();
        } catch (NoSuchAlgorithmException e) {

        }
        return hash;
    }

    /**
     * 从异常中获取有用信息
     * 
     * @param ex
     * @return [参数说明]
     * 
     * @return String [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static String getExceptionMsg(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        return result;
    }

    /**
     * 如果data为null或者"' 返回"" 否则返回本身
     * 
     * @param data
     * @return
     */
    public static String getDataOrEmpty(String data) {
        if (isEmpty(data))
            return "";
        return data;
    }

    public static String getIntegerOrZero(Integer data) {
        if (data == null) {
            data = 0;
        }
        return data + "";
    }

    /**
     * 解析url包含的参数键值对
     * 
     * @param url
     * @param pKey
     * @return
     */
    public static Map<String, String> parseUrlParams(String url, String... pKey) {
        Map<String, String> pMap = new HashMap<String, String>();
        try {
            List<NameValuePair> pList = URLEncodedUtils.parse(URI.create(url),
                "UTF-8");
            for (NameValuePair nvp : pList) {
                for (String key : pKey) {
                    if (key.equals(nvp.getName())) {
                        pMap.put(key, nvp.getValue());
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pMap;
    }


    /**
     * 截取年月日时间串
     * 
     * @param dateStr
     * @return
     */
    public static String clipYMDDate(String dateStr) {
        if (dateStr == null || dateStr.length() < 10) {
            return dateStr;
        }
        return dateStr.substring(0, 10);
    }

    /**
     * 截取月日时分
     * 
     * @param dateStr
     * @return
     */
    public static String clipMDHM(String dateStr) {
        if (dateStr == null || dateStr.length() < 16) {
            return dateStr;
        }
        return dateStr.substring(5, 16);
    }

    /**
     * 截取长字符
     * @param str
     * @return
     */
    public static String clipLongText(String str) {
        if (str != null) {
            final String encoding = "GBK";
            try {
                byte[] b = str.getBytes(encoding);
                if (b.length > 16) {
                    int end = 14;
                    String result = new String(b, 0, end, encoding);
                    if (str.indexOf(result) == -1) {
                        return new String(b, 0, end - 1, encoding) + "...";
                    }
                    return result + "...";
                }
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return str;
    }

    public static String clipLongTextByChineseCount(String str, int count) {
        if (str != null) {
            final String encoding = "GBK";
            try {
                byte[] b = str.getBytes(encoding);
                if (b.length >= (count + 1) * 2) {
                    int end = count * 2;
                    String result = new String(b, 0, end, encoding);
                    if (str.indexOf(result) == -1) {
                        return new String(b, 0, end - 1, encoding) + "...";
                    }
                    return result + "...";
                }
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return str;
    }
    
    /**
     * 获取字符串字节数
     * @param str
     * @return
     */
    public static int getStringByteLength(String str) {
        if (str != null) {
            try {
                return str.getBytes("GBK").length;
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return 0;
    }
    
    /**
     * 去空
     * @param data
     * @return
     */
    public static String trimString(String data) {
        if (data != null) {
            return data.trim();
        }
        return data;
    }

    /**
     * 替换Web换行符
     * @param str
     * @return
     */
    public static String replaceNewlineChar(String str) {
        return TextUtils.isEmpty(str) ? str : str.replace("\n", "<br/>");
    }

}
