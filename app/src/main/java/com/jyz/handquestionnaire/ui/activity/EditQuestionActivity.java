package com.jyz.handquestionnaire.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jyz.handquestionnaire.BaseActivity;
import com.jyz.handquestionnaire.R;
import com.jyz.handquestionnaire.bean.QuestionItem;
import com.jyz.handquestionnaire.bean.QuestionnaireItem;
import com.jyz.handquestionnaire.ui.widget.CustomMoreView;
import com.jyz.handquestionnaire.ui.widget.MMAlert;
import com.jyz.handquestionnaire.util.Constant;
import com.jyz.handquestionnaire.util.MyUtil;

import java.util.ArrayList;

/**
 * @discription 编辑问卷页面
 * @autor songzhihang
 * @time 2017/10/16  下午4:10
 **/
public class EditQuestionActivity extends BaseActivity {
    private static final String TAG = "CreateQuestionActivity";

    public static final int REQUEST_CODE = 0x10;
    public static final int RESULT_CODE = 0x11;
    private TextView aeql_tv_title;
    private TextView aeql_tv_edit_title;
    private TextView aeql_tv_introduce;
    private LinearLayout aeql_ll_question_layout;
    private TextView aeql_tv_add;
    private TextView aeql_tv_thanks;
    private TextView aeql_tv_edit_thanks;
    private View curUpdateView;

    private QuestionnaireItem questionnaireItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createActivityList.add(this);
    }

    @Override
    protected void setView() {
        setContentView(R.layout.activity_edit_question_layout);
    }

    @Override
    protected void findViews() {
        setTitle("编辑问卷");
        setMenu();
        questionnaireItem = new QuestionnaireItem();
        aeql_tv_title = (TextView) findViewById(R.id.aeql_tv_title);
        aeql_tv_introduce = (TextView) findViewById(R.id.aeql_tv_introduce);
        aeql_tv_edit_title = (TextView) findViewById(R.id.aeql_tv_edit_title);
        aeql_ll_question_layout = (LinearLayout) findViewById(R.id.aeql_ll_question_layout);
        aeql_tv_add = (TextView) findViewById(R.id.aeql_tv_add);
        aeql_tv_thanks = (TextView) findViewById(R.id.aeql_tv_thanks);
        aeql_tv_edit_thanks = (TextView) findViewById(R.id.aeql_tv_edit_thanks);
    }

    @Override
    protected void initData() {
        String title = getIntent().getBundleExtra("bundle").getString("title");
        aeql_tv_title.setText(title);
        questionnaireItem.setTitle(title);
        questionnaireItem.setThanks(aeql_tv_thanks.getText().toString());
    }

    @Override
    protected void setListener() {

        aeql_tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomMoreView customMoreView = new CustomMoreView(EditQuestionActivity.this);
                customMoreView.init();
                customMoreView.showMoreWindow(toolbar);
                customMoreView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (Integer) v.getTag();
                        if (position == 0) {//单选
                            Intent intent = new Intent();
                            intent.setClass(EditQuestionActivity.this, CreateSelectionActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("type", "1");
                            intent.putExtra("bundle", bundle);
                            startActivityForResult(intent, REQUEST_CODE);
                        }
                        if (position == 1) {//多选
                            Intent intent = new Intent();
                            intent.setClass(EditQuestionActivity.this, CreateSelectionActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("type", "2");
                            intent.putExtra("bundle", bundle);
                            startActivityForResult(intent, REQUEST_CODE);
                        }
                        if (position == 2) {//填空题
                            Intent intent = new Intent();
                            intent.setClass(EditQuestionActivity.this, CreateBlankActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("type", "3");
                            intent.putExtra("bundle", bundle);
                            startActivityForResult(intent, REQUEST_CODE);
                        }
                    }
                });
            }
        });

        aeql_tv_edit_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("questionnaireItem", questionnaireItem);
                intent.setClass(mContext, UpdateContentActivity.class);
                intent.putExtra("type", "1");
                startActivityForResult(intent, UpdateContentActivity.REQUEST_CODE);
            }
        });

        aeql_tv_edit_thanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("questionnaireItem", questionnaireItem);
                intent.setClass(mContext, UpdateContentActivity.class);
                intent.putExtra("type", "2");
                startActivityForResult(intent, UpdateContentActivity.REQUEST_CODE);
            }
        });
    }

    void setMenu() {
        toolbar.inflateMenu(R.menu.menu_submit);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.toolbar_submit) {
                    //预览页面
                    updateQuestionnaireItem();
                    Bundle bundle = new Bundle();
                    bundle.putString("title", getIntent().getBundleExtra("bundle").getString("title"));
                    bundle.putSerializable("questionnaireItem", questionnaireItem);
                    jumpToNext(QuestionPreviewActivity.class, bundle);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 添加问题
     *
     * @param questionItem
     */
    private void addQuestionView(final QuestionItem questionItem) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_question_create_layout, null);
        TextView iqcl_tv_delete = (TextView) view.findViewById(R.id.iqcl_tv_delete);
        final HorizontalScrollView iqcl_scroll_layout = (HorizontalScrollView) view.findViewById(R.id.iqcl_scroll_layout);
        RelativeLayout iqcl_rl_content_layout = (RelativeLayout) view.findViewById(R.id.iqcl_rl_content_layout);
        TextView iqcl_tv_delete_click = (TextView) view.findViewById(R.id.iqcl_tv_delete_click);
        TextView aqcl_tv_title = (TextView) view.findViewById(R.id.aqcl_tv_title);
        TextView aqcl_tv_type = (TextView) view.findViewById(R.id.aqcl_tv_type);
        iqcl_scroll_layout.setHorizontalScrollBarEnabled(false);// 隐藏滚动条
        iqcl_scroll_layout.setScrollbarFadingEnabled(false);
        iqcl_scroll_layout.setFadingEdgeLength(0);
        iqcl_scroll_layout.setOverScrollMode(HorizontalScrollView.OVER_SCROLL_NEVER);
        iqcl_scroll_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {//延时处理
                    iqcl_scroll_layout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            final int scrollX = iqcl_scroll_layout.getScrollX();
                            Log.e(TAG, "scrollX:" + scrollX);
                            if (scrollX < MyUtil.toDip(70) / 2) {
                                iqcl_scroll_layout.smoothScrollBy(-MyUtil.toDip(70), 0);
                            } else {
                                iqcl_scroll_layout.smoothScrollBy(MyUtil.toDip(70), 0);
                            }
                        }
                    }, 50);
                }
                return false;
            }
        });
        iqcl_tv_delete_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MMAlert.showAlert(mContext, null, "确定删除?", getResources().getString(R.string.app_tip), null, null,
                        new MMAlert.OnDialogClick() {

                            @Override
                            public void onOkListener(String content) {
                                aeql_ll_question_layout.removeView(view);
                            }

                            @Override
                            public void onClickPreListener(EditText et) {
                            }

                            @Override
                            public void onCancelListener(EditText et) {
                                iqcl_scroll_layout.smoothScrollBy(-MyUtil.toDip(70), 0);
                            }
                        }, true);
            }
        });
        iqcl_rl_content_layout.getLayoutParams().width = Constant.getScreenWidth(mContext) - MyUtil.toDip(30);
        view.setTag(questionItem);
        aqcl_tv_title.setText(questionItem.getTitle());
        aqcl_tv_type.setText(getTypeStr(questionItem.getType()));
        iqcl_rl_content_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curUpdateView = view;
                Intent intent = new Intent();
                if (TextUtils.equals("3", ((QuestionItem) view.getTag()).getType())) {
                    intent.setClass(EditQuestionActivity.this, CreateBlankActivity.class);
                }else{
                    intent.setClass(EditQuestionActivity.this, CreateSelectionActivity.class);
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("questionItem", (QuestionItem) view.getTag());
                intent.putExtra("bundle", bundle);
                startActivityForResult(intent, RESULT_CODE);
            }
        });
        aeql_ll_question_layout.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MyUtil.toDip(45)));
    }

    /**
     * 更新修改的条目
     *
     * @param questionItem
     */
    private void updateQuestionView(QuestionItem questionItem) {
        TextView aqcl_tv_title = (TextView) curUpdateView.findViewById(R.id.aqcl_tv_title);
        TextView aqcl_tv_type = (TextView) curUpdateView.findViewById(R.id.aqcl_tv_type);
        curUpdateView.setTag(questionItem);
        aqcl_tv_title.setText(questionItem.getTitle());
        aqcl_tv_type.setText(getTypeStr(questionItem.getType()));
    }

    /**
     * 统计条目
     */
    private void updateQuestionnaireItem() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < aeql_ll_question_layout.getChildCount(); i++) {
            arrayList.add(aeql_ll_question_layout.getChildAt(i).getTag());
        }
        questionnaireItem.setQuestionItemList(arrayList);
    }

    private void updateDateView() {
        aeql_tv_title.setText(questionnaireItem.getTitle());
        aeql_tv_thanks.setText(questionnaireItem.getThanks());
        if (!TextUtils.isEmpty(questionnaireItem.getIntroduce())) {
            aeql_tv_introduce.setText(questionnaireItem.getIntroduce());
        }
    }

    /**
     * 获取题型
     *
     * @param type
     * @return
     */
    private String getTypeStr(String type) {
        if (TextUtils.equals("1", type)) {
            return "单选题";
        }
        if (TextUtils.equals("2", type)) {
            return "多选题";
        }
        if (TextUtils.equals("3", type)) {
            return "填空题";
        }
        return "单选题";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {//添加
            QuestionItem questionItem = (QuestionItem) data.getSerializableExtra("questionItem");
            addQuestionView(questionItem);
        }
        if (requestCode == RESULT_CODE && resultCode == RESULT_CODE) {//修改
            QuestionItem questionItem = (QuestionItem) data.getSerializableExtra("questionItem");
            updateQuestionView(questionItem);
        }
        if (requestCode == UpdateContentActivity.REQUEST_CODE && resultCode == UpdateContentActivity.RESULT_CODE) {
            questionnaireItem = (QuestionnaireItem) data.getSerializableExtra("questionnaireItem");
            updateDateView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        createActivityList.remove(this);
    }
}
