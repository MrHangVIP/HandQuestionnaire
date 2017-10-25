package com.jyz.handquestionnaire.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyz.handquestionnaire.BaseActivity;
import com.jyz.handquestionnaire.R;
import com.jyz.handquestionnaire.bean.QuestionItem;
import com.jyz.handquestionnaire.ui.widget.WheelViewDialog;
import com.jyz.handquestionnaire.util.WheelUtil;
import com.jyz.handquestionnaire.util.WheelViewDialogUtil;

import java.util.LinkedHashMap;

/**
 * @discription 创建填空题的页面
 * @autor songzhihang
 * @time 2017/10/25  下午7:03
 **/
public class CreateBlankActivity extends BaseActivity {
    private static final String TAG = "CreateBlankActivity";
    private TextView acbl_et_title;
    private TextView acbl_tv_lines;
    private CheckBox acbl_cb_must;
    private TextView acbl_tv_create;
    private WheelViewDialog selectionDialog;


    @Override
    protected void setView() {
        setContentView(R.layout.activity_create_blank_layout);
    }

    @Override
    protected void findViews() {
        setTitle("填空题");
        acbl_et_title = (TextView) findViewById(R.id.acbl_et_title);
        acbl_tv_lines = (TextView) findViewById(R.id.acbl_tv_lines);
        acbl_cb_must = (CheckBox) findViewById(R.id.acbl_cb_must);
        acbl_tv_create = (TextView) findViewById(R.id.acbl_tv_create);

    }

    @Override
    protected void initData() {
        if (getIntent() != null && getIntent().getBundleExtra("bundle") != null) {
            if (getIntent().getBundleExtra("bundle").getSerializable("questionItem") != null) {//修改
                setDateToView((QuestionItem) getIntent().getBundleExtra("bundle").getSerializable("questionItem"));
            }
        }
    }

    @Override
    protected void setListener() {

        acbl_tv_lines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹框提示
                if (selectionDialog == null) {
                    final WheelUtil typeWheelUtil = new WheelUtil(mContext);
                    typeWheelUtil.setDatas(new String[]{"1", "2", "3", "4", "5"});
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    LinkedHashMap<View, LinearLayout.LayoutParams> map = new LinkedHashMap<>();
                    map.put(typeWheelUtil.getWheelView(), params);
                    selectionDialog = WheelViewDialogUtil.showWheelViewDialog(CreateBlankActivity.this, "文本最大行数", new WheelViewDialog.DialogSubmitListener() {
                        @Override
                        public void onSubmitClick(View v) {
                            acbl_tv_lines.setText(typeWheelUtil.getItems()[typeWheelUtil.getWheelView().getCurrentItem()].toString());
                            selectionDialog.dismiss();
                        }
                    }, map);
                } else {
                    selectionDialog.show();
                }
            }
        });

        acbl_tv_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = acbl_et_title.getText().toString();
                if (TextUtils.isEmpty(title)) {
                    toast("问题不能为空!");
                    return;
                }
                QuestionItem item = new QuestionItem();
                item.setType("3");
                item.setTitle(title);
                item.setLines(acbl_tv_lines.getText().toString());
                item.setIsMust(acbl_cb_must.isChecked() ? "1" : "0");
                Intent intent = new Intent();
                intent.putExtra("questionItem", item);
                setResult(EditQuestionActivity.RESULT_CODE, intent);
                finish();
            }
        });
    }

    /**
     * 修改赋值
     */
    private void setDateToView(QuestionItem questionItem) {
        acbl_et_title.setText(questionItem.getTitle());
        acbl_cb_must.setChecked(TextUtils.equals("1", questionItem.getIsMust()));
        acbl_tv_lines.setText(questionItem.getLines());
    }
}
