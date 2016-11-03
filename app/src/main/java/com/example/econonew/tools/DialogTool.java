package com.example.econonew.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.TextView;

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


	public DialogTool(Context context, String[] str, final TextView text) {
		this.text = text;
		this.mContext = context;
		this.str = str;
		builder = new AlertDialog.Builder(context);
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
