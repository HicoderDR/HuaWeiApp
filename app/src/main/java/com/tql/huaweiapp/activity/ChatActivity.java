package com.tql.huaweiapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huawei.hiai.asr.AsrConstants;
import com.huawei.hiai.asr.AsrError;
import com.huawei.hiai.asr.AsrListener;
import com.huawei.hiai.asr.AsrRecognizer;
import com.microsoft.cognitiveservices.luis.clientlibrary.LUISClient;
import com.microsoft.cognitiveservices.luis.clientlibrary.LUISEntity;
import com.microsoft.cognitiveservices.luis.clientlibrary.LUISResponse;
import com.microsoft.cognitiveservices.luis.clientlibrary.LUISResponseHandler;
import com.tql.huaweiapp.R;
import com.tql.huaweiapp.adapter.ChatMessageAdapter;
import com.tql.huaweiapp.utils.CommonUtils;
import com.tql.huaweiapp.utils.HuaWeiNiuBiAPIUtils;
import com.tql.huaweiapp.utils.ServerUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.tql.huaweiapp.utils.GetAnswer.GetAnswers;
import static com.tql.huaweiapp.utils.GetAnswer.PrettyPrint;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private ImageView backImageview;
    /**
     * 这是名字
     */
    private TextView characterNameTextview;
    private ImageView characterInfoImageview;
    private ImageView microPhoneImageview;
    private EditText messageEdittext;
    private LinearLayout inputAreaLinearlayout;
    private ImageView sendMessageImageview;
    private LinearLayout messageInputLinearlayout;
    private LinearLayout voiceRecordLinearlayout;
    private LinearLayout record;
    private RecyclerView messageListRecyclerview;
    private ArrayList<Integer> avatars;
    private ArrayList<Integer> from;
    private ArrayList<String> messages;
    private String bot_id = "0";//正在聊天的对象id
    private String name = "null";//正在聊天的对象de ming zi
    private String LUISappID = "f281723c-4764-405e-bbeb-cd6c12332a22";
    private String LUISappKey = "36fb4cae87a246169da2edf98e082113";


    private static final String TAG = "ASRActivity";

    private static final int END_AUTO_TEST = 0;
    private final static int INIT_ENGINE = 1;
    private static final int NEXT_FILE_TEST = 2;
    private static final int WRITE_RESULT_SD = 3;
    private static final int DELAYED_SATRT_RECORD = 4;

    private long startTime;
    private long endTime;
    private long waitTime;

    private AsrRecognizer mAsrRecognizer;

    private String mResult;
    private boolean isAutoTest = true;
    private boolean isAutoTestEnd = false;
    private boolean isWritePcm = false;
    private int count = 0;

    private List<String> resultList = new ArrayList<>();
    private MyAsrListener mMyAsrListener = new MyAsrListener();
    private List<String> pathList = new ArrayList<>();

    private String TEST_HIAI_PATH = "/storage/emulated/0/Android/data/com.huawei.hiai";
    private String TEST_FILES_PATH = "/storage/emulated/0/Android/data/com.huawei.hiai/files";
    private String TEST_FILE_PATH = "/storage/emulated/0/Android/data/com.huawei.hiai/files/test";
    private String TEST_RESULT_FILE_PATH = "/storage/emulated/0/Android/data/com.huawei.hiai/files/result";
    private String TEST_PCM_PATH = "/storage/emulated/0/Android/data/com.huawei.hiai/files/pcm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(CommonUtils.getTheme(this));
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        bot_id = intent.getStringExtra("bot_id");
        name = intent.getStringExtra("name");

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        HuaWeiNiuBiAPIUtils.requestMicPermission(this);
        HuaWeiNiuBiAPIUtils.requestStoragePermission(this);

        makeResDir();

        if (isSupportAsr()) {
            initEngine(AsrConstants.ASR_SRC_TYPE_RECORD);
        } else {
            Log.e(TAG, "not support asr!");
        }

        CommonUtils.addActivity(this);
        initView();
    }

    private void initView() {
        backImageview = findViewById(R.id.back_imageview);
        backImageview.setOnClickListener(this);
        characterNameTextview = findViewById(R.id.character_name_textview);
        characterInfoImageview = findViewById(R.id.character_info_imageview);
        characterInfoImageview.setOnClickListener(this);
        microPhoneImageview = findViewById(R.id.micro_phone_imageview);
        microPhoneImageview.setOnClickListener(this);
        messageEdittext = findViewById(R.id.message_edittext);
        messageEdittext.setOnClickListener(this);
        inputAreaLinearlayout = findViewById(R.id.input_area_linearlayout);
        sendMessageImageview = findViewById(R.id.send_message_imageview);
        sendMessageImageview.setOnClickListener(this);
        messageInputLinearlayout = findViewById(R.id.message_input_linearlayout);
        voiceRecordLinearlayout = findViewById(R.id.voice_record_linearlayout);
        record = findViewById(R.id.record_linearlayout);
        record.setOnClickListener(this);
        record.setOnLongClickListener(this);
        record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        messageListRecyclerview = findViewById(R.id.message_list_recyclerview);

        characterNameTextview.setText(name);

        initChatList();
    }

    /**
     * 初始化聊天消息记录
     */
    private void initChatList() {
        avatars = new ArrayList<>();
        messages = new ArrayList<>();
        from = new ArrayList<>();
        for (String line : CommonUtils.getMessagesFromLocal(this, bot_id)) {
            if (Integer.valueOf(line.split("::")[0]).equals(ChatMessageAdapter.MY_MESSAGE)) {
                from.add(ChatMessageAdapter.MY_MESSAGE);
                avatars.add(R.mipmap.default_avatar);
            } else {
                from.add(ChatMessageAdapter.YOUR_MESSAGE);
                avatars.add(R.mipmap.default_character_avatar);
            }
            messages.add(line.split("::")[1]);
        }
        ChatMessageAdapter chatMessageAdapter = new ChatMessageAdapter(avatars, messages, from);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        messageListRecyclerview.setHasFixedSize(false);
        messageListRecyclerview.setLayoutManager(layoutManager);
        messageListRecyclerview.setAdapter(chatMessageAdapter);
        messageListRecyclerview.scrollToPosition(chatMessageAdapter.getItemCount() - 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.back_imageview:
                //销毁当前Activity
                finish();
                break;
            case R.id.character_info_imageview:
//                startActivity(new Intent(ChatActivity.this, CharacterInfoActivity.class));
                menuClickEvent(v);
                break;
            case R.id.micro_phone_imageview:
                voiceRecordLinearlayout.setVisibility(View.VISIBLE);
                break;
            case R.id.send_message_imageview:
                // TODO: 2018/9/17 发送消息逻辑
                sendMessage();
                break;
            case R.id.message_edittext:
                voiceRecordLinearlayout.setVisibility(View.GONE);
                break;
            case R.id.record_linearlayout:
//                Toast.makeText(this, "请长按麦克风按钮进行录音！", Toast.LENGTH_SHORT).show();
                startRecord();
                break;
        }
    }

    /**
     * 发送消息逻辑
     */
    private void sendMessage() {
        String msg = messageEdittext.getText().toString().replace('\n', ' ');
        if (msg.isEmpty()) toast("内容不能为空！");
        else {
            avatars.add(R.mipmap.default_avatar);
            messages.add(msg);
            from.add(ChatMessageAdapter.MY_MESSAGE);
            CommonUtils.saveMessageToLocal(this, ChatMessageAdapter.MY_MESSAGE, msg, bot_id);
            //向LUIS发送并获得intent
            try {
                LUISClient client = new LUISClient(LUISappID, LUISappKey, true);
                client.predict(msg, new LUISResponseHandler() {
                    @Override
                    public void onSuccess(LUISResponse response) {
                        String topIntent = response.getTopIntent().getName();
                        System.out.println(topIntent);
                        List<LUISEntity> entities = response.getEntities();
                        getAns(topIntent);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        System.out.println(e.getMessage());
                    }
                });
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            messageListRecyclerview.setAdapter(new ChatMessageAdapter(avatars, messages, from));
            messageListRecyclerview.scrollToPosition(avatars.size() - 1);
            messageEdittext.setText("");
        }
    }

    //获取回答
    private void getAns(String msg) {
        try {
            String response = GetAnswers(msg);
            avatars.add(R.mipmap.default_character_avatar);
            String answer = polishAnswer(PrettyPrint(response));
            messages.add(answer);
            from.add(ChatMessageAdapter.YOUR_MESSAGE);
            CommonUtils.saveMessageToLocal(this, ChatMessageAdapter.YOUR_MESSAGE, answer, bot_id);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * 整理回答格式
     *
     * @param s
     * @return
     */
    private String polishAnswer(String s) {
        JSONObject data = JSON.parseObject(JSON.parseObject(s).getJSONArray("answers").getString(0));
        System.out.println(data);
        System.out.println("+++++++++" + data.getString("answer"));
        return data.getString("answer").trim();//去掉前后空格和换行
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.record_linearlayout:
                // TODO: 2018/9/17 开始录音

                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 弹出菜单
     *
     * @param v
     */
    private void menuClickEvent(final View v) {
        PopupMenu pm = new PopupMenu(this, v);
        pm.inflate(R.menu.character_info_menu);
        pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                // TODO: 2018/9/17 添加“喜欢”逻辑
                switch (menuItem.getItemId()) {
                    case R.id.introduction:
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                        View view = LayoutInflater.from(ChatActivity.this).inflate(R.layout.character_profile_card, null);
                        builder.setView(view);
                        builder.create().show();
                        break;
                    case R.id.favorite:
                        final ProgressDialog dialog = getProgressDialog("正在收藏...");
                        ServerUtils.setFavorite(CommonUtils.getCurrentUserEmail(ChatActivity.this), bot_id, new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                dialog.dismiss();
                                if (msg.what == ServerUtils.FAILED) {
                                    toast("收藏失败，请重试！");
                                } else {
                                    toast("已收藏！");
                                }
                            }
                        });
                        break;
                }
                return true;
            }
        });
        pm.show();
    }

    private ProgressDialog getProgressDialog(String s) {
        ProgressDialog waitingDialog = new ProgressDialog(this);
        waitingDialog.setIndeterminate(true);
        waitingDialog.setMessage(s);
        waitingDialog.setCancelable(false);
        waitingDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        waitingDialog.show();
        WindowManager.LayoutParams params = waitingDialog.getWindow().getAttributes();
        params.width = 450;
        params.gravity = Gravity.CENTER;
        waitingDialog.getWindow().setAttributes(params);
        return waitingDialog;
    }


    private boolean isSupportAsr() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo("com.huawei.hiai", 0);
            Log.d(TAG, "Engine versionName: " + packageInfo.versionName + " ,versionCode: " + packageInfo.versionCode);
            if (packageInfo.versionCode <= 801000300) {
                return false;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    private void makeResDir() {
        File root_test = new File(TEST_HIAI_PATH);
        File files_test = new File(TEST_FILES_PATH);
        File test = new File(TEST_FILE_PATH);
        File result = new File(TEST_RESULT_FILE_PATH);
        File pcm = new File(TEST_PCM_PATH);
        if (!root_test.exists()) {
            root_test.mkdir();
        }
        if (!files_test.exists()) {
            files_test.mkdir();
        }
        if (!test.exists()) {
            test.mkdir();
        }
        if (!result.exists()) {
            result.mkdir();
        }
        if (!pcm.exists()) {
            pcm.mkdir();
        }

        Log.d(TAG, "onCreate: " + TEST_FILE_PATH + "==" + TEST_RESULT_FILE_PATH + "==" + TEST_PCM_PATH);

    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause() ");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop() ");
        super.onStop();
        reset();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyEngine();
        mAsrRecognizer = null;
    }

    private void destroyEngine() {
        Log.d(TAG, "destroyEngine() ");
        if (mAsrRecognizer != null) {
            mAsrRecognizer.destroy();
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "handleMessage: " + msg.what);
            switch (msg.what) {
                case INIT_ENGINE:
                    handleInitEngine();
                    break;
                case DELAYED_SATRT_RECORD:
                    if (isAutoTestEnd || isWritePcm) {
                        if (mAsrRecognizer != null) {
                            mAsrRecognizer.destroy();
                        }
                        mHandler.sendEmptyMessageDelayed(END_AUTO_TEST, 300);
                    } else {
                        startListening(-1, null);
                    }
                    break;
                default:
                    break;
            }
        }
    };

//    public void setBtEnabled(boolean isEnabled) {
//        stopListeningBtn.setEnabled(isEnabled);
//        startRecord.setEnabled(isEnabled);
//    }


    private void startRecord() {
        Log.d(TAG, "startRecord() ");
        isAutoTest = false;
//        startRecord.setEnabled(false);
        messageEdittext.setText("识别中：");
        mHandler.sendEmptyMessage(DELAYED_SATRT_RECORD);
    }

    private void initEngine(int srcType) {
        Log.d(TAG, "initEngine() ");
        mAsrRecognizer = AsrRecognizer.createAsrRecognizer(this);
        Intent initIntent = new Intent();
        initIntent.putExtra(AsrConstants.ASR_AUDIO_SRC_TYPE, srcType);

        if (mAsrRecognizer != null) {
            mAsrRecognizer.init(initIntent, mMyAsrListener);
        }

        mAsrRecognizer.startPermissionRequestForEngine();
    }

    private void handleInitEngine() {
        if (isAutoTest) {
            initEngine(AsrConstants.ASR_SRC_TYPE_FILE);
//            setBtEnabled(false);
            Log.d(TAG, "handleMessage: " + count + " path :" + pathList.get(count));
            startListening(AsrConstants.ASR_SRC_TYPE_FILE, pathList.get(count));
        }
    }

    private void startListening(int srcType, String filePath) {
        Log.d(TAG, "startListening() " + "src_type:" + srcType);
        if (count == 0) {
            startTime = getTimeMillis();
        }
        Intent intent = new Intent();
        intent.putExtra(AsrConstants.ASR_VAD_FRONT_WAIT_MS, 4000);
        intent.putExtra(AsrConstants.ASR_VAD_END_WAIT_MS, 5000);
        intent.putExtra(AsrConstants.ASR_TIMEOUT_THRESHOLD_MS, 20000);
        if (srcType == AsrConstants.ASR_SRC_TYPE_FILE) {
            Log.d(TAG, "startListening() :filePath= " + filePath);
            intent.putExtra(AsrConstants.ASR_SRC_FILE, filePath);
        }
        if (mAsrRecognizer != null) {
            mAsrRecognizer.startListening(intent);
        }
    }

    private void stopListening() {
        Log.d(TAG, "stopListening() ");
        if (mAsrRecognizer != null) {
            mAsrRecognizer.stopListening();
        }
    }

    private void cancelListening() {
        Log.d(TAG, "cancelListening() ");
//        startRecord.setEnabled(true);
        if (mAsrRecognizer != null) {
            mAsrRecognizer.cancel();
        }
    }

    private class MyAsrListener implements AsrListener {
        @Override
        public void onInit(Bundle params) {
            Log.d(TAG, "onInit() called with: params = [" + params + "]");
        }

        @Override
        public void onBeginningOfSpeech() {
            Log.d(TAG, "onBeginningOfSpeech() called");
        }

        @Override
        public void onRmsChanged(float rmsdB) {
            Log.d(TAG, "onRmsChanged() called with: rmsdB = [" + rmsdB + "]");
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            Log.d(TAG, "onBufferReceived() called with: buffer = [" + buffer + "]");
        }

        @Override
        public void onEndOfSpeech() {
            Log.d(TAG, "onEndOfSpeech: ");


        }

        @Override
        public void onError(int error) {
            Log.d(TAG, "onError() called with: error = [" + error + "]");
            if (error == AsrError.SUCCESS) {
                return;
            }

            if (error == AsrError.ERROR_CLIENT_INSUFFICIENT_PERMISSIONS) {
                Toast.makeText(getApplicationContext(), "请在设置中打开麦克风权限!", Toast.LENGTH_LONG).show();
            }

//            setBtEnabled(true);
        }


        @Override
        public void onResults(Bundle results) {
            Log.d(TAG, "onResults() called with: results = [" + results + "]");
            endTime = getTimeMillis();
            waitTime = endTime - startTime;
            mResult = getOnResult(results, AsrConstants.RESULTS_RECOGNITION);

            stopListening();
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            Log.d(TAG, "onPartialResults() called with: partialResults = [" + partialResults + "]");
            getOnResult(partialResults, AsrConstants.RESULTS_PARTIAL);
        }

        @Override
        public void onEnd() {

        }

        private String getOnResult(Bundle partialResults, String key) {
            Log.d(TAG, "getOnResult() called with: getOnResult = [" + partialResults + "]");
            String json = partialResults.getString(key);
            final StringBuilder sb = new StringBuilder();
            try {
                org.json.JSONObject result = new org.json.JSONObject(json);
                JSONArray items = result.getJSONArray("result");
                for (int i = 0; i < items.length(); i++) {
                    String word = items.getJSONObject(i).getString("word");
                    double confidences = items.getJSONObject(i).getDouble("confidence");
                    sb.append(word);
                    Log.d(TAG, "asr_engine: result str " + word);
                    Log.d(TAG, "asr_engine: confidence " + String.valueOf(confidences));
                }
                Log.d(TAG, "getOnResult: " + sb.toString());
                messageEdittext.setText(sb.toString());
            } catch (JSONException exp) {
                Log.w(TAG, "JSONException: " + exp.toString());
            }
            return sb.toString();
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            Log.d(TAG, "onEvent() called with: eventType = [" + eventType + "], params = [" + params + "]");
        }
    }

    public long getTimeMillis() {
        long time = System.currentTimeMillis();
        return time;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i(TAG, "onBackPressed() : finish()");
        finish();
    }

    private void reset() {
        cancelListening();
//        setBtEnabled(true);
        messageEdittext.setText("");
//        input_result.setText("");
    }

}
