package com.jyz.handquestionnaire.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jyz.handquestionnaire.BaseActivity;
import com.jyz.handquestionnaire.R;

/**
 * @discription 修改密码activity
 * @autor   songzhihang
 * @time   2017/10/13  下午5:53
 **/
public class UpdatePassActivity extends BaseActivity {


    private EditText newPassET ,newPassAgainET;
    private Button saveBT;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_update_pass);
    }


    @Override
    protected void findViews() {
        setTitle("忘记密码");
        newPassET=(EditText)findViewById(R.id.et_new_pass);
        newPassAgainET=(EditText)findViewById(R.id.et_new_pass_again);
        saveBT=(Button)findViewById(R.id.bt_save);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        saveBT.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_save:
                jumpToNext(LoginActivity.class);
                finish();
                break;
        }
    }


}

