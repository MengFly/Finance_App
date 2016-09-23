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
import com.example.econonew.view.activity.BaseActivity;
import com.example.econonew.view.activity.FinanceApplication;
import com.example.econonew.view.activity.User.UserActivity;
import com.example.econonew.view.activity.User.UserLoginActivity;
import com.example.econonew.view.activity.channel.ChannelAddActivity;
import com.example.econonew.view.customview.ViewPagerIndicator;
import com.example.econonew.resource.AllMessage;
import com.example.econonew.resource.Constant;
import com.example.econonew.tools.adapter.FragmentAdapter;
import com.example.econonew.view.fragment.MsgListFragment;
import com.example.econonew.tools.SettingManager;
import com.example.econonew.tools.ShareTool;
import com.example.econonew.tools.Voice;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements OnClickListener {

	private ViewPager mViewPager;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.act_main);

		Button voiceBtn = (Button) findViewById(R.id.act_main_voice_btn);
		Button addChannelBtn = (Button) findViewById(R.id.add_channel_btn);
		Button userBtn = (Button) findViewById(R.id.act_main_user_btn);
		ImageButton shareBtn = (ImageButton) findViewById(R.id.act_main_title_bar_share);

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
		}, null);
	}

	// 初始化ViewPager
	private void initViewPager() {
		mViewPager = (ViewPager) findViewById(R.id.act_main_viewpager);
		List<Fragment> list = new ArrayList<>();
		for (String tabName : Constant.tabList) {
			list.add(MsgListFragment.newInstance(this, tabName));
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
		int id = arg0.getId();
		switch (id) {
			case R.id.add_channel_btn:
				if (Constant.user == null) {
					showTipDialog(null, "你还没有登陆,是否登录", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							openOtherActivity(UserLoginActivity.class, false);
						}
					}, null);
				} else {
					openOtherActivity(ChannelAddActivity.class, false);
				}
				break;
			case R.id.act_main_user_btn:
				openOtherActivity(UserActivity.class, false);
				break;
			case R.id.act_main_voice_btn:
				setRead(Constant.read_tab);
				break;
			case R.id.act_main_title_bar_share:
				ShareTool.shareFiles(this, FinanceApplication.getInstance().getApplicationFile());
				break;
			default:
				break;
		}
	}

	// 设置朗读项
	public void setRead(int tab) {
		AllMessage messageManager = AllMessage.getInstance(Constant.tabList.get(tab));
		if (messageManager != null &&"自定义".equals(messageManager.getName())) {
			if (Constant.user == null) {
				new Voice(this).readStr("您还没有登陆，请登陆后在查看自定义信息");
			}
		} else if(messageManager != null){
			messageManager.readMsgList();
		}
	}

}
