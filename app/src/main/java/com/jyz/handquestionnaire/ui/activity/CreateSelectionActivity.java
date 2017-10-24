package com.jyz.handquestionnaire.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyz.handquestionnaire.BaseActivity;
import com.jyz.handquestionnaire.R;
import com.jyz.handquestionnaire.bean.QuestionItem;
import com.jyz.handquestionnaire.bean.SelectionItem;
import com.jyz.handquestionnaire.ui.widget.MMAlert;
import com.jyz.handquestionnaire.ui.widget.WheelViewDialog;
import com.jyz.handquestionnaire.util.WheelUtil;
import com.jyz.handquestionnaire.util.WheelViewDialogUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @discription 创建选项页面 包含单选和多选
 * @autor songzhihang
 * @time 2017/10/17  下午5:08
 **/
public class CreateSelectionActivity extends BaseActivity {
    private static final String TAG = "CreateSelectionActivity";
    private EditText acsl_et_title;
    private TextView acsl_tv_single;
    private TextView acsl_tv_more;
    private LinearLayout acsl_ll_selection_layout;
    private TextView acsl_tv_selection_add;
    private LinearLayout acsl_ll_least_layout;
    private TextView acsl_tv_select_least;
    private LinearLayout acsl_ll_more_layout;
    private TextView acsl_tv_select_more;
    private CheckBox acsl_cb_must;
    private TextView acsl_tv_create;

    private WheelViewDialog selectionDialog;

    private String type = "1";//类型 1单选,2多选
    private ArrayList<CheckBox> checkBoxes = new ArrayList<>();
    private ArrayList<EditText> editTexts = new ArrayList<>();

    @Override
    protected void setView() {
        setContentView(R.layout.activity_create_selection_layout);
    }

    @Override
    protected void findViews() {
        acsl_et_title = (EditText) findViewById(R.id.acsl_et_title);
        acsl_tv_single = (TextView) findViewById(R.id.acsl_tv_single);
        acsl_tv_more = (TextView) findViewById(R.id.acsl_tv_more);
        acsl_ll_selection_layout = (LinearLayout) findViewById(R.id.acsl_ll_selection_layout);
        acsl_tv_selection_add = (TextView) findViewById(R.id.acsl_tv_selection_add);
        acsl_ll_least_layout = (LinearLayout) findViewById(R.id.acsl_ll_least_layout);
        acsl_tv_select_least = (TextView) findViewById(R.id.acsl_tv_select_least);
        acsl_ll_more_layout = (LinearLayout) findViewById(R.id.acsl_ll_more_layout);
        acsl_tv_select_more = (TextView) findViewById(R.id.acsl_tv_select_more);
        acsl_cb_must = (CheckBox) findViewById(R.id.acsl_cb_must);
        acsl_tv_create = (TextView) findViewById(R.id.acsl_tv_create);
//        acsl_tv_selection_add.getCompoundDrawables()[0].setBounds(0, 0, MyUtil.toDip(20), MyUtil.toDip(20));
    }

    @Override
    protected void initData() {
        if (getIntent() != null && getIntent().getBundleExtra("bundle") != null) {
            if (getIntent().getBundleExtra("bundle").getSerializable("questionItem") != null) {//修改
                setDateToView((QuestionItem) getIntent().getBundleExtra("bundle").getSerializable("questionItem"));
                return;
            } else {//添加
                type = getIntent().getBundleExtra("bundle").getString("type", "1");
            }
        }
        //默认添加两个
        addSelectionView("", false);
        addSelectionView("", false);
        setSelectTv();
    }

    @Override
    protected void setListener() {

        acsl_tv_single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "1";
                setSelectTv();
            }
        });

        acsl_tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "2";
                setSelectTv();
            }
        });

        acsl_ll_least_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹框提示
                final WheelUtil typeWheelUtil = new WheelUtil(mContext);
                typeWheelUtil.setDatas(getDatas());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                LinkedHashMap<View, LinearLayout.LayoutParams> map = new LinkedHashMap<>();
                map.put(typeWheelUtil.getWheelView(), params);
                selectionDialog = WheelViewDialogUtil.showWheelViewDialog(CreateSelectionActivity.this, "最少选择", new WheelViewDialog.DialogSubmitListener() {
                    @Override
                    public void onSubmitClick(View v) {
                        acsl_tv_select_least.setText(typeWheelUtil.getItems()[typeWheelUtil.getWheelView().getCurrentItem()].toString());
                        selectionDialog.dismiss();
                    }
                }, map);
            }
        });

        acsl_ll_more_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹框提示
                final WheelUtil typeWheelUtil = new WheelUtil(mContext);
                typeWheelUtil.setDatas(getDatas());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                LinkedHashMap<View, LinearLayout.LayoutParams> map = new LinkedHashMap<>();
                map.put(typeWheelUtil.getWheelView(), params);
                selectionDialog = WheelViewDialogUtil.showWheelViewDialog(CreateSelectionActivity.this, "最多选择", new WheelViewDialog.DialogSubmitListener() {
                    @Override
                    public void onSubmitClick(View v) {
                        acsl_tv_select_more.setText(typeWheelUtil.getItems()[typeWheelUtil.getWheelView().getCurrentItem()].toString());
                        selectionDialog.dismiss();
                    }
                }, map);
            }
        });


        acsl_tv_selection_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSelectionView("", false);
            }
        });

        acsl_tv_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAction();
            }
        });
    }

    /**
     * 修改赋值
     */
    private void setDateToView(QuestionItem questionItem) {
        acsl_et_title.setText(questionItem.getTitle());
        type = questionItem.getType();
        acsl_cb_must.setChecked(TextUtils.equals("1", questionItem.getIsMust()));
        for (int i = 0; i < questionItem.getSelectionItemList().size(); i++) {
            SelectionItem selectionItem = questionItem.getSelectionItemList().get(i);
            addSelectionView(selectionItem.getTitle(), TextUtils.equals("1", selectionItem.getIsSelect()));
        }
        if (TextUtils.equals("2", type)) {
            acsl_tv_select_least.setText(questionItem.getLeast());
            acsl_tv_select_more.setText(questionItem.getMore());
        }
        setSelectTv();
    }

    /**
     * 添加选项
     */
    private void addSelectionView(String title, boolean ischeck) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_selection_create_layout, null);
        final CheckBox iscl_checkbox = (CheckBox) view.findViewById(R.id.iscl_checkbox);
        final EditText iscl_edittext = (EditText) view.findViewById(R.id.iscl_edittext);
        ImageView iscl_iv_delete = (ImageView) view.findViewById(R.id.iscl_iv_delete);
        checkBoxes.add(iscl_checkbox);
        editTexts.add(iscl_edittext);
        iscl_checkbox.setChecked(ischeck);
        iscl_edittext.setText(title);
        iscl_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (TextUtils.equals("1", type) && isChecked) {//单选则更新其他为不选中
                    for (CheckBox checkBox : checkBoxes) {
                        if (checkBox != iscl_checkbox) {
                            checkBox.setChecked(false);
                        }
                    }
                }
            }
        });

        iscl_iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (acsl_ll_selection_layout.getChildCount() <= 2) {
                    toast("选项不能小于2个");
                } else {
                    MMAlert.showAlert(mContext, null, "确定删除?", getResources().getString(R.string.app_tip), null, null,
                            new MMAlert.OnDialogClick() {

                                @Override
                                public void onOkListener(String content) {
                                    acsl_ll_selection_layout.removeView(view);
                                    checkBoxes.remove(iscl_checkbox);
                                    editTexts.remove(iscl_edittext);
                                    //刷新最大最小选项
                                    String text = acsl_tv_select_least.getText().toString().trim();
                                    if (!TextUtils.equals("不限", text) && Integer.parseInt(text) > acsl_ll_selection_layout.getChildCount()) {
                                        acsl_tv_select_least.setText(acsl_ll_selection_layout.getChildCount());
                                    }
                                    text = acsl_tv_select_more.getText().toString().trim();
                                    if (!TextUtils.equals("不限", text) && Integer.parseInt(text) > acsl_ll_selection_layout.getChildCount()) {
                                        acsl_tv_select_more.setText(acsl_ll_selection_layout.getChildCount());
                                    }
                                }

                                @Override
                                public void onClickPreListener(EditText et) {
                                }

                                @Override
                                public void onCancelListener(EditText et) {
                                }
                            }, true);
                }
            }
        });
        acsl_ll_selection_layout.addView(view);
    }

    /**
     * 根据子选项多少获取弹框内容
     *
     * @return
     */
    private String[] getDatas() {
        String[] datas = new String[acsl_ll_selection_layout.getChildCount() + 1];
        datas[0] = "不限";
        for (int i = 1; i < datas.length; i++) {
            datas[i] = i + "";
        }
        return datas;
    }

    /**
     * 提示
     */
    private void showTipToast() {
        toast("必填项不能为空!");
    }

    /**
     * 选项切换
     */
    private void setSelectTv() {
        if (TextUtils.equals("1", type)) {
            setTitle("单选题");
            acsl_tv_more.setSelected(false);
            acsl_tv_single.setSelected(true);
            acsl_ll_least_layout.setVisibility(View.GONE);
            acsl_ll_more_layout.setVisibility(View.GONE);
        } else {
            setTitle("多选题");
            acsl_tv_single.setSelected(false);
            acsl_tv_more.setSelected(true);
            acsl_ll_least_layout.setVisibility(View.VISIBLE);
            acsl_ll_more_layout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 创建
     */
    private void submitAction() {
        QuestionItem questionItem = new QuestionItem();
        if (TextUtils.isEmpty(acsl_et_title.getText().toString())) {
            showTipToast();
            return;
        }
        questionItem.setTitle(acsl_et_title.getText().toString());

        List<SelectionItem> selectionItems = new ArrayList<>();
        for (int i = 0; i < editTexts.size(); i++) {
            EditText editText = editTexts.get(i);
            if (TextUtils.isEmpty(editText.getText().toString())) {
                showTipToast();
                return;
            } else {
                SelectionItem selectionItem = new SelectionItem();
                selectionItem.setTitle(editText.getText().toString());
                selectionItem.setIsSelect(checkBoxes.get(i).isChecked() ? "1" : "0");
                selectionItems.add(selectionItem);
            }
        }
        questionItem.setSelectionItemList(selectionItems);
        questionItem.setIsMust(acsl_cb_must.isChecked() ? "1" : "0");
        questionItem.setType(type);
        if (TextUtils.equals("2", type)) {
            questionItem.setLeast(acsl_tv_select_least.getText().toString());
            questionItem.setMore(acsl_tv_select_more.getText().toString());
        }
        Intent intent = new Intent();
        intent.putExtra("questionItem", questionItem);
        setResult(EditQuestionActivity.RESULT_CODE, intent);
        finish();
    }

}
