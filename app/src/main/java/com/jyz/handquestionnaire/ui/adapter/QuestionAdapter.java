package com.jyz.handquestionnaire.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyz.handquestionnaire.BaseActivity;
import com.jyz.handquestionnaire.R;
import com.jyz.handquestionnaire.bean.QuestionnaireItem;
import com.jyz.handquestionnaire.ui.activity.AnswerQuestionnaireActivity;
import com.jyz.handquestionnaire.ui.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

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
            Glide.with(mContext).load(questionnaireItem.getHeadUrl()).placeholder(R.drawable.img_user_default).into(holder.iql_civ_head);
        }
        holder.iql_tv_publisher.setText(questionnaireItem.getNickName());
        holder.iql_tv_title.setText(questionnaireItem.getTitle());
        holder.iql_tv_time.setText(questionnaireItem.getCreateTime());
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
                Bundle bundle = new Bundle();
                bundle.putSerializable("questionnaireItem", questionnaireItem);
                ((BaseActivity) mContext).jumpToNext(AnswerQuestionnaireActivity.class, bundle);
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
}
