package com.jyz.handquestionnaire.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jyz.handquestionnaire.BaseActivity;
import com.jyz.handquestionnaire.R;

/**
 * @discription 创建问卷页面
 * @autor songzhihang
 * @time 2017/10/16  下午4:10
 **/
public class CreateQuestionActivity extends BaseActivity {
    private static final String TAG = "CreateQuestionActivity";

    private EditText acql_et_name;
    private TextView acql_tv_notice;
    private TextView acql_tv_submit;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_create_question_layout);
    }

    @Override
    protected void findViews() {
        setTitle("创建问卷");
        acql_et_name = (EditText) findViewById(R.id.acql_et_name);
        acql_tv_notice = (TextView) findViewById(R.id.acql_tv_notice);
        acql_tv_submit = (TextView) findViewById(R.id.acql_tv_submit);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        acql_et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        acql_tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(acql_et_name.getText().toString())) {
                    acql_tv_notice.setVisibility(View.VISIBLE);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", acql_et_name.getText().toString().trim());
                    acql_tv_notice.setVisibility(View.GONE);
                    jumpToNext(EditQuestionActivity.class, bundle);
                }
            }
        });
    }
}
