package com.example.econonew.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.econonew.view.activity.FinanceApplication;
import com.example.econonew.R;
import com.example.econonew.entity.MsgItemEntity;
import com.example.econonew.resource.Constant;
import com.example.econonew.resource.DB_Information;

import java.util.List;

/**
 * MyListViewAdapter
 *
 * @author agnes 是显示信息的ListView的适配器 在MyFragment中引用
 */
public class MsgListViewAdapter extends BaseAdapter {

	public static boolean dataSurplus = true;

	private List<MsgItemEntity> msgItems;
	private LayoutInflater inflater;
	private ImageLoader mImageLoader;
	private String msgName;
	private Context mContext;

	public MsgListViewAdapter(Context context, List<MsgItemEntity> list, ListView listView, String msgName) {
		this.msgItems = list;
		inflater = LayoutInflater.from(context);
		this.mImageLoader = new ImageLoader(context, listView);
		this.msgName = msgName;
		mContext = context;
	}

	@Override // 得到listView的长度
	public int getCount() {
		return msgItems.size();
	}

	/**
	 * 停止加载数据
	 */
	public void cancelLoad() {
		if (mImageLoader != null) {
			mImageLoader.cacelAllTasks();
		}
	}

	@Override
	public Object getItem(int arg0) {
		return msgItems.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override // 根据getcount的数据逐一绘制
	public View getView(int arg0, View view, ViewGroup arg2) {
		if (msgItems.size() == 0) {
			return null;
		}
		ViewHolder holder = null;
		final MsgItemEntity item = msgItems.get(arg0);
		if (view == null) {
			view = inflater.inflate(R.layout.item_msg_list, null);
			holder = new ViewHolder();
			holder.titleTv = (TextView) view.findViewById(R.id.itemText);
			holder.titleIv = (ImageView) view.findViewById(R.id.itemImage);
			holder.isVipTv = (TextView) view.findViewById(R.id.vip_tv);
			holder.isLoveCb = (CheckBox) view.findViewById(R.id.item_is_love_cb);
			holder.shareTv = (TextView) view.findViewById(R.id.item_share_tv);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		initUi(holder, item);
		return view;
	}

	private void initUi(ViewHolder holder, final MsgItemEntity item) {
		holder.titleTv.setText(item.getMsgTitle());
		holder.titleIv.setTag(item.getImageTitleUrl());
		holder.titleIv.setImageResource(R.drawable.default_title);

		mImageLoader.showImageFromURL(holder.titleIv, item.getImageTitleUrl());
		holder.isLoveCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (item.isLove() == isChecked)
					return;
				item.setLove(isChecked);
				Toast.makeText(FinanceApplication.app, "设置成功", Toast.LENGTH_SHORT).show();
				DB_Information db = new DB_Information(FinanceApplication.app);
				db.upDateMsgContentIsLove(item.getMsgContentUrl(), Constant.getTableName(msgName), isChecked);
			}
		});
		holder.shareTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ShareTool.shareText(mContext, "我在Fiance_app中发现了这条消息，挺不错的，发送给你" + item.getMsgContentUrl());
			}
		});
		holder.isVipTv.setVisibility(item.isVip() ? View.VISIBLE : View.GONE);
		holder.isLoveCb.setChecked(item.isLove());

	}

	// 存放控件的viewHolder
	class ViewHolder {
		public TextView titleTv;
		public ImageView titleIv;
		public TextView isVipTv;
		public CheckBox isLoveCb;
		public TextView shareTv;
	}
}
