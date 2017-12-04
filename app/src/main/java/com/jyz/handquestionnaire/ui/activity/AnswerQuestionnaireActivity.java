package com.jyz.handquestionnaire.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Selection;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jyz.handquestionnaire.BaseActivity;
import com.jyz.handquestionnaire.BaseApplication;
import com.jyz.handquestionnaire.R;
import com.jyz.handquestionnaire.api.OkHttpHelp;
import com.jyz.handquestionnaire.bean.AnswerItem;
import com.jyz.handquestionnaire.bean.AnswerTagItem;
import com.jyz.handquestionnaire.bean.QuestionItem;
import com.jyz.handquestionnaire.bean.QuestionnaireItem;
import com.jyz.handquestionnaire.bean.ResultItem;
import com.jyz.handquestionnaire.bean.SelectionItem;
import com.jyz.handquestionnaire.bean.UserItem;
import com.jyz.handquestionnaire.listener.ResponseListener;
import com.jyz.handquestionnaire.ui.widget.ShareDialog;
import com.jyz.handquestionnaire.util.Constant;
import com.jyz.handquestionnaire.util.MyUtil;
import com.jyz.handquestionnaire.util.ProgressDialogUtil;
import com.jyz.handquestionnaire.util.SpfUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @discription 回答问卷页面
 * @autor songzhihang
 * @time 2017/11/19  下午4:50
 **/
public class AnswerQuestionnaireActivity extends BaseActivity {
    private static final String TAG = "AnswerQuestionnaireActivity";

    private LinearLayout aqal_ll_table_layout;
    private TextView aqal_tv_introduce;
    private TextView aqal_tv_submit;

    private ArrayList<EditText> editTexts = new ArrayList<>();
    private ArrayList<AnswerTagItem> answerTagItems = new ArrayList<>();
    private QuestionnaireItem questionnaireItem;


    @Override
    protected void setView() {
        setContentView(R.layout.activity_question_answer_layout);
    }

    @Override
    protected void findViews() {
        aqal_ll_table_layout = (LinearLayout) findViewById(R.id.aqal_ll_table_layout);
        aqal_tv_introduce = (TextView) findViewById(R.id.aqal_tv_introduce);
        aqal_tv_submit = (TextView) findViewById(R.id.aqal_tv_submit);
        setMenu();
    }

    @Override
    protected void initData() {
        questionnaireItem = (QuestionnaireItem) getIntent().getBundleExtra("bundle").getSerializable("questionnaireItem");
        String title = questionnaireItem.getTitle();
        setTitle(title);
        createContentView(questionnaireItem);
    }

    @Override
    protected void setListener() {
        aqal_tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmitAction();
            }
        });
    }

    void setMenu(){
        toolbar.inflateMenu(R.menu.menu_share);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new ShareDialog((Activity)AnswerQuestionnaireActivity.this ).show();
                return true;
            }
        });
    }

    private void createContentView(QuestionnaireItem questionnaireItem) {
        if (!TextUtils.isEmpty(questionnaireItem.getIntroduce())) {
            aqal_tv_introduce.setText(questionnaireItem.getIntroduce());
        }
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
        }
    }

    /**
     * 单选
     *
     * @param questionItem
     * @param position
     */
    private void addSingleSelection(QuestionItem questionItem, int position) {
        final ArrayList<RadioButton> radioButtons = new ArrayList<>();
        AnswerTagItem answerTagItem = new AnswerTagItem();
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_single_selection, null);
        TextView lss_tv_num = (TextView) view.findViewById(R.id.lss_tv_num);
        LinearLayout lss_ll_table_layout = (LinearLayout) view.findViewById(R.id.lss_ll_table_layout);
        String textStr = (position + 1) + "（单选题）. " + questionItem.getTitle();
        if (TextUtils.equals("1", questionItem.getIsMust())) {
            textStr = textStr + " * ";
            SpannableString contentStr = new SpannableString(textStr);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#DC3C38"));
            contentStr.setSpan(colorSpan, textStr.length() - 3, textStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            lss_tv_num.setText(contentStr);
        }
        if (questionItem.getSelectionItemList() != null) {
            int count = 0;
            for (SelectionItem selectionItem : questionItem.getSelectionItemList()) {
                count++;
                final AnswerItem answerItem = new AnswerItem();
                answerItem.setQuestionnaireId(questionnaireItem.getQuestionnaireId());
                answerItem.setQuestionId(questionItem.getQuestionId());
                answerItem.setSelectionId(selectionItem.getSelectionId());
                answerItem.setAnswer(count + "");
                final RadioButton radioButton = new RadioButton(mContext);
                radioButton.setTag(selectionItem);
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
                LinearLayout linearLayout = new LinearLayout(mContext);
                linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                TextView textView = new TextView(mContext);
                textView.setText(count + ". ");
                textView.setTextSize(16);
                textView.setTextColor(Color.parseColor("#333333"));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.rightMargin = MyUtil.toDip(5);
                linearLayout.addView(textView, params);
                linearLayout.addView(radioButton);
                lss_ll_table_layout.addView(linearLayout);
            }
        }
        aqal_ll_table_layout.addView(view);
        answerTagItem.setQuestionItem(questionItem);
        answerTagItem.setRadioButtons(radioButtons);
        answerTagItems.add(answerTagItem);
    }

    /**
     * 多选
     *
     * @param questionItem
     * @param position
     */
    private void addMoreSelection(QuestionItem questionItem, int position) {
        final ArrayList<CheckBox> checkBoxes = new ArrayList<>();
        AnswerTagItem answerTagItem = new AnswerTagItem();
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_more_selection, null);
        TextView lss_tv_num = (TextView) view.findViewById(R.id.lss_tv_num);
        LinearLayout lss_ll_table_layout = (LinearLayout) view.findViewById(R.id.lss_ll_table_layout);
        final TextView lss_tv_notice = (TextView) view.findViewById(R.id.lss_tv_notice);
        String textStr = (position + 1) + "（多选题）. " + questionItem.getTitle();
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
            int count = 0;
            for (final SelectionItem selectionItem : questionItem.getSelectionItemList()) {
                final AnswerItem answerItem = new AnswerItem();
                answerItem.setQuestionnaireId(questionnaireItem.getQuestionnaireId());
                answerItem.setQuestionId(questionItem.getQuestionId());
                answerItem.setSelectionId(selectionItem.getSelectionId());
                answerItem.setAnswer(count + "");
                count++;
                CheckBox checkBox = new CheckBox(mContext);
                checkBox.setTag(selectionItem);
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
                        if (checkedNum < leastNum && leastNum != 0) {
                            lss_tv_notice.setText("最少选择" + leastNum + "项");
                            lss_tv_notice.setVisibility(View.VISIBLE);
                        }
                        if (checkedNum > moreNum && moreNum != 0) {
                            lss_tv_notice.setText((TextUtils.isEmpty(lss_tv_notice.getText().toString()) ?
                                    "" : lss_tv_notice.getText() + ",") + "最多选择" + moreNum + "项");
                            lss_tv_notice.setVisibility(View.VISIBLE);
                        }
                    }
                });
                LinearLayout linearLayout = new LinearLayout(mContext);
                linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                TextView textView = new TextView(mContext);
                textView.setText(count + ". ");
                textView.setTextSize(16);
                textView.setTextColor(Color.parseColor("#333333"));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.rightMargin = MyUtil.toDip(5);
                linearLayout.addView(textView, params);
                linearLayout.addView(checkBox);
                checkBoxes.add(checkBox);
                lss_ll_table_layout.addView(linearLayout);
            }
        }
        aqal_ll_table_layout.addView(view);
        answerTagItem.setQuestionItem(questionItem);
        answerTagItem.setCheckBoxes(checkBoxes);
        answerTagItems.add(answerTagItem);
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
        String textStr = (position + 1) + "（填空题）. " + questionItem.getTitle();
        if (TextUtils.equals("1", questionItem.getIsMust())) {
            textStr = textStr + " * ";
            SpannableString contentStr = new SpannableString(textStr);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#DC3C38"));
            contentStr.setSpan(colorSpan, textStr.length() - 3, textStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            lss_tv_num.setText(contentStr);
        }
        lbs_et_answer.setTag(questionItem);
        final AnswerItem answerItem = new AnswerItem();
        answerItem.setQuestionnaireId(questionnaireItem.getQuestionnaireId());
        answerItem.setQuestionId(questionItem.getQuestionId());
        lbs_et_answer.setMaxLines(Integer.parseInt(questionItem.getLine()));
        lbs_et_answer.setLines(Integer.parseInt(questionItem.getLine()));
        editTexts.add(lbs_et_answer);
        aqal_ll_table_layout.addView(view);
    }

    private void onSubmitAction() {
        UserItem userItem = BaseApplication.getAPPInstance().getmUser();
        if (!SpfUtil.getBoolean(Constant.IS_LOGIN, false) || userItem == null) {
            toast("请先登陆！");
            return;
        }
        ArrayList<AnswerItem> answerItemList = new ArrayList<>();
        for (EditText editText : editTexts) {
            QuestionItem item = (QuestionItem) editText.getTag();
            if (TextUtils.equals("1", item.getIsMust()) && editText.getText().length() <= 0) {
                showTip();
                answerItemList.clear();
                return;
            }
            if (editText.getText().length() > 0) {
                AnswerItem answerItem = new AnswerItem();
                answerItem.setAnswer(editText.getText().toString());
                answerItem.setType(item.getType());
                answerItem.setQuestionnaireId(item.getQuestionnaireId());
                answerItem.setQuestionId(item.getQuestionId());
                answerItem.setUserId(userItem.getUserId());
                answerItemList.add(answerItem);
            }
        }

        for (AnswerTagItem answerTagItem : answerTagItems) {
            QuestionItem item = answerTagItem.getQuestionItem();
            String type = item.getType();
            if (TextUtils.equals("1", type)) {//单选
                ArrayList<RadioButton> radioButtons=answerTagItem.getRadioButtons();
                SelectionItem selectionItem=null;
                for (RadioButton radioButton:radioButtons){
                    if(radioButton.isChecked()){
                        selectionItem=(SelectionItem)radioButton.getTag();
                    }
                }
                if (TextUtils.equals("1", item.getIsMust())&& selectionItem==null) {
                    showTip();
                    answerItemList.clear();
                    return;
                }
                if(selectionItem!=null){
                    AnswerItem answerItem = new AnswerItem();
                    answerItem.setAnswer(selectionItem.getTitle());
                    answerItem.setType(item.getType());
                    answerItem.setQuestionnaireId(item.getQuestionnaireId());
                    answerItem.setQuestionId(item.getQuestionId());
                    answerItem.setSelectionId(selectionItem.getSelectionId());
                    answerItem.setUserId(userItem.getUserId());
                    answerItemList.add(answerItem);
                }
            }
            if (TextUtils.equals("2",type)){
                ArrayList<CheckBox> checkBoxes=answerTagItem.getCheckBoxes();
                int selection=0;
                for (CheckBox checkBox:checkBoxes){
                    if(checkBox.isChecked()){
                        selection++;
                       SelectionItem selectionItem=(SelectionItem)checkBox.getTag();
                        AnswerItem answerItem = new AnswerItem();
                        answerItem.setAnswer(selectionItem.getTitle());
                        answerItem.setType(item.getType());
                        answerItem.setQuestionnaireId(item.getQuestionnaireId());
                        answerItem.setQuestionId(item.getQuestionId());
                        answerItem.setSelectionId(selectionItem.getSelectionId());
                        answerItem.setUserId(userItem.getUserId());
                        answerItemList.add(answerItem);
                    }
                }
                int least=Integer.parseInt(item.getLeast());
                int more=Integer.parseInt(item.getMore());
                if(least<more){
                    least=more;
                }
                if(selection<least || selection>more){
                    toast("多选题选项个数请参考提示！");
                    return;
                }
            }
        }
        ProgressDialogUtil.showProgressDialog(this, true);
        Map<String, String> params = new HashMap<>();
        int count = 0;
        for(AnswerItem answerItem:answerItemList){
            params.put("questionnaireId"+count, answerItem.getQuestionnaireId()+"");
            params.put("questionId"+count, answerItem.getQuestionId()+"");
            params.put("userId"+count, answerItem.getUserId()+"");
            params.put("answer"+count, answerItem.getAnswer()+"");
            params.put("selectionId"+count, answerItem.getSelectionId()+"");
            params.put("type"+count, answerItem.getType()+"");
            count++;
        }
        OkHttpHelp<ResultItem> httpHelp = OkHttpHelp.getInstance();
        httpHelp.httpRequest("post", Constant.CREATE_ANSWER, params, new ResponseListener<ResultItem>() {
            @Override
            public void onSuccess(ResultItem object) {
                ProgressDialogUtil.dismissProgressdialog();
                if (!object.getResult().equals("fail")) {
                    toast(questionnaireItem.getThanks());
                    finish();
                } else {
                    toast("回答失敗!请稍后重试");
                }
            }
            @Override
            public void onFailed(String message) {
                ProgressDialogUtil.dismissProgressdialog();
            }

            @Override
            public Class<ResultItem> getEntityClass() {
                return ResultItem.class;
            }
        });
    }

    private void showTip() {
        toast("必填项必须回答！");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
