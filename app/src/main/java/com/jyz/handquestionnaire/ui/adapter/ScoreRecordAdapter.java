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
import com.jyz.handquestionnaire.bean.ScoreRecordItem;
import com.jyz.handquestionnaire.ui.activity.AnswerQuestionnaireActivity;
import com.jyz.handquestionnaire.ui.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @discription 积分记录适配器
 * @autor songzhihang
 * @time 2017/11/28  下午4:55
 **/
public class ScoreRecordAdapter extends RecyclerView.Adapter<ScoreRecordAdapter.ViewHolder> {

    private static final String TAG = "ScoreRecordAdapter";
    private Context mContext;

    private List<ScoreRecordItem> scoreRecordList = new ArrayList<>();

    public ScoreRecordAdapter(Context mContext, List<ScoreRecordItem> scoreRecordList) {
        this.mContext = mContext;
        this.scoreRecordList = scoreRecordList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_socre_record_layout, parent, false);
        TextView isrl_tv_time = (TextView) view.findViewById(R.id.isrl_tv_time);
        TextView isrl_tv_content = (TextView) view.findViewById(R.id.isrl_tv_content);
        TextView isrl_tv_record = (TextView) view.findViewById(R.id.isrl_tv_record);
        ViewHolder holder = new ViewHolder(view, isrl_tv_time, isrl_tv_content, isrl_tv_record);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ScoreRecordItem item = scoreRecordList.get(position);
        holder.isrl_tv_time.setText(item.getCreatetime());
        holder.isrl_tv_content.setText(item.getAction());
        if (item.getScore_change()>0){
            holder.isrl_tv_record.setText("+"+item.getScore_change());
        }else{
            holder.isrl_tv_record.setText(item.getScore_change()+"");
        }
    }

    @Override
    public int getItemCount() {
        return scoreRecordList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView isrl_tv_time;
        public final TextView isrl_tv_content;
        public final TextView isrl_tv_record;

        public ViewHolder(View view , TextView isrl_tv_time, TextView isrl_tv_content,TextView isrl_tv_record) {
            super(view);
            mView = view;
            this.isrl_tv_time = isrl_tv_time;
            this.isrl_tv_content = isrl_tv_content;
            this.isrl_tv_record = isrl_tv_record;
        }
    }
}
