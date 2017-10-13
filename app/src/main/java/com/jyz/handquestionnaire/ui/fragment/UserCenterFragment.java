package com.jyz.handquestionnaire.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jyz.handquestionnaire.BaseFragment;
import com.jyz.handquestionnaire.R;
import com.jyz.handquestionnaire.ui.activity.LoginActivity;
import com.jyz.handquestionnaire.ui.widget.CircleImageView;

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

    @Override
    protected View getLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_usercenter_layout, container, false);
    }

    @Override
    protected void initView(View view) {
        ful_iv_head = (CircleImageView) contentView.findViewById(R.id.ful_iv_head);
        ful_tv_login_and_regist = (TextView) contentView.findViewById(R.id.ful_tv_login_and_regist);
        ful_tv_nickname = (TextView) contentView.findViewById(R.id.ful_tv_nickname);
        ful_tv_publish = (TextView) contentView.findViewById(R.id.ful_tv_publish);
        ful_tv_write = (TextView) contentView.findViewById(R.id.ful_tv_write);
        ful_tv_setting = (TextView) contentView.findViewById(R.id.ful_tv_setting);
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
    }

    @Override
    public void onClick(View view) {

    }
}
