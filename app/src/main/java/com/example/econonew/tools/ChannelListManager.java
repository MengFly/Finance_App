package com.example.econonew.tools;

import java.util.HashMap;
import java.util.Map;

public class ChannelListManager {

	private static Map<String, String[]> channelInfo = new HashMap<>();

	// 股票定制的标签
	private static final String[] STOCK_NAME = new String[] { "沪深", "新三科创", "港股", "美股", "欧股", "日经" };
	// 期货定制的标签
	private static final String[] FUTURES_NAME = new String[] { "股指", "国债", "原油", "黄金", "金属", "农产品" };
	// 基金定制的标签
	private static final String[] FUND_NAME = new String[] { "股票", "货币", "债券", "混合" };
	// 理财定制的标签
	private static final String[] MONEY_NAME = new String[] { "银行", "信托", "保险", "券商", "互联网" };
	// 外汇定制的标签
	private static final String[] EXCHANGE_NAME = new String[] { "美元", "欧元", "人民币", "英镑", "日元", "港币" };
	// 二级信息标签
	private static final String[] CHANNEL_LABEL = new String[] { "公告", "数据", "资讯", "分析", "定制" };

	static {
		channelInfo.put("股票", STOCK_NAME);
		channelInfo.put("期货", FUTURES_NAME);
		channelInfo.put("基金", FUND_NAME);
		channelInfo.put("理财", MONEY_NAME);
		channelInfo.put("外汇", EXCHANGE_NAME);
	}

	/**
	 * 根据频道类型获取相应的一级标签
	 *
	 * @param channelName
	 *            频道的类型
	 * @return 频道对应的一级标签
	 */
	public static String[] getChannelFirstLabel(String channelName) {
		return channelInfo.get(channelName);
	}

	/** 获取二级标签 */
	public static String[] getChannelSecondLabel() {
		return CHANNEL_LABEL;
	}

}
