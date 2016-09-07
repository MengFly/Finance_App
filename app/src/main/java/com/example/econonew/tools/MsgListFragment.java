package com.example.econonew.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.econonew.R;
import com.example.econonew.activity.BaseActivity;
import com.example.econonew.activity.FinanceApplication;
import com.example.econonew.activity.MsgContentActivity;
import com.example.econonew.entity.ChannelEntity;
import com.example.econonew.entity.MsgItemEntity;
import com.example.econonew.main.object.AllMessage;
import com.example.econonew.resource.Constant;
import com.example.econonew.server.JsonCast;
import com.example.econonew.server.NetClient;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * MyFragment
 *
 * @author agnes 用来创建viewpager的Fragment 在MyViewPager中使用
 */
public class MsgListFragment extends Fragment implements OnItemClickListener,
		OnItemLongClickListener {

	private static Map<String, MsgListFragment> fragmentManager = new HashMap<>();

	private String fragmentName;
	private AllMessage mMessage;

	private ListView msgList_lv;
	private MsgListViewAdapter msgAdapter;
	private List<MsgItemEntity> msgList = new ArrayList<MsgItemEntity>();

	private List<ChannelEntity> channelList = new ArrayList<ChannelEntity>();
	private ChannelListViewAdapter channelAdapter;

	// 刷新视图
	private SwipeRefreshLayout refreshLayout;

	private TextView noMsgTip;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == FinanceApplication.HANDLER_STATE_OK) {
				if (fragmentName != null) {
					if (fragmentName.equals("自定义")) {
						channelAdapter.notifyDataSetChanged();
					} else {
						msgAdapter.notifyDataSetChanged();
					}
				}
				stopFreash();
				hintProDialog();
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		bindMessage();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_main_view_page, container,
				false);
		if (savedInstanceState != null) {
			fragmentName = savedInstanceState.getString("name");
		}
		if (msgList == null && channelList == null) {
			bindMessage();
		}
		initView(view);
		return view;
	}

	// 初始化界面的控件
	private void initView(View view) {

		noMsgTip = (TextView) view.findViewById(R.id.msg_no_tip_tv);

		msgList_lv = (ListView) view.findViewById(R.id.msg_listview);
		if (fragmentName.equals("自定义")) {
			channelAdapter = new ChannelListViewAdapter(getActivity(),
					channelList);
			msgList_lv.setAdapter(channelAdapter);
		} else {
			msgAdapter = new MsgListViewAdapter(getActivity(), msgList,
					msgList_lv, fragmentName);
			msgList_lv.setAdapter(msgAdapter); // 给listView添加适配器

		}
		refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.msg_fresh);
		refreshLayout.setColorSchemeResources(R.color.swipe_color_1,
				R.color.swipe_color_2, R.color.swipe_color_3);
		refreshLayout.setSize(SwipeRefreshLayout.LARGE);
		refreshLayout.setProgressViewEndTarget(true, 100);

		initListener();
	}

	private void initListener() {
		if (fragmentName.equals("自定义")) {
			msgList_lv.setOnItemLongClickListener(this);
		} else {
			Log.v("testUtils", "setOnItemClickListener");
			msgList_lv.setOnItemClickListener(this);
		}

		refreshLayout
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

					@Override
					public void onRefresh() {
						if (fragmentName.equals("自定义")) {
							FinanceApplication.getInstance().refreshUserData(
									Constant.user);
						} else {
							FinanceApplication.getInstance().refreshPublicData();
						}
					}
				});

	}

	/**
	 * 为列表项设置内容，或者用于刷新
	 *
	 * @param list
	 *            列表项的内容
	 */
	public void setList(final List<MsgItemEntity> list) {
		this.msgList.clear();
		this.msgList.addAll(list);
		handler.sendEmptyMessage(FinanceApplication.HANDLER_STATE_OK);
	}

	public void setChannelList(List<ChannelEntity> list) {
		this.channelList.clear();
		this.channelList.addAll(list);
		handler.sendEmptyMessage(FinanceApplication.HANDLER_STATE_OK);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("name", fragmentName);
		super.onSaveInstanceState(outState);
	}

	private void bindMessage() {
		Bundle bundle = getArguments();
		if (bundle != null) {
			fragmentName = bundle.getString("name");
			Log.v("test", fragmentName);
			mMessage = AllMessage.getInstance(fragmentName);
			if (fragmentName.equals("自定义")) {
				setChannelList(mMessage.getChannelList());
			} else {
				setList(mMessage.getMsgList());
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		if (!msgList.isEmpty()) {
			MsgItemEntity item = msgList.get(position);
			Intent intent = new Intent(getActivity(), MsgContentActivity.class);
			intent.putExtra("msgTitle", item.getMsgTitle());
			intent.putExtra("msgContentUrl", item.getMsgContentUrl());
			getActivity().startActivity(intent);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
								   final int position, long id) {
		final ChannelEntity entity = channelList.get(position);
		((BaseActivity) getActivity()).showTipDialog("提示！",
				"是否要删除频道" + entity.getName() + ":" + entity.getType() + ":"
						+ entity.getAttribute(), new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						deleteChannel(entity);
					}
				}, null);

		return false;
	}

	/**
	 * 删除频道
	 *
	 * @param entity
	 */
	protected void deleteChannel(final ChannelEntity entity) {
		if (Constant.user != null) {
			showproDialog();
			deleteChannelThread(entity);// 开启一个删除频道的线程
		} else {
			Toast.makeText(getContext(), "当前还没有登录，不能进行删除频道的操作",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void deleteChannelThread(final ChannelEntity entity) {
		final String url = Constant.URL + "/"
				+ Constant.OPERATION_DELETE_CHANNEL + "?phone="
				+ Constant.user.getName() + "&channelId=" + entity.getId();
		final NetClient.OnResultListener listener = new NetClient.OnResultListener() {

			@Override
			public void onSuccess(String response) {
				JSONObject obj = JsonCast.getJsonObject(response);
				if (obj != null) {
					String result = JsonCast.getString(obj, "result");
					if (result != null) {
						if ("success".equals(result)) {
							if (channelAdapter != null) {
								mMessage.removeChannel(entity);
								showTipDialog("频道删除成功");
							}
						} else {
							showTipDialog("删除频道失败:" + result);
						}
					} else {
						showToast("删除频道失败");
					}
				} else {
					showToast("删除频道失败");
				}
				hintProDialog();
				stopFreash();
			}
			@Override
			public void onError(VolleyError error) {
				super.onError(error);
				hintProDialog();
				stopFreash();
			}
		};
		new Thread() {
			@Override
			public void run() {
				NetClient.getInstance().excuteGetForString(getContext(), url, listener);
			}
		}.start();
	}

	/**
	 * 获取Fragment实例
	 *
	 * @param fragmentName
	 * @return
	 */
	public static MsgListFragment newInstance(Context context,
											  String fragmentName) {
		if (fragmentManager.containsKey(fragmentName)) {
			return fragmentManager.get(fragmentName);
		} else {
			Bundle bundle = new Bundle();
			bundle.putString("name", fragmentName);
			Log.v("testLog", fragmentName);
			MsgListFragment fragment = new MsgListFragment();
			fragment.setArguments(bundle);
			new AllMessage(context, fragment, fragmentName);
			fragmentManager.put(fragmentName, fragment);
			return fragment;
		}
	}

	private void showproDialog() {
		if (((BaseActivity) getActivity()) != null) {
			((BaseActivity) getActivity()).showProDialog();
		}
	}

	private void hintProDialog() {
		if (((BaseActivity) getActivity()) != null) {
			((BaseActivity) getActivity()).hintProDialog();
		}
	}

	private void showTipDialog(String message) {
		if (((BaseActivity) getActivity()) != null) {
			((BaseActivity) getActivity()).showTipDialog(null, message, null,
					null);
		}
	}

	private void showToast(String toast) {
		Toast.makeText(FinanceApplication.getInstance(), toast,
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * 停止刷新
	 */
	public void stopFreash() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				if (refreshLayout != null) {
					refreshLayout.setRefreshing(false);
				}
				if (msgList.isEmpty() && channelList.isEmpty()
						&& noMsgTip != null) {
					noMsgTip.setVisibility(View.VISIBLE);
				} else if (noMsgTip != null) {
					noMsgTip.setVisibility(View.GONE);
				}

			}
		});

	}

}
