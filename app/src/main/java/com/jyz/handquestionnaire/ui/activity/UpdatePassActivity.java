package com.jyz.handquestionnaire.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jyz.handquestionnaire.BaseActivity;
import com.jyz.handquestionnaire.BaseApplication;
import com.jyz.handquestionnaire.R;
import com.jyz.handquestionnaire.api.OkHttpHelp;
import com.jyz.handquestionnaire.bean.ResultItem;
import com.jyz.handquestionnaire.bean.UserItem;
import com.jyz.handquestionnaire.listener.ResponseListener;
import com.jyz.handquestionnaire.util.Constant;
import com.jyz.handquestionnaire.util.ProgressDialogUtil;
import com.jyz.handquestionnaire.util.SpfUtil;
import com.jyz.handquestionnaire.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class UpdatePassActivity extends BaseActivity {


    private EditText newPassET, newPassAgainET;
    private Button saveBT;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_update_pass);
    }


    @Override
    protected void findViews() {
        setTitle("忘记密码");
        newPassET = (EditText) findViewById(R.id.et_new_pass);
        newPassAgainET = (EditText) findViewById(R.id.et_new_pass_again);
        saveBT = (Button) findViewById(R.id.bt_save);
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
        switch (v.getId()) {
            case R.id.bt_save:
                updatePassWord();
                break;
        }
    }


    private void updatePassWord() {
        String newPass = newPassET.getText().toString();
        String newPassAgain = newPassAgainET.getText().toString();
        if (newPass.length() < 8 || newPassAgain.length() < 8) {
            toast("密码长度不能低于8位！");
            return;
        }
        if (!TextUtils.equals(newPass,newPassAgain)) {
            toast("2次密码不一致！");
            return;
        }
        if(TextUtils.equals(newPass,BaseApplication.getAPPInstance().getmUser().getUserPass())){
            toast("密码与原密码相同，无需修改！");
            return;
        }
        ProgressDialogUtil.showProgressDialog(this, true);
        Map<String, String> params = new HashMap<>();
        params.put("userPhone", BaseApplication.getAPPInstance().getmUser().getUserPhone());
        params.put("password", newPass);
        params.put("token", SpfUtil.getString(Constant.TOKEN, ""));
        OkHttpHelp<ResultItem> okHttpHelp = OkHttpHelp.getInstance();
        okHttpHelp.httpRequest("", Constant.UPDATE_PASS_WORD, params, new ResponseListener<ResultItem>() {
            @Override
            public void onSuccess(ResultItem object) {
                ProgressDialogUtil.dismissProgressdialog();
                if (object.getResult().equals("fail")) {
                    if (object.getData().equals("token error")) {
                        toast("token失效,请重新登录");
                        tokenError();
                    } else {
                        toast("更新失败");
                    }
                } else {
                    toast("修改成功,请重新登陆");
                    //重新登陆
                    tokenError();
                }
            }

            @Override
            public void onFailed(String message) {
                ProgressDialogUtil.dismissProgressdialog();
                toast("网络错误");
            }

            @Override
            public Class<ResultItem> getEntityClass() {
                return ResultItem.class;
            }
        });

    }

}

