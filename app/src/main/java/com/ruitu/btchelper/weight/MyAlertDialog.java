package com.ruitu.btchelper.weight;

import com.ruitu.btchelper.R;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class MyAlertDialog {
    private Context context;
    private AlertDialog ad;
    private TextView titleView;
    private TextView messageView;
    private View contentView;
    private LinearLayout buttonLayout;
    private static MyAlertDialog instance;
    private boolean hasNegativeButton = false;
    private boolean hasPositiveButton = false;

    public static synchronized MyAlertDialog getInstance(Context context) {
        if (instance == null) {
            instance = new MyAlertDialog(context);
        }
        return instance;
    }

    private MyAlertDialog(Context context) {
        this.context = context;
        ad = new AlertDialog.Builder(context).create();
        ad.setCancelable(false);
        ad.show();
        // 关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
        Window window = ad.getWindow();
        window.setContentView(R.layout.myalertdialog);
        titleView = (TextView) window.findViewById(R.id.title);
        messageView = (TextView) window.findViewById(R.id.message);
        buttonLayout = (LinearLayout) window.findViewById(R.id.buttonLayout);
    }

    public void setWidthandHeight(int width, int height) {
        ad.getWindow().setLayout(width, height);
    }

    public void setTitle(int resId) {
        titleView.setText(resId);
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setMessage(int resId) {
        messageView.setText(resId);
    }

    public void setMessage(String message) {
        messageView.setText(message);
    }

    /**
     * 设置按钮
     * 
     * @param text
     * @param listener
     */
    public void setPositiveButton(String text,
            final View.OnClickListener listener) {
        Button button = new Button(context);
        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(params);
        button.setBackgroundResource(R.drawable.alertdialog_button);
        button.setText(text);
        button.setTextColor(Color.WHITE);
        button.setTextSize(20);
        button.setOnClickListener(listener);
        if (!hasPositiveButton) {
            buttonLayout.addView(button);
            hasPositiveButton = true;
        }
    }

    /**
     * 设置按钮
     * 
     * @param text
     * @param listener
     */
    public void setNegativeButton(String text,
            final View.OnClickListener listener) {
        Button button = new Button(context);
        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(params);
        button.setBackgroundResource(R.drawable.alertdialog_button);
        button.setText(text);
        button.setTextColor(Color.WHITE);
        button.setTextSize(20);
        button.setOnClickListener(listener);
        if (buttonLayout.getChildCount() > 0) {
            params.setMargins(20, 0, 0, 0);
            button.setLayoutParams(params);
            if (!buttonLayout.hasWindowFocus())
                buttonLayout.addView(button, 1);
        } else {
            button.setLayoutParams(params);
            if (!hasNegativeButton) {
                buttonLayout.addView(button);
                hasNegativeButton = true;
            }
        }

    }

    /**
     * 关闭对话框
     */
    public void dismiss() {
        ad.dismiss();
        instance = null;
    }

}
