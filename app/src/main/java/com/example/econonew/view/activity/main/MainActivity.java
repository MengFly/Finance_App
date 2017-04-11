package com.example.econonew.view.activity.main;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.econonew.R;
import com.example.econonew.databinding.ActMainBinding;
import com.example.econonew.presenter.MainPresenter;
import com.example.econonew.resource.Constant;
import com.example.econonew.tools.SettingManager;
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
		},null);
	}

	// 初始化ViewPager
	private void initViewPager() {
		List<Fragment> list = new ArrayList<>();
		for (String tabName : Constant.tabList) {
			if("自定义".equals(tabName)) {
				list.add(ChannelMessageFragment.newInstance(tabName));
			} else {
				list.add(MainMessageFragment.newInstance(tabName));
			}
		}
		mBinding.actMainViewpager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), list));
		mBinding.actMainTabLy.setupWithViewPager(mBinding.actMainViewpager);
		mBinding.actMainTitleBar.setPresenter(new MainPresenter(this));
	}


}
