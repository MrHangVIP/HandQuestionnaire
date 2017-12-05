package com.jyz.handquestionnaire.ui.activity;

import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.jyz.handquestionnaire.BaseActivity;
import com.jyz.handquestionnaire.R;
import com.jyz.handquestionnaire.api.OkHttpHelp;
import com.jyz.handquestionnaire.bean.AnswerItem;
import com.jyz.handquestionnaire.bean.QuestionItem;
import com.jyz.handquestionnaire.bean.ResultItem;
import com.jyz.handquestionnaire.bean.SelectionItem;
import com.jyz.handquestionnaire.listener.ResponseListener;
import com.jyz.handquestionnaire.util.Constant;
import com.jyz.handquestionnaire.util.MyUtil;
import com.jyz.handquestionnaire.util.ProgressDialogUtil;
import com.jyz.handquestionnaire.util.StringAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 问卷结果统计页面
 * Created by Songzhihang on 2017/12/5.
 */
public class AnswerCountActivity extends BaseActivity {
    private static final String TAG = "AnswerCountActivity";

    private BarChart aacl_bar_chat;

    private TextView aacl_tv_question;

    private QuestionItem questionItem;
    private ArrayList<AnswerItem> answerItems;
    private LinearLayout aacl_ll_selection_layout;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_answer_count_layout);
    }

    @Override
    protected void findViews() {
        //饼状图
        aacl_tv_question = (TextView) findViewById(R.id.aacl_tv_question);
        aacl_ll_selection_layout = (LinearLayout) findViewById(R.id.aacl_ll_selection_layout);
        aacl_bar_chat = (BarChart) findViewById(R.id.aacl_bar_chat);
        //隐藏网格线,保留水平线
        XAxis xAxis = aacl_bar_chat.getXAxis();
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        aacl_bar_chat.getAxisLeft().setDrawAxisLine(false);
        //隐藏右边Y轴
        aacl_bar_chat.getAxisLeft().setEnabled(true);
        aacl_bar_chat.getAxisRight().setEnabled(false);
        //设置坐标原点
        xAxis.setXOffset(0);
        aacl_bar_chat.getAxisLeft().setAxisMinValue(0f);
        aacl_bar_chat.setFitBars(true);//使两侧的柱图完全显示
        aacl_bar_chat.animateX(1500);//数据显示动画，从左往右依次显示
        //设置是否支持拖拽
        aacl_bar_chat.setDragEnabled(false);
        //设置能否缩放
        aacl_bar_chat.setScaleEnabled(false);
        //设置true支持两个指头向X、Y轴的缩放，如果为false，只能支持X或者Y轴的当方向缩放
        aacl_bar_chat.setPinchZoom(false);
        //设置所有的数值在图形的上面,而不是图形上
        aacl_bar_chat.setDrawValueAboveBar(true);
    }

    @Override
    protected void initData() {
        questionItem = (QuestionItem) getIntent().getBundleExtra("bundle").getSerializable("questionItem");
        setTitle("统计结果");
        aacl_tv_question.setText("问题："+questionItem.getTitle()+"的统计结果如下：");
        getQuestionAnswers();
    }

    @Override
    protected void setListener() {

    }

    private void setData() {
        ArrayList<BarEntry> entries = new ArrayList<>();//显示条目
        ArrayList<String> xVals = new ArrayList<String>();//横坐标标签
        //模拟数据
        int position=0;
        for (SelectionItem selection : questionItem.getSelectionItemList()) {
            int count=0;
            TextView textView=new TextView(mContext);
            textView.setText(numToChar(position)+": "+selection.getTitle());
            textView.setTextSize(13);
            textView.setTextColor(Color.parseColor("#333333"));
            textView.setPadding(0, MyUtil.toDip(6),0,MyUtil.toDip(6));
            aacl_ll_selection_layout.addView(textView);
            for (AnswerItem answer : answerItems) {
                if (answer.getSelectionId() == selection.getSelectionId()) {
                    count++;
                }
            }
            entries.add(new BarEntry(count,position));
            xVals.add(numToChar(position));
            position++;
        }
        //x坐标轴设置
        IAxisValueFormatter xAxisFormatter = new StringAxisValueFormatter(xVals);//设置自定义的x轴值格式化器
        XAxis xAxis = aacl_bar_chat.getXAxis();//获取x轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴标签显示位置
        xAxis.setDrawGridLines(false);//不绘制格网线
        xAxis.setGranularity(1f);//设置最小间隔，防止当放大时，出现重复标签。
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setTextSize(12f);//设置标签字体大小
        xAxis.setLabelCount(xVals.size());//设置标签显示的个数

        BarDataSet dataSet = new BarDataSet(entries,"选项回答统计表，单位个");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(dataSet);
        aacl_bar_chat.setData(data);
        //设置Y方向上动画animateY(int time);
        aacl_bar_chat.animateY(1500);
        //图表描述
        Description description=new Description();
        description.setText(questionItem.getTitle());
    }

    /**
     * 数字转换成字母
     * @param mun
     * @return
     */
    private String numToChar(int mun){
        char c1=(char) (mun+65);
        return  c1+"";
    }

    /**
     * 获取问题答案，只需要更具问题id查询出所有答案
     */
    private void getQuestionAnswers() {
        ProgressDialogUtil.showProgressDialog(this, true);
        Map<String, String> params = new HashMap<>();
        params.put("questionId", questionItem.getQuestionId() + "");
        OkHttpHelp<ResultItem> httpHelp = OkHttpHelp.getInstance();
        httpHelp.httpRequest("post", Constant.GET_QUESTION_ANSWERS, params, new ResponseListener<ResultItem>() {
            @Override
            public void onSuccess(ResultItem object) {
                ProgressDialogUtil.dismissProgressdialog();
                if ("fail".equals(object.getResult())) {
                    toast("网络错误，请重试！");
                    return;
                }
                answerItems = new ArrayList<AnswerItem>();
                JSONArray jsonArray = null;
                answerItems.clear();
                try {
                    jsonArray = new JSONArray(object.getData());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        AnswerItem postBarItem = new Gson().fromJson(jsonArray.get(i).toString(), AnswerItem.class);
                        answerItems.add(postBarItem);
                    }
                    setData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String message) {
                ProgressDialogUtil.dismissProgressdialog();
            }

            @Override
            public Class<ResultItem> getEntityClass() {
                return ResultItem.class;
            }
        });
    }
}
