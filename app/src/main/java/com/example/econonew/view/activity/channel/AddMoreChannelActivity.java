package com.example.econonew.view.activity.channel;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.econonew.R;
import com.example.econonew.databinding.ActAddMoreChannelBinding;
import com.example.econonew.entity.ChannelEntity;
import com.example.econonew.presenter.AddMoreChannelPresenter;
import com.example.econonew.resource.Constant;
import com.example.econonew.view.activity.BaseActivity;
import com.example.econonew.view.activity.FinanceApplication;

import java.util.List;

/**
 * 添加多个频道的界面
 */
public class AddMoreChannelActivity extends BaseActivity<AddMoreChannelPresenter> {

	private ActAddMoreChannelBinding mBinding;
	private String tipMessage = "";// 添加多个频道过程中的提示信息

	@Override
	protected void initView(Bundle savedInstanceState) {
		mBinding = DataBindingUtil.setContentView(mContext, R.layout.act_add_more_channel);
		bindPresenter(new AddMoreChannelPresenter(this));
		mBinding.setPresenter(mPresenter);
		initActionBar(false , "添加多个频道", true);
		showSelectChannelCounts(0);
	}

	@Override
	protected void initDatas() {
		initAddedChannels();
		initNotAddChannels();
	}

	//初始化没有添加的频道的界面
	private void initNotAddChannels() {
		List<ChannelEntity> notAddChannelList = mPresenter.getNotAddChannels();
		for (ChannelEntity entity: notAddChannelList) {
			CheckBox notAddChannelCB = mPresenter.getChannelCheckBox(entity);
			mBinding.actAddMoreChannelNotaddLy.addView(notAddChannelCB);
		}
	}

	//初始化已经添加过的界面
	private void initAddedChannels() {
		List<ChannelEntity> entityList = mPresenter.getAddedChannelList();
		for (ChannelEntity entity: entityList) {
			TextView channelTv = mPresenter.getChannelTextView(entity);
			mBinding.actAddMoreChannelAddedLy.addView(channelTv);
		}
	}

	public void showSelectChannelCounts(int channelCounts) {
		String constStringTip = "当前选择的频道个数 :   ";
		SpannableString selectChannelsTipString = new SpannableString(constStringTip + channelCounts + " 个");
		//设置字体相对大小
		selectChannelsTipString.setSpan(new RelativeSizeSpan(2f), constStringTip.length(), selectChannelsTipString.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);	//设置字体类型为粗体
		selectChannelsTipString.setSpan(new StyleSpan(Typeface.BOLD), constStringTip.length(), selectChannelsTipString.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		//设置字体的前景色
		selectChannelsTipString.setSpan(new ForegroundColorSpan(Color.RED), constStringTip.length(), selectChannelsTipString.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		mBinding.actAddMoreChannelSelectCount.setText(selectChannelsTipString);
	}

	public void removeChannelView(int index) {
		mBinding.actAddMoreChannelNotaddLy.removeViewAt(index);
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
	public void updateProMessage(int addedCount, int max, String tip) {
		String title = "正在添加频道..." + addedCount + "/" + max;
		showProDialog(title);
		tipMessage += "\n" + tip;
		if (addedCount == max) {
			hintProDialog();
			showTipDialog("添加完成", tipMessage, null, null);
			tipMessage = "";
			FinanceApplication.getInstance().refreshUserData(Constant.user);
		}
	}
}
