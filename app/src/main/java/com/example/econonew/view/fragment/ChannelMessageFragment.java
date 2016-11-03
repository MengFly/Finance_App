package com.example.econonew.view.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.econonew.R;
import com.example.econonew.entity.ChannelEntity;
import com.example.econonew.resource.Constant;
import com.example.econonew.resource.msg.ChannelMessage;
import com.example.econonew.server.NetClient;
import com.example.econonew.server.URLManager;
import com.example.econonew.tools.adapter.ChannelListViewAdapter;
import com.example.econonew.view.activity.BaseActivity;
import com.example.econonew.view.activity.FinanceApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于显示用户频道的Fragment
 * Created by mengfei on 2016/10/2.
 */

public class ChannelMessageFragment extends MsgBaseFragment<ChannelMessage, ChannelEntity> implements AdapterView.OnItemLongClickListener {


    //用于存储已经实例化的Fragment，下次再需要的时候可以直接从Map里面进行获取，提高效率
    private static Map<String, ChannelMessageFragment> fragmentManager = new HashMap<>();

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private ListView channelLv;
    private TextView noMsgTip;
    private SwipeRefreshLayout refreshLayout;

    private String fragmentName;
    private List<ChannelEntity> channelList = new ArrayList<>();
    private ChannelListViewAdapter channelAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        bindMessage();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_main_view_page, container, false);
        channelLv = (ListView) view.findViewById(R.id.msg_listview);
        Bundle arguments = getArguments();
        if (arguments != null) {
            fragmentName = arguments.getString("name");
        }
        if (savedInstanceState != null) {
            fragmentName = savedInstanceState.getString("name");
        }
        bindMessage();
        initView(view);
        return view;
    }

    //刷新界面数据进行显示
    private void refreshDatas() {
        if (channelAdapter != null) {
            channelAdapter.notifyDataSetChanged();
        }
    }

    // 初始化界面的控件
    private void initView(View view) {

        noMsgTip = (TextView) view.findViewById(R.id.msg_no_tip_tv);
        channelAdapter = new ChannelListViewAdapter(getActivity(), channelList);
        channelLv.setAdapter(channelAdapter);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.msg_fresh);
        refreshLayout.setColorSchemeResources(R.color.swipe_color_1, R.color.swipe_color_2, R.color.swipe_color_3);
        initListener();
    }

    private void initListener() {
        channelLv.setOnItemLongClickListener(this);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                FinanceApplication.getInstance().refreshUserData(Constant.user);
            }
        });
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
            bindMsgManager(ChannelMessage.getInstance(fragmentName));
            setList(msgManager.getMessage());
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final ChannelEntity entity = channelList.get(position);
        ((BaseActivity) getActivity()).showTipDialog("提示！",
                "是否要删除频道" + entity.toString(), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteChannel(entity);
                    }
                }, null);
        return false;
    }

    /**
     * 删除频道
     */
    protected void deleteChannel(final ChannelEntity entity) {
        if (Constant.user != null) {
            showProDialog();
            deleteChannelThread(entity);// 开启一个删除频道的线程
            msgManager.removeMsg(entity);
        } else {
            Toast.makeText(getContext(), "当前还没有登录，不能进行删除频道的操作", Toast.LENGTH_SHORT).show();
        }
    }

    //请求删除频道的线程
    private void deleteChannelThread(final ChannelEntity entity) {
        final String url = URLManager.getDeleteChannelURL(Constant.user.getName(), entity);
        final NetClient.OnResultListener listener = new NetClient.OnResultListener() {

            @Override
            public void onSuccess(String response) {
                if (channelAdapter != null) {
                    showToast("频道删除成功");
                }
                hintProDialog();
                stopFresh();
            }

            @Override
            public void onError(VolleyError error) {
                super.onError(error);
                hintProDialog();
                stopFresh();
            }
        };
        new Thread() {
            @Override
            public void run() {
                NetClient.getInstance().executeGetForString(getContext(), url, listener);
            }
        }.start();
    }

    /**
     * 获取Fragment实例
     */
    public static ChannelMessageFragment newInstance(String fragmentName) {
        if (fragmentManager.containsKey(fragmentName)) {
            return fragmentManager.get(fragmentName);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("name", fragmentName);
            ChannelMessageFragment fragment = new ChannelMessageFragment();
            fragment.setArguments(bundle);
            new ChannelMessage(fragment, fragmentName);
            fragmentManager.put(fragmentName, fragment);
            return fragment;
        }
    }

    private void showProDialog() {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).showProDialog();
        }
    }

    private void hintProDialog() {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).hintProDialog();
        }
    }

    private void showTipDialog(String message) {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).showTipDialog(null, message, null, null);
        }
    }

    private void showToast(String toast) {
        Toast.makeText(FinanceApplication.getInstance(), toast,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * 停止刷新
     */
    public void stopFresh() {
        hintProDialog();
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
        }
        if (channelList.isEmpty() && noMsgTip != null) {
            noMsgTip.setVisibility(View.VISIBLE);
        } else if (noMsgTip != null) {
            noMsgTip.setVisibility(View.GONE);
        }
    }


    @Override
    public void setList(List<ChannelEntity> list) {
        this.channelList.clear();
        this.channelList.addAll(list);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                refreshDatas();//刷新界面
                stopFresh();//停止刷新提示
            }
        });
    }
}
