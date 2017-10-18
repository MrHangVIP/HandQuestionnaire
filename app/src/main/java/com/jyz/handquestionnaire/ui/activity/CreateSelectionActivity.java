package com.jyz.handquestionnaire.ui.activity;

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

import java.util.HashMap;

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
    private TextView acsl_tv_selection_add;
    private LinearLayout acsl_ll_least_layout;
    private TextView acsl_tv_select_least;
    private LinearLayout acsl_ll_more_layout;
    private TextView acsl_tv_select_more;
    private TextView acsl_cb_must;
    private TextView acsl_tv_create;

    private String type;//类型 1单选,2多选
    private HashMap<Integer,CheckBox> checkMaps=new HashMap<>();

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
        acsl_tv_selection_add=(TextView)findViewById(R.id.acsl_tv_selection_add);
        acsl_ll_least_layout=(LinearLayout)findViewById(R.id.acsl_ll_least_layout);
        acsl_tv_select_least=(TextView)findViewById(R.id.acsl_tv_select_least);
        acsl_ll_more_layout=(LinearLayout)findViewById(R.id.acsl_ll_more_layout);
        acsl_tv_select_more=(TextView)findViewById(R.id.acsl_tv_select_more);
        acsl_cb_must=(CheckBox)findViewById(R.id.acsl_cb_must);
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

        acsl_tv_single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type="1";
                acsl_ll_least_layout.setVisibility(View.GONE);
                acsl_ll_more_layout.setVisibility(View.GONE);
            }
        });

        acsl_tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type="2";
                acsl_ll_least_layout.setVisibility(View.VISIBLE);
                acsl_ll_more_layout.setVisibility(View.VISIBLE);
            }
        });

        acsl_tv_selection_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acsl_ll_selection_layout
            }
        });
    }

    /**
     * 添加选项
     */
    private void addSelectionView(){
        final View view= LayoutInflater.from(mContext).inflate(R.layout.item_selection_create_layout,null);
        CheckBox iscl_checkbox=(CheckBox)view.findViewById(R.id.iscl_checkbox);
        EditText iscl_edittext=(EditText)view.findViewById(R.id.iscl_edittext);
        ImageView iscl_iv_delete=(ImageView)view.findViewById(R.id.iscl_iv_delete);
        checkMaps.put(acsl_ll_selection_layout.getChildCount(),iscl_checkbox);
        is
        iscl_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(TextUtils.equals("1",type)){//单选则更新其他为不选中

                }
                if(isChecked){
                }else {

                }
            }
        });

        iscl_iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(acsl_ll_selection_layout.getChildCount()<=2){
                    toast("选项不能小于2个");
                }else{
                    acsl_ll_selection_layout.removeView(view);
                    checkMaps.remove()
                }
            }
        });
        acsl_ll_selection_layout.addView(view);
    }

}
