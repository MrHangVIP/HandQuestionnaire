package com.jyz.handquestionnaire.ui.activity;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyz.handquestionnaire.BaseActivity;
import com.jyz.handquestionnaire.R;

/**
 * @discription 创建选项页面 包含单选和多选
 * @autor songzhihang
 * @time 2017/10/17  下午5:08
 **/
public class CreateSelectionActivity extends BaseActivity{
    private static final String TAG = "CreateSelectionActivity";
    private EditText acsl_et_title;
    private TextView acsl_tv_single;
    private TextView acsl_tv_more;
    private LinearLayout acsl_ll_selection_layout;
    private TextView acsl_tv_create;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_create_selection_layout);
    }

    @Override
    protected void findViews() {
        acsl_et_title=(EditText)findViewById(R.id.acsl_et_title);
        acsl_tv_single=(TextView)findViewById(R.id.acsl_tv_single);
        acsl_tv_more=(TextView)findViewById(R.id.acsl_tv_more);
        acsl_ll_selection_layout=(LinearLayout)findViewById(R.id.acsl_ll_selection_layout);
        acsl_tv_create=(TextView)findViewById(R.id.acsl_tv_create);
    }

    @Override
    protected void initData() {
//        String title=getIntent().getBundleExtra("bundle").getString("title",);
//        setTitle(title);
        setTitle("单选题");
    }

    @Override
    protected void setListener() {

    }
}
