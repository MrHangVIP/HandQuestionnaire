package com.jyz.handquestionnaire.ui.activity;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jyz.handquestionnaire.BaseActivity;
import com.jyz.handquestionnaire.R;
import com.jyz.handquestionnaire.bean.QuestionItem;
import com.jyz.handquestionnaire.bean.QuestionnaireItem;
import com.jyz.handquestionnaire.bean.SelectionItem;

import java.util.ArrayList;

/**
 * @discription 预览页面
 * @autor songzhihang
 * @time 2017/10/25  下午4:50
 **/
public class QuestionPreviewActivity extends BaseActivity {
    private static final String TAG = "QuestionPreviewActivity";

    private TextView aqpl_ll_tv_introduce;
    private LinearLayout aqpl_ll_table_layout;
    private TextView aqpl_tv_empty;
    private TextView aqpl_tv_submit;

    private ArrayList<RadioButton> radioButtons = new ArrayList<>();
    private ArrayList<CheckBox> checkBoxes = new ArrayList<>();
    private QuestionnaireItem questionnaireItem;


    @Override
    protected void setView() {
        setContentView(R.layout.activity_question_preview_layout);
    }

    @Override
    protected void findViews() {
        aqpl_ll_tv_introduce = (TextView) findViewById(R.id.aqpl_ll_tv_introduce);
        aqpl_ll_table_layout = (LinearLayout) findViewById(R.id.aqpl_ll_table_layout);
        aqpl_tv_empty = (TextView) findViewById(R.id.aqpl_tv_empty);
        aqpl_tv_submit = (TextView) findViewById(R.id.aqpl_tv_submit);
    }

    @Override
    protected void initData() {
        String title = getIntent().getBundleExtra("bundle").getString("title");
        questionnaireItem = (QuestionnaireItem) getIntent().getBundleExtra("bundle").getSerializable("questionnaireItem");
        setTitle(title);
        aqpl_ll_tv_introduce.setText(questionnaireItem.getIntroduce());
        createContentView(questionnaireItem);
    }

    @Override
    protected void setListener() {
        aqpl_tv_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        aqpl_tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void createContentView(QuestionnaireItem questionnaireItem) {
        if (questionnaireItem != null && questionnaireItem.getQuestionItemList() != null && questionnaireItem.getQuestionItemList().size() != 0) {
            for (int i = 0; i < questionnaireItem.getQuestionItemList().size(); i++) {
                QuestionItem questionItem = questionnaireItem.getQuestionItemList().get(i);
                String type = questionItem.getType();
                if (TextUtils.equals(type, "1")) {
                    addSingleSelection(questionItem, i);
                }
                if (TextUtils.equals(type, "2")) {
                    addMoreSelection(questionItem, i);
                }
                if (TextUtils.equals(type, "3")) {
                    addBlankView(questionItem, i);
                }
            }
        } else {
            aqpl_tv_empty.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 单选
     *
     * @param questionItem
     * @param position
     */
    private void addSingleSelection(QuestionItem questionItem, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_single_selection, null);
        TextView lss_tv_num = (TextView) view.findViewById(R.id.lss_tv_num);
        LinearLayout lss_ll_table_layout = (LinearLayout) view.findViewById(R.id.lss_ll_table_layout);
        String textStr = (position + 1) + ". " + questionItem.getTitle();
        if (TextUtils.equals("1", questionItem.getIsMust())) {
            textStr = textStr + " * ";
            SpannableString contentStr = new SpannableString(textStr);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#DC3C38"));
            contentStr.setSpan(colorSpan, textStr.length() - 3, textStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            lss_tv_num.setText(contentStr);
        }
        if (questionItem.getSelectionItemList() != null) {
            for (SelectionItem selectionItem : questionItem.getSelectionItemList()) {
                final RadioButton radioButton = new RadioButton(mContext);
                radioButton.setText(selectionItem.getTitle());
                if (selectionItem.getIsSelect() != null && TextUtils.equals("1", selectionItem.getIsSelect())) {
                    radioButton.setChecked(true);
                }
                radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            for (RadioButton item : radioButtons) {
                                if (radioButton != item) {
                                    item.setChecked(false);
                                }
                            }
                        }
                    }
                });
                radioButtons.add(radioButton);
                lss_ll_table_layout.addView(radioButton);
            }
        }
        aqpl_ll_table_layout.addView(view);
    }

    /**
     * 多选
     *
     * @param questionItem
     * @param position
     */
    private void addMoreSelection(QuestionItem questionItem, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_more_selection, null);
        TextView lss_tv_num = (TextView) view.findViewById(R.id.lss_tv_num);
        LinearLayout lss_ll_table_layout = (LinearLayout) view.findViewById(R.id.lss_ll_table_layout);
        final TextView lss_tv_notice = (TextView) view.findViewById(R.id.lss_tv_notice);
        String textStr = (position + 1) + ". " + questionItem.getTitle();
        String mustStr = "";
        if (TextUtils.equals("1", questionItem.getIsMust())) {
            mustStr = " * ";
        }
        int least = 0;
        String selectionStr = "";
        if (!TextUtils.equals("不限", questionItem.getLeast())) {
            selectionStr = "[最少选择" + questionItem.getLeast() + "项]";
            least = Integer.parseInt(questionItem.getLeast());
        }
        final int leastNum = least;
        int more = 0;
        if (!TextUtils.equals("不限", questionItem.getMore())) {
            selectionStr = (TextUtils.isEmpty(selectionStr) ? "" : selectionStr + ",") + "[最多选择" + questionItem.getMore() + "项]";
            more = Integer.parseInt(questionItem.getMore());
        } else {
            more = 100;
        }
        SpannableString contentStr = new SpannableString(textStr + mustStr + selectionStr);
        if (!TextUtils.isEmpty(mustStr)) {
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#DC3C38"));
            contentStr.setSpan(colorSpan, (textStr + mustStr).length() - mustStr.length(), (textStr + mustStr).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        if (!TextUtils.isEmpty(selectionStr)) {
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#2C7FFC"));
            contentStr.setSpan(colorSpan, (textStr + mustStr + selectionStr).length() - selectionStr.length(), (textStr + mustStr + selectionStr).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        lss_tv_num.setText(contentStr);
        final int moreNum = more;
        if (questionItem.getSelectionItemList() != null) {
            for (SelectionItem selectionItem : questionItem.getSelectionItemList()) {
                CheckBox checkBox = new CheckBox(mContext);
                checkBox.setText(selectionItem.getTitle());
                if (selectionItem.getIsSelect() != null && TextUtils.equals("1", selectionItem.getIsSelect())) {
                    checkBox.setChecked(true);
                }
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int checkedNum = 0;
                        for (CheckBox item : checkBoxes) {
                            if (item.isChecked()) {
                                checkedNum++;
                            }
                        }
                        lss_tv_notice.setText("");
                        lss_tv_notice.setVisibility(View.GONE);
                        if (checkedNum < leastNum) {
                            lss_tv_notice.setText("最少选择" + leastNum + "项");
                            lss_tv_notice.setVisibility(View.VISIBLE);
                        }
                        if (checkedNum > moreNum) {
                            lss_tv_notice.setText((TextUtils.isEmpty(lss_tv_notice.getText().toString()) ?
                                    "" : lss_tv_notice.getText() + ",") + "最多选择" + moreNum + "项");
                            lss_tv_notice.setVisibility(View.VISIBLE);
                        }
                    }
                });
                checkBoxes.add(checkBox);
                lss_ll_table_layout.addView(checkBox);
            }
        }
        aqpl_ll_table_layout.addView(view);
    }

    /**
     * 填空
     *
     * @param questionItem
     * @param position
     */
    private void addBlankView(QuestionItem questionItem, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_blank_selection, null);
        TextView lss_tv_num = (TextView) view.findViewById(R.id.lbs_tv_num);
        EditText lbs_et_answer = (EditText) view.findViewById(R.id.lbs_et_answer);
        String textStr = (position + 1) + ". " + questionItem.getTitle();
        if (TextUtils.equals("1", questionItem.getIsMust())) {
            textStr = textStr + " * ";
            SpannableString contentStr = new SpannableString(textStr);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#DC3C38"));
            contentStr.setSpan(colorSpan, textStr.length() - 3, textStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            lss_tv_num.setText(contentStr);
        }
        lbs_et_answer.setMaxLines(Integer.parseInt(questionItem.getLines()));
        lbs_et_answer.setLines(Integer.parseInt(questionItem.getLines()));
        aqpl_ll_table_layout.addView(view);
    }

    /**
     * 发布
     */
    private void onSubmitAction(){

    }
}
