package com.jyz.handquestionnaire.ui.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.jyz.handquestionnaire.BaseActivity;
import com.jyz.handquestionnaire.R;
import com.jyz.handquestionnaire.ui.activity.CreateSelectionActivity;

import java.util.ArrayList;

/**
 * @discription更多弹框
 * @autor songzhihang
 * @time 2017/10/17  上午10:32
 **/
public class CustomMoreView extends PopupWindow implements OnClickListener {

    private String TAG = CustomMoreView.class.getSimpleName();
    Activity mContext;
    private int mWidth;
    private int mHeight;
    private int statusBarHeight;
    private OnClickListener listener;
    private Handler mHandler = new Handler();

    public CustomMoreView(Activity context) {
        mContext = context;
    }

    public void init() {
        Rect frame = new Rect();
        mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;
        DisplayMetrics metrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        mWidth = metrics.widthPixels;
        mHeight = metrics.heightPixels;
        setWidth(mWidth);
        setHeight(mHeight);
    }

    /**
     * 显示更多按钮的样式
     *
     * @param anchor 在哪个view的下面
     * @param views  需要显示的view
     * @param travel 显示的行数
     * @param column 显示的列数
     */
    public void showMoreWindow(View anchor) {
        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_more_view, null);
        final LinearLayout lmv_ll_layout = (LinearLayout) layout.findViewById(R.id.lmv_ll_layout);
        setContentView(layout);
        setBackgroundDrawable(new ColorDrawable(Color.parseColor("#55000000")));
        ImageView close = (ImageView) layout.findViewById(R.id.lmv_iv_close);
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isShowing()) {
                    closeAnimation(lmv_ll_layout);
                }
            }

        });
        showAnimation(lmv_ll_layout);
        setOutsideTouchable(true);
        setFocusable(true);
        showAtLocation(anchor, Gravity.BOTTOM, 0, statusBarHeight);
    }

    private void showAnimation(ViewGroup layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            final View child = layout.getChildAt(i);
            final int position=i;
            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        v.setTag(position);
                        listener.onClick(v);//回调
                    }
                    dismiss();
                }
            });
            child.setVisibility(View.INVISIBLE);
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    child.setVisibility(View.VISIBLE);
                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 600, 0);
                    fadeAnim.setDuration(300);
                    KickBackAnimator kickAnimator = new KickBackAnimator();
                    kickAnimator.setDuration(150);
                    fadeAnim.setEvaluator(kickAnimator);
                    fadeAnim.start();
                }
            }, i * 50);
        }

    }

    private void closeAnimation(final ViewGroup layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            final View child = layout.getChildAt(i);
            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((BaseActivity) mContext).jumpToNext(CreateSelectionActivity.class);
                    dismiss();
                }
            });
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    child.setVisibility(View.VISIBLE);
                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 0, 600);
                    fadeAnim.setDuration(200);
                    KickBackAnimator kickAnimator = new KickBackAnimator();
                    kickAnimator.setDuration(100);
                    fadeAnim.setEvaluator(kickAnimator);
                    fadeAnim.start();
                    fadeAnim.addListener(new AnimatorListener() {

                        @Override
                        public void onAnimationStart(Animator animation) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            child.setVisibility(View.INVISIBLE);
                            if (child == layout.getChildAt(layout.getChildCount() - 1)) {
                                dismiss();
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            // TODO Auto-generated method stub

                        }
                    });
                }
            }, (layout.getChildCount() - i) * 40);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }

    public class KickBackAnimator implements TypeEvaluator<Float> {
        private final float s = 1.70158f;
        float mDuration = 0f;

        public void setDuration(float duration) {
            mDuration = duration;
        }

        @Override
        public Float evaluate(float fraction, Float startValue, Float endValue) {
            float t = mDuration * fraction;
            float b = startValue.floatValue();
            float c = endValue.floatValue() - startValue.floatValue();
            float d = mDuration;
            float result = calculate(t, b, c, d);
            return result;
        }

        public Float calculate(float t, float b, float c, float d) {
            return c * ((t = t / d - 1) * t * ((s + 1) * t + s) + 1) + b;
        }
    }

    public void setOnClickListener(OnClickListener listener){
        this.listener=listener;
    }

}
