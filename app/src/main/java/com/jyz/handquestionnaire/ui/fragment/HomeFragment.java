package com.jyz.handquestionnaire.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jyz.handquestionnaire.BaseFragment;
import com.jyz.handquestionnaire.R;
import com.jyz.handquestionnaire.util.Constant;

import java.util.ArrayList;

/**
 * Created by Songzhihang on 2017/10/6.
 * 首页fragment
 */
public class HomeFragment extends BaseFragment {
    private static final String TAG = "HomeFragment";

    private LinearLayout fhl_ll_tab_layout;
    private View fhl_cursor;
    private TextView fhl_tv_hot;
    private TextView fhl_tv_new;
    private TextView fhl_tv_all;
    private ViewPager fhl_viewpager;
    private RelativeLayout.LayoutParams cursorMargin;

    private ArrayList<Fragment> fragments;
    private ArrayList<TextView> tabTexts;
    private int curIndex = 0;

    @Override
    protected View getLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_home_layout, container, false);
    }

    @Override
    protected void initView(View view) {
        fhl_cursor = (View) contentView.findViewById(R.id.fhl_cursor);
        fhl_ll_tab_layout = (LinearLayout) contentView.findViewById(R.id.fhl_ll_tab_layout);
        fhl_tv_hot = (TextView) contentView.findViewById(R.id.fhl_tv_hot);
        fhl_tv_new = (TextView) contentView.findViewById(R.id.fhl_tv_new);
        fhl_tv_all = (TextView) contentView.findViewById(R.id.fhl_tv_all);
        fhl_viewpager = (ViewPager) contentView.findViewById(R.id.fhl_viewpager);
        cursorMargin = (RelativeLayout.LayoutParams) fhl_cursor.getLayoutParams();
        cursorMargin.leftMargin = (Constant.getScreenWidth(getActivity()) - fhl_ll_tab_layout.getMeasuredWidth()) / 2;
    }

    @Override
    protected void initData() {
        tabTexts = new ArrayList<>();
        tabTexts.add(fhl_tv_hot);
        tabTexts.add(fhl_tv_new);
        tabTexts.add(fhl_tv_all);
        fragments = new ArrayList<>();
        Bundle bundle = new Bundle();
        QuestionnaireFragment hotFragment = new QuestionnaireFragment();
        bundle.putString("type", "1");
        hotFragment.setArguments(bundle);
        QuestionnaireFragment newFragment = new QuestionnaireFragment();
        bundle.putString("type", "2");
        newFragment.setArguments(bundle);
        QuestionnaireFragment allFragment = new QuestionnaireFragment();
        bundle.putString("type", "3");
        allFragment.setArguments(bundle);
        fragments.add(hotFragment);
        fragments.add(newFragment);
        fragments.add(allFragment);
        fhl_viewpager.setAdapter(new MyFragmentAdapter(getFragmentManager()));
        fhl_viewpager.setCurrentItem(curIndex);
        updatePosition(0);
    }

    @Override
    protected void initEvent() {

        for (TextView view : tabTexts) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = v.getId();
                    if (i == R.id.fhl_tv_hot) {
                        fhl_viewpager.setCurrentItem(0);
                    } else if (i == R.id.fhl_tv_new) {
                        fhl_viewpager.setCurrentItem(1);
                    } else if (i == R.id.fhl_tv_all) {
                        fhl_viewpager.setCurrentItem(2);
                    }
                }
            });
        }

        fhl_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                cursorMargin = (RelativeLayout.LayoutParams) fhl_cursor.getLayoutParams();
                int margin = (int) ((positionOffset + position) * (fhl_ll_tab_layout.getMeasuredWidth() / 3));
                Log.e("SZH", "margin: " + margin);
                Log.e("SZH", "position: " + position);
                cursorMargin.leftMargin = (Constant.getScreenWidth(getActivity()) - fhl_ll_tab_layout.getMeasuredWidth()) / 2 + margin;
                fhl_cursor.setLayoutParams(cursorMargin);
                fhl_cursor.invalidate();
            }

            @Override
            public void onPageSelected(int position) {
                curIndex = position;
                updatePosition(curIndex);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //    1开始改变,手指按下 ,2手指抬起 0,滑动结束
                if (state != 0) {
                    return;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    private void updatePosition(int position) {
        for (int i = 0; i < tabTexts.size(); i++) {
            if (position == i) {
                tabTexts.get(i).setTextColor(Color.parseColor("#FFFFFF"));
            } else {
                tabTexts.get(i).setTextColor(Color.parseColor("#DC3C38"));
            }
        }
    }

    class MyFragmentAdapter extends FragmentPagerAdapter {


        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * 返回需要展示的fragment
         *
         * @param position
         * @return
         */
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        /**
         * 返回需要展示的fangment数量
         *
         * @return
         */
        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
