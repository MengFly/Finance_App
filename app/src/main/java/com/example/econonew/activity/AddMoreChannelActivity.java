package com.example.econonew.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.econonew.R;
import com.example.econonew.customview.FlowLayout;
import com.example.econonew.entity.ChannelEntity;
import com.example.econonew.main.object.AllMessage;
import com.example.econonew.resource.Constant;
import com.example.econonew.server.JsonCast;
import com.example.econonew.server.NetClient;
import com.example.econonew.tools.ChannelListManager;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class AddMoreChannelActivity extends BaseActivity implements OnCheckedChangeListener, OnClickListener {

	private FlowLayout addedChannelLy;
	private FlowLayout notAddChannelLy;

	private TextView selectChannelCountTv;

	private Button okBtn;// 确认添加的按钮

	private List<ChannelEntity> mAddedChannelList;// 已经添加过的频道列表
	private List<ChannelEntity> mNotAddChannelList;// 没有添加过的频道
	private List<ChannelEntity> mWantAddChannelList;// 想要添加的频道列表

	String tipMessage = "";// 添加多个频道过程中的提示信息
	private int count = 0;// 已经添加的数据

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.act_add_more_channel);
		initActionBar("添加多个频道", true);
		addedChannelLy = (FlowLayout) findViewById(R.id.act_add_more_channel_added_ly);
		notAddChannelLy = (FlowLayout) findViewById(R.id.act_add_more_channel_notadd_ly);
		selectChannelCountTv = (TextView) findViewById(R.id.act_add_more_channel_select_count);
		okBtn = (Button) findViewById(R.id.act_add_channel_add_more_ok_btn);

	}

	@Override
	protected void initDatas() {
		okBtn.setOnClickListener(this);
		initChannelList();
		initAddedChannels();
		initNotAddChannels();
	}

	private void initChannelList() {
		mAddedChannelList = AllMessage.getInstance("自定义").getChannelList();
		mNotAddChannelList = new ArrayList<>();
		mWantAddChannelList = new ArrayList<>();
	}

	// 初始化未添加过的频道
	private void initNotAddChannels() {
		List<ChannelEntity> totalChannels = getTotalChannel();
		for (ChannelEntity entity : totalChannels) {
			if (!isAdded(entity)) {// 如果没有添加过，那么就添加到没有添加过的频道里面
				mNotAddChannelList.add(entity);
				CheckBox checkBox = getChannelCheckBox(entity);
				checkBox.setTag(entity);
				checkBox.setOnCheckedChangeListener(this);
				notAddChannelLy.addView(checkBox);
			}
		}
	}

	private CheckBox getChannelCheckBox(ChannelEntity entity) {
		CheckBox checkBox = new CheckBox(mContext);
		checkBox.setPadding(10, 10, 10, 10);
		MarginLayoutParams params = new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT,
				MarginLayoutParams.WRAP_CONTENT);
		params.leftMargin = 8;
		params.topMargin = 3;
		params.rightMargin = 3;
		params.bottomMargin = 3;
		checkBox.setLayoutParams(params);
		checkBox.setBackgroundResource(R.drawable.show_channel_back);
		checkBox.setButtonDrawable(R.drawable.none);
		checkBox.setText(entity.getName() + " " + entity.getType() + " " + entity.getAttribute());
		return checkBox;
	}

	// 获取全部的频道
	private List<ChannelEntity> getTotalChannel() {
		List<ChannelEntity> totalChannelList = new ArrayList<>();
		for (String channelName : Constant.publicItemNames) {
			String[] channelFistLabels = ChannelListManager.getChannelFirstLable(channelName);
			for (String channelFistLabel : channelFistLabels) {
				String[] secondLabels = ChannelListManager.getChannelSecondLable();
				for (String secondLabel : secondLabels) {
					ChannelEntity entity = new ChannelEntity();
					entity.setName(channelName);
					entity.setType(channelFistLabel);
					entity.setAttribute(secondLabel);
					entity.setCode(null);
					totalChannelList.add(entity);
				}
			}
		}
		return totalChannelList;
	}

	// 判断频道是否添加过了
	private boolean isAdded(ChannelEntity channel) {
		for (ChannelEntity addedChannel : mAddedChannelList) {
			if (addedChannel.equals(channel)) {
				return true;
			}
		}
		return false;
	}

	// 初始化已经添加过的频道
	private void initAddedChannels() {
		for (ChannelEntity addedChannel : mAddedChannelList) {
			TextView channelTv = getChannelTextView(addedChannel);
			addedChannelLy.addView(channelTv);
		}
	}

	// 根据频道信息获取显示频道信息的TextView
	private TextView getChannelTextView(ChannelEntity addedChannel) {
		TextView channelTv = new TextView(mContext);
		channelTv.setPadding(10, 10, 10, 10);
		MarginLayoutParams params = new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT,
				MarginLayoutParams.WRAP_CONTENT);
		params.leftMargin = 3;
		params.topMargin = 3;
		params.rightMargin = 3;
		params.bottomMargin = 3;
		channelTv.setLayoutParams(params);
		channelTv.setBackgroundResource(R.drawable.tv_back);
		channelTv.setTextColor(getResources().getColor(R.color.text_white));
		channelTv.setTextSize(12.0f);
		String code = addedChannel.getCode() == null ? "" : addedChannel.getCode();
		channelTv.setText(
				addedChannel.getName() + " " + addedChannel.getType() + " " + addedChannel.getAttribute() + " " + code);
		return channelTv;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		ChannelEntity entity = (ChannelEntity) buttonView.getTag();
		if (isChecked) {
			if (!mWantAddChannelList.contains(entity)) {
				mWantAddChannelList.add(entity);
			}
		} else {
			if (mWantAddChannelList.contains(entity)) {
				mWantAddChannelList.remove(entity);
			}
		}
		selectChannelCountTv.setText("" + mWantAddChannelList.size());
	}

	@Override
	public void onClick(View v) {
		if (mWantAddChannelList.isEmpty()) {
			showToast("当前还没有选择任何的频道");
		} else {
			openThreadToAddChannel();// 开启线程，去添加频道
		}
	}

	private void openThreadToAddChannel() {
		final List<ChannelEntity> addList = new ArrayList<>(mWantAddChannelList);
		mWantAddChannelList.clear();
		tipMessage = "";
		count = 0;
		selectChannelCountTv.setText("0");
		new Thread() {
			public void run() {
				for (int i = 0; i < addList.size(); i++) {
					final int max = addList.size();
					final ChannelEntity entity = addList.get(i);
					final String url = Constant.URL + "/" + Constant.OPERATION_SETCHNL + "?" + encodeParams(entity);
					NetClient.getInstance().excuteGetForString(mContext, url, new NetClient.OnResultListener() {

						@Override
						public void onSuccess(String response) {
							count++;
							notAddChannelLy.removeViewAt(mNotAddChannelList.indexOf(entity));
							mNotAddChannelList.remove(mNotAddChannelList.indexOf(entity));
							JSONObject obj = JsonCast.getJsonObject(response);
							if (obj != null) {
								if ("success".equals(JsonCast.getString(obj, "status"))) {
									int id = JsonCast.getInt(obj, "result");
									entity.setId(id);
									AllMessage.getInstance("自定义").setChannels(Arrays.asList(entity), true, true);
									updateProMessage(count, max, entity + "添加成功");
								} else {
									updateProMessage(count, max,
											"频道添加失败：" + entity + JsonCast.getString(obj, "result"));
								}
							} else {
								updateProMessage(count, max, "频道添加失败" + entity);
							}
						}

						@Override
						public void onError(VolleyError error) {
							super.onError(error);
							count++;
							updateProMessage(count, max, "频道添加失败：" + entity + error.getMessage());
						}
					});
				}
			};
		}.start();

	}

	/**
	 * 更新提示框的信息
	 *
	 * @param addedCount
	 *            正在添加的频道是第几个频道
	 * @param max
	 *            总共有几个频道
	 * @param tip
	 *            要显示的提示信息
	 */
	private void updateProMessage(int addedCount, int max, String tip) {
		String title = "正在添加频道..." + addedCount + "/" + max;
		showProDialog(title);
		tipMessage += "\n" + tip;
		if (addedCount == max) {
			hintProDialog();
			showTipDialog("添加完成", tipMessage, null, null);
		}
	}

	// 对要传输的URL进行转码
	private String encodeParams(ChannelEntity entity) {
		StringBuilder msg = new StringBuilder();
		try {
			msg.append("phone=");
			msg.append(Constant.user.getName());
			msg.append("&typeName=");
			msg.append(URLEncoder.encode(entity.getName(), "utf-8"));
			msg.append("&domainName=");
			msg.append(URLEncoder.encode(entity.getType(), "utf-8"));
			msg.append("&stairName=");
			msg.append(URLEncoder.encode(entity.getAttribute(), "utf-8"));
			msg.append("&stockCode=");
			msg.append(URLEncoder.encode(entity.getCode(), "utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg.toString();
	}

}
