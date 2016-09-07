package com.example.econonew.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 增加一个Context属性
 增加showDialog方法，将Dialog显示放在showDialog方法里面
 先判断Activity是否已经销毁，如果销毁那么这个Dialog就不显示
 */

/**
 * DialogTool
 *
 * @author lenovo-PC 此类是一个工具类，主要用来构造对话框然后用户选择
 */
public class DialogTool implements DialogInterface.OnClickListener {
	private TextView text;
	private String[] str;
	private int index = 0;
	private String chooseItem;
	private AlertDialog.Builder builder;
	private Context mContext;

	/**
	 * 创建供选择的对话框
	 *
	 * @param context
	 * @param list
	 *            选择的各项
	 * @param text
	 *            显示选择哪一项的TextView
	 */
	public DialogTool(Context context, ArrayList<String> list, TextView text) {
		this.text = text;
		this.mContext = context;
		str = new String[list.size()];
		builder = new AlertDialog.Builder(context);
		builder.setTitle("请选择");
		builder.setSingleChoiceItems(str, 0, this);
		builder.setPositiveButton("确定", this);
		builder.setNegativeButton("取消", null);
		showDialog();
	}

	public DialogTool(Context context, String[] str, final TextView text) {
		this.text = text;
		this.mContext = context;
		this.str = str;
		builder = new AlertDialog.Builder(context);
	}

	public DialogTool(Context context) {
		this.mContext = context;
		builder = new AlertDialog.Builder(context);
	}

	/**
	 * 设置提示信息
	 *
	 * @param tip
	 *            提示信息
	 */
	public void setNegativeTip(String tip) {
		builder.setTitle("提示信息");
		builder.setMessage(tip);
		builder.setNegativeButton("取消", null);
		showDialog();
	}

	/**
	 * 设置只带有确定按钮的Dialog
	 *
	 * @param tip
	 *            提示信息
	 * @param listener
	 *            监听器
	 */
	public void setPositiveTip(String tip, OnClickListener listener) {
		builder.setTitle("提示信息");
		builder.setMessage(tip);
		builder.setPositiveButton("确定", listener);
		showDialog();
	}

	/**
	 * 创建带有确定和取消按钮的Dialog
	 * @param tip dialog的提示信息
	 * @param okListener	确定按钮的点击事件
	 * @param cancelListener	取消事件的点击事件
	 */
	public void setPositiveTip(String tip, OnClickListener okListener, OnClickListener cancelListener) {
		builder.setTitle("提示信息");
		builder.setMessage(tip);
		builder.setPositiveButton("确定", okListener);
		builder.setNegativeButton("取消", cancelListener);
		showDialog();
	}

	public void setDialog() {
		builder.setTitle("请选择");
		builder.setSingleChoiceItems(str, 0, this);
		builder.setPositiveButton("确定", this);
		builder.setNegativeButton("取消", null);
		showDialog();

	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		Log.v("index", index + "");
		if (which >= 0) {
			index = which;
			chooseItem = str[index];
		} else {
			text.setText(chooseItem);
		}
	}

	/**
	 * 判断Activity是否已经销毁
	 * 如果销毁则就不再展示此Dialog
	 */
	@SuppressLint("NewApi")
	private void showDialog() {
		if (mContext instanceof Activity) {
			if (!((Activity) mContext).isFinishing())
				builder.create().show();
		} else {
			builder = null;
		}
	}
}
