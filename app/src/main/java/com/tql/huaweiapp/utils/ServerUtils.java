package com.tql.huaweiapp.utils;

import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tql.huaweiapp.entity.User;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.microsoft.cognitiveservices.luis.clientlibrary.LUISResponse;


public class ServerUtils {
    private static final String REST_API = "http://39.108.180.185:8080";
    private static final String GET_VERIFICATION_CODE = REST_API.concat("/get-verification-code");
    private static final String ADD_USER = REST_API.concat("/add-user");
    private static final String USER_LOGIN = REST_API.concat("/user-login");
    private static final String SET_USER = REST_API.concat("/set-user-info");
    private static final String GET_USER_INFO = REST_API.concat("/get-user-by-mail");
    private static final String CHECK_UPDATE = REST_API.concat("/get-version");
    private static final String GET_BOT_INTRODUCTION = REST_API.concat("/get-introduction");
    private static final String GET_BOT_BY_ID = REST_API.concat("/get-bot");
    private static final String GET_ALL_BOT = REST_API.concat("/get-all-bot");
    private static final String GET_FAVORITE = REST_API.concat("/get-favourite");
    private static final String SET_FAVORITE = REST_API.concat("/change-favourite");
    private static final String DELETE_FAVORITE = REST_API.concat("/delete-favourite");
    private static final String GET_ANSWER_WITH_ENTITIES = REST_API.concat("/get-answer-with-entity");
    public static final int SUCCESSFUL = 0;
    public static final int FAILED = 1;

    /**
     * 获取注册验证码
     *
     * @param email
     * @param handler
     */
    public static void getVerificationCode(String email, final Handler handler) {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(GET_VERIFICATION_CODE + "?mail=" + email).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Fail");
                e.printStackTrace();
                handler.sendEmptyMessage(FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("Connection Successful");
                String data = response.body().string();
                System.out.println(data);
                JSONObject object = JSON.parseObject(data);
                if (object.getString("hr").equals("200")) {
                    Message msg = new Message();
                    msg.what = SUCCESSFUL;
                    msg.obj = object.getString("data");
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = FAILED;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    /**
     * 用户注册，添加一个新的用户到数据库
     *
     * @param email
     * @param password
     */
    public static void addUser(String email, String password, final Handler handler) {
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
                Message msg = new Message();
                msg.what = FAILED;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("Connection Successful");
                String data = response.body().string();
                System.out.println(data);
                JSONObject object = JSON.parseObject(data);
                if (object.getString("hr").equals("200")) {
                    Message msg = new Message();
                    msg.what = SUCCESSFUL;
                    handler.sendMessage(msg);
                } else if (object.getString("message").equals("添加失败，该邮箱已被注册过")) {
                    Message msg = new Message();
                    msg.what = FAILED;
                    msg.obj = "添加失败，该邮箱已被注册过";
                    handler.sendMessage(msg);
                }
            }
        });
    }

    /**
     * 登录逻辑
     *
     * @param email
     * @param password
     */
    public static void login(String email, String password, final Handler handler) {
        User user = new User();
        user.setMail(email);
        user.setPassword(password);
        String userJson = JSON.toJSONString(user);
        RequestBody body = FormBody.create(MediaType.parse("application/json"), userJson);
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(USER_LOGIN).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Fail");
                Message msg = new Message();
                msg.what = FAILED;
                handler.sendMessage(msg);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("Connection Successful");
                String data = response.body().string();
                System.out.println(data);
                JSONObject object = JSON.parseObject(data);
                if (object.getString("message").equals("未知错误") || !object.getBoolean("data")) {
                    Message msg = new Message();
                    msg.what = FAILED;
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = SUCCESSFUL;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    /**
     * 更新用户资料
     *
     * @param user
     * @param handler
     */
    public static void updateUserInfo(User user, final Handler handler) {
        String userJson = JSON.toJSONString(user);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = FormBody.create(MediaType.parse("application/json"), userJson);
        final Request request = new Request.Builder().url(SET_USER).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Fail");
                e.printStackTrace();
                Message msg = new Message();
                msg.what = FAILED;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("Connection Successful");
                String data = response.body().string();
                System.out.println(data);
                JSONObject object = JSON.parseObject(data);
                if (object.getString("hr").equals("200")) {
                    Message msg = new Message();
                    msg.what = SUCCESSFUL;
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = FAILED;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    /**
     * 获取用户资料
     *
     * @param email
     * @param handler
     */
    public static void getUserInfo(String email, final Handler handler) {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(GET_USER_INFO + "?mail=" + email).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Fail");
                e.printStackTrace();
                handler.sendEmptyMessage(FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("Connection Successful");
                String data = response.body().string();
                System.out.println(data);
                JSONObject object = JSON.parseObject(data);
                if (object.getString("hr").equals("200")) {
                    Message msg = new Message();
                    msg.what = SUCCESSFUL;
                    msg.obj = object.getString("data");
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = FAILED;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    /**
     * 检查更新
     */
    public static void checkUpdate(final Handler handler) {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(CHECK_UPDATE).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Fail");
                e.printStackTrace();
                handler.sendEmptyMessage(FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("Connection Successful");
                String data = response.body().string();
                System.out.println(data);
                JSONObject object = JSON.parseObject(data);
                if (object.getString("hr").equals("200")) {
                    Message msg = new Message();
                    msg.what = SUCCESSFUL;
                    msg.obj = object.getString("data");
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = FAILED;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    /**
     * 获取人物简介
     */
    public static void getCharacterInfo(String name, final Handler handler) {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(GET_BOT_INTRODUCTION + "?name=" + name).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Fail");
                e.printStackTrace();
                handler.sendEmptyMessage(FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("Connection Successful");
                String data = response.body().string();
                System.out.println(data);
                JSONObject object = JSON.parseObject(data);
                if (object.getString("hr").equals("200")) {
                    Message msg = new Message();
                    msg.what = SUCCESSFUL;
                    msg.obj = object.getString("data");
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = FAILED;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    /**
     * 获取所有聊天bot
     *
     * @param handler
     */
    public static void getAllBot(final Handler handler) {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(GET_ALL_BOT).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Fail");
                e.printStackTrace();
                handler.sendEmptyMessage(FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("Connection Successful");
                String data = response.body().string();
                System.out.println(data);
                JSONObject object = JSON.parseObject(data);
                if (object.getString("hr").equals("200")) {
                    Message msg = new Message();
                    msg.what = SUCCESSFUL;
                    msg.obj = object.getString("data");
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = FAILED;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    /**
     * 添加收藏人物
     *
     * @param email
     * @param favorite
     * @param handler
     */
    public static void setFavorite(String email, String favorite, final Handler handler) {
        User user = new User();
        user.setMail(email);
        user.setFavourite(favorite);
        String userJson = JSON.toJSONString(user);
        RequestBody body = FormBody.create(MediaType.parse("application/json"), userJson);
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(SET_FAVORITE).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Fail");
                e.printStackTrace();
                handler.sendEmptyMessage(FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("Connection Successful");
                String data = response.body().string();
                System.out.println(data);
                JSONObject object = JSON.parseObject(data);
                if (object.getString("hr").equals("200")) {
                    Message msg = new Message();
                    msg.what = SUCCESSFUL;
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = FAILED;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    public static void deleteFavorite(String email, String favoriteToDelete, final Handler handler) {
        User user = new User();
        user.setMail(email);
        user.setFavourite(favoriteToDelete);
        String userJson = JSON.toJSONString(user);
        RequestBody body = FormBody.create(MediaType.parse("application/json"), userJson);
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(DELETE_FAVORITE).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Fail");
                e.printStackTrace();
                handler.sendEmptyMessage(FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("Connection Successful");
                String data = response.body().string();
                System.out.println(data);
                JSONObject object = JSON.parseObject(data);
                if (object.getString("hr").equals("200")) {
                    Message msg = new Message();
                    msg.what = SUCCESSFUL;
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = FAILED;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    public static void getFavorite(String email, final Handler handler) {
        System.out.println("GET_FAVORITE:" + email);
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(GET_FAVORITE + "?mail=" + email).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Fail");
                e.printStackTrace();
                handler.sendEmptyMessage(FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("Connection Successful");
                String data = response.body().string();
                System.out.println("getFavorite:" + data);
                JSONObject object = JSON.parseObject(data);
                if (object.getString("hr").equals("200")) {
                    Message msg = new Message();
                    msg.what = SUCCESSFUL;
                    msg.obj = object.getString("data");
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = FAILED;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    public static void getBotByID(String bot_id, final Handler handler) {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(GET_BOT_BY_ID + "?botID=" + bot_id).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Fail");
                e.printStackTrace();
                handler.sendEmptyMessage(FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("Connection Successful");
                String data = response.body().string();
                System.out.println("getBotByID:" + data);
                JSONObject object = JSON.parseObject(data);
                if (object.getString("hr").equals("200")) {
                    Message msg = new Message();
                    msg.what = SUCCESSFUL;
                    msg.obj = object.getString("data");
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = FAILED;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    public static void getAnswerWithEntities(LUISResponse response, final Handler handler) {
        String responseJson = response.toString() ;
        OkHttpClient client = new OkHttpClient();
        RequestBody body = FormBody.create(MediaType.parse("application/json"), responseJson);
        final Request request = new Request.Builder().url(GET_ANSWER_WITH_ENTITIES ).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Fail");
                e.printStackTrace();
                Message msg = new Message();
                msg.what = FAILED;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("Connection Successful");
                String data = response.body().string();
                System.out.println(data);
                JSONObject object = JSON.parseObject(data);
                if (object.getString("hr").equals("200")) {
                    Message msg = new Message();
                    msg.what = SUCCESSFUL;
                    msg.obj = object.getString("data");
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = FAILED;
                    handler.sendMessage(msg);
                }
            }
        });
    }
}
