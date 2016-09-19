package com.example.econonew.view.activity.channel;


import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.econonew.R;
import com.example.econonew.view.activity.BaseActivity;

/**
 * 添加频道的Activity，点击各种添加频道的Activity
 * @author agnes
 */
public class ChannelAddActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.act_addchannel);

		Button exchange_choose = (Button) findViewById(R.id.exchange_choose);
		Button stock_choose = (Button) findViewById(R.id.stock_choose);
		Button money_choose = (Button) findViewById(R.id.money_choose);
		Button funds_choose = (Button) findViewById(R.id.funds_choose);
		Button futures_choose = (Button) findViewById(R.id.futures_choose);
		Button special_choose = (Button) findViewById(R.id.special_choose);
		Button channel_quit = (Button) findViewById(R.id.channel_quit);

		exchange_choose.setOnClickListener(this);
		stock_choose.setOnClickListener(this);
		special_choose.setOnClickListener(this);
		funds_choose.setOnClickListener(this);
		money_choose.setOnClickListener(this);
		futures_choose.setOnClickListener(this);
		channel_quit.setOnClickListener(this);

		TextView addMoreChannelTV = (TextView) findViewById(R.id.act_add_channel_add_more_channel);
		addMoreChannelTV.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
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
			openOtherActivity(ExchangeActivity.class, false);
			break;
		case R.id.stock_choose:
			openOtherActivity(StockActivity.class, false);
			break;
		case R.id.funds_choose:
			openOtherActivity(FundsActivity.class, false);
			break;
		case R.id.futures_choose:
			openOtherActivity(FuturesActivity.class, false);
			break;
		case R.id.money_choose:
			openOtherActivity(MoneyActivity.class, false);
			break;
		case R.id.special_choose:
			openOtherActivity(SpecialActivity.class, false);
			break;
		case R.id.channel_quit:
			backHomeActivity();
			break;
		case R.id.act_add_channel_add_more_channel :
			openOtherActivity(AddMoreChannelActivity.class, false);
			break;
		default:
			break;
		}
	}

}
