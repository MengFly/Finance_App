package com.example.econonew.presenter;

import com.example.econonew.resource.Constant;
import com.example.econonew.resource.msg.MainMessage;
import com.example.econonew.tools.ShareTool;
import com.example.econonew.tools.Voice;
import com.example.econonew.view.activity.FinanceApplication;
import com.example.econonew.view.activity.User.UserActivity;
import com.example.econonew.view.activity.channel.ChannelAddActivity;
import com.example.econonew.view.activity.main.MainActivity;

/**
 * MainActivity界面的业务实现类
 * Created by mengfei on 2017/1/13.
 */
public class MainPresenter extends BasePresenter<MainActivity> {

    public MainPresenter(MainActivity activity) {
        super(activity);
    }

    //主界面的添加频道的按钮的点击方法
    public void onAddChannelBtnClick() {
        if (Constant.user == null) {
            mActivity.showNoLoginDialog();//显示没有登陆的提示框
        } else {
            mActivity.openOtherActivity(ChannelAddActivity.class, false);
        }
    }

    //主界面语音朗读方法的点击方法
    public void onVoiceBtnClick() {
        setRead(Constant.read_tab);
    }

    //主界用户按钮的点击方法
    public void onUserBtnClick() {
        mActivity.openOtherActivity(UserActivity.class, false);
    }

    //主界分享按钮的点击方法
    public void onShareImageBtnClick() {
        ShareTool.shareFiles(mActivity, FinanceApplication.getInstance().getApplicationFile());
    }

    /**
     * 对消息进行阅读
     *
     * @param tab 要从哪一个tab开始阅读
     */
    private void setRead(int tab) {
        Voice voice = Voice.getInstance();
        if (tab < Constant.publicItemNames.length && voice.getReadStat() == Voice.VOICE_STAT_NO_READ) {
            for (int i = tab; i < Constant.publicItemNames.length; i++) {
                MainMessage message = MainMessage.getInstance(Constant.publicItemNames[i]);
                if (message != null) {
                    voice.setList(message.getMessage());//设置要进行阅读的列表
                }
            }
        }
        voice.read();
    }
}
