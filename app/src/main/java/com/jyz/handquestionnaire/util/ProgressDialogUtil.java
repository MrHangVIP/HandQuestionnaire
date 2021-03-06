package com.jyz.handquestionnaire.util;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.jyz.handquestionnaire.R;

import java.util.Timer;
import java.util.TimerTask;


/**
 * @discription 等待进度框辅助类
 * @autor   songzhihang
 * @time   2017/10/13  下午5:41
 **/
public class ProgressDialogUtil {
    public static ImageView img;
    public static Context mcontext;
    private static Dialog mProgressDialog = null;

    private static long showTime;

    public static Dialog showProgressDialog(Context context, boolean NOdimiss) {
        Log.e("222", "--ProcessDialog-0-" + context);
        if (mcontext != context) {
            // Activity如果切换了，则需重新生成dialog.
//			dismissProgressdialog();
            mProgressDialog = null;
        }
        showTime = System.currentTimeMillis();
        if (mProgressDialog == null) {
            mProgressDialog = new Dialog(context, R.style.progress_dialog);
        }
        if (mProgressDialog.isShowing()) {
            return mProgressDialog;
        }
        // 加载布局文件
        LayoutInflater inflater = LayoutInflater.from(context);
        // (LayoutInflater)
        // context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_progress, null);
        img = (ImageView) view.findViewById(R.id.dp_iv_load);
        // dialog添加视图
        mProgressDialog.setContentView(view);// 设置布局
        WindowManager.LayoutParams windowParams = mProgressDialog.getWindow()
                .getAttributes();
        windowParams.width = Constant.getScreenWidth(context); // 设置宽度
        windowParams.height = Constant.getScreenHeight(context); // 设置高度
        mProgressDialog.getWindow().setAttributes(windowParams);
        if (NOdimiss) {
            mProgressDialog.setCancelable(false);
        }
        // 给图片添加动态效果
        img.setBackgroundResource(R.drawable.frame_loading);
        AnimationDrawable animationDrawable = (AnimationDrawable) img.getBackground();
        animationDrawable.stop();
        animationDrawable.start();
        if (((Activity) context).isFinishing()) {
            // Activity如果 关闭了就不显示，否则会报错
            mProgressDialog = null;
        } else {
            mProgressDialog.show();
            Log.e("222", "--ProcessDialog--");
        }
        mcontext = context;
        return mProgressDialog;
    }

    public static void dismissProgressdialog() {
        Log.e("222", "=====dismis====s");
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        if (mProgressDialog != null) {
            final long time = System.currentTimeMillis() - showTime;
            synchronized (mProgressDialog){
                if (time < 1000) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Log.e("222", "=====dismis====time:" + time);
                                if (mProgressDialog != null) {
                                    mProgressDialog.dismiss();
                                    mProgressDialog = null;
                                }
                        }
                    }, 1000 - time);
                } else {
                    Log.e("222", "=====dismis====s");
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                    }
                }
            }
        }
    }
}
