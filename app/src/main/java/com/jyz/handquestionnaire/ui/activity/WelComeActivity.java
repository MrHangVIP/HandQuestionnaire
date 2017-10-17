package com.jyz.handquestionnaire.ui.activity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jyz.handquestionnaire.BaseActivity;
import com.jyz.handquestionnaire.R;
import com.jyz.handquestionnaire.util.Constant;
import com.jyz.handquestionnaire.util.SpfUtil;

import java.util.ArrayList;
import java.util.List;

public class WelComeActivity extends BaseActivity {
    private static final String TAG = "WelComeActivity";
    private ViewPager viewPager;
    private TextView startTV;
    private List<View> views;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_welcome_layout);
    }

    @Override
    protected void findViews() {
        startTV = (TextView) findViewById(R.id.awl_tv_start);
        viewPager = (ViewPager) this.findViewById(R.id.awl_vp_viewpager);
    }

    @Override
    protected void initData() {
        views = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setImageResource(R.drawable.img_welcome_01);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            views.add(imageView);
        }
        viewPager.setAdapter(new ViewPagerAdapter());
    }

    @Override
    protected void setListener() {
        startTV.setOnClickListener(this);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    startTV.setVisibility(View.VISIBLE);
                } else {
                    startTV.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.awl_tv_start:
                SpfUtil.saveBoolean(Constant.IS_FIRST, true);
                if (SpfUtil.getBoolean(Constant.IS_LOGIN, false)) {
                    goToNext(MainTabActivity.class);
                } else {
                    goToNext(MainTabActivity.class);
                }
            default:
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!SpfUtil.getBoolean(Constant.IS_FIRST, true)) {
            goToNext(MainTabActivity.class);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

    }
}
