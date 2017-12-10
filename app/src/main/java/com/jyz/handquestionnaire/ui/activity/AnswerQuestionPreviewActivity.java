package com.jyz.handquestionnaire.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
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
import com.jyz.handquestionnaire.listener.ResponseListener;
import com.jyz.handquestionnaire.ui.widget.ShareDialog;
import com.jyz.handquestionnaire.util.Constant;
import com.jyz.handquestionnaire.util.MyUtil;
import com.jyz.handquestionnaire.util.ProgressDialogUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @discription问卷答案展示页面
 * @autor songzhihang
 * @time 2017/11/19  下午4:50
 **/
public class AnswerQuestionPreviewActivity extends BaseActivity {
    private static final String TAG = "AnswerQuestionPreviewActivity";

    private LinearLayout aqal_ll_table_layout;
    private TextView aqal_tv_introduce;

    private QuestionnaireItem questionnaireItem;
    private ArrayList<AnswerItem> answerItems;


    @Override
    protected void setView() {
        setContentView(R.layout.activity_question_answer__preview_layout);
    }

    @Override
    protected void findViews() {
        aqal_ll_table_layout = (LinearLayout) findViewById(R.id.aqal_ll_table_layout);
        aqal_tv_introduce = (TextView) findViewById(R.id.aqal_tv_introduce);
        setMenu();
    }

    @Override
    protected void initData() {
        questionnaireItem = (QuestionnaireItem) getIntent().getBundleExtra("bundle").getSerializable("questionnaireItem");
        answerItems = (ArrayList<AnswerItem>) getIntent().getBundleExtra("bundle").getSerializable("answerList");
        String title = questionnaireItem.getTitle();
        setTitle(title);
        createContentView(questionnaireItem);
    }

    @Override
    protected void setListener() {
    }

    void setMenu() {
        toolbar.inflateMenu(R.menu.menu_share);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new ShareDialog((Activity) AnswerQuestionPreviewActivity.this).
                        setShareContent("我在掌上问卷上回答了（" + questionnaireItem.getTitle() + "）这个问卷，快来看看吧！").show();
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
                final RadioButton radioButton = new RadioButton(mContext);
                radioButton.setTag(selectionItem);
                radioButton.setText(selectionItem.getTitle());
                for (AnswerItem answerItem : answerItems) {
                    if (answerItem.getSelectionId() == selectionItem.getSelectionId()) {
                        radioButton.setChecked(true);
                    }
                }
                radioButton.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return true;
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
                linearLayout.addView(radioButton);
                lss_ll_table_layout.addView(linearLayout);
            }
        }
        aqal_ll_table_layout.addView(view);
        answerTagItem.setQuestionItem(questionItem);
    }

    /**
     * 多选
     *
     * @param questionItem
     * @param position
     */
    private void addMoreSelection(QuestionItem questionItem, int position) {
        AnswerTagItem answerTagItem = new AnswerTagItem();
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_more_selection, null);
        TextView lss_tv_num = (TextView) view.findViewById(R.id.lss_tv_num);
        LinearLayout lss_ll_table_layout = (LinearLayout) view.findViewById(R.id.lss_ll_table_layout);
        String textStr = (position + 1) + "（多选题）. " + questionItem.getTitle();
        String mustStr = "";
        if (TextUtils.equals("1", questionItem.getIsMust())) {
            mustStr = " * ";
        }
        String selectionStr = "";
        if (!TextUtils.equals("不限", questionItem.getLeast())) {
            selectionStr = "[最少选择" + questionItem.getLeast() + "项]";
        }
        if (!TextUtils.equals("不限", questionItem.getMore())) {
            selectionStr = (TextUtils.isEmpty(selectionStr) ? "" : selectionStr + ",") + "[最多选择" + questionItem.getMore() + "项]";
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
        if (questionItem.getSelectionItemList() != null) {
            int count = 0;
            for (final SelectionItem selectionItem : questionItem.getSelectionItemList()) {
                CheckBox checkBox = new CheckBox(mContext);
                checkBox.setTag(selectionItem);
                checkBox.setText(selectionItem.getTitle());
                if (selectionItem.getIsSelect() != null && TextUtils.equals("1", selectionItem.getIsSelect())) {
                    checkBox.setChecked(true);
                }
                for (AnswerItem answerItem : answerItems) {
                    if (answerItem.getSelectionId() == selectionItem.getSelectionId()) {
                        checkBox.setChecked(true);
                    }
                }
                checkBox.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return true;
                    }
                });
                count++;
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
                lss_ll_table_layout.addView(linearLayout);
            }
        }
        aqal_ll_table_layout.addView(view);
        answerTagItem.setQuestionItem(questionItem);
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
        for (AnswerItem answerItem : answerItems) {
            if (answerItem.getQuestionId() == questionItem.getQuestionId()) {
                lbs_et_answer.setText(answerItem.getAnswer());
                lbs_et_answer.setFocusable(false);
                lbs_et_answer.setFocusableInTouchMode(false);
            }
        }
        aqal_ll_table_layout.addView(view);
    }

//    private void getAnswer() {
//        ProgressDialogUtil.showProgressDialog(this, true);
//        Map<String, String> params = new HashMap<>();
//        params.put("userId", BaseApplication.getAPPInstance().getmUser().getUserId() + "");
//        params.put("userId", questionnaireItem.getQuestionnaireId() + "");
//        OkHttpHelp<ResultItem> httpHelp = OkHttpHelp.getInstance();
//        httpHelp.httpRequest("post", Constant.CREATE_ANSWER, params, new ResponseListener<ResultItem>() {
//            @Override
//            public void onSuccess(ResultItem object) {
//                ProgressDialogUtil.dismissProgressdialog();
//                if ("fail".equals(object.getResult())) {
//                    toast("网络错误，请重试！");
//                    return;
//                }
//                answerItems = new ArrayList<AnswerItem>();
//                JSONArray jsonArray = null;
//                answerItems.clear();
//                try {
//                    jsonArray = new JSONArray(object.getData());
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        AnswerItem postBarItem = new Gson().fromJson(jsonArray.get(i).toString(), AnswerItem.class);
//                        answerItems.add(postBarItem);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailed(String message) {
//                ProgressDialogUtil.dismissProgressdialog();
//            }
//
//            @Override
//            public Class<ResultItem> getEntityClass() {
//                return ResultItem.class;
//            }
//        });
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
