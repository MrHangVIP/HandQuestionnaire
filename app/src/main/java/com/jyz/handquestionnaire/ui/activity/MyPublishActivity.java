package com.jyz.handquestionnaire.ui.activity;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyz.handquestionnaire.BaseActivity;
import com.jyz.handquestionnaire.R;
import com.jyz.handquestionnaire.bean.QuestionItem;
import com.jyz.handquestionnaire.bean.QuestionnaireItem;

/**我发布问卷的统计页面
 * Created by Songzhihang on 2017/12/4.
 */
public class MyPublishActivity extends BaseActivity{

    private static final String TAG = "MyPublishActivity";
    private LinearLayout aqal_ll_table_layout;
    private TextView aqal_tv_introduce;

    private QuestionnaireItem questionnaireItem;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_my_publish_layout);
    }

    @Override
    protected void findViews() {
        aqal_ll_table_layout = (LinearLayout) findViewById(R.id.aqal_ll_table_layout);
        aqal_tv_introduce = (TextView) findViewById(R.id.aqal_tv_introduce);
    }

    @Override
    protected void initData() {
        questionnaireItem = (QuestionnaireItem) getIntent().getBundleExtra("bundle").getSerializable("questionnaireItem");
        String title = questionnaireItem.getTitle();
        setTitle(title);
        addQuestionLayout();
    }

    @Override
    protected void setListener() {

    }

    private void addQuestionLayout(){
        if (questionnaireItem != null && questionnaireItem.getQuestionItemList() != null && questionnaireItem.getQuestionItemList().size() != 0) {
            aqal_tv_introduce.setText(questionnaireItem.getIntroduce());
            for (int i = 0; i < questionnaireItem.getQuestionItemList().size(); i++) {
                QuestionItem questionItem = questionnaireItem.getQuestionItemList().get(i);
                View view = LayoutInflater.from(mContext).inflate(R.layout.layout_blank_selection, null);
                TextView lss_tv_num = (TextView) view.findViewById(R.id.lbs_tv_num);
                EditText lbs_et_answer = (EditText) view.findViewById(R.id.lbs_et_answer);
                View lbs_line =  view.findViewById(R.id.lbs_line);
                lbs_et_answer.setVisibility(View.GONE);
                lss_tv_num.setLines(2);
                lbs_line.setVisibility(View.VISIBLE);
                String type = questionItem.getType();
                if (TextUtils.equals(questionItem.getType(), "1")) {
                   type="单选题";
                }
                if (TextUtils.equals(questionItem.getType(), "2")) {
                    type="多选题";
                }
                if (TextUtils.equals(questionItem.getType(), "3")) {
                    type="填空题";
                }
                String textStr = (i + 1) + "（"+type+"）. " + questionItem.getTitle();
                if (TextUtils.equals("1", questionItem.getIsMust())) {
                    textStr = textStr + " * "+" 点此查看统计结果";
                    SpannableString contentStr = new SpannableString(textStr);
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#DC3C38"));
                    contentStr.setSpan(colorSpan, textStr.length() - 11, textStr.length()-8, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.parseColor("#2C7FFC"));
                    contentStr.setSpan(colorSpan2, textStr.length() - 8, textStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    lss_tv_num.setText(contentStr);
                }else {
                    textStr = textStr +"点此查看统计结果";
                    SpannableString contentStr = new SpannableString(textStr);
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#2C7FFC"));
                    contentStr.setSpan(colorSpan, textStr.length() - 8, textStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    lss_tv_num.setText(contentStr);
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //跳转统计结果页
                    }
                });
                aqal_ll_table_layout.addView(view);
            }
        }
    }
}
