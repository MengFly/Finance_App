package com.example.econonew.tools;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * 对分享功能的封装
 *
 * @author mengfei
 *
 */
public class ShareTool {

	/**
	 * 以文字的形式进行分享
	 *
	 * @param context
	 *            context
	 * @param text
	 *            要分享的文字内容
	 */
	public static void shareText(Context context, String text) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");// 纯文本
		intent.putExtra(Intent.EXTRA_TEXT, text);
		context.startActivity(Intent.createChooser(intent, "分享"));
	}

	/**
	 * 以文件的形式进行分享
	 *
	 * @param context
	 *            context
	 * @param shareFile
	 *            要分享的文件
	 */
	public static void shareFiles(Context context, File shareFile) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_SEND);
		intent.setType("*/*");
		intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(shareFile));
		context.startActivity(Intent.createChooser(intent, "分享FinaceApp"));
	}

}
