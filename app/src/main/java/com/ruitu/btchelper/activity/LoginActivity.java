package com.ruitu.btchelper.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.ruitu.btchelper.setting.LocalSharePreference;
import com.ruitu.btchelper.util.DataHelper;
import com.ruitu.btchelper.util.DataUtil;
import com.ruitu.btchelper.util.DialogUtils;

public class LoginActivity extends BaseActivity implements OnClickListener,
        IToStringMap, IRequestContentCallback, DataHelper {
    private EditText username, userpass;
    private String username_text, userpass_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initViews();
    }
    private void initViews() {
        this.username = (EditText) this.findViewById(R.id.username_et_login);
        this.userpass = (EditText) this.findViewById(R.id.userpass_et_login);
        this.findViewById(R.id.ok_btn_login).setOnClickListener(this);
        this.findViewById(R.id.cancel_btn_login).setOnClickListener(this);
        this.findViewById(R.id.tv_register_login).setOnClickListener(this);
        this.findViewById(R.id.tengxun_text).setOnClickListener(this);
        this.findViewById(R.id.weibolog_text).setOnClickListener(this);
        this.findViewById(R.id.btn_login).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.ok_btn_login:
            login();
            break;
        case R.id.cancel_btn_login:
            this.finish();
            break;
        case R.id.tengxun_text:

            break;
        case R.id.weibolog_text:

            break;
        case R.id.tv_register_login:
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            break;
        case R.id.btn_login:
            this.finish();
            break;
        default:
            break;
        }
    }

    private void login() {
        username_text = this.username.getText().toString().trim();
        userpass_text = this.userpass.getText().toString().trim();
        if ("".equals(username_text)) {
            DialogUtils.showUserNameIsNull(this);
        } else if ("".equals(userpass_text)) {
            DialogUtils.showPassIsNull(this);
        } else {
            userpass_text = DataUtil.getMd5(userpass_text);
            // 向服务器提交注册数据
            uploadDataToServer();
        }
    }

    private void uploadDataToServer() {
        ClientEngine clientEngine = ClientEngine.getInstance(this);
        BaseRequestPacket packet = new BaseRequestPacket();
        packet.action = DataHelper.LOGIN_ACTION;
        packet.url = DataHelper.LOGIN_URL;
        packet.context = this;
        packet.object = this;
        clientEngine.httpPostRequest(packet, this);
    }

    @Override
    public void onResult(int requestAction, Boolean isSuccess, String content,
            Object extra) {
        if (isSuccess) {
            switch (requestAction) {
            case LOGIN_ACTION:
                try {
                    Map<String, Object> map = ParseJson.parseLoginStr(content);
                    if (map.containsKey(SUCCESS)) {
                        boolean isLoginSuccess = (Boolean) map.get(SUCCESS);
                        if (isLoginSuccess) {// 登录成功
                            DialogUtils.showLoginSuccess(this);
                            LocalSharePreference.setUserName(this,
                                    username_text);
                        } else {
                            if (map.containsKey(INFO)) {
                                int type = (Integer) map.get(INFO);
                                switch (type) {
                                case 1:// 用户名不存在
                                    DialogUtils.showUserNameIsNotHave(this);
                                    break;
                                case 2:// 密码错误
                                    DialogUtils.showUserPassIsWrong(this);
                                    break;
                                default:
                                    break;
                                }
                            }
                        }
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
