package com.jyz.handquestionnaire.ui.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jyz.handquestionnaire.BaseActivity;
import com.jyz.handquestionnaire.BaseFragment;
import com.jyz.handquestionnaire.R;
import com.jyz.handquestionnaire.ui.adapter.QuestionAdapter;

import java.util.HashMap;
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

    //    private List<ArticleItem> articleList = new ArrayList<>();
    private String type;//问卷类型 1最热,2最新,3所有
    private static final int refresh = 0x100;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case refresh:
                    swipeRefreshLayout.setRefreshing(false);
                    if (getActivity() != null) {
                        ((BaseActivity) getActivity()).toast("刷新成功！");
                    }
                    break;
                default:
            }
        }
    };

    @Override
    protected View getLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_question_layout, container, false);
    }

    @Override
    protected void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fql_rv_recycleview);
        mRecyclerView.setAdapter(new QuestionAdapter(getActivity()));
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
        getData();
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
        handler.sendEmptyMessageDelayed(refresh, 2000);
    }

    @Override
    public void onResume() {
        super.onResume();
        mRecyclerView.setAdapter(new QuestionAdapter(getActivity()));
    }

    private void getData() {
//        articleList.clear();
//        ProgressDialogUtil.showProgressDialog(getActivity(), true);
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
//        OkHttpHelp<ResultItem> httpHelp = OkHttpHelp.getInstance();
//        httpHelp.httpRequest("", Constant.GET_TYPE_ARTICLE, map, new ResponseListener<ResultItem>() {
//            @Override
//            public void onSuccess(ResultItem object) {
//                ProgressDialogUtil.dismissProgressdialog();
//                if (object.getResult().equals("success")) {
//                    try {
//                        JSONArray jsonArray = new JSONArray(object.getData());
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            ArticleItem articleItem = new Gson().fromJson(jsonArray.get(i).toString(), ArticleItem.class);
//                            articleList.add(articleItem);
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        MyUtil.MyLogE(TAG, e.toString());
//                    }
//                    mRecyclerView.setAdapter(new HomeArticleAdapter(getActivity(), articleList, ""));
//                    if (getActivity() != null && swipeRefreshLayout.isRefreshing()) {
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailed(String message) {
//                ProgressDialogUtil.dismissProgressdialog();
//            }
//
//            @Override
//            public Class<ResultItem> getEntityClass() {
//                return ResultItem.class;
//            }
//        });
    }
}
