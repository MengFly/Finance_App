package com.example.econonew.view.activity.channel;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.econonew.R;
import com.example.econonew.view.activity.BaseActivity;
import com.example.econonew.view.customview.UnderLineTextView;

/**
 * 添加频道的Activity，点击各种添加频道的Activity
 * @author agnes
 */
public class ChannelAddActivity extends BaseActivity implements OnClickListener {

	private Button exchange_choose ;
	private Button stock_choose;
	private Button money_choose;
	private Button funds_choose;
	private Button futures_choose;
	private Button special_choose;
	private Button channel_quit;
	private UnderLineTextView addMoreChannelTV;


	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.act_addchannel);

		exchange_choose = (Button) findViewById(R.id.exchange_choose);
		stock_choose = (Button) findViewById(R.id.stock_choose);
		money_choose = (Button) findViewById(R.id.money_choose);
		funds_choose = (Button) findViewById(R.id.funds_choose);
		futures_choose = (Button) findViewById(R.id.futures_choose);
		special_choose = (Button) findViewById(R.id.special_choose);
		channel_quit = (Button) findViewById(R.id.channel_quit);
		addMoreChannelTV = (UnderLineTextView) findViewById(R.id.act_add_channel_add_more_channel);

		initListener();

	}

	private void initListener() {
		exchange_choose.setOnClickListener(this);
		stock_choose.setOnClickListener(this);
		special_choose.setOnClickListener(this);
		funds_choose.setOnClickListener(this);
		money_choose.setOnClickListener(this);
		futures_choose.setOnClickListener(this);
		channel_quit.setOnClickListener(this);

		addMoreChannelTV.setOnClickListener(this);
	}

	@Override
	protected void initDatas() {

	}
	@SuppressWarnings("unchecked")
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.exchange_choose:
			openAddOneChannelActivity("外汇");
			break;
		case R.id.stock_choose:
			openAddOneChannelActivity("股票");
			break;
		case R.id.funds_choose:
			openAddOneChannelActivity("基金");
			break;
		case R.id.futures_choose:
			openAddOneChannelActivity("期货");
			break;
		case R.id.money_choose:
			openAddOneChannelActivity("理财");
			break;
		case R.id.special_choose:
			openOtherActivity(AddChannelFromCodeActivity.class, false);
			break;
		case R.id.act_add_channel_add_more_channel :
			openOtherActivity(AddMoreChannelActivity.class, false);
			break;
		case R.id.channel_quit:
			backHomeActivity();
			break;
		default:
			break;
		}
	}

	private void openAddOneChannelActivity(String channelName) {
		Intent intent = new Intent(mContext, AddOneChannelActivity.class);
		intent.putExtra(AddOneChannelActivity.CHANNEL_NAME, channelName);
		startActivity(intent);
		finish();
	}

}
