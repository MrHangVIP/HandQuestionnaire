package com.jyz.handquestionnaire.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jyz.handquestionnaire.BaseApplication;
import com.jyz.handquestionnaire.BaseFragment;
import com.jyz.handquestionnaire.R;
import com.jyz.handquestionnaire.api.OkHttpHelp;
import com.jyz.handquestionnaire.bean.QuestionnaireItem;
import com.jyz.handquestionnaire.bean.ResultItem;
import com.jyz.handquestionnaire.bean.UserItem;
import com.jyz.handquestionnaire.listener.ResponseListener;
import com.jyz.handquestionnaire.ui.activity.LoginActivity;
import com.jyz.handquestionnaire.ui.activity.QuestionnaireListActivity;
import com.jyz.handquestionnaire.ui.adapter.QuestionAdapter;
import com.jyz.handquestionnaire.ui.widget.CircleImageView;
import com.jyz.handquestionnaire.util.Constant;
import com.jyz.handquestionnaire.util.ProgressDialogUtil;
import com.jyz.handquestionnaire.util.SpfUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Songzhihang on 2017/10/6.
 * 用户中心fragment
 */
public class UserCenterFragment extends BaseFragment {
    private static final String TAG = "UserCenterFragment";

    private CircleImageView ful_iv_head;
    private TextView ful_tv_login_and_regist;
    private TextView ful_tv_nickname;
    private TextView ful_tv_publish;
    private TextView ful_tv_write;
    private TextView ful_tv_setting;
    private LinearLayout asl_ll_replaceuser;
    private TextView ful_tv_score;

    @Override
    protected View getLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_usercenter_layout, container, false);
    }

    @Override
    protected void initView(View view) {
        ful_iv_head = (CircleImageView) contentView.findViewById(R.id.ful_iv_head);
        ful_tv_login_and_regist = (TextView) contentView.findViewById(R.id.ful_tv_login_and_regist);
        ful_tv_nickname = (TextView) contentView.findViewById(R.id.ful_tv_nickname);
        ful_tv_score = (TextView) contentView.findViewById(R.id.ful_tv_score);
        ful_tv_publish = (TextView) contentView.findViewById(R.id.ful_tv_publish);
        ful_tv_write = (TextView) contentView.findViewById(R.id.ful_tv_write);
        asl_ll_replaceuser = (LinearLayout) contentView.findViewById(R.id.asl_ll_replaceuser);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        ful_tv_login_and_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToNext(LoginActivity.class);
            }
        });
        asl_ll_replaceuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpfUtil.clearAll();
                BaseApplication.getAPPInstance().setmUser(null);
                jumpToNext(LoginActivity.class);
            }
        });

        ful_tv_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SpfUtil.getBoolean(Constant.IS_LOGIN, false) || BaseApplication.getAPPInstance().getmUser() == null) {
                    toast("请先登陆！");
                    return;
                }
                Intent intent = new Intent(context, QuestionnaireListActivity.class);
                intent.putExtra("type", "publish");
                intent.putExtra("userId", BaseApplication.getAPPInstance().getmUser().getUserId() + "");
                getActivity().startActivity(intent);
            }
        });

        ful_tv_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SpfUtil.getBoolean(Constant.IS_LOGIN, false) || BaseApplication.getAPPInstance().getmUser() == null) {
                    toast("请先登陆！");
                    return;
                }
                Intent intent = new Intent(context, QuestionnaireListActivity.class);
                intent.putExtra("type", "answer");
                intent.putExtra("userId", BaseApplication.getAPPInstance().getmUser().getUserId() + "");
                getActivity().startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (SpfUtil.getBoolean(Constant.IS_LOGIN, false)) {//已登陆获取用户信息
            setSelfData();
        } else {
            unLogin();
        }
    }

    private void unLogin() {
        ful_iv_head.setImageResource(R.drawable.img_loading_2);
        ful_tv_nickname.setVisibility(View.GONE);
        ful_tv_score.setVisibility(View.GONE);
        ful_tv_login_and_regist.setVisibility(View.VISIBLE);
        publishStr("0");
        answerNum("0");
    }

    private void updateData(UserItem user) {
        ful_tv_nickname.setText(user.getNickName());
        if (!TextUtils.isEmpty(user.getHeadUrl())) {
            Glide.with(context)
                    .load(Constant.DEFAULT_URL + Constant.IMAGE_URL + user.getHeadUrl())
                    .placeholder(R.drawable.img_loading_2)
                    .into(ful_iv_head);
        }
        ful_tv_nickname.setVisibility(View.VISIBLE);
        ful_tv_score.setVisibility(View.VISIBLE);
        ful_tv_score.setText(user.getScore() + "");
        ful_tv_login_and_regist.setVisibility(View.GONE);
        getMyQuestionnaireNum();
        getMyAnswerNum();
    }

    private void setSelfData() {
        ProgressDialogUtil.showProgressDialog(context, true);
        if (BaseApplication.getAPPInstance().getmUser() != null) {
            UserItem mUser = BaseApplication.getAPPInstance().getmUser();
            updateData(mUser);
            ProgressDialogUtil.dismissProgressdialog();
        }
        getUserData();
    }

    private void getUserData() {
        Map<String, String> params = new HashMap<>();
        params.put("token", SpfUtil.getString(Constant.TOKEN, ""));
        params.put("userPhone", SpfUtil.getString(Constant.LOGIN_USERPHONE, ""));
        OkHttpHelp<ResultItem> httpHelp = OkHttpHelp.getInstance();
        httpHelp.httpRequest("", Constant.GET_USER_URL, params, new ResponseListener<ResultItem>() {
                    @Override
                    public void onSuccess(ResultItem object) {
                        if ("fail".equals(object.getResult())) {
                            if ("token error".equals(object.getData())) {
                                toast("token失效,请重新登录");
                                tokenError();
                            }
                        } else {
                            JSONObject userJson = null;
                            try {
                                userJson = new JSONObject(object.getData());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            UserItem userItem = (new Gson()).fromJson(userJson.toString(), UserItem.class);
                            UserItem mUser = userItem;
                            updateData(mUser);
                            ProgressDialogUtil.dismissProgressdialog();
                            BaseApplication.getAPPInstance().setmUser(userItem);
                        }
                    }

                    @Override
                    public void onFailed(String message) {
                        ProgressDialogUtil.dismissProgressdialog();
                    }

                    @Override
                    public Class getEntityClass() {
                        return ResultItem.class;
                    }
                }

        );
    }

    /**
     * 获取我的问卷数量
     */
    private void getMyQuestionnaireNum() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", BaseApplication.getAPPInstance().getmUser().getUserId() + "");
        map.put("type", "publish");
        ProgressDialogUtil.showProgressDialog(getActivity(), true);
        OkHttpHelp<ResultItem> okHttpHelp = OkHttpHelp.getInstance();
        okHttpHelp.httpRequest("", Constant.GET_QUESTIONNAIRELIST, map, new ResponseListener<ResultItem>() {
            @Override
            public void onSuccess(ResultItem object) {
                ProgressDialogUtil.dismissProgressdialog();
                if ("fail".equals(object.getResult())) {
                    publishStr(0 + "");
                    return;
                }
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(object.getData());
                    if (jsonArray != null) {
                        publishStr(jsonArray.length() + "");
                    } else {
                        publishStr(0 + "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String message) {
                publishStr(0 + "");
            }

            @Override
            public Class<ResultItem> getEntityClass() {
                return ResultItem.class;
            }
        });
    }

    /**
     * 获取我回答的问卷数量
     */
    private void getMyAnswerNum() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", BaseApplication.getAPPInstance().getmUser().getUserId() + "");
        map.put("type", "answer");
        ProgressDialogUtil.showProgressDialog(getActivity(), true);
        OkHttpHelp<ResultItem> okHttpHelp = OkHttpHelp.getInstance();
        okHttpHelp.httpRequest("", Constant.GET_QUESTIONNAIRELIST, map, new ResponseListener<ResultItem>() {
            @Override
            public void onSuccess(ResultItem object) {
                ProgressDialogUtil.dismissProgressdialog();
                if ("fail".equals(object.getResult())) {
                    answerNum(0 + "");
                    return;
                }
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(object.getData());
                    if (jsonArray != null) {
                        answerNum(jsonArray.length() + "");
                    } else {
                        answerNum(0 + "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String message) {
                answerNum(0 + "");
            }

            @Override
            public Class<ResultItem> getEntityClass() {
                return ResultItem.class;
            }
        });
    }

    private void publishStr(String num) {
        ful_tv_publish.setText("我发布的\n" + num);
    }

    private void answerNum(String num) {
        ful_tv_write.setText("我回答的\n" + num);
    }
}
