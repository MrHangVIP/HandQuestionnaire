package com.jyz.handquestionnaire.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.jyz.handquestionnaire.BaseActivity;
import com.jyz.handquestionnaire.BaseApplication;
import com.jyz.handquestionnaire.R;
import com.jyz.handquestionnaire.api.OkHttpHelp;
import com.jyz.handquestionnaire.bean.ResultItem;
import com.jyz.handquestionnaire.bean.ScoreRecordItem;
import com.jyz.handquestionnaire.listener.ResponseListener;
import com.jyz.handquestionnaire.ui.adapter.ScoreRecordAdapter;
import com.jyz.handquestionnaire.util.Constant;
import com.jyz.handquestionnaire.util.ProgressDialogUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @discription 积分记录列表页
 * @autor songzhihang
 * @time 2017/11/28  下午4:54
 **/
public class ScoreRecordActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "ScoreRecordActivity";
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private List<ScoreRecordItem> scoreRecordList = new ArrayList<>();

    @Override
    protected void setView() {
        setContentView(R.layout.app_main_layout);
    }

    @Override
    protected void findViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
    }

    @Override
    protected void initData() {
        setTitle("积分记录");
        onRefresh();
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onRefresh() {
        Map<String, String> map = new HashMap<>();
        String userId = BaseApplication.getAPPInstance().getmUser().getUserId() + "";
        if (!TextUtils.isEmpty(userId)) {
            map.put("userId", userId);
        }
        ProgressDialogUtil.showProgressDialog(mContext, true);
        OkHttpHelp<ResultItem> okHttpHelp = OkHttpHelp.getInstance();
        okHttpHelp.httpRequest("", Constant.GET_SCORE_RECORD, map, new ResponseListener<ResultItem>() {
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
                scoreRecordList.clear();
                try {
                    jsonArray = new JSONArray(object.getData());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ScoreRecordItem scoreRecordItem = new Gson().fromJson(jsonArray.get(i).toString(), ScoreRecordItem.class);
                        scoreRecordList.add(scoreRecordItem);
                    }
                    mRecyclerView.setAdapter(new ScoreRecordAdapter(mContext, scoreRecordList));
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

    private void getData() {

    }
}
