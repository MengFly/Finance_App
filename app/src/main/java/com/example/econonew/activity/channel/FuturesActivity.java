package com.example.econonew.activity.channel;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.econonew.activity.BaseActivity;
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
public class FuturesActivity extends BaseActivity implements OnClickListener {
	private Button futures_name;
	private Button futures_info;
	private TextView futures_name_show;
	private TextView futures_info_show;
	private EditText futures_code_input;
	private Button channel_sure; // 确认按钮

	private String channelName = "期货";

	private String[] channelFirstLabel = ChannelListManager.getChannelFirstLable(channelName);

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.activity_addchannel_futures);

		futures_name = (Button) findViewById(R.id.futures_name);
		futures_info = (Button) findViewById(R.id.futures_info);
		channel_sure = (Button) findViewById(R.id.futures_channel_sure);
		futures_info_show = (TextView) findViewById(R.id.futures_info_show);
		futures_name_show = (TextView) findViewById(R.id.futures_name_show);
		futures_code_input = (EditText) findViewById(R.id.futures_code_input);

		channel_sure.setOnClickListener(this);
		futures_info.setOnClickListener(this);
		futures_name.setOnClickListener(this);
	}

	@Override
	protected void initDatas() {
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
			case R.id.futures_channel_sure:
				String type = futures_name_show.getText().toString();
				String attribute = futures_info_show.getText().toString();
				String code = futures_code_input.getText().toString();
				String channelType = type.equals("请选择") ? "" : type;
				String channelAttribute = attribute.equals("请选择") ? "" : attribute;
				ChannelEntity channelEntity = new ChannelEntity();
				channelEntity.setName(channelName);
				channelEntity.setType(channelType);
				channelEntity.setAttribute(channelAttribute);
				channelEntity.setCode(code);
				if (channelEntity.isAddChannel(type, attribute, code, this)) {
					DialogInterface.OnClickListener listener = new AddChannelClickListener(channelEntity,
							FuturesActivity.this);
					showTipDialog(null, "是否确定添加频道：" + channelEntity.getName() + "," + channelEntity.getType() + ","
							+ channelEntity.getAttribute() + "," + channelEntity.getCode(), listener, null);
				}
				break;
			case R.id.futures_name:
				DialogTool dialogName = new DialogTool(this, channelFirstLabel, futures_name_show);
				dialogName.setDialog();
				break;
			case R.id.futures_info:
				DialogTool dialogInfo = new DialogTool(this, ChannelListManager.getChannelSecondLable(), futures_info_show);
				dialogInfo.setDialog();
				break;
			default:
				break;
		}
	}

}
