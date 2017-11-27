package com.jyz.handquestionnaire.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.jyz.handquestionnaire.BaseActivity;
import com.jyz.handquestionnaire.ui.fragment.QuestionnaireFragment;

/**
 * Created by Songzhihang on 2017/11/27.
 */
public class QuestionnaireListActivity extends BaseActivity{
    private static final String TAG = "QuestionnaireListActivity";

    @Override
    protected void setView() {

    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void initData() {
        QuestionnaireFragment fragment=new QuestionnaireFragment();
        Bundle bundle=new Bundle();
        bundle.putString("type",getIntent().getStringExtra("type"));
        bundle.putString("userId",getIntent().getStringExtra("userId"));
        fragment.setArguments(bundle);
        addFragment(fragment);
    }

    @Override
    protected void setListener() {

    }

    private void addFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.add(android.R.id.content, fragment);
        transaction.commit();
    }
}
