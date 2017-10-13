package com.jyz.handquestionnaire.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jyz.handquestionnaire.R;

/**
 * @discription 问卷适配器
 * @autor songzhihang
 * @time 2017/10/13  下午3:22
 **/
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private static final String TAG = "QuestionAdapter";
    private Context mContext;

    public QuestionAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_layout, parent, false);
        TextView iql_tv_title = (TextView) view.findViewById(R.id.iql_tv_title);
        TextView iql_tv_publisher = (TextView) view.findViewById(R.id.iql_tv_publisher);
        TextView iql_tv_time = (TextView) view.findViewById(R.id.iql_tv_time);
        ViewHolder holder = new ViewHolder(view, iql_tv_title, iql_tv_publisher, iql_tv_time);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView iql_tv_title;
        public final TextView iql_tv_publisher;
        public final TextView iql_tv_time;


        public ViewHolder(View view, TextView iql_tv_title, TextView iql_tv_publisher, TextView iql_tv_time) {
            super(view);
            mView = view;
            this.iql_tv_title = iql_tv_title;
            this.iql_tv_publisher = iql_tv_publisher;
            this.iql_tv_time = iql_tv_time;
        }
    }
}
