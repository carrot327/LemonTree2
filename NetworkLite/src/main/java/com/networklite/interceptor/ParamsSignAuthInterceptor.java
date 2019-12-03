package com.networklite.interceptor;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.networklite.util.GsonUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * @author linqi
 * @description 参数签名验证拦截器
 */
public class ParamsSignAuthInterceptor implements Interceptor {

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String UTF_8 = "UTF-8";
    private static final String API_SECURITY_NONCESTR = "noncestr";
    private static final String API_SECURITY_TIMESTAMP = "timestamp";
    private static final String API_SECURITY_APP_ID = "appId";
    private static final String API_SECURITY_SIGNATURE = "signature";

    private Callback callback;
    private List<String> filterList;
    private long timeOffset = 0L;
    private String appId;
    private String tag;
    private String appKey;

    public ParamsSignAuthInterceptor(Callback callback) {
        if (callback == null) {
            throw new RuntimeException("callback can not be null!");
        }
        this.callback = callback;
        this.appId = callback.getAppId();
        this.tag = callback.generateLogTag();
        this.appKey = getAppKey(appId);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        String url = request.url().toString();
        ParamsSignAuthHelper helper = new ParamsSignAuthHelper(request.method() + " " + decode(url));

        boolean useParamsSignAuth = true;
        filterList = callback.getFilterList();
        if (filterList != null && filterList.size() > 0 && !TextUtils.isEmpty(url)) {
            for (String regex : filterList) {
                if (!TextUtils.isEmpty(regex) && url.matches(regex)) {
                    useParamsSignAuth = false;
                    break;
                }
            }
        }
        if (useParamsSignAuth) {
            timeOffset = callback.getTimeOffset();
            helper.initialize(timeOffset, appId, appKey);
            if ("GET".equals(request.method())) {
                request = helper.addGetParams(request);
            } else if ("POST".equals(request.method())) {
                request = helper.addPostParams(request);
            }
        }

        i(helper.getLogContent());

        return chain.proceed(request);
    }

    /**
     * 获取AppKey
     */
    private String getAppKey(String appId) {
        return new Md5Helper().getMD5String(appId).substring(7, 23);
    }

    public static String encode(String text) {
        String rtn;
        try {
            rtn = URLEncoder.encode(text, UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            rtn = URLEncoder.encode(text);
        }
        return rtn;
    }

    public static String decode(String text) {
        String rtn;
        try {
            rtn = URLDecoder.decode(text, UTF_8);
        } catch (Exception e) {
            rtn = text;
            e.printStackTrace();
        }
        return rtn;
    }

    private void i(String msg) {
        if (callback.getLogOutputState()) {
            Log.i(tag, msg);
        }
    }

    private void e(String msg) {
        if (callback.getLogOutputState()) {
            Log.e(tag, msg);
        }
    }

    private class ParamsSignAuthHelper {

        private long timeOffset = 0L;
        private String appId;
        private String appKey;
        private long timestamp;
        private String noncestr;
        private StringBuffer logBuffer;

        public ParamsSignAuthHelper(String initText) {
            logBuffer = new StringBuffer();
            logBuffer.append(initText);
        }

        public void initialize(long timeOffset, String appId, String appKey) {
            this.timeOffset = timeOffset;
            this.appId = appId;
            this.appKey = appKey;

            timestamp = generateFeServerTimestamp();
            int randomInt = new Random().nextInt(17) + 16;
            noncestr = getRandomString(randomInt);

            logBuffer.append("\ntimestamp: " + timestamp + ", noncestr: " + noncestr);
        }

        public String getLogContent() {
            return logBuffer.toString();
        }

        /**
         * GET请求添加公共参数和签名
         *
         * @param request
         * @return
         */
        private Request addGetParams(Request request) {
            //添加公共参数
            HttpUrl httpUrl = request.url().newBuilder().build();

            //添加签名
            Set<String> nameSet = httpUrl.queryParameterNames();
            ArrayList<String> nameList = new ArrayList<String>();
            nameList.addAll(nameSet);
            if (nameList.contains(API_SECURITY_NONCESTR)) {
                nameList.remove(API_SECURITY_NONCESTR);
            }
            if (nameList.contains(API_SECURITY_TIMESTAMP)) {
                nameList.remove(API_SECURITY_TIMESTAMP);
            }
            if (nameList.contains(API_SECURITY_APP_ID)) {
                nameList.remove(API_SECURITY_APP_ID);
            }
            if (nameList.contains(API_SECURITY_SIGNATURE)) {
                nameList.remove(API_SECURITY_SIGNATURE);
            }
            nameList.add(API_SECURITY_NONCESTR);
            nameList.add(API_SECURITY_TIMESTAMP);
            Collections.sort(nameList, new StringComparator());

            StringBuffer sb = new StringBuffer();
            String name;
            String value;
            List<String> valueList;
            for (int i = 0; i < nameList.size(); i++) {
                name = nameList.get(i);
                if (!TextUtils.isEmpty(name)) {
                    if (API_SECURITY_NONCESTR.equalsIgnoreCase(name)) {
                        value = noncestr;
                    } else if (API_SECURITY_TIMESTAMP.equalsIgnoreCase(name)) {
                        value = timestamp + "";
                    } else {
                        valueList = httpUrl.queryParameterValues(name);
                        if (valueList != null && valueList.size() > 0) {
                            value = encode(valueList.get(0));
                        } else {
                            value = "";
                        }
                    }
                    if (sb.length() > 0) {
                        sb.append("&");
                    }
                    sb.append(name)
                            .append("=")
                            .append(value);
                }
            }

            String signature = generateSignature(sb.toString());

            httpUrl = httpUrl.newBuilder()
                    .addQueryParameter(API_SECURITY_NONCESTR, noncestr)
                    .addQueryParameter(API_SECURITY_TIMESTAMP, timestamp + "")
                    .addQueryParameter(API_SECURITY_APP_ID, appId)
                    .addQueryParameter(API_SECURITY_SIGNATURE, signature)
                    .build();

            request = request.newBuilder().url(httpUrl).build();
            return request;
        }

        /**
         * POST请求添加公共参数和签名
         *
         * @param request
         * @return
         */
        private Request addPostParams(Request request) throws IOException {
            RequestBody requestBody = request.body();
            logBuffer.append("\naddPostParams...requestBody=" + requestBody);

            if (requestBody == null) {
                FormBody formBody = new FormBody.Builder().build();
                request = addPostFormBodyParams(request, formBody);
            } else {
                MediaType mediaType = requestBody.contentType();
                logBuffer.append("\naddPostParams...mediaType=" + mediaType);
                if (requestBody instanceof FormBody) {
                    FormBody formBody = (FormBody) request.body();
                    request = addPostFormBodyParams(request, formBody);
                } else if (mediaType != null && mediaType.toString().contains("multipart")) {
                    //do nothing
                } else {
                    Request.Builder builder = request.newBuilder();

                    Buffer buffer = new Buffer();
                    requestBody.writeTo(buffer);
                    String bodyText = buffer.readString(Charset.forName(UTF_8));
                    logBuffer.append("\naddPostParams...body content: " + bodyText);

                    String serializeText = generateSerializeText(noncestr, timestamp, bodyText);
                    String signature = generateSignature(serializeText);

                    String tmpText;
                    if (!TextUtils.isEmpty(bodyText) && bodyText.length() > 0 && !"{}".equals(bodyText)) {
                        tmpText = bodyText.substring(0, bodyText.length() - 1) + ",";
                    } else {
                        tmpText = "{";
                    }
                    bodyText = tmpText +
                            "\"" + API_SECURITY_APP_ID + "\":\"" + appId + "\"" +
                            ",\"" + API_SECURITY_TIMESTAMP + "\":" + timestamp +
                            ",\"" + API_SECURITY_NONCESTR + "\":\"" + noncestr + "\"" +
                            ",\"" + API_SECURITY_SIGNATURE + "\":\"" + signature + "\"" +
                            "}";

                    request = builder.post(RequestBody.create(MEDIA_TYPE_JSON, bodyText)).build();
                }
            }
            return request;
        }

        /**
         * POST请求FormBody添加公共参数和签名
         *
         * @param request
         * @param formBody
         * @return
         * @throws UnsupportedEncodingException
         */
        private Request addPostFormBodyParams(Request request, FormBody formBody) throws UnsupportedEncodingException {
            FormBody.Builder newFormBodyBuilder = new FormBody.Builder();

            String originName;
            //把原来的参数添加到新的构造器（因为没找到直接添加，所以就new新的）
            for (int i = 0, size = formBody.size(); i < size; i++) {
                originName = formBody.encodedName(i);
                if (API_SECURITY_NONCESTR.equals(originName)) {
                    continue;
                }
                if (API_SECURITY_TIMESTAMP.equals(originName)) {
                    continue;
                }
                if (API_SECURITY_APP_ID.equals(originName)) {
                    continue;
                }
                if (API_SECURITY_SIGNATURE.equals(originName)) {
                    continue;
                }
                newFormBodyBuilder.addEncoded(originName, formBody.encodedValue(i));
            }
            newFormBodyBuilder.addEncoded(API_SECURITY_NONCESTR, noncestr)
                    .addEncoded(API_SECURITY_TIMESTAMP, timestamp + "");
            formBody = newFormBodyBuilder.build();

            Map<String, String> bodyMap = new HashMap<String, String>();
            List<String> nameList = new ArrayList<String>();
            for (int i = 0, size = formBody.size(); i < size; i++) {
                nameList.add(formBody.encodedName(i));
                bodyMap.put(formBody.encodedName(i),
                        URLDecoder.decode(formBody.encodedValue(i), UTF_8));
            }
            Collections.sort(nameList, new StringComparator());

            StringBuffer sb = new StringBuffer();
            String value;
            for (String name : nameList) {
                value = bodyMap.get(name);
                if (value == null) {
                    continue;
                }
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(name).append("=");
                if (!TextUtils.isEmpty(value)) {
                    value = value.trim();
                    sb.append(encode(value));
                }
            }

            String signature = generateSignature(sb.toString());

            formBody = newFormBodyBuilder
                    .addEncoded(API_SECURITY_APP_ID, appId)
                    .addEncoded(API_SECURITY_SIGNATURE, signature)
                    .build();

            request = request.newBuilder().post(formBody).build();

            return request;
        }

        /**
         * 生成序列化字符串
         *
         * @param noncestr
         * @param timestamp
         * @param bodyText
         * @return
         */
        private String generateSerializeText(String noncestr, long timestamp, String bodyText) {
            if (!TextUtils.isEmpty(bodyText)) {
                if (isJSONText(bodyText)) {
                    JsonElement jsonElement = GsonUtils.getGsonInstance().fromJson(bodyText, JsonElement.class);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();

                    ArrayList<String> nameList = new ArrayList<String>();
                    Map<String, JsonElement> bodyMap = new HashMap<String, JsonElement>();
                    String key;
                    JsonElement value;
                    for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                        key = entry.getKey();
                        value = entry.getValue();
                        if (API_SECURITY_NONCESTR.equals(key)) {
                            continue;
                        }
                        if (API_SECURITY_TIMESTAMP.equals(key)) {
                            continue;
                        }
                        if (API_SECURITY_APP_ID.equals(key)) {
                            continue;
                        }
                        if (API_SECURITY_SIGNATURE.equals(key)) {
                            continue;
                        }
                        nameList.add(key);
                        bodyMap.put(key, value);
                    }
                    nameList.add(API_SECURITY_NONCESTR);
                    nameList.add(API_SECURITY_TIMESTAMP);

                    JsonObject newJsonObject = new JsonObject();
                    for (String name : nameList) {
                        if (API_SECURITY_NONCESTR.equals(name)) {
                            newJsonObject.add(API_SECURITY_NONCESTR, new JsonPrimitive(noncestr));
                        } else if (API_SECURITY_TIMESTAMP.equals(name)) {
                            newJsonObject.add(API_SECURITY_TIMESTAMP, new JsonPrimitive(timestamp));
                        } else {
                            newJsonObject.add(name, bodyMap.get(name));
                        }
                    }

                    bodyText = parseJsonObject(newJsonObject, "");
                }
            } else {
                bodyText = API_SECURITY_NONCESTR + "=" + noncestr + "&" + API_SECURITY_TIMESTAMP +
                        "=" + timestamp;
            }
            return bodyText;
        }

        private String parseJsonObject(JsonObject jsonObject, String preText) {
            StringBuffer sb = new StringBuffer();

            TreeMap<String, JsonElement> treeMap = new TreeMap<String, JsonElement>(new StringComparator());
            for (Map.Entry<String, JsonElement> jsonElementEntry : jsonObject.entrySet()) {
                treeMap.put(jsonElementEntry.getKey(), jsonElementEntry.getValue());
            }

            String key;
            JsonElement value;
            JsonObject tmpJsonObject;
            JsonArray tmpJsonArray;
            JsonPrimitive tmpJsonPrimitive;
            String prefix;
            for (Map.Entry<String, JsonElement> entry : treeMap.entrySet()) {
                key = entry.getKey();
                value = entry.getValue();
                if (value.isJsonNull()) {
                    if (sb.length() > 0) {
                        sb.append("&");
                    }
                    sb.append(preText);
                    if (!TextUtils.isEmpty(preText)) {
                        sb.append(".");
                    }
                    sb.append(key);
                    sb.append("=");
                } else if (value.isJsonObject()) {
                    tmpJsonObject = value.getAsJsonObject();
                    //排除{a:{}}、{a:{b:{}}}的情况
                    if (tmpJsonObject.size() == 0) {
                        continue;
                    }
                    if (sb.length() > 0) {
                        sb.append("&");
                    }
                    prefix = generatePrefix(preText, key);
                    sb.append(parseJsonObject(tmpJsonObject, prefix));
                } else if (value.isJsonPrimitive()) {
                    if (sb.length() > 0) {
                        sb.append("&");
                    }
                    tmpJsonPrimitive = value.getAsJsonPrimitive();
                    prefix = generatePrefix(preText, key);
                    sb.append(parseJsonPrimitive(tmpJsonPrimitive, prefix));
                } else if (value.isJsonArray()) {
                    tmpJsonArray = value.getAsJsonArray();
                    //排除{a:[]}、{a:{b:[]}}的情况
                    if (tmpJsonArray.size() == 0) {
                        continue;
                    }
                    if (sb.length() > 0) {
                        sb.append("&");
                    }
                    prefix = generatePrefix(preText, key);
                    sb.append(parseJsonArray(value.getAsJsonArray(), prefix));
                }
            }

            return sb.toString();
        }

        private String parseJsonPrimitive(JsonPrimitive jsonPrimitive, String preText) {
            String prefix;
            String value;
            if (jsonPrimitive.isNumber()) {
                //去除小数点后多余的零 e.g. 100.0 -> 100 ; 100.01 -> 100.01
                value = new BigDecimal(String.valueOf(jsonPrimitive.getAsNumber()))
                        .stripTrailingZeros().toPlainString();
            } else {
                value = jsonPrimitive.getAsString();
            }
            //兼容RFC3986 JS端标准
            prefix = encode(preText)
                    .replace("+", "%20")
                    .replace("*", "%2A")
                    .replace("%7E", "~");
            value = encode(value)
                    .replace("+", "%20")
                    .replace("*", "%2A")
                    .replace("%7E", "~");
            return prefix + "=" + value;
        }

        private String parseJsonArray(JsonArray jsonArray, String preText) {
            StringBuffer sb = new StringBuffer();
            Iterator<JsonElement> iterator = jsonArray.iterator();
            JsonElement jsonElement;
            TreeMap<String, JsonElement> map = new TreeMap<String, JsonElement>(new StringComparator());
            int i = 0;
            while (iterator.hasNext()) {
                jsonElement = iterator.next();
                map.put(String.valueOf(i), jsonElement);
                i++;
            }
            for (Map.Entry<String, JsonElement> entry : map.entrySet()) {
                jsonElement = entry.getValue();
                if (jsonElement.isJsonObject()) {
                    if (sb.length() > 0) {
                        sb.append("&");
                    }
                    sb.append(parseJsonObject(jsonElement.getAsJsonObject(), preText));
                } else if (jsonElement.isJsonArray()) {
                    if (sb.length() > 0) {
                        sb.append("&");
                    }
                    sb.append(parseJsonArray(jsonElement.getAsJsonArray(), preText));
                } else if (jsonElement.isJsonNull()) {
                    if (sb.length() > 0) {
                        sb.append("&");
                    }
                    sb.append(preText);
                    sb.append("=");
                } else if (jsonElement.isJsonPrimitive()) {
                    if (sb.length() > 0) {
                        sb.append("&");
                    }
                    sb.append(parseJsonPrimitive(jsonElement.getAsJsonPrimitive(), preText));
                }
            }
            return sb.toString();
        }

        /**
         * 生成签名
         *
         * @param serializeText 序列化字符串
         * @return
         */
        private String generateSignature(String serializeText) {
            String text = serializeText + appKey;
            logBuffer.append("\nbefore md5 signature: " + text);
            String md5String = new Md5Helper().getMD5String(text);
            logBuffer.append("\nafter md5 signature: " + md5String);
            return md5String;
        }

        /**
         * 根据本地时间戳和差值生成服务器时间戳
         *
         * @return 服务器时间戳
         */
        private long generateFeServerTimestamp() {
            return System.currentTimeMillis() - timeOffset;
        }

        /**
         * 生成随机字符串
         *
         * @param length 生成字符串的长度
         * @return 随机字符串
         */
        private String getRandomString(int length) {
            String base = "abcdefghijklmnopqrstuvwxyz0123456789";
            Random random = new Random();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < length; i++) {
                int number = random.nextInt(base.length());
                sb.append(base.charAt(number));
            }
            return sb.toString();
        }

        /**
         * 是否是JSON
         *
         * @param text
         * @return
         */
        private boolean isJSONText(String text) {
            if (!TextUtils.isEmpty(text)) {
                text = text.trim();
                return text.startsWith("{") && text.endsWith("}");
            }
            return false;
        }

        /**
         * 生成前缀
         *
         * @param preText
         * @param key
         * @return
         */
        private String generatePrefix(String preText, String key) {
            String prefix;
            if (TextUtils.isEmpty(preText)) {
                prefix = key;
            } else {
                prefix = preText + "." + key;
            }
            return prefix;
        }
    }

    private static class Md5Helper {

        private final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        private MessageDigest messageDigest = null;

        public Md5Helper() {
            try {
                messageDigest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        public String getMD5String(String str) {
            return getMD5String(str.getBytes());
        }

        public String getMD5String(byte[] bytes) {
            if (messageDigest == null) {
                return new String(bytes);
            }
            messageDigest.update(bytes);
            return bytesToHex(messageDigest.digest());
        }

        public String bytesToHex(byte bytes[]) {
            return bytesToHex(bytes, 0, bytes.length);
        }

        public String bytesToHex(byte bytes[], int start, int len) {
            StringBuilder sb = new StringBuilder();
            for (int i = start; i < start + len; i++) {
                sb.append(byteToHex(bytes[i]));
            }
            return sb.toString();
        }

        public String byteToHex(byte bt) {
            return HEX_DIGITS[(bt & 0xf0) >> 4] + "" + HEX_DIGITS[bt & 0xf];
        }
    }

    private static class StringComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            return o1.toLowerCase().compareTo(o2.toLowerCase());
        }
    }

    public interface Callback {

        /**
         * 获取不使用参数签名验证功能的请求地址的过滤列表（正则表达式）
         */
        List<String> getFilterList();

        /**
         * 获取本地与服务器时差=本地时间-服务器时间
         */
        long getTimeOffset();

        /**
         * 获取AppId
         */
        String getAppId();

        /**
         * 获取日志输出状态
         */
        boolean getLogOutputState();

        /**
         * 生成日志TAG
         */
        String generateLogTag();
    }

    public static abstract class SimpleCallback implements Callback {

        @Override
        public List<String> getFilterList() {
            return null;
        }

        @Override
        public boolean getLogOutputState() {
            return false;
        }

        @Override
        public String generateLogTag() {
            return "";
        }
    }
}
