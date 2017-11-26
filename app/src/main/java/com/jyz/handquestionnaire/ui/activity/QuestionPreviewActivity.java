package com.jyz.handquestionnaire.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Selection;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jyz.handquestionnaire.BaseActivity;
import com.jyz.handquestionnaire.BaseApplication;
import com.jyz.handquestionnaire.R;
import com.jyz.handquestionnaire.api.OkHttpHelp;
import com.jyz.handquestionnaire.bean.QuestionItem;
import com.jyz.handquestionnaire.bean.QuestionnaireItem;
import com.jyz.handquestionnaire.bean.ResultItem;
import com.jyz.handquestionnaire.bean.SelectionItem;
import com.jyz.handquestionnaire.bean.UserItem;
import com.jyz.handquestionnaire.listener.ResponseListener;
import com.jyz.handquestionnaire.ui.widget.WheelViewDialog;
import com.jyz.handquestionnaire.util.Constant;
import com.jyz.handquestionnaire.util.DateUtil;
import com.jyz.handquestionnaire.util.MyUtil;
import com.jyz.handquestionnaire.util.ProgressDialogUtil;
import com.jyz.handquestionnaire.util.SpfUtil;
import com.jyz.handquestionnaire.util.WheelDateUtil;
import com.jyz.handquestionnaire.util.WheelViewDialogUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @discription 预览页面
 * @autor songzhihang
 * @time 2017/10/25  下午4:50
 **/
public class QuestionPreviewActivity extends BaseActivity {
    private static final String TAG = "QuestionPreviewActivity";

    private LinearLayout aqpl_ll_table_layout;
    private TextView aqpl_tv_empty;
    private EditText aqpl_et_introduce;
    private TextView aqpl_tv_submit;

    private QuestionnaireItem questionnaireItem;
    private WheelDateUtil wheelDateUtil;
    private WheelViewDialog timeWheelDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createActivityList.add(this);
    }

    @Override
    protected void setView() {
        setContentView(R.layout.activity_question_preview_layout);
    }

    @Override
    protected void findViews() {
        aqpl_ll_table_layout = (LinearLayout) findViewById(R.id.aqpl_ll_table_layout);
        aqpl_tv_empty = (TextView) findViewById(R.id.aqpl_tv_empty);
        aqpl_et_introduce = (EditText) findViewById(R.id.aqpl_et_introduce);
        aqpl_tv_submit = (TextView) findViewById(R.id.aqpl_tv_submit);
    }

    @Override
    protected void initData() {
        String title = getIntent().getBundleExtra("bundle").getString("title");
        questionnaireItem = (QuestionnaireItem) getIntent().getBundleExtra("bundle").getSerializable("questionnaireItem");
        setTitle(title);
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
            public void onClick(View view) {
                questionnaireItem.setIntroduce(aqpl_et_introduce.getText().toString());
                onSubmitAction();
            }
        });
    }

    private void initTimeDialog() {
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params1.weight = 1;
        params1.gravity = Gravity.RIGHT;
        wheelDateUtil = new WheelDateUtil(mContext);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(MyUtil.toDip(50), ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params3.weight = 1;
        params3.gravity = Gravity.LEFT;
        wheelDateUtil.setCyclic(true);
        wheelDateUtil.textSize = 17;
        LinkedHashMap<View, LinearLayout.LayoutParams> map = new LinkedHashMap<>();
        map.put(wheelDateUtil.getWv_year(), params1);
        map.put(wheelDateUtil.getWv_month(), params2);
        map.put(wheelDateUtil.getWv_day(), params3);
        timeWheelDialog = WheelViewDialogUtil.showWheelViewDialog(QuestionPreviewActivity.this, "选择结束时间", new WheelViewDialog.DialogSubmitListener() {
            @Override
            public void onSubmitClick(View v) {
                String finishTime = wheelDateUtil.getYear()
                        + "-" + wheelDateUtil.getMonth() + "-" + wheelDateUtil.getDay();
                questionnaireItem.setFinishTime(finishTime);
                timeWheelDialog.dismiss();
            }
        }, map);
    }

    private void createContentView(QuestionnaireItem questionnaireItem) {
        if (questionnaireItem != null && !TextUtils.isEmpty(questionnaireItem.getIntroduce())) {
            aqpl_et_introduce.setText(questionnaireItem.getIntroduce());
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
        final ArrayList<RadioButton> radioButtons = new ArrayList<>();
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
        final ArrayList<CheckBox> checkBoxes = new ArrayList<>();
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
        lbs_et_answer.setMaxLines(Integer.parseInt(questionItem.getLine()));
        lbs_et_answer.setLines(Integer.parseInt(questionItem.getLine()));
        aqpl_ll_table_layout.addView(view);
    }

    private void onSubmitAction() {
        if (questionnaireItem == null || questionnaireItem.getQuestionItemList() == null || questionnaireItem.getQuestionItemList().size() == 0) {
            toast("不能发表空问卷！");
            return;
        }
        if (TextUtils.isEmpty(questionnaireItem.getIntroduce())) {
            toast("问卷简介不能为空！");
            return;
        }
        if (TextUtils.isEmpty(questionnaireItem.getThanks())) {
            toast("感谢语不能为空！");
            return;
        }
        if (TextUtils.isEmpty(questionnaireItem.getTitle())) {
            toast("问卷标题不能为空！");
            return;
        }
        if (TextUtils.isEmpty(questionnaireItem.getFinishTime())) {
            //提示问卷截止日期选择
            if (timeWheelDialog == null) {
                initTimeDialog();
                Calendar calendar=Calendar.getInstance();
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                wheelDateUtil.setPicker(year,month,day);
            }
            timeWheelDialog.show();
            return;
        }
        ProgressDialogUtil.showProgressDialog(this, true);
        Map<String, String> params = new HashMap<>();
        UserItem userItem = BaseApplication.getAPPInstance().getmUser();
        params.put("userId", userItem.getUserId() + "");
        params.put("nickname", userItem.getNickName() + "");
        params.put("title", questionnaireItem.getTitle());
        params.put("introduce", questionnaireItem.getIntroduce());
        params.put("thanks", questionnaireItem.getThanks());
        params.put("finishTime", questionnaireItem.getFinishTime());
        params.put("finishTimeStmp", DateUtil.getStringToDate(questionnaireItem.getFinishTime(),DateUtil.Date_Format_1)+"");
        int count = 0;
        for (int i = 0; i < questionnaireItem.getQuestionItemList().size(); i++) {
            QuestionItem questionItem = questionnaireItem.getQuestionItemList().get(i);
            params.put("title" + i, questionItem.getTitle());
            params.put("type" + i, questionItem.getType());
            params.put("isMust" + i, questionItem.getIsMust());
            if (TextUtils.equals("1", questionItem.getIsMust())) {
                count++;
            }
            if (TextUtils.equals("3", questionItem.getType())) {
                params.put("lines" + i, questionItem.getLine());
            } else {
                if (TextUtils.equals("2", questionItem.getType())) {
                    params.put("least" + i, questionItem.getLeast());
                    params.put("more" + i, questionItem.getMore());
                }
                for (int j = 0; j < questionItem.getSelectionItemList().size(); j++) {
                    SelectionItem selection = questionItem.getSelectionItemList().get(j);
                    params.put(i + "title" + j, selection.getTitle());
                    params.put(i + "isSelect" + j, selection.getIsSelect());
                }
            }
        }
        if (count == 0) {
            toast("至少有一个问题需要是必填！");
            return;
        }
        OkHttpHelp<ResultItem> httpHelp = OkHttpHelp.getInstance();
        httpHelp.httpRequest("post", Constant.CREATE_QUESTIONNAIRE, params, new ResponseListener<ResultItem>() {
            @Override
            public void onSuccess(ResultItem object) {
                ProgressDialogUtil.dismissProgressdialog();
                if (!object.getResult().equals("fail")) {
                    toast("问卷创建成功！");
                    for (BaseActivity activity : createActivityList) {
                        if (activity != QuestionPreviewActivity.this) {
                            activity.finish();
                        }
                    }
                    finish();
                } else {
                    toast("创建问卷出错!请稍后重试");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        createActivityList.remove(this);
    }
}
