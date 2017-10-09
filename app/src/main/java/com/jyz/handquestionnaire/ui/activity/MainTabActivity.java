package com.jyz.handquestionnaire.ui.activity;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyz.handquestionnaire.BaseActivity;
import com.jyz.handquestionnaire.R;

/**
 * Created by Songzhihang on 2017/10/6.
 */
public class MainTabActivity extends BaseActivity{
    private static final String TAG = "MainTabActivity";
    private FrameLayout contentFL;
    private LinearLayout tabLL;
    private TextView leftTV;
    private TextView rightTV;
    private ImageView addIV;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_maintab_layout);
    }

    @Override
    protected void findViews() {
        contentFL=(FrameLayout)findViewById(R.id.aml_fl_content);
        tabLL=(LinearLayout)findViewById(R.id.aml_ll_tab_layout);
        leftTV=(TextView)findViewById(R.id.aml_tv_left);
        rightTV=(TextView)findViewById(R.id.aml_tv_right);
        addIV=(ImageView)findViewById(R.id.aml_iv_add);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        leftTV.setOnClickListener(this);
        rightTV.setOnClickListener(this);
        addIV.setOnClickListener(this);
    }
}
