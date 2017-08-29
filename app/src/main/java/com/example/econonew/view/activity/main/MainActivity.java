package com.example.econonew.view.activity.main;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.example.econonew.R;
import com.example.econonew.databinding.ActMainBinding;
import com.example.econonew.presenter.MainPresenter;
import com.example.econonew.resource.Constant;
import com.example.econonew.tools.SettingManager;
import com.example.econonew.tools.Voice;
import com.example.econonew.tools.adapter.FragmentAdapter;
import com.example.econonew.view.activity.BaseActivity;
import com.example.econonew.view.activity.FinanceApplication;
import com.example.econonew.view.fragment.ChannelMessageFragment;
import com.example.econonew.view.fragment.MainMessageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用的主界面
 */
public class MainActivity extends BaseActivity<MainPresenter> {

    private ActMainBinding mBinding;

    @Override
    protected void initView(Bundle savedInstanceState) {
        mBinding = DataBindingUtil.setContentView(mContext, R.layout.act_main);
        bindPresenter(new MainPresenter(this));
        mBinding.setPresenter(mPresenter);
        initViewPager();
        initPublicData();
        initListener();
    }

    private void initListener() {
        Voice.getInstance().setStatChangeListener(new Voice.OnVoiceStatChangeListener() {
            @Override
            public void statChange(int stat) {
                //获取到现在的topDrawable
                Drawable topDrawable = mBinding.actMainVoiceBtn.getCompoundDrawables()[1];
                if (stat == Voice.VOICE_STAT_NO_READ) {
                    Drawable drawable = getResources().getDrawable(R.drawable.icon_voice);
                    drawable.setBounds(topDrawable.getBounds());
                    mBinding.actMainVoiceBtn.setCompoundDrawables(null, drawable, null, null);
                } else if (stat == Voice.VOICE_STAT_PAUSE) {
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_pause);
                    drawable.setBounds(topDrawable.getBounds());
                    mBinding.actMainVoiceBtn.setCompoundDrawables(null, drawable, null, null);
                } else if (stat == Voice.VOICE_STAT_READING) {
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_reading);
                    drawable.setBounds(topDrawable.getBounds());
                    mBinding.actMainVoiceBtn.setCompoundDrawables(null, drawable, null, null);
                }
            }
        });
    }

    // 初始化公共信息
    private void initPublicData() {
        // 如果之前没有加载过数据，则加载数据
        if (!SettingManager.isLoadedDatas()) {
            showProDialog();
            FinanceApplication.getInstance().refreshUserData(Constant.user);//先初始化用户数据，便于对以后的数据过滤
            FinanceApplication.getInstance().refreshPublicData();
        }
    }

    @Override
    public void onBackPressed() {
        showTipDialog(null, "是否要退出应用？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FinanceApplication.getInstance().exit();
            }
        }, null);
    }

    // 初始化ViewPager
    private void initViewPager() {
        List<Fragment> list = new ArrayList<>();
        for (String tabName : Constant.tabList) {
            if ("自定义".equals(tabName)) {
                list.add(ChannelMessageFragment.newInstance(tabName));
            } else {
                list.add(MainMessageFragment.newInstance(tabName));
            }
        }
        mBinding.actMainViewpager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), list));
        mBinding.actMainViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Constant.read_tab = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBinding.actMainTabLy.setupWithViewPager(mBinding.actMainViewpager);
        mBinding.actMainTitleBar.setPresenter(mPresenter);
    }

    @Override
    protected void onPause() {
        Voice.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        Voice.onResume();
        super.onResume();
    }
}
