package com.example.econonew.tools;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.econonew.entity.MsgItemEntity;
import com.example.econonew.view.activity.FinanceApplication;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
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
	private static final String TAG = "Voice";

	private static volatile Voice mInstance;

	public static final int VOICE_STAT_NO_READ = 0;//当前没有在阅读
	public static final int VOICE_STAT_READING = 1;//当前正在阅读
	public static final int VOICE_STAT_PAUSE = 2;//当前暂停中

	// 讯飞语音的设置// 1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
	private SpeechSynthesizer mTts;

	// 用HashMap存储听写结果
	private HashMap<String, String> mIatResults = new LinkedHashMap<>();

	private ArrayList<List<MsgItemEntity>> readList;//这里面存储着要进行阅读的list

	private int readStat;// 朗读标志,分别是上面的三种状态

	/**
	 * 获取语音播放类的实例
	 * @return 语音播放类的实例
     */
	public static Voice getInstance() {
		if (mInstance == null) {
			synchronized (Voice.class) {
				if (mInstance == null) {
					mInstance = new Voice(FinanceApplication.app);
				}
			}
		}
		return mInstance;
	}

	private Voice(Context context) {
		mTts = SpeechSynthesizer.createSynthesizer(context, null);
		// 2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
		mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoqi");// 设置发音人
		mTts.setParameter(SpeechConstant.SPEED, "50");// 设置语速
		mTts.setParameter(SpeechConstant.VOLUME, "70");// 设置音量，范围0~100
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); // 设置云端

		readStat = VOICE_STAT_NO_READ;
		readList = new ArrayList<>();
	}


	/**
	 * 设置要进行阅读的列表，可以进行链式设置，设置好的列表将会依次进行阅读
     */
	public Voice setList(List<MsgItemEntity> list) {
		readList.add(list);
		return this;
	}

	/**
	 * 重置要进行阅读的数据
	 */
	public void resetVoice() {
		readStat = VOICE_STAT_NO_READ;
		readList.clear();
		currentReadPoint = 0;
	}

	/**
	 * 获取当前正在阅读的状态
	 */
	public int getReadStat() {
		return readStat;
	}

	public void read() {
		if (readStat == VOICE_STAT_NO_READ) {
			readList();
			readStat = VOICE_STAT_READING;
		} else if (readStat == VOICE_STAT_READING) {
			pauseList();
			readStat = VOICE_STAT_PAUSE;
		} else {
			resumeList();
			readStat = VOICE_STAT_READING;
		}
	}


	private int currentReadPoint;//当前阅读的位置

	// 列表朗读
	private void readList() {
		if(currentReadPoint >= readList.size()) {
			resetVoice();
		} else {
			StringBuilder readString = new StringBuilder();
			List<MsgItemEntity> currentReadList = readList.get(currentReadPoint);
			//如果当前要阅读的列表为空,那么就跳过这次阅读
			if(currentReadList == null || currentReadList.isEmpty()) {
				currentReadPoint ++;
				readList();
			} else {
				for (MsgItemEntity entity : currentReadList) {
					readString.append(entity.getMsgTitle());
					readString.append("\n");
					readStr(readString.toString());
				}
			}
		}
	}

	// 列表暂停朗读
	private void pauseList() {
		mTts.pauseSpeaking();
		readStat = VOICE_STAT_PAUSE;
	}

	// 恢复阅读
	private void resumeList() {
		mTts.resumeSpeaking();
		readStat = VOICE_STAT_READING;
	}

	// 读单句话
	private void readStr(String str) {
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
	}

	/**
	 * 听写UI监听器
	 */
	@SuppressWarnings("unused")
	private RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener() {
		public void onResult(RecognizerResult results, boolean isLast) {
			Log.v("ihg", "" + results.getResultString());
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
			currentReadPoint ++;
			Log.d(TAG, "onCompleted: " + currentReadPoint);
			readList();
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
