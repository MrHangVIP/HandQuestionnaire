package com.jyz.handquestionnaire.ui.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyz.handquestionnaire.BaseActivity;
import com.jyz.handquestionnaire.R;

/**
 * @discription 编辑问卷页面
 * @autor songzhihang
 * @time 2017/10/16  下午4:10
 **/
public class EditQuestionActivity extends BaseActivity {
    private static final String TAG = "CreateQuestionActivity";

    private TextView aeql_tv_title;
    private TextView aeql_tv_edit_title;
    private LinearLayout aeql_ll_question_layout;
    private TextView aeql_tv_add;
    private TextView aeql_tv_thanks;
    private TextView aeql_tv_edit_thanks;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_edit_question_layout);
    }

    @Override
    protected void findViews() {
        setTitle("编辑问卷");
        aeql_tv_title = (TextView) findViewById(R.id.aeql_tv_title);
        aeql_tv_edit_title = (TextView) findViewById(R.id.aeql_tv_edit_title);
        aeql_ll_question_layout = (LinearLayout) findViewById(R.id.aeql_ll_question_layout);
        aeql_tv_add = (TextView) findViewById(R.id.aeql_tv_add);
        aeql_tv_thanks = (TextView) findViewById(R.id.aeql_tv_thanks);
        aeql_tv_edit_thanks = (TextView) findViewById(R.id.aeql_tv_edit_thanks);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

        aeql_tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        aeql_tv_edit_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        aeql_tv_edit_thanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
