package com.example.econonew.presenter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.econonew.R;
import com.example.econonew.entity.ChannelEntity;
import com.example.econonew.resource.Constant;
import com.example.econonew.resource.msg.ChannelMessage;
import com.example.econonew.server.NetClient;
import com.example.econonew.server.URLManager;
import com.example.econonew.server.json.JsonCast;
import com.example.econonew.tools.ChannelListManager;
import com.example.econonew.view.activity.channel.AddMoreChannelActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 添加多个频道的Presenter
 * Created by mengfei on 2017/1/14.
 */
public class AddMoreChannelPresenter extends BasePresenter<AddMoreChannelActivity> {

    private List<ChannelEntity> mAddedChannelList;// 已经添加过的频道列表
    private List<ChannelEntity> mNotAddChannelList;// 没有添加过的频道
    private List<ChannelEntity> mWantAddChannelList = new ArrayList<>();// 想要添加的频道列表
    private int wantAddChannelCount = 0;//想要添加的频道的个数

    public AddMoreChannelPresenter(AddMoreChannelActivity activity) {
        super(activity);
    }

    // 初始化未添加过的频道
    public List<ChannelEntity> getNotAddChannels() {
        mNotAddChannelList = new ArrayList<>();
        List<ChannelEntity> totalChannels = getTotalChannel();
        for (ChannelEntity entity : totalChannels) {
            if (!isAdded(entity)) {// 如果没有添加过，那么就添加到没有添加过的频道里面
                mNotAddChannelList.add(entity);
            }
        }
        return mNotAddChannelList;
    }

    //获取已经添加过的频道的列表
    public List<ChannelEntity> getAddedChannelList() {
        ChannelMessage message = ChannelMessage.getInstance("自定义");
        if (message != null) {
            mAddedChannelList = new ArrayList<>(message.getMessage());
        } else {
            mAddedChannelList = Collections.emptyList();
        }
        return mAddedChannelList;
    }

    // 获取全部的频道
    private List<ChannelEntity> getTotalChannel() {
        List<ChannelEntity> totalChannelList = new ArrayList<>();
        for (String channelName : Constant.publicItemNames) {
            String[] channelFistLabels = ChannelListManager.getChannelFirstLabel(channelName);
            for (String channelFistLabel : channelFistLabels) {
                String[] secondLabels = ChannelListManager.getChannelSecondLabel();
                for (String secondLabel : secondLabels) {
                    ChannelEntity entity = new ChannelEntity();
                    entity.setName(channelName);
                    entity.setType(channelFistLabel);
                    entity.setAttribute(secondLabel);
                    totalChannelList.add(entity);
                }
            }
        }
        return totalChannelList;
    }

    // 判断频道是否添加过了
    private boolean isAdded(ChannelEntity channel) {
        for (ChannelEntity addedChannel : mAddedChannelList) {
            if(addedChannel.equals(channel)) {
                return true;
            }
        }
        return false;
    }

    // 根据频道信息获取显示频道信息的TextView
    public TextView getChannelTextView(ChannelEntity addedChannel) {
        TextView channelTv = new TextView(mActivity);
        channelTv.setPadding(10, 10, 10, 10);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        params.leftMargin = params.topMargin = params.rightMargin = params.bottomMargin = 5;
        channelTv.setLayoutParams(params);
        channelTv.setBackgroundResource(R.drawable.tv_back);
        channelTv.setTextColor(mActivity.getResources().getColor(R.color.text_white));
        channelTv.setTextSize(12.0f);
        channelTv.setText(addedChannel.toString());
        return channelTv;
    }
    private void openThreadToAddChannel() {
        final List<ChannelEntity> addList = new ArrayList<>(mWantAddChannelList);
        mWantAddChannelList.clear();
        wantAddChannelCount = 0;
        mActivity.showSelectChannelCounts(0);
        new Thread() {
            public void run() {
                for (int i = 0; i < addList.size(); i++) {
                    final int max = addList.size();
                    final ChannelEntity entity = addList.get(i);
                    final String url = URLManager.getSetChannelURL(Constant.user.getName(), entity);
                    NetClient.getInstance().executeGetForString(mActivity, url, new NetClient.OnResultListener() {

                        @Override
                        public void onSuccess(String response) {
                            wantAddChannelCount++;
                            mActivity.removeChannelView(mNotAddChannelList.indexOf(entity));
                            mNotAddChannelList.remove(mNotAddChannelList.indexOf(entity));
                            JSONObject obj = JsonCast.getJsonObject(response);
                            if (obj != null) {
                                if ("success".equals(JsonCast.getString(obj, "status"))) {
                                    int id = JsonCast.getInt(obj, "result");
                                    entity.setId(id);
                                    entity.setUserName(Constant.user.getName());
                                    ChannelMessage.getInstance("自定义").setMessage(Arrays.asList(entity), true, true);
                                    mActivity.updateProMessage(wantAddChannelCount, max, entity + "添加成功");
                                } else {
                                    mActivity.updateProMessage(wantAddChannelCount, max, "频道添加失败：" + entity + JsonCast.getString(obj, "result"));
                                }
                            } else {
                                mActivity.updateProMessage(wantAddChannelCount, max, "频道添加失败" + entity);
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            super.onError(error);
                            wantAddChannelCount++;
                            mActivity.updateProMessage(wantAddChannelCount, max, "频道添加失败：" + entity + error.getMessage());
                        }
                    });
                }
            }
        }.start();

    }

    public CheckBox getChannelCheckBox(ChannelEntity entity) {
        CheckBox checkBox = new CheckBox(mActivity);
        checkBox.setPadding(10, 10, 10, 10);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        params.leftMargin = params.topMargin = params.rightMargin = params.bottomMargin = 8;
        checkBox.setLayoutParams(params);
        checkBox.setBackgroundResource(R.drawable.show_channel_back);
        checkBox.setButtonDrawable(R.drawable.none);
        checkBox.setText(entity.toString());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBoxChangeListener(buttonView, isChecked);
            }
        });
        return checkBox;
    }

    private void checkBoxChangeListener(CompoundButton buttonView, boolean isChecked) {
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
        mActivity.showSelectChannelCounts(mWantAddChannelList.size());
    }

    public void okBtnClick() {
        if (mWantAddChannelList.isEmpty()) {
            mActivity.showToast("当前还没有选择任何的频道");
        } else {
            openThreadToAddChannel();// 开启线程，去添加频道
        }
    }
}
