package com.example.econonew.activity.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.econonew.R;
import com.example.econonew.activity.BaseActivity;
import com.example.econonew.activity.FinanceApplication;
import com.example.econonew.activity.User.UserActivity;
import com.example.econonew.activity.channel.ChannelAddActivity;
import com.example.econonew.customview.ViewPagerIndicator;
import com.example.econonew.main.object.AllMessage;
import com.example.econonew.resource.Constant;
import com.example.econonew.tools.FragmentAdapter;
import com.example.econonew.tools.MsgListFragment;
import com.example.econonew.tools.SettingManager;
import com.example.econonew.tools.ShareTool;
import com.example.econonew.tools.Voice;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements OnClickListener {

	private Button voiceBtn;
	private Button addChannelBtn;
	private Button userBtn;
	private ImageButton shareBtn;

	private ViewPagerIndicator indicator;
	private ViewPager mViewPager;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);

		voiceBtn = (Button) findViewById(R.id.act_main_voice_btn);
		addChannelBtn = (Button) findViewById(R.id.add_channel_btn);
		userBtn = (Button) findViewById(R.id.act_main_user_btn);
		shareBtn = (ImageButton) findViewById(R.id.act_main_title_bar_share);

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
			FinanceApplication.getInstance().refreshPublicData();
			FinanceApplication.getInstance().refreshUserData(Constant.user);
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
		List<Fragment> list = new ArrayList<Fragment>();
		for (String tabName : Constant.tabList) {
			list.add(MsgListFragment.newInstance(this, tabName));
		}
		mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),
				list));
		initTabLine();
	}

	// 初始化底部导航条以及当行条上面的名称
	private void initTabLine() {
		indicator = (ViewPagerIndicator) findViewById(R.id.act_main_viewpager_indicator);
		indicator.setTabTitles(Constant.tabList);
		indicator.setViewPager(mViewPager, 0);
	}

	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		switch (id) {
			case R.id.add_channel_btn:
				if (Constant.user == null) {
					showTipDialog(null, "你还没有登陆，登陆后才能添加频道", null, null);
				} else {
					intent.setClass(MainActivity.this, ChannelAddActivity.class);
					startActivity(intent);
				}
				break;
			case R.id.act_main_user_btn:
				intent.setClass(MainActivity.this, UserActivity.class);
				startActivity(intent);
				break;
			case R.id.act_main_voice_btn:
				setRead(Constant.read_tab);
				break;
			case R.id.act_main_title_bar_share:
				ShareTool.shareFiles(this, FinanceApplication.getInstance()
						.getApplicationFile());
				break;
			default:
				break;
		}
	}

	// 设置朗读项
	public void setRead(int tab) {
		AllMessage messageManager = AllMessage.getInstance(Constant.tabList
				.get(tab));
		if (messageManager.getName() == "自定义") {
			if (Constant.user == null) {
				new Voice(this).readStr("您还没有登陆，请登陆后在查看自定义信息");
			}
		} else {
			messageManager.readMsgList();
		}
	}

}
