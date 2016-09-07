package com.example.econonew.tools;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.econonew.entity.MsgItemEntity;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Voice 为主类中加载TTS功能，实现语音播放 讯飞语音
 *
 * @author agnes
 *
 */
public class Voice {

	// 讯飞语音的设置
	private SpeechSynthesizer mTts = null;
	// 用HashMap存储听写结果
	private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
	// 语音听写UI
	private ArrayList<MsgItemEntity> list = new ArrayList<>();
	public int readNum;
	public int flag;// 朗读标志 0代表当前没有读信息, 1代表朗读中暂停。2代表正在朗读

	public Voice(Context context) {
		SpeechUtility.createUtility(context, SpeechConstant.APPID + "=552a964f");
		// 1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
		mTts = SpeechSynthesizer.createSynthesizer(context, null);
		// 2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
		mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoqi");// 设置发音人
		mTts.setParameter(SpeechConstant.SPEED, "50");// 设置语速
		mTts.setParameter(SpeechConstant.VOLUME, "70");// 设置音量，范围0~100
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); // 设置云端
		flag = 0;
	}

	public void read() {
		if (flag == 0) {
			readList();
			flag = 2;
		} else if (flag == 2) {
			pauseList();
			flag = 1;
		} else {
			resumeList();
			flag = 2;
		}
	}

	/**
	 * 为阅读器设置消息内容
	 *
	 * @param list
	 */
	public void setList(List<MsgItemEntity> list) {
		this.list.clear();
		this.list.addAll(list);
	}

	// 列表朗读
	private void readList() {
		flag = 2;
		StringBuilder readString = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			readString.append(list.get(i).getMsgTitle() + "。");
		}
		readStr(readString.toString());
	}

	// 列表暂停朗读
	private void pauseList() {
		mTts.pauseSpeaking();
		flag = 1;
	}

	// 恢复列表
	private void resumeList() {
		mTts.resumeSpeaking();
		flag = 2;
	}

	// 读单句话
	public void readStr(String str) {
		mTts.startSpeaking(str, mSynListener);
	}

	private void printResult(RecognizerResult results) {
		String text = JsonHelper.parseIatResult(results.getResultString());
		String sn = null;
		// 读取json结果中的sn字段
		try {
			JSONObject resultJson = new JSONObject(results.getResultString());
			sn = resultJson.optString("sn");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mIatResults.put(sn, text);
		StringBuffer resultBuffer = new StringBuffer();
		for (String key : mIatResults.keySet()) {
			resultBuffer.append(mIatResults.get(key));
		}
	}

	/**
	 * 听写UI监听器
	 */
	@SuppressWarnings("unused")
	private RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener() {
		public void onResult(RecognizerResult results, boolean isLast) {
			Log.v("ihg", "" + results.getResultString());
			// mytext.setText(results.getResultString());
			printResult(results);
		}

		/**
		 * 识别回调错误.
		 */
		public void onError(SpeechError error) {
		}

	};
	// 合成监听器
	private SynthesizerListener mSynListener = new SynthesizerListener() {
		// 会话结束回调接口，没有错误时，error为null
		public void onCompleted(SpeechError error) {
		}

		// 缓冲进度回调
		// percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在
		// 文本中结束位置，info为附加信息。
		public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
		}

		// 开始播放
		public void onSpeakBegin() {
		}

		// 暂停播放
		public void onSpeakPaused() {
		}

		// 播放进度回调
		// percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文
		// 本中结束位置.
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
		}

		// 恢复播放回调接口
		public void onSpeakResumed() {
		}

		// 会话事件回调接口
		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
		}
	};

}
