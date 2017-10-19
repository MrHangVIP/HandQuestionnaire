package com.jyz.handquestionnaire.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyz.handquestionnaire.BaseActivity;
import com.jyz.handquestionnaire.R;
import com.jyz.handquestionnaire.bean.QuestionItem;
import com.jyz.handquestionnaire.ui.widget.CustomMoreView;

import org.w3c.dom.Text;

/**
 * @discription 编辑问卷页面
 * @autor songzhihang
 * @time 2017/10/16  下午4:10
 **/
public class EditQuestionActivity extends BaseActivity {
    private static final String TAG = "CreateQuestionActivity";

    public static final int REQUEST_CODE = 0x10;
    public static final int RESULT_CODE = 0x11;
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
        String title = getIntent().getBundleExtra("bundle").getString("title");
        aeql_tv_title.setText(title);
    }

    @Override
    protected void setListener() {

        aeql_tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomMoreView customMoreView = new CustomMoreView(EditQuestionActivity.this);
                customMoreView.init();
                customMoreView.showMoreWindow(toolbar);
                customMoreView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (Integer) v.getTag();
                        if (position == 0) {//单选
                            Intent intent = new Intent();
                            intent.setClass(EditQuestionActivity.this, CreateSelectionActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("type", "1");
                            intent.putExtra("bundle", bundle);
                            startActivityForResult(intent, REQUEST_CODE);
                        }
                        if (position == 1) {//多选
                            Intent intent = new Intent();
                            intent.setClass(EditQuestionActivity.this, CreateSelectionActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("type", "2");
                            intent.putExtra("bundle", bundle);
                            startActivityForResult(intent, REQUEST_CODE);
                        }
                    }
                });
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

    private void addQuestionView(final QuestionItem questionItem) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_question_create_layout, null);
        TextView aqcl_tv_title = (TextView) view.findViewById(R.id.aqcl_tv_title);
        TextView aqcl_tv_type = (TextView) view.findViewById(R.id.aqcl_tv_type);
        view.setTag(questionItem);
        aqcl_tv_title.setText(questionItem.getTitle());
        aqcl_tv_type.setText(getTypeStr(questionItem.getType()));
        aeql_ll_question_layout.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(EditQuestionActivity.this, CreateSelectionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("questionItem", questionItem);
                intent.putExtra("bundle", bundle);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    /**
     * 获取题型
     *
     * @param type
     * @return
     */
    private String getTypeStr(String type) {
        if (TextUtils.equals("1", type)) {
            return "单选题";
        }
        if (TextUtils.equals("2", type)) {
            return "多选题";
        }
        if (TextUtils.equals("3", type)) {
            return "填空题";
        }
        return "单选题";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            QuestionItem questionItem = (QuestionItem) data.getSerializableExtra("questionItem");
            addQuestionView(questionItem);
        }
    }
}
