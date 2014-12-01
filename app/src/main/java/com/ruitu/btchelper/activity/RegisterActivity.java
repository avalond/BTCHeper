package com.ruitu.btchelper.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.ruitu.btchelper.R;
import com.ruitu.btchelper.domain.IToStringMap;
import com.ruitu.btchelper.network.BaseRequestPacket;
import com.ruitu.btchelper.network.ClientEngine;
import com.ruitu.btchelper.network.IRequestContentCallback;
import com.ruitu.btchelper.network.ParseJson;
import com.ruitu.btchelper.util.DataHelper;
import com.ruitu.btchelper.util.DataUtil;
import com.ruitu.btchelper.util.DialogUtils;

public class RegisterActivity extends BaseActivity implements DataHelper,
        OnClickListener, IToStringMap, IRequestContentCallback {
    private static final String TAG =RegisterActivity.class.getSimpleName();
    private EditText username, userpass;
    private String username_text, userpass_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initViews();
    }

    private void initViews() {
        this.username = (EditText) this.findViewById(R.id.username_et_register);
        this.userpass = (EditText) this.findViewById(R.id.userpass_et_register);
        this.findViewById(R.id.ok_btn_register).setOnClickListener(this);
        this.findViewById(R.id.cancel_btn_register).setOnClickListener(this);
        this.findViewById(R.id.tengxun_text).setOnClickListener(this);
        this.findViewById(R.id.weibolog_text).setOnClickListener(this);
        this.findViewById(R.id.btn_register).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.ok_btn_register:
            register();
            break;
        case R.id.cancel_btn_register:
            this.finish();
            break;
        case R.id.tengxun_text:

            break;
        case R.id.weibolog_text:

            break;
        case R.id.btn_register:
            this.finish();
            break;
        default:
            break;
        }
    }

    private void register() {
        Log.e(TAG, "注册开始");
        username_text = this.username.getText().toString().trim();
        userpass_text = this.userpass.getText().toString().trim();
        if ("".equals(username_text)) {
            DialogUtils.showUserNameIsNull(this);
        } else if ("".equals(userpass_text)) {
            DialogUtils.showPassIsNull(this);
        } else {
            userpass_text = DataUtil.getMd5(userpass_text);
            // 提交注册
            saveRegister();
        }
    }

    private void saveRegister() {
        ClientEngine clientEngine = ClientEngine.getInstance(this);
        BaseRequestPacket packet = new BaseRequestPacket();
        packet.action = REGSITER_ACTION;
        packet.url = REGISTER_URL;
        packet.context = this;
        packet.object = this;
        clientEngine.httpPostRequest(packet, this);
    }

    @Override
    public void onResult(int requestAction, Boolean isSuccess, String content,
            Object extra) {
        if (isSuccess) {
            switch (requestAction) {
            case REGSITER_ACTION:
                try {
                    boolean su = ParseJson.parseRegisterStr(content);
                    if (su) {// 注册成功
                        DialogUtils.showRegisterSuccess(this);
                    }
                } catch (JSONException e) {
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
        map.put("Username", username_text);
        map.put("Userpass", userpass_text);
        return map;
    }
}
