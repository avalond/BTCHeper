package com.ruitu.btchelper.util;

import com.ruitu.btchelper.R;
import com.ruitu.btchelper.weight.MyAlertDialog;

import android.R.raw;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.view.View;

public class DialogUtils {
    public static void showCantBackCost(Context context){
        final MyAlertDialog myAlertDialog = MyAlertDialog.getInstance(context);
        myAlertDialog.setTitle(R.string.not_text);
        myAlertDialog.setMessage(R.string.cant_back_cost_text);
        myAlertDialog.setPositiveButton(
                context.getResources().getString(R.string.ok_text),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myAlertDialog.dismiss();
                    }
                });
    }
    public static void showTeuDialog(Context context, int title, int str,
            int btn_str) {
        final MyAlertDialog myAlertDialog = MyAlertDialog.getInstance(context);
        myAlertDialog.setTitle(title);
        myAlertDialog.setMessage(str);
        myAlertDialog.setPositiveButton(
                context.getResources().getString(btn_str),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myAlertDialog.dismiss();
                    }
                });
    }

    public static void showNotSelectorPlat(Context context) {
        // final MyAlertDialog myAlertDialog = new MyAlertDialog(context);
        final MyAlertDialog myAlertDialog = MyAlertDialog.getInstance(context);
        myAlertDialog.setTitle(R.string.not_text);
        myAlertDialog.setMessage(R.string.not_select_platform);
        myAlertDialog.setPositiveButton(
                context.getResources().getString(R.string.ok_text),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myAlertDialog.dismiss();
                    }
                });
    }

    public static void showUserNameIsNull(Context context) {
        final MyAlertDialog myAlertDialog = MyAlertDialog.getInstance(context);
        myAlertDialog.setTitle(R.string.not_text);
        // myAlertDialog.setWidthandHeight(400, 200);
        myAlertDialog.setMessage(R.string.username_is_null);
        myAlertDialog.setPositiveButton(
                context.getResources().getString(R.string.ok_text),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myAlertDialog.dismiss();
                    }
                });
    }

    public static void showPassIsNull(Context context) {
        // final MyAlertDialog myAlertDialog = new MyAlertDialog(context);
        final MyAlertDialog myAlertDialog = MyAlertDialog.getInstance(context);
        myAlertDialog.setTitle(R.string.not_text);
        // myAlertDialog.setWidthandHeight(400, 200);
        myAlertDialog.setMessage(R.string.userpass_is_null);
        myAlertDialog.setPositiveButton(
                context.getResources().getString(R.string.ok_text),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myAlertDialog.dismiss();
                    }
                });
    }

    public static void showEmailisNull(Context context) {
        // final MyAlertDialog myAlertDialog = new MyAlertDialog(context);
        final MyAlertDialog myAlertDialog = MyAlertDialog.getInstance(context);
        myAlertDialog.setTitle(R.string.not_text);
        // myAlertDialog.setWidthandHeight(400, 200);
        myAlertDialog.setMessage(R.string.email_is_null);
        myAlertDialog.setPositiveButton(
                context.getResources().getString(R.string.ok_text),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myAlertDialog.dismiss();
                    }
                });
    }

    public static void showEmailIsIll(Context context) {
        // final MyAlertDialog myAlertDialog = new MyAlertDialog(context);
        final MyAlertDialog myAlertDialog = MyAlertDialog.getInstance(context);
        myAlertDialog.setTitle(R.string.not_text);
        // myAlertDialog.setWidthandHeight(400, 200);
        myAlertDialog.setMessage(R.string.email_is_illage);
        myAlertDialog.setPositiveButton(
                context.getResources().getString(R.string.ok_text),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myAlertDialog.dismiss();
                    }
                });
    }

    public static void showNoNetDialog(Context context) {
        // final MyAlertDialog myAlertDialog = new MyAlertDialog(context);
        final MyAlertDialog myAlertDialog = MyAlertDialog.getInstance(context);
        myAlertDialog.setTitle(R.string.not_text);
        // myAlertDialog.setWidthandHeight(400, 200);
        myAlertDialog.setMessage(R.string.loadfail_toast);
        myAlertDialog.setPositiveButton(
                context.getResources().getString(R.string.ok_text),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myAlertDialog.dismiss();
                    }
                });
    }
    public static void showNoNetDialogTeuActivity(Context context) {
        // final MyAlertDialog myAlertDialog = new MyAlertDialog(context);
        final MyAlertDialog myAlertDialog = MyAlertDialog.getInstance(context);
        myAlertDialog.setTitle(R.string.not_text);
        // myAlertDialog.setWidthandHeight(400, 200);
        myAlertDialog.setMessage(R.string.loadfail_toast);
        myAlertDialog.setPositiveButton(
                context.getResources().getString(R.string.ok_text),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myAlertDialog.dismiss();
                    }
                });
    }
    public static void showNoDataDialog(Context context) {
        // final MyAlertDialog myAlertDialog = new MyAlertDialog(context);
        final MyAlertDialog myAlertDialog = MyAlertDialog.getInstance(context);
        myAlertDialog.setTitle(R.string.not_text);
        myAlertDialog.setMessage(R.string.no_data);
        // myAlertDialog.setWidthandHeight(400, 200);
        myAlertDialog.setPositiveButton(
                context.getResources().getString(R.string.ok_text),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myAlertDialog.dismiss();
                    }
                });
    }

    public static void showUserNameIsNotHave(Context context) {
        // final MyAlertDialog myAlertDialog = new MyAlertDialog(context);
        final MyAlertDialog myAlertDialog = MyAlertDialog.getInstance(context);
        myAlertDialog.setTitle(R.string.not_text);
        myAlertDialog.setMessage(R.string.username_is_not);
        // myAlertDialog.setWidthandHeight(400, 200);
        myAlertDialog.setPositiveButton(
                context.getResources().getString(R.string.ok_text),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myAlertDialog.dismiss();
                    }
                });
    }

    public static void showUserPassIsWrong(Context context) {
        // final MyAlertDialog myAlertDialog = new MyAlertDialog(context);
        final MyAlertDialog myAlertDialog = MyAlertDialog.getInstance(context);
        myAlertDialog.setTitle(R.string.not_text);
        myAlertDialog.setMessage(R.string.userpass_is_wrong);
        myAlertDialog.setPositiveButton(
                context.getResources().getString(R.string.ok_text),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myAlertDialog.dismiss();
                    }
                });
    }

    public static void showLoginSuccess(final Context context) {
        // final MyAlertDialog myAlertDialog = new MyAlertDialog(context);
        final MyAlertDialog myAlertDialog = MyAlertDialog.getInstance(context);
        myAlertDialog.setTitle(R.string.not_text);
        myAlertDialog.setMessage(R.string.login_success);
        myAlertDialog.setPositiveButton(
                context.getResources().getString(R.string.ok_text),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((Activity) context).finish();
                        myAlertDialog.dismiss();
                    }
                });
    }

    public static void showRegisterSuccess(final Context context) {
        // final MyAlertDialog myAlertDialog = new MyAlertDialog(context);
        final MyAlertDialog myAlertDialog = MyAlertDialog.getInstance(context);
        myAlertDialog.setTitle(R.string.not_text);
        myAlertDialog.setMessage(R.string.register_success);
        myAlertDialog.setPositiveButton(
                context.getResources().getString(R.string.ok_text),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((Activity) context).finish();
                        myAlertDialog.dismiss();
                    }
                });
    }

    public static void showFeedbackSuccessful(final Context context) {
        // final MyAlertDialog myAlertDialog = new MyAlertDialog(context);
        final MyAlertDialog myAlertDialog = MyAlertDialog.getInstance(context);
        myAlertDialog.setTitle(R.string.not_text);
        myAlertDialog.setMessage(R.string.feedback_success);
        myAlertDialog.setPositiveButton(
                context.getResources().getString(R.string.ok_text),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((Activity) context).finish();
                        myAlertDialog.dismiss();
                    }
                });
    }

    public static void showFeedbackFail(final Context context) {
        // final MyAlertDialog myAlertDialog = new MyAlertDialog(context);
        final MyAlertDialog myAlertDialog = MyAlertDialog.getInstance(context);
        myAlertDialog.setTitle(R.string.not_text);
        myAlertDialog.setMessage(R.string.feedback_failed);
        myAlertDialog.setPositiveButton(
                context.getResources().getString(R.string.ok_text),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((Activity) context).finish();
                        myAlertDialog.dismiss();
                    }
                });
    }

}
