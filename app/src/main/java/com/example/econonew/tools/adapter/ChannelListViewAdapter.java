package com.example.econonew.tools.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.econonew.R;
import com.example.econonew.entity.ChannelEntity;

import java.util.List;

/**
 * MyListViewAdapter
 *
 * @author agnes 是显示信息的ListView的适配器 在MyFragment中引用
 */
public class ChannelListViewAdapter extends BaseAdapter {

	private List<ChannelEntity> msgItems;
	private LayoutInflater inflater;

	public ChannelListViewAdapter(Context context, List<ChannelEntity> list) {
		this.msgItems = list;
		inflater = LayoutInflater.from(context);
	}

	@Override // 得到listView的长度
	public int getCount() {
		return msgItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		return msgItems.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override // 根据getCount的数据逐一绘制
	public View getView(int arg0, View view, ViewGroup arg2) {
		ViewHolder holder;
		ChannelEntity item;
		if (msgItems.size() == 0) {
			return null;
		}
		item = msgItems.get(arg0);
		if (view == null) {
			view = inflater.inflate(R.layout.item_channel_list, null);
			holder = new ViewHolder();
			holder.parentLy = (LinearLayout) view.findViewById(R.id.item_channel_ly);
			holder.channelNameTv = (TextView) view.findViewById(R.id.channel_name);
			holder.channelTypeTv = (TextView) view.findViewById(R.id.channel_type);
			holder.channelAttributeTv = (TextView) view.findViewById(R.id.channel_attribute);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		initUi(holder, item);
		return view;
	}

	private void initUi(ViewHolder holder, final ChannelEntity item) {
		holder.channelNameTv.setText(item.getName());
		holder.channelTypeTv.setText(item.getType());
		holder.channelAttributeTv.setText(item.getAttribute());
		switch (item.getName()) {
			case "股票":
				holder.parentLy.setBackgroundResource(R.color.item_back_1);
				break;
			case "期货":
				holder.parentLy.setBackgroundResource(R.color.item_back_2);
				break;
			case "理财":
				holder.parentLy.setBackgroundResource(R.color.item_back_3);
				break;
			case "基金":
				holder.parentLy.setBackgroundResource(R.color.item_back_4);
				break;
			case "外汇":
				holder.parentLy.setBackgroundResource(R.color.item_back_5);
				break;
			default:
				break;
		}
	}

	// 存放控件的viewHolder
 	private class ViewHolder {
		LinearLayout parentLy;
		TextView channelNameTv;
		TextView channelTypeTv;
		TextView channelAttributeTv;
	}
}
