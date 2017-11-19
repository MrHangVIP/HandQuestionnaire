package com.jyz.handquestionnaire.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jyz.handquestionnaire.BaseActivity;
import com.jyz.handquestionnaire.R;
import com.jyz.handquestionnaire.bean.QuestionnaireItem;

/**
 * @discription 修改内容相关页面 包括修改标题说明和结束语
 * @autor songzhihang
 * @time 2017/10/20  下午2:05
 **/
public class UpdateContentActivity extends BaseActivity {
    private static final String TAG = "UpdateContentActivity";

    public static final int REQUEST_CODE = 0x200;
    public static final int RESULT_CODE = 0x201;
    private TextView aupcl_tv_first_label;
    private EditText aupcl_et_first;
    private TextView aupcl_tv_second_label;
    private EditText aupcl_et_second;
    private TextView aupcl_tv_submit;

    private String type;
    private QuestionnaireItem questionnaireItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createActivityList.add(this);
    }

    @Override
    protected void setView() {
        setContentView(R.layout.activity_update_content_layout);
    }

    @Override
    protected void findViews() {
        aupcl_tv_first_label = (TextView) findViewById(R.id.aupcl_tv_first_label);
        aupcl_et_first = (EditText) findViewById(R.id.aupcl_et_first);
        aupcl_tv_second_label = (TextView) findViewById(R.id.aupcl_tv_second_label);
        aupcl_et_second = (EditText) findViewById(R.id.aupcl_et_second);
        aupcl_tv_submit = (TextView) findViewById(R.id.aupcl_tv_submit);
    }

    @Override
    protected void initData() {
        type = getIntent().getStringExtra("type");
        questionnaireItem = (QuestionnaireItem) getIntent().getSerializableExtra("questionnaireItem");
        if (TextUtils.equals(type, "1")) {//修改标题和说明
            setTitle("修改标题与说明");
            aupcl_tv_first_label.setVisibility(View.VISIBLE);
            aupcl_et_first.setVisibility(View.VISIBLE);
            aupcl_et_first.setText(questionnaireItem.getTitle());
            aupcl_et_second.setText(questionnaireItem.getIntroduce());
            aupcl_tv_first_label.setText("问卷标题");
            aupcl_tv_second_label.setText("问卷说明");
        } else {//修改感谢语
            setTitle("修改感谢语");
            aupcl_et_second.setText(questionnaireItem.getThanks());
            aupcl_tv_second_label.setText("问卷结束语");
        }
    }

    @Override
    protected void setListener() {
        aupcl_tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.equals("1", type)) {
                    if (TextUtils.isEmpty(aupcl_et_first.getText().toString().trim())) {
                        toast("标题不能为空");
                        return;
                    } else {
                        questionnaireItem.setTitle(aupcl_et_first.getText().toString().trim());
                    }
                    if (TextUtils.isEmpty(aupcl_et_second.getText().toString().trim())) {
                        toast("说明不能为空");
                        return;
                    } else {
                        questionnaireItem.setIntroduce(aupcl_et_second.getText().toString().trim());
                    }
                } else {
                    if (TextUtils.isEmpty(aupcl_et_second.getText().toString().trim())) {
                        toast("感谢语不能为空!");
                        return;
                    } else {
                        questionnaireItem.setThanks(aupcl_et_second.getText().toString().trim());
                    }
                }
                Intent intent = new Intent();
                intent.putExtra("questionnaireItem", questionnaireItem);
                setResult(RESULT_CODE, intent);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        createActivityList.remove(this);
    }
}
