package com.jyz.handquestionnaire.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jyz.handquestionnaire.BaseActivity;
import com.jyz.handquestionnaire.BaseApplication;
import com.jyz.handquestionnaire.BaseFragment;
import com.jyz.handquestionnaire.R;
import com.jyz.handquestionnaire.api.OkHttpHelp;
import com.jyz.handquestionnaire.bean.ResultItem;
import com.jyz.handquestionnaire.bean.UserItem;
import com.jyz.handquestionnaire.listener.ResponseListener;
import com.jyz.handquestionnaire.ui.activity.ForgetPassActivity;
import com.jyz.handquestionnaire.util.Constant;
import com.jyz.handquestionnaire.util.ProgressDialogUtil;
import com.jyz.handquestionnaire.util.SpfUtil;
import com.jyz.handquestionnaire.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @discription 登录fragment
 * @autor songzhihang
 * @time 2017/10/13  下午5:27
 **/
public class LoginFragment extends BaseFragment {

    private static final String TAG = "LoginFragment";
    private View view;
    private EditText phoneNumberET;
    private EditText passwordET;
    private TextView frogetPassTV;
    private Button loginBT;


    @Override
    protected View getLayout(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_login_layout, container, false);
        return view;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        phoneNumberET = (EditText) view.findViewById(R.id.et_username);
        passwordET = (EditText) view.findViewById(R.id.et_password);
        frogetPassTV = (TextView) view.findViewById(R.id.tv_forget_pass);
        loginBT = (Button) view.findViewById(R.id.bt_login);
    }

    @Override
    protected void initEvent() {
        frogetPassTV.setOnClickListener(this);
        loginBT.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bt_login:
                String username = phoneNumberET.getText().toString();
                String userpass = passwordET.getText().toString();
                if (!StringUtils.isMobile(username)) {
                    toast("请输入正确的手机号");
                    loginFail();
                    break;
                }

                if (userpass.length() < 8) {
                    toast("密码长度不能低于8位");
                    loginFail();
                    break;
                }
                ProgressDialogUtil.showProgressDialog(getActivity(), true);
                Map<String, String> params = new HashMap<>();
                params.put("userPhone", username);
                params.put("userPass", userpass);
                params.put("MAC",Constant.MacAddress);
                OkHttpHelp<ResultItem> httpHelp = OkHttpHelp.getInstance();
                httpHelp.httpRequest("post", Constant.LOGIN_URL, params, new ResponseListener<ResultItem>() {
                    @Override
                    public void onSuccess(ResultItem object) {
                        ProgressDialogUtil.dismissProgressdialog();
                        if (!object.getResult().equals("fail")) {
                            toast("登录成功！");
                            SpfUtil.saveString(Constant.TOKEN,object.getResult());
                            JSONObject userJson = null;
                            try {
                                userJson = new JSONObject(object.getData());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            UserItem userItem = (new Gson()).fromJson(userJson.toString(), UserItem.class);
                            BaseApplication.getAPPInstance().setmUser(userItem);
                            SpfUtil.saveBoolean(Constant.IS_LOGIN, true);
                            SpfUtil.saveString(Constant.LOGIN_USERPHONE,userItem.getUserPhone());
                            getActivity().finish();
                        } else {
                            toast("用户名或密码错误");
                            loginFail();
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

                break;


            case R.id.tv_forget_pass:
                jumpToNext(ForgetPassActivity.class);
                getActivity().finish();

            default:
                break;
        }
    }

    private void loginFail() {
        phoneNumberET.setText("");
        phoneNumberET.requestFocus();
        passwordET.setText("");
    }
}
