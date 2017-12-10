package com.jyz.handquestionnaire.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jyz.handquestionnaire.BaseActivity;
import com.jyz.handquestionnaire.BaseApplication;
import com.jyz.handquestionnaire.R;
import com.jyz.handquestionnaire.api.OkHttpHelp;
import com.jyz.handquestionnaire.bean.AnswerItem;
import com.jyz.handquestionnaire.bean.QuestionnaireItem;
import com.jyz.handquestionnaire.bean.ResultItem;
import com.jyz.handquestionnaire.listener.ResponseListener;
import com.jyz.handquestionnaire.ui.activity.AnswerQuestionPreviewActivity;
import com.jyz.handquestionnaire.ui.activity.AnswerQuestionnaireActivity;
import com.jyz.handquestionnaire.ui.activity.MyPublishActivity;
import com.jyz.handquestionnaire.ui.widget.CircleImageView;
import com.jyz.handquestionnaire.ui.widget.MMAlert;
import com.jyz.handquestionnaire.util.Constant;
import com.jyz.handquestionnaire.util.ProgressDialogUtil;
import com.jyz.handquestionnaire.util.SpfUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @discription 问卷适配器
 * @autor songzhihang
 * @time 2017/10/13  下午3:22
 **/
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private static final String TAG = "QuestionAdapter";
    private Context mContext;

    private List<QuestionnaireItem> questionnaireItemList = new ArrayList<>();

    public QuestionAdapter(Context mContext, List<QuestionnaireItem> questionnaireItemList) {
        this.mContext = mContext;
        this.questionnaireItemList = questionnaireItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_layout, parent, false);
        CircleImageView iql_civ_head = (CircleImageView) view.findViewById(R.id.iql_civ_head);
        TextView iql_tv_title = (TextView) view.findViewById(R.id.iql_tv_title);
        TextView iql_tv_publisher = (TextView) view.findViewById(R.id.iql_tv_publisher);
        TextView iql_tv_time = (TextView) view.findViewById(R.id.iql_tv_time);
        TextView iql_tv_finish_time = (TextView) view.findViewById(R.id.iql_tv_finish_time);
        ViewHolder holder = new ViewHolder(view, iql_civ_head, iql_tv_title, iql_tv_publisher, iql_tv_time, iql_tv_finish_time);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final QuestionnaireItem questionnaireItem = questionnaireItemList.get(position);
        if (!TextUtils.isEmpty(questionnaireItem.getHeadUrl())) {
            Glide.with(mContext)
                    .load(Constant.DEFAULT_URL+Constant.IMAGE_URL+questionnaireItem.getHeadUrl())
                    .placeholder(R.drawable.img_user_default)
                    .into(holder.iql_civ_head);
        }
        holder.iql_tv_publisher.setText(questionnaireItem.getNickName());
        holder.iql_tv_title.setText(questionnaireItem.getTitle());
        holder.iql_tv_time.setText(questionnaireItem.getCreateTime());
        Log.e(TAG, "FinishTimeStmp: " + questionnaireItem.getFinishTimeStmp());
        Log.e(TAG, "currentTimeMillis: " + System.currentTimeMillis());
        if (questionnaireItem.getFinishTimeStmp() < System.currentTimeMillis()) {
            holder.iql_tv_finish_time.setText("已结束");
            holder.iql_tv_finish_time.setTextColor(Color.parseColor("#333333"));
        } else {
            holder.iql_tv_finish_time.setText("进行中");
            holder.iql_tv_finish_time.setTextColor(Color.parseColor("#DC3C38"));
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断是否登录，已经登陆则判断是否是自己问卷，是否已经回答过问卷
                if (SpfUtil.getBoolean(Constant.IS_LOGIN, false) && BaseApplication.getAPPInstance().getmUser() != null) {
                    int userId = BaseApplication.getAPPInstance().getmUser().getUserId();
                    if (userId == questionnaireItem.getUserId()) {//是否是自己发布的
                        MMAlert.showAlert(mContext, "此问卷是您发布的！是否查看统计结果？", "温馨提示", "查看", "返回", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //进入
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("questionnaireItem", questionnaireItem);
                                ((BaseActivity) mContext).jumpToNext(MyPublishActivity.class, bundle);
                            }
                        },null);
                        return;
                    }
                    //是否回答
                    getAnswerList(userId, questionnaireItem);
                    return;
                }
                goDetail(questionnaireItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionnaireItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView iql_tv_title;
        public final TextView iql_tv_publisher;
        public final TextView iql_tv_time;
        public final CircleImageView iql_civ_head;
        public final TextView iql_tv_finish_time;

        public ViewHolder(View view, CircleImageView iql_civ_head, TextView iql_tv_title, TextView iql_tv_publisher, TextView iql_tv_time, TextView iql_tv_finish_time) {
            super(view);
            mView = view;
            this.iql_civ_head = iql_civ_head;
            this.iql_tv_title = iql_tv_title;
            this.iql_tv_publisher = iql_tv_publisher;
            this.iql_tv_time = iql_tv_time;
            this.iql_tv_finish_time = iql_tv_finish_time;
        }
    }

    private void getAnswerList(int userId, final QuestionnaireItem questionnaireItem) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId + "");
        map.put("questionnaireId", questionnaireItem.getQuestionnaireId() + "");
        ProgressDialogUtil.showProgressDialog(mContext, true);
        OkHttpHelp<ResultItem> okHttpHelp = OkHttpHelp.getInstance();
        okHttpHelp.httpRequest("", Constant.GET_ANSWER, map, new ResponseListener<ResultItem>() {
            @Override
            public void onSuccess(ResultItem object) {
                ProgressDialogUtil.dismissProgressdialog();
                JSONArray jsonArray = null;
                final ArrayList<AnswerItem> answerItems = new ArrayList<AnswerItem>();
                try {
                    jsonArray = new JSONArray(object.getData());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        AnswerItem answerItem = new Gson().fromJson(jsonArray.get(i).toString(), AnswerItem.class);
                        answerItems.add(answerItem);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (answerItems.size() > 0) {
                        MMAlert.showAlert(mContext, "您已经回答过此问卷啦！是否查看结果？", "温馨提示", "查看", "返回", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //进入
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("questionnaireItem", questionnaireItem);
                                bundle.putSerializable("answerList", answerItems);
                                ((BaseActivity) mContext).jumpToNext(AnswerQuestionPreviewActivity.class, bundle);
                            }
                        },null);
                        return;
                    } else {
                        goDetail(questionnaireItem);
                    }
                }
            }
            @Override
            public void onFailed(String message) {
                ProgressDialogUtil.dismissProgressdialog();
                goDetail(questionnaireItem);
            }

            @Override
            public Class<ResultItem> getEntityClass() {
                return ResultItem.class;
            }
        });
    }

    private void goDetail(QuestionnaireItem item) {
        if (item.getFinishTimeStmp() < System.currentTimeMillis()) {
            Toast.makeText(mContext,"此问卷已结束！",Toast.LENGTH_SHORT).show();
        }else{
            Bundle bundle = new Bundle();
            bundle.putSerializable("questionnaireItem", item);
            ((BaseActivity) mContext).jumpToNext(AnswerQuestionnaireActivity.class, bundle);
        }
    }
}
