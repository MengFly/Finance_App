package com.example.econonew.view.activity.channel;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.econonew.R;
import com.example.econonew.databinding.ActAddchannelBinding;

/**
 * 添加频道的Activity，点击各种添加频道的Activity
 * @author agnes
 */
public class ChannelAddActivity extends BaseChannelActivity implements OnClickListener {

	private ActAddchannelBinding mBinding;

	@Override
	protected void initView(Bundle savedInstanceState) {
		mBinding = DataBindingUtil.setContentView(mContext, R.layout.act_addchannel);
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
