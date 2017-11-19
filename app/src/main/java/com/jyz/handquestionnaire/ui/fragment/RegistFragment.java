package com.jyz.handquestionnaire.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jyz.handquestionnaire.BaseFragment;
import com.jyz.handquestionnaire.R;
import com.jyz.handquestionnaire.api.OkHttpHelp;
import com.jyz.handquestionnaire.bean.ResultItem;
import com.jyz.handquestionnaire.listener.ResponseListener;
import com.jyz.handquestionnaire.ui.activity.LoginActivity;
import com.jyz.handquestionnaire.util.Constant;
import com.jyz.handquestionnaire.util.ProgressDialogUtil;
import com.jyz.handquestionnaire.util.SpfUtil;
import com.jyz.handquestionnaire.util.StringUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * @discription 注册 fragment
 * @autor songzhihang
 * @time 2017/10/13  下午5:26
 **/
public class RegistFragment extends BaseFragment {
    private static final String TAG = "RegistFragment";
    private View view;
    private EditText phoneNumberET;
    private EditText passwordET;
    private EditText checkCodeET;
    private Button registBT;

    @Override
    protected View getLayout(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_regist_layout, container, false);
        return view;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        phoneNumberET = (EditText) view.findViewById(R.id.et_username);
        checkCodeET = (EditText) view.findViewById(R.id.et_check_code);
        passwordET = (EditText) view.findViewById(R.id.et_password);
        registBT = (Button) view.findViewById(R.id.bt_regist);
    }

    @Override
    protected void initEvent() {
        registBT.setOnClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bt_regist:
                String username = phoneNumberET.getText().toString();
                String userpass = passwordET.getText().toString();
                if (!StringUtils.isMobile(username)) {
                    toast("请输入正确的手机号");
                    registFail();
                    break;
                }
                if (userpass.length() < 8) {
                    toast("密码长度不能地域8位");
                    registFail();
                    break;
                }
                ProgressDialogUtil.showProgressDialog(getActivity(),true);
                Map<String,String> params=new HashMap<>();
                params.put("userPhone",username);
                params.put("userPass",userpass);
                OkHttpHelp<ResultItem> httpHelp= OkHttpHelp.getInstance();
                httpHelp.httpRequest("post", Constant.REGIST_URL, params, new ResponseListener<ResultItem>() {
                    @Override
                    public void onSuccess(ResultItem object) {
                        ProgressDialogUtil.dismissProgressdialog();
                        if(object.getResult().equals("success")){
                            toast("注册成功,请登录！");
                            ((LoginActivity)getActivity()).tabFragment();
                        }else{
                            if(object.getData()!=null && object.getData().equals("exist")){
                                toast("该手机已经注册请登录");
                            }else{
                                toast("注册失败");
                            }
                        }
                        registFail();
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

                break;

            case R.id.bt_send:
            default:
                break;

        }
    }

    private void registFail() {
        phoneNumberET.setText("");
        phoneNumberET.requestFocus();
        passwordET.setText("");
        checkCodeET.setText("");
    }

}
