package com.jyz.handquestionnaire.ui.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.jyz.handquestionnaire.BaseActivity;
import com.jyz.handquestionnaire.BaseFragment;
import com.jyz.handquestionnaire.R;
import com.jyz.handquestionnaire.api.OkHttpHelp;
import com.jyz.handquestionnaire.bean.QuestionnaireItem;
import com.jyz.handquestionnaire.bean.ResultItem;
import com.jyz.handquestionnaire.listener.ResponseListener;
import com.jyz.handquestionnaire.ui.adapter.QuestionAdapter;
import com.jyz.handquestionnaire.util.Constant;
import com.jyz.handquestionnaire.util.ProgressDialogUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @discription 问卷列表Fragment
 * @autor songzhihang
 * @time 2017/10/13  下午2:25
 **/
public class QuestionnaireFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "QuestionnaireFragment";

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private List<QuestionnaireItem> questionnaireItemList = new ArrayList<>();
    private String type;//问卷类型 1最热,2最新,3所有
    private String userId;

    @Override
    protected View getLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_question_layout, container, false);
    }

    @Override
    protected void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fql_rv_recycleview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fql_sr_swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
    }

    @Override
    protected void initData() {
        type = getArguments().getString("type");
        userId=getArguments().getString("userId");
        if(toolbar!=null){
            toolbar.setVisibility(View.GONE);
        }
        if(TextUtils.equals("publish",type)){
            setTitle("我发布的问卷");
            toolbar.setVisibility(View.VISIBLE);
        }
        if(TextUtils.equals("answer",type)){
            setTitle("我回答的问卷");
            toolbar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh() {
        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        if(!TextUtils.isEmpty(userId)){
            map.put("userId", userId);
        }
        ProgressDialogUtil.showProgressDialog(getActivity(), true);
        OkHttpHelp<ResultItem> okHttpHelp = OkHttpHelp.getInstance();
        okHttpHelp.httpRequest("", Constant.GET_QUESTIONNAIRELIST, map, new ResponseListener<ResultItem>() {
            @Override
            public void onSuccess(ResultItem object) {
                swipeRefreshLayout.setRefreshing(false);
                ProgressDialogUtil.dismissProgressdialog();
                if ("fail".equals(object.getResult())) {
                    toast("网络错误，请重试！");
                    return;
                }
//                handler.sendEmptyMessage(refresh);
                JSONArray jsonArray = null;
                questionnaireItemList.clear();
                try {
                    jsonArray = new JSONArray(object.getData());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        QuestionnaireItem postBarItem = new Gson().fromJson(jsonArray.get(i).toString(), QuestionnaireItem.class);
                        questionnaireItemList.add(postBarItem);
                    }
                    mRecyclerView.setAdapter(new QuestionAdapter(getActivity(), questionnaireItemList));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String message) {
                ProgressDialogUtil.dismissProgressdialog();
                swipeRefreshLayout.setRefreshing(false);
                toast("网络错误，请重试！");
            }

            @Override
            public Class<ResultItem> getEntityClass() {
                return ResultItem.class;
            }
        });
    }
}
