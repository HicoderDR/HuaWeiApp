package com.tql.huaweiapp.utils;

import com.alibaba.fastjson.JSON;
import com.tql.huaweiapp.entry.User;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServerUtils {
    private static final String REST_API = "http://39.108.180.185:8080";
    private static final String GET_VERIFICATION_CODE = REST_API.concat("/get-verification-code");
    private static final String ADD_USER = REST_API.concat("/add-user");

    public static void getVerificationCode(String email) {

    }

    public static void addUser(String email, String password) {
        User user = new User();
        user.setMail(email);
        user.setPassword(password);
        String userJson = JSON.toJSONString(user);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = FormBody.create(MediaType.parse("application/json"), userJson);
        final Request request = new Request.Builder().url(ADD_USER).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Fail");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("Connection Successful");
                System.out.println(response.toString());
            }
        });
    }
}
