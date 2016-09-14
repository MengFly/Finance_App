package com.example.econonew.view.activity.channel;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.econonew.view.activity.BaseActivity;
import com.example.econonew.R;
import com.example.econonew.entity.AddChannelClickListener;
import com.example.econonew.entity.ChannelEntity;
import com.example.econonew.tools.ChannelListManager;
import com.example.econonew.tools.DialogTool;
/**
 * * @agnes
 *
 * @date 创建时间：2015-10-22 下午4:22:33 在channelActivity中调用共同生成频道添加界面 按股票搜索
 */
public class MoneyActivity extends BaseActivity implements OnClickListener {
	private Button money_name;
	private Button money_info;
	private TextView money_name_show;
	private TextView money_info_show;
	private EditText money_code_input;
	private Button channel_sure; // 确认按钮

	private String channelName = "理财";

	private String[] channelFirstLabel = ChannelListManager.getChannelFirstLable(channelName);

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.activity_addchannel_money);
		money_name = (Button) findViewById(R.id.money_name);
		money_info = (Button) findViewById(R.id.money_info);
		channel_sure = (Button) findViewById(R.id.money_channel_sure);
		money_info_show = (TextView) findViewById(R.id.money_info_show);
		money_name_show = (TextView) findViewById(R.id.money_name_show);
		money_code_input = (EditText) findViewById(R.id.money_code_input);

		channel_sure.setOnClickListener(this);
		money_info.setOnClickListener(this);
		money_name.setOnClickListener(this);
	}

	@Override
	protected void initDatas() {
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
			case R.id.money_channel_sure:
				String type = money_name_show.getText().toString();
				String attribute = money_info_show.getText().toString();
				String code = money_code_input.getText().toString();
				String channelType = type.equals("请选择") ? "" : type;
				String channelAttribute = attribute.equals("请选择") ? "" : attribute;
				ChannelEntity channelEntity = new ChannelEntity();
				channelEntity.setName(channelName);
				channelEntity.setType(channelType);
				channelEntity.setAttribute(channelAttribute);
				channelEntity.setCode(code);
				if (channelEntity.isAddChannel(type, attribute, code, this)) {
					DialogInterface.OnClickListener listener = new AddChannelClickListener(channelEntity,
							MoneyActivity.this);
					showTipDialog(null, "是否确定添加频道：" + channelEntity.getName() + "," + channelEntity.getType() + ","
							+ channelEntity.getAttribute() + "," + channelEntity.getCode(), listener, null);
				}
				break;
			case R.id.money_name:
				DialogTool dialogName = new DialogTool(this, channelFirstLabel, money_name_show);
				dialogName.setDialog();
				break;
			case R.id.money_info:
				DialogTool dialogInfo = new DialogTool(this, ChannelListManager.getChannelSecondLabel(), money_info_show);
				dialogInfo.setDialog();
				break;
			default:
				break;
		}
	}

}
