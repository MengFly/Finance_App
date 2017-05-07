package com.example.econonew.view.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.example.econonew.R;
import com.example.econonew.databinding.FragMainViewPageBinding;
import com.example.econonew.entity.MsgItemEntity;
import com.example.econonew.resource.msg.MainMessage;
import com.example.econonew.tools.adapter.MsgListViewAdapter;
import com.example.econonew.view.activity.BaseActivity;
import com.example.econonew.view.activity.FinanceApplication;
import com.example.econonew.view.activity.main.MsgContentActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于显示公共消息的Fragment
 * Created by mengfei on 2016/10/2.
 */

public class MainMessageFragment extends MsgBaseFragment<MainMessage, MsgItemEntity> implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    //用于存储已经实例化的Fragment，下次再需要的时候可以直接从Map里面进行获取，提高效率
    private static Map<String, MainMessageFragment> fragmentManager = new HashMap<>();

    private FragMainViewPageBinding mBinding;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private String fragmentName;
    private MsgListViewAdapter msgAdapter;
    private List<MsgItemEntity> msgList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        bindMessage();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.frag_main_view_page, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            fragmentName = arguments.getString("name");
        }
        if (savedInstanceState != null) {
            fragmentName = savedInstanceState.getString("name");
        }
        if (msgList == null) {
            bindMessage();
        }
        initView();
        stopFresh();
        return mBinding.getRoot();
    }

    //刷新界面数据进行显示
    private void refreshDatas() {
        if (msgAdapter != null) {
            msgAdapter.notifyDataSetChanged();
        }
    }

    public void showFreshView() {
        if (mBinding != null && mBinding.msgFresh != null) {
            mBinding.msgFresh.setRefreshing(true);
        }
    }

    // 初始化界面的控件
    private void initView() {
        msgAdapter = new MsgListViewAdapter(getActivity(), msgList, mBinding.msgListview, fragmentName);
        mBinding.msgListview.setAdapter(msgAdapter);
        mBinding.msgListview.setEmptyView(mBinding.msgNoTipTv);
        mBinding.msgFresh.setColorSchemeResources(R.color.swipe_color_1, R.color.swipe_color_2, R.color.swipe_color_3);
        initListener();
    }

    private void initListener() {
        mBinding.msgListview.setOnItemClickListener(this);
        mBinding.msgListview.setOnScrollListener(this);
        mBinding.msgFresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                FinanceApplication.getInstance().refreshPublicData();
            }
        });
    }

    //从本地数据库加载数据
    private void loadDatasFromDatabases() {
        //TODO 还没有实现的方法,从本地数据库加载历史信息
    }

    /**
     * 为列表项设置内容，或者用于刷新
     *
     * @param list 列表项的内容
     */
    public void setList(List<MsgItemEntity> list) {
        this.msgList.clear();
        this.msgList.addAll(list);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                refreshDatas();//刷新界面
                stopFresh();//停止刷新提示
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("name", fragmentName);
        super.onSaveInstanceState(outState);
    }

    private void bindMessage() {
        if (msgManager == null) {
            bindMsgManager(MainMessage.getInstance(fragmentName));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MsgItemEntity item = msgList.get(position);
        Intent intent = new Intent(getActivity(), MsgContentActivity.class);
        intent.putExtra("msgContext", item.getMsgContent());
        intent.putExtra("msgTitle", item.getMsgTitle());
        intent.putExtra("msgContentUrl", item.getMsgContentUrl());
        getActivity().startActivity(intent);
    }

    /**
     * 获取Fragment实例
     */
    public static MainMessageFragment newInstance(String fragmentName) {
        if (fragmentManager.containsKey(fragmentName)) {
            return fragmentManager.get(fragmentName);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("name", fragmentName);
            MainMessageFragment fragment = new MainMessageFragment();
            fragment.setArguments(bundle);
            new MainMessage(fragment, fragmentName);
            fragmentManager.put(fragmentName, fragment);
            return fragment;
        }
    }

    private void hintProDialog() {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).hintProDialog();
        }
    }

    /**
     * 停止刷新
     */
    public void stopFresh() {
        hintProDialog();
        if (mBinding != null && mBinding.msgFresh != null) {
            mBinding.msgFresh.setRefreshing(false);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount) {
            loadDatasFromDatabases();//从本地数据库加载数据
        }
    }

}
