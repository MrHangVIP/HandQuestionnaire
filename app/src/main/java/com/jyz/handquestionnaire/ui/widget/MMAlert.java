package com.jyz.handquestionnaire.ui.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.jyz.handquestionnaire.R;
import com.jyz.handquestionnaire.util.Constant;
import com.jyz.handquestionnaire.util.MyUtil;

/**
 * 对话框公共类
 */
@SuppressLint("InflateParams")
public class MMAlert {

    public interface OnAlertSelectId {
        void onClick(int whichButton);
    }

    public interface OnDialogClick {
        void onOkListener(String content);

        void onCancelListener(EditText et);

        void onClickPreListener(EditText et);
    }

    public interface OnAlertSelectObj {
        void onClick(String title, int whichButton);
    }

    /**
     * 统一设置对话框动画
     *
     * @param alert
     */
    private static void setDialogAnim(Dialog alert) {
        Window mWindow = alert.getWindow();
        mWindow.setWindowAnimations(R.style.MMAlertdialogAnimation);
    }

    /**
     * 普通对话框
     *
     * @param context
     * @param msg
     * @param title
     * @param lOk
     * @param lCancel
     * @return
     */
    public static AlertDialog showAlert(Context context, String msg,
                                        String title, DialogInterface.OnClickListener lOk,
                                        DialogInterface.OnClickListener lCancel) {
        return showAlert(context, msg, title,
                context.getString(R.string.app_ok),
                context.getString(R.string.app_cancel), lOk, lCancel);
    }

    /**
     * 普通对话框
     *
     * @param context
     * @param msg
     * @param title
     * @param yes
     * @param no
     * @param lOk
     * @param lCancel
     * @return
     */
    public static AlertDialog showAlert(Context context, String msg,
                                        String title, String yes, String no,
                                        DialogInterface.OnClickListener lOk,
                                        DialogInterface.OnClickListener lCancel) {
        return showAlert(context, null, msg, title, yes, no, lOk, lCancel, true);
    }

    /**
     * 普通对话框
     *
     * @param context
     * @param msg
     * @param title
     * @param yes
     * @param no
     * @param lOk
     * @param lCancel
     * @param cancelable
     * @return
     */
    public static AlertDialog showAlert(Context context, String msg,
                                        String title, String yes, String no,
                                        DialogInterface.OnClickListener lOk,
                                        DialogInterface.OnClickListener lCancel, boolean cancelable) {
        return showAlert(context, null, msg, title, yes, no, lOk, lCancel, true);
    }

    /**
     * 普通对话框
     *
     * @param context
     * @param icon
     * @param msg
     * @param title
     * @param lOk
     * @param lCancel
     * @return
     */
    public static AlertDialog showAlert(Context context, Drawable icon,
                                        String msg, String title, DialogInterface.OnClickListener lOk,
                                        DialogInterface.OnClickListener lCancel, boolean cancelable) {
        return showAlert(context, icon, msg, title,
                context.getString(R.string.app_ok),
                context.getString(R.string.app_cancel), lOk, lCancel,
                cancelable);
    }

    /**
     * 普通对话框
     *
     * @param context
     * @param msg
     * @param title
     * @param yes
     * @param no
     * @param lOk
     * @param lCancel
     * @param cancelable
     * @return
     */
    @SuppressLint("NewApi")
    public static AlertDialog showAlert(Context context, Drawable icon,
                                        String msg, String title, String yes, String no,
                                        DialogInterface.OnClickListener lOk,
                                        DialogInterface.OnClickListener lCancel, boolean cancelable) {
        if (!(context instanceof Activity)
                || ((Activity) context).isFinishing()) {
            return null;
        }
        Builder builder = new AlertDialog.Builder(context,
                R.style.MyDialogStyle);
        builder.setIcon(R.drawable.ic_dialog_alert);
        if (icon != null) {
            builder.setIcon(icon);
        }
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(yes, lOk);
        builder.setNegativeButton(no, lCancel);
        AlertDialog alert = builder.create();
        alert.setCancelable(cancelable);
        setDialogAnim(alert);
        alert.show();
        return alert;
    }

    /**
     * 完全自定义对话框
     *
     * @param context
     * @param
     * @param cancelable
     * @param
     * @return
     */
    public static Dialog showAudioAlert(Context context, View view,
                                        boolean cancelable, int width, int height) {
        final Dialog dialog = new Dialog(context, R.style.MyAudioDialogStyle);
        dialog.setContentView(view);
        dialog.setCancelable(cancelable);
        // 设置大小
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        if (width != 0) {
            p.width = width;
        }
        if (height != 0) {
            p.height = height;
        }
        dialogWindow.setAttributes(p);

        dialog.show();
        return dialog;
    }

    /**
     * 完全自定义对话框
     *
     * @param context
     * @param
     * @param cancelable
     * @param
     * @return
     */
    public static Dialog showAlert(Context context, View view,
                                   boolean cancelable, int width, int height) {
        final Dialog dialog = new Dialog(context, R.style.MyDialogStyle);
        dialog.setContentView(view);
        dialog.setCancelable(cancelable);
        // 设置大小
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        if (width != 0) {
            p.width = width;
        }
        if (height != 0) {
            p.height = height;
        }
        dialogWindow.setAttributes(p);

        dialog.show();
        return dialog;
    }

    public static Dialog showAlert(Context context, Drawable icon, String msg,
                                   String title, String yes, String no,
                                   final OnDialogClick clickListener, boolean cancelable) {
        return showAlert(context, icon, msg, title, yes, no, clickListener,
                cancelable, true);
    }

    /**
     * 自定义 alert
     *
     * @param context
     * @param icon           图标
     * @param msg            提示消息
     * @param title          标题
     * @param yes            确认提示
     * @param no             取消提示
     * @param clickListener  按钮监听
     * @param cancelable     是否可以取消
     * @param hasTwoSelector 是否有2个按钮 默认用上面那个方法就行了
     * @return
     */
    public static Dialog showAlert(Context context, Drawable icon, String msg,
                                   String title, String yes, String no,
                                   final OnDialogClick clickListener, boolean cancelable,
                                   boolean hasTwoSelector) {
        final Dialog dialog = new Dialog(context, R.style.MMTheme_DataSheet);
        View view = LayoutInflater.from(context).inflate(R.layout.alert_layout,
                null);
        dialog.setContentView(view);
        dialog.setCancelable(cancelable);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                (int) (Constant.getScreenWidth(context) * 0.8),
                FrameLayout.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        setDialogAnim(dialog);

        View bottomLine = (View) view.findViewById(R.id.dialog_bottom_line);
        TextView okBtn = (TextView) view.findViewById(R.id.dialog_ok_btn);
        TextView cancelBtn = (TextView) view
                .findViewById(R.id.dialog_cancel_btn);
        TextView messageTv = (TextView) view.findViewById(R.id.dialog_msg);
        TextView titleTv = (TextView) view.findViewById(R.id.dialog_title);
        ImageView img = (ImageView) view.findViewById(R.id.dialog_img);
        //
        if (icon != null) {
            img.setImageDrawable(icon);
        }
        if (!TextUtils.isEmpty(msg)) {
            messageTv.setText(msg);
        } else {
            messageTv.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        } else {
            titleTv.setVisibility(View.GONE);
        }
        //
        if (hasTwoSelector || !TextUtils.isEmpty(yes)) {
            if (!TextUtils.isEmpty(yes))
                okBtn.setText(yes);
            MyUtil.setVisibility(okBtn, View.VISIBLE);
            okBtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (clickListener != null) {
                        clickListener.onOkListener(null);
                    }
                    dialog.dismiss();
                }
            });
        } else {
            MyUtil.setVisibility(okBtn, View.GONE);
            MyUtil.setVisibility(bottomLine, View.GONE);
        }
        if (hasTwoSelector || !TextUtils.isEmpty(no)) {
            if (!TextUtils.isEmpty(no))
                cancelBtn.setText(no);
            MyUtil.setVisibility(cancelBtn, View.VISIBLE);
            cancelBtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (clickListener != null) {
                        clickListener.onCancelListener(null);
                    }
                    dialog.dismiss();
                }
            });
        } else {
            MyUtil.setVisibility(cancelBtn, View.GONE);
            MyUtil.setVisibility(bottomLine, View.GONE);
        }

        dialog.show();
        return dialog;
    }

    /**
     * 自定义view的对话框
     *
     * @param context
     * @param title
     * @param view
     * @param lOk
     * @return
     */
    public static AlertDialog showAlert(Context context, String title,
                                        View view, DialogInterface.OnClickListener lOk) {
        return showAlert(context, title, view,
                context.getString(R.string.app_ok), "", lOk, null);
    }

    /**
     * 自定义view的对话框
     *
     * @param context
     * @param title
     * @param view
     * @param ok
     * @param cancel
     * @param lOk
     * @param lCancel
     * @return
     */
    public static AlertDialog showAlert(Context context, String title,
                                        View view, String ok, String cancel,
                                        DialogInterface.OnClickListener lOk,
                                        DialogInterface.OnClickListener lCancel) {
        return showAlert(context, title, view, ok, cancel, lOk, lCancel, true);
    }

    /**
     * 自定义view的对话框
     *
     * @param context
     * @param title
     * @param view
     * @param ok
     * @param cancel
     * @param lOk
     * @param lCancel
     * @param cancelable
     * @return
     */
    @SuppressLint("NewApi")
    public static AlertDialog showAlert(Context context, String title,
                                        View view, String ok, String cancel,
                                        DialogInterface.OnClickListener lOk,
                                        DialogInterface.OnClickListener lCancel, boolean cancelable) {
        if (!(context instanceof Activity)
                || ((Activity) context).isFinishing()) {
            return null;
        }

        Builder builder = new AlertDialog.Builder(context,
                R.style.MyDialogStyle);
        builder.setTitle(title);
        builder.setView(view);
        builder.setPositiveButton(ok, lOk);
        if (!TextUtils.isEmpty(cancel) && lCancel != null) {
            builder.setNegativeButton(cancel, lCancel);
        }
        AlertDialog alert = builder.create();
        setDialogAnim(alert);
        alert.setCancelable(cancelable);
        alert.show();
        return alert;
    }

    /**
     * Web对话框
     *
     * @param context
     * @param title
     * @param rawUrl
     * @param client
     * @param lOk
     * @param lDismiss
     * @return
     */
    public static AlertDialog showWebAlert(Context context, String title,
                                           String rawUrl, WebViewClient client,
                                           DialogInterface.OnClickListener lOk,
                                           DialogInterface.OnDismissListener lDismiss) {
        return showWebAlert(context, title, rawUrl, client, null, null, lOk,
                null, lDismiss);
    }

    /**
     * Web对话框
     *
     * @param context
     * @param title
     * @param rawUrl
     * @param client
     * @param ok
     * @param cancel
     * @param lOk
     * @param lCancel
     * @param lDismiss
     * @return
     */
    public static AlertDialog showWebAlert(Context context, String title,
                                           String rawUrl, WebViewClient client, String ok, String cancel,
                                           DialogInterface.OnClickListener lOk,
                                           DialogInterface.OnClickListener lCancel,
                                           final DialogInterface.OnDismissListener lDismiss) {
        View view = View.inflate(context, R.layout.webalert, null);
        AlertDialog alert = showAlert(context, title, view, ok, cancel, lOk,
                lCancel);
        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                if (lDismiss != null) {
                    lDismiss.onDismiss(dialog);
                }
            }
        });

        WebView info = (WebView) view.findViewById(R.id.info_wv);
        info.loadUrl(rawUrl);
        if (client != null) {
            info.setWebViewClient(client);
        }
        return alert;
    }


    /**
     * 列表样式对话框
     *
     * @param context
     * @param title
     * @param adapter
     * @param mOnClickListener
     * @return
     */
    @SuppressLint("NewApi")
    public static Dialog showListAlert(Context context, String title,
                                       ListAdapter adapter,
                                       DialogInterface.OnClickListener mOnClickListener) {
        if (!(context instanceof Activity)
                || ((Activity) context).isFinishing()) {
            return null;
        }
        if (null == adapter) {
            return null;
        }

        Builder builder = new AlertDialog.Builder(context,
                R.style.MyDialogStyle);
        builder.setTitle(title);
        builder.setAdapter(adapter, mOnClickListener);
        AlertDialog alert = builder.create();
        setDialogAnim(alert);
        alert.show();
        return alert;
    }

    /**
     * 默认列表对话框
     *
     * @param context
     * @param items
     * @param alertDo
     * @return
     */
    public static Dialog showDefaultAlert(Context context, String[] items,
                                          final OnAlertSelectId alertDo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDo.onClick(which);
            }
        });
        AlertDialog dialog = builder.create();
        setDialogAnim(dialog);
        dialog.show();
        return dialog;
    }

    /**
     * 输入对话框 不带图片
     *
     * @param context
     * @param message
     * @param dialogListener
     */
    public static void showInputDialog(Context context, String message,
                                       int color, final OnDialogClick dialogListener) {
        showInputDialog(context, null, message, color, dialogListener);
    }

    /**
     * TODO 输入对话框
     *
     * @param context
     */
    public static void showInputDialog(Context context, Bitmap icon,
                                       String message, int color, final OnDialogClick dialogListener) {
        final Dialog dialog = new Dialog(context, R.style.MMTheme_DataSheet);
        View view = LayoutInflater.from(context).inflate(R.layout.input_dialog,
                null);
        dialog.setContentView(view);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                (int) (Constant.getScreenWidth(context) * 0.8),
                FrameLayout.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);

        TextView okBtn = (TextView) view.findViewById(R.id.dialog_ok_btn);
        TextView cancelBtn = (TextView) view
                .findViewById(R.id.dialog_cancel_btn);
        TextView messageTv = (TextView) view.findViewById(R.id.dialog_title);
        ImageView iconImg = (ImageView) view.findViewById(R.id.dialog_icon);
        final EditText et = (EditText) view.findViewById(R.id.dialog_input);

        if (dialogListener != null) {
            dialogListener.onClickPreListener(et);
        }
        if (!TextUtils.isEmpty(message)) {
            messageTv.setText(message);
        } else {
            messageTv.setVisibility(View.GONE);
        }
        if (icon != null) {
            iconImg.setVisibility(View.VISIBLE);
            iconImg.setImageBitmap(icon);
        }
        if (color != 0) {
            try {
                okBtn.setTextColor(color);
                cancelBtn.setTextColor(color);
            } catch (Exception e) {
            }
        }
        okBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                if (dialogListener != null) {
                    dialogListener.onOkListener(et.getText().toString());
                    MyUtil.hideSoftInput(et);
                }
            }
        });
        cancelBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (dialogListener != null) {
                    dialogListener.onCancelListener(et);
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
