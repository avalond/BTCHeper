package com.ruitu.btchelper.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.ruitu.btchelper.R;
import com.ruitu.btchelper.domain.IToStringMap;
import com.ruitu.btchelper.network.BaseRequestPacket;
import com.ruitu.btchelper.network.ClientEngine;
import com.ruitu.btchelper.network.IRequestContentCallback;
import com.ruitu.btchelper.network.ParseJson;
import com.ruitu.btchelper.setting.LocalSharePreference;
import com.ruitu.btchelper.util.DataHelper;
import com.ruitu.btchelper.util.DialogUtils;

public class FeedbackActivity extends BaseActivity implements OnClickListener,
        IToStringMap, IRequestContentCallback, DataHelper {
    private static final String TAG = FeedbackActivity.class.getSimpleName();
    private Button sub_btn;
    private EditText email, info;
    private Button back_btn;
    private String email_text, info_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_activity);
        initViews();
    }

    private void initViews() {
        this.back_btn = (Button) this.findViewById(R.id.btn_feedback);
        this.email = (EditText) this.findViewById(R.id.email_et_feedback);
        email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        this.info = (EditText) this.findViewById(R.id.info_et_feedback);
        this.sub_btn = (Button) this.findViewById(R.id.btn_et_feedback);
        this.back_btn.setOnClickListener(this);
        this.sub_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_feedback:
            this.finish();
            break;
        case R.id.btn_et_feedback:
            submit();
            break;
        default:
            break;
        }

    }

    private void submit() {
        email_text = email.getText().toString();
        info_text = info.getText().toString();
        Log.e(TAG, email_text);
        Log.e(TAG, info_text);
        if (email_text == null || "".equals(email_text)) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.not_text)
                    .setMessage(R.string.email_is_null)
                    .setPositiveButton(R.string.ok_text,
                            new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    email.requestFocus();
                                }
                            }).create().show();
        } else if (info_text == null || "".equals(info_text)) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.not_text)
                    .setMessage(R.string.info_is_null)
                    .setPositiveButton(R.string.ok_text,
                            new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    info.requestFocus();
                                }
                            }).create().show();
        } else {
            // 向服务器提交建议数据
            uploadDataToServer();
        }
    }

    private void uploadDataToServer() {
        ClientEngine clientEngine = ClientEngine.getInstance(this);
        BaseRequestPacket packet = new BaseRequestPacket();
        packet.action = DataHelper.FEEDBACK_ACTION;
        packet.url = DataHelper.FEEDBACK_URL;
        packet.context = this;
        packet.object = this;
        clientEngine.httpPostRequest(packet, this);
    }

    @Override
    public void onResult(int requestAction, Boolean isSuccess, String content,
            Object extra) {
        if (isSuccess) {
            switch (requestAction) {
            case FEEDBACK_ACTION:
                try {
                    boolean issuccess = ParseJson.parseFeedbackStr(content);
                    if (issuccess) {
                        DialogUtils.showFeedbackSuccessful(this);
                    } else {
                        DialogUtils.showFeedbackFail(this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
            }
        }
    }

    @Override
    public Map<String, String> toStringMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("Username", LocalSharePreference.getUserName(this));
        map.put("email", email_text);
        map.put("info", info_text);
        return map;
    }
}
