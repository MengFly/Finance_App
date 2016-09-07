package com.example.econonew.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.econonew.R;
import com.example.econonew.channel.view.ExchangeActivity;
import com.example.econonew.channel.view.FundsActivity;
import com.example.econonew.channel.view.FuturesActivity;
import com.example.econonew.channel.view.MoneyActivity;
import com.example.econonew.channel.view.SpecialActivity;
import com.example.econonew.channel.view.StockActivity;

/**
 * ChannelAddActivity ������Ҫ��ʵ��ѡ���ǩ������Ƶ�������ҷ��ؽ����MainActivity
 * ������DialogTool�����ɶԻ�ѡ������DataThread���Ѿ����ر�������� ��Ƶ��������ɵķ���������Ҫ��ϸ̸��ϸ��
 * 
 * @author agnes
 */
public class ChannelAddActivity extends BaseActivity implements OnClickListener {

	private Button exchange_choose;
	private Button stock_choose;
	private Button money_choose;
	private Button futures_choose;
	private Button special_choose;
	private Button funds_choose;
	private Button channel_quit;

	private TextView addMoreChannelTV;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.activity_addchannel);

		exchange_choose = (Button) findViewById(R.id.exchange_choose);
		stock_choose = (Button) findViewById(R.id.stock_choose);
		money_choose = (Button) findViewById(R.id.money_choose);
		funds_choose = (Button) findViewById(R.id.funds_choose);
		futures_choose = (Button) findViewById(R.id.futures_choose);
		special_choose = (Button) findViewById(R.id.special_choose);
		channel_quit = (Button) findViewById(R.id.channel_quit);
		
		addMoreChannelTV = (TextView) findViewById(R.id.act_add_channel_add_more_channel);

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

	/*
	 * ���ü����¼�
	 */
	@Override
	public void onClick(View view) {
		int id = view.getId();
		Intent intent = new Intent();
		switch (id) {
		case R.id.exchange_choose:
			intent.setClass(ChannelAddActivity.this, ExchangeActivity.class);
			break;
		case R.id.stock_choose:
			intent.setClass(ChannelAddActivity.this, StockActivity.class);
			break;
		case R.id.funds_choose:
			intent.setClass(ChannelAddActivity.this, FundsActivity.class);
			break;
		case R.id.futures_choose:
			intent.setClass(ChannelAddActivity.this, FuturesActivity.class);
			break;
		case R.id.money_choose:
			intent.setClass(ChannelAddActivity.this, MoneyActivity.class);
			break;
		case R.id.special_choose:
			intent.setClass(ChannelAddActivity.this, SpecialActivity.class);
			break;
		case R.id.channel_quit:
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setClass(ChannelAddActivity.this, MainActivity.class);
			finish();
			break;
		case R.id.act_add_channel_add_more_channel :
			intent.setClass(ChannelAddActivity.this, AddMoreChannelActivity.class);
			break;
		default:
			break;
		}
		startActivity(intent);
	}

}
