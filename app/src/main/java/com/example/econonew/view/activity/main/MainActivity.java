package com.example.econonew.view.activity.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.econonew.R;
import com.example.econonew.presenter.MsgPresenter;
import com.example.econonew.resource.Constant;
import com.example.econonew.tools.SettingManager;
import com.example.econonew.tools.ShareTool;
import com.example.econonew.tools.adapter.FragmentAdapter;
import com.example.econonew.view.activity.BaseActivity;
import com.example.econonew.view.activity.FinanceApplication;
import com.example.econonew.view.activity.User.UserActivity;
import com.example.econonew.view.activity.channel.ChannelAddActivity;
import com.example.econonew.view.customview.ViewPagerIndicator;
import com.example.econonew.view.fragment.ChannelMessageFragment;
import com.example.econonew.view.fragment.MainMessageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用的主界面
 */
public class MainActivity extends BaseActivity<MsgPresenter> implements OnClickListener {

	private ViewPager mViewPager;
	private Button voiceBtn;
	private Button addChannelBtn;
	private Button userBtn;
	private ImageButton shareBtn;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.act_main);

		voiceBtn = (Button) findViewById(R.id.act_main_voice_btn);
		addChannelBtn = (Button) findViewById(R.id.add_channel_btn);
		userBtn = (Button) findViewById(R.id.act_main_user_btn);
		shareBtn = (ImageButton) findViewById(R.id.act_main_title_bar_share);
		initListener();
		bindPresenter(new MsgPresenter(this));
	}

	private void initListener() {
		addChannelBtn.setOnClickListener(this);
		shareBtn.setOnClickListener(this);
		voiceBtn.setOnClickListener(this);
		userBtn.setOnClickListener(this);
	}

	protected void initDatas() {
		initViewPager();
		initPublicData();
	}

	// 初始化公共信息
	private void initPublicData() {
		boolean isLoadedData = SettingManager.getInstance().isLoadedDatas();
		if (!isLoadedData) {// 如果之前没有加载过数据，则加载数据
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
		mViewPager = (ViewPager) findViewById(R.id.act_main_viewpager);
		List<Fragment> list = new ArrayList<>();
		for (String tabName : Constant.tabList) {
			if("自定义".equals(tabName)) {
				list.add(ChannelMessageFragment.newInstance(this, tabName));
			} else {
				list.add(MainMessageFragment.newInstance(this, tabName));
			}
		}
		mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), list));
		initTabLine();
	}

	// 初始化底部导航条以及当行条上面的名称
	private void initTabLine() {
		ViewPagerIndicator indicator = (ViewPagerIndicator) findViewById(R.id.act_main_viewpager_indicator);
		indicator.setTabTitles(Constant.tabList);
		indicator.setViewPager(mViewPager, 0);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
			case R.id.add_channel_btn:
				if (Constant.user == null) {
					showNoLoginDialog();
				} else {
					openOtherActivity(ChannelAddActivity.class, false);
				}
				break;
			case R.id.act_main_user_btn:
				openOtherActivity(UserActivity.class, false);
				break;
			case R.id.act_main_voice_btn:
				mPresenter.setRead(Constant.read_tab);
				break;
			case R.id.act_main_title_bar_share:
				ShareTool.shareFiles(this, FinanceApplication.getInstance().getApplicationFile());
				break;
			default:
				break;
		}
	}
}
