package com.jyz.handquestionnaire.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyz.handquestionnaire.BaseActivity;
import com.jyz.handquestionnaire.R;
import com.jyz.handquestionnaire.library.DefaultAnimationHandler2;
import com.jyz.handquestionnaire.library.FloatingActionMenu;
import com.jyz.handquestionnaire.library.MenuAnimationHandler2;
import com.jyz.handquestionnaire.ui.fragment.HomeFragment;
import com.jyz.handquestionnaire.ui.fragment.UserCenterFragment;
import com.jyz.handquestionnaire.util.MyUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Songzhihang on 2017/10/6.
 * 首页tabActivity
 */
public class MainTabActivity extends BaseActivity {
    private static final String TAG = "MainTabActivity";
    private FrameLayout contentFL;
    private LinearLayout tabLL;
    private TextView leftTV;
    private TextView rightTV;
    private ImageView addIV;
    private FloatingActionMenu centerBottomMenu;
    private View aml_translate_bg;
    private HomeFragment homeFragment;
    private UserCenterFragment userCenterFragment;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_maintab_layout);
    }

    @Override
    protected void findViews() {
        contentFL = (FrameLayout) findViewById(R.id.aml_fl_content);
        tabLL = (LinearLayout) findViewById(R.id.aml_ll_tab_layout);
        leftTV = (TextView) findViewById(R.id.aml_tv_left);
        rightTV = (TextView) findViewById(R.id.aml_tv_right);
        addIV = (ImageView) findViewById(R.id.aml_iv_add);
        aml_translate_bg = (View) findViewById(R.id.aml_translate_bg);
    }

    @Override
    protected void initData() {
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        replaceFragment(homeFragment);
    }

    @Override
    protected void setListener() {
        leftTV.setOnClickListener(this);
        rightTV.setOnClickListener(this);
        addIV.setOnClickListener(this);
        aml_translate_bg.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.aml_tv_left) {
            if (homeFragment == null) {
                homeFragment = new HomeFragment();
            }
            replaceFragment(homeFragment);
        }
        if (v.getId() == R.id.aml_tv_right) {
            if (userCenterFragment == null) {
                userCenterFragment = new UserCenterFragment();
            }
            replaceFragment(userCenterFragment);
        }

        if (v.getId() == R.id.aml_iv_add) {
            showFloatActionMenu();
        }
        if (v.getId() == R.id.aml_translate_bg) {
            closeWindow();
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.aml_fl_content, fragment);
        transaction.commit();
    }

    private void showFloatActionMenu() {
        //否则显示弹出框
        if (centerBottomMenu == null) {
            MenuAnimationHandler2 animationHandler = new DefaultAnimationHandler2();
            List<FloatingActionMenu.Item> subActionItems = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                ImageView rlIcon = new ImageView(mContext);
                rlIcon.setImageResource(R.drawable.icon_publish);
                subActionItems.add(new FloatingActionMenu.Item(rlIcon, MyUtil.toDip(38), MyUtil.toDip(38)));
                rlIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeWindow();
                    }
                });
            }
            centerBottomMenu = new FloatingActionMenu(addIV, 200, 340, MyUtil.toDip(100),
                    subActionItems, animationHandler, true, null, false);

            centerBottomMenu
                    .setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {

                        @Override
                        public void onMenuOpened(FloatingActionMenu menu) {
                            aml_translate_bg.setVisibility(View.VISIBLE);
                            addIV.startAnimation(getRotateAnim(0f, 45f));
                        }

                        @Override
                        public void onMenuClosed(FloatingActionMenu menu) {
                            aml_translate_bg.setVisibility(View.GONE);
                            addIV.startAnimation(getRotateAnim(45f, 0f));
                        }
                    });
        }

        if (centerBottomMenu != null && centerBottomMenu.isOpen()) {
            centerBottomMenu.close(true);
        } else if (centerBottomMenu != null) {
            centerBottomMenu.open(true);
        }
    }

    private void closeWindow() {
        if (centerBottomMenu != null && centerBottomMenu.isOpen()) {
            centerBottomMenu.close(true);
            aml_translate_bg.setVisibility(View.GONE);
        }
    }

    protected RotateAnimation getRotateAnim(Float from, Float to) {
        final RotateAnimation animation = new RotateAnimation(from, to,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setDuration(300);// 设置动画持续时间
        animation.setFillAfter(true);// 动画执行完后是否停留在执行完的状态
        animation.setStartOffset(0);
        return animation;
    }
}
