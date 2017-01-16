package com.example.econonew.view.activity.channel;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.econonew.R;
import com.example.econonew.databinding.ActAddchannelBinding;

import static com.example.econonew.R.id.channel_quit;
import static com.example.econonew.R.id.exchange_choose;
import static com.example.econonew.R.id.funds_choose;
import static com.example.econonew.R.id.futures_choose;
import static com.example.econonew.R.id.money_choose;
import static com.example.econonew.R.id.special_choose;
import static com.example.econonew.R.id.stock_choose;

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
		case exchange_choose:
			openAddOneChannelActivity("外汇");
			break;
		case stock_choose:
			openAddOneChannelActivity("股票");
			break;
		case funds_choose:
			openAddOneChannelActivity("基金");
			break;
		case futures_choose:
			openAddOneChannelActivity("期货");
			break;
		case money_choose:
			openAddOneChannelActivity("理财");
			break;
		case special_choose:
			openOtherActivity(AddChannelFromCodeActivity.class, false);
			break;
		case R.id.act_add_channel_add_more_channel :
			openOtherActivity(AddMoreChannelActivity.class, false);
			break;
		case channel_quit:
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
