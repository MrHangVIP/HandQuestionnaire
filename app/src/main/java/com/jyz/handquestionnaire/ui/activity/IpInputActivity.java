package com.jyz.handquestionnaire.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jyz.handquestionnaire.BaseActivity;
import com.jyz.handquestionnaire.R;
import com.jyz.handquestionnaire.util.Constant;

/**
 * Created by Songzhihang on 2018/4/24.
 */
public class IpInputActivity extends BaseActivity {
    private static final String TAG = "IpInputActivity";
    private EditText aiil_et_ip;
    private Button aiil_bt_next;


    @Override
    protected void setView() {
        setContentView(R.layout.activity_ip_input_layout);
    }

    @Override
    protected void findViews() {
        aiil_et_ip = (EditText) findViewById(R.id.aiil_et_ip);
        aiil_bt_next = (Button) findViewById(R.id.aiil_bt_next);
    }

    @Override
    protected void initData() {
        aiil_et_ip.setText(Constant.DEFAULT_IP);
    }

    @Override
    protected void setListener() {
        aiil_bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.DEFAULT_IP = aiil_et_ip.getText().toString();
                goToNext(WelComeActivity.class);
            }
        });
    }
}
