package cn.ghost.common.utils;

import cn.ghost.common.exception.GatewayException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/01 19:22
 */
public class OkhttpUtils {
    private static final String HTTP_JSON = "application/json;charset=utf-8";

    private static Gson gson = new GsonBuilder().create();

    private static OkHttpClient client;

    static {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    /**
     * send post request with body t
     * application/json
     *
     * @param url
     * @param t
     * @param <T>
     */
    public static <T> void doPost(String url, T t) {
        RequestBody requestBody = RequestBody.create(MediaType.parse(HTTP_JSON), gson.toJson(t));
        Request request = new Request.Builder()
                .post(requestBody)
                .url(url)
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            if (response.code() < 200 || response.code() >= 300) {
                throw new GatewayException("request " + url + " fail,http code:" + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new GatewayException("request " + url + " fail");
        }
    }

    /**
     * send put request
     *
     * @param url
     * @param queryParamMap
     * @param body
     * @return
     */
    public static String doPut(String url, Map<String, Object> queryParamMap, String body) {
        String requestUrl;
        if (queryParamMap == null) {
            requestUrl = url;
        } else {

            StringBuilder sb = new StringBuilder(url);
            sb.append("?");
            for (Map.Entry<String, Object> entry : queryParamMap.entrySet()) {
                sb.append(entry.getKey() + "=" + entry.getValue());
                sb.append("&");
            }
            requestUrl = sb.toString();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse(HTTP_JSON), body);
        Request request = new Request.Builder()
                .put(requestBody)
                .url(requestUrl)
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            if (response.code() < 200 || response.code() >= 300) {
                throw new GatewayException("request " + requestUrl + " fail,http code:" + response.code());
            }
            return response.body().string();
        } catch (IOException e) {
            throw new GatewayException("request " + requestUrl + " fail");
        }
    }
}
