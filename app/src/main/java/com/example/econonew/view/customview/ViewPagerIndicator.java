package com.example.econonew.view.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.econonew.resource.Constant;
import com.example.econonew.resource.msg.MainMessage;

import java.util.List;

/**
 * 实现自定义的ViewPagerIndicator导航栏
 *
 * @author mengfei
 *
 */
public class ViewPagerIndicator extends LinearLayout {

	private Paint mPaint;
	private Path mPath;

	// 底部导航条的宽度和高度
	private int mTriangleWidth;
	private int mTriangleHeight;

	// 初始化偏移量和偏移量
	private int mInitTranslationX;
	private int mTranslationX;

	// 可见Tab的数量以及默认的可见Tab的数量
	private int mTabVisibleCount = COUNT_DEFAULT_TAB;
	private static final int COUNT_DEFAULT_TAB = 6;

	//设置TAB底下的导航条的高度
	private static final int TAB_LINE_HEIGHT = 3;

	// 为每个Tab设置title
	private List<String> mTitles;

	public ViewPagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);

		// 初始化画笔
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.parseColor("#ffffff"));// 设置颜色
		mPaint.setStyle(Style.FILL);// 设置填充类型
		mPaint.setPathEffect(new CornerPathEffect(5));// 设置圆角
	}

	/*
	 * 初始化三角形的数据 包括三角形的底边的宽以及三角形的高 以及三角形的初始的偏移量
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mTriangleWidth = (int) (w / mTabVisibleCount);// 设置导航条的宽度
		mTriangleHeight = TAB_LINE_HEIGHT;// 设置导航条的高度
		mInitTranslationX = 0;// 设置导航条最初的偏移量的位置
		initTabLine();// 初始化底部导航条
	}

	// 初始化Tab下面的导航条
	private void initTabLine() {
		mPath = new Path();
		mPath.moveTo(0, 0);
		mPath.lineTo(mTriangleWidth, 0);
		mPath.lineTo(mTriangleWidth, -mTriangleHeight);
		mPath.lineTo(0, -mTriangleHeight);
		mPath.close();
	}

	// 绘制底部导航条
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		canvas.save();
		canvas.translate(mInitTranslationX + mTranslationX, getHeight());
		canvas.drawPath(mPath, mPaint);
		canvas.restore();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		int cCount = getChildCount();
		if (cCount == 0)
			return;
		for (int i = 0; i < cCount; i++) {
			View view = getChildAt(i);
			LinearLayout.LayoutParams lp = (LayoutParams) view.getLayoutParams();
			lp.weight = 0;
			lp.width = getScreenWidth() / mTabVisibleCount;
			view.setLayoutParams(lp);
		}
		setItemClickEvent();
	}

	/**
	 * 获得屏幕宽度
	 *
	 * @return 屏幕的宽度
	 */
	private int getScreenWidth() {
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

	// 指示器跟随手指进行滚动
	public void scroll(int position, float offset) {
		int tabWidth = getWidth() / mTabVisibleCount;
		mTranslationX = (int) (tabWidth * (offset + position));
		// 容器移动，在Tab移动至最后一个时
		if (position >= (mTabVisibleCount - 1) && offset > 0 && getChildCount() > mTabVisibleCount) {
			if (mTabVisibleCount != 1) {
				this.scrollTo((position - (mTabVisibleCount - 1)) * tabWidth + (int) (tabWidth * offset), 0);
			} else {
				this.scrollTo(position * tabWidth + (int) (tabWidth * offset), 0);
			}
		}
		invalidate();// 进行重绘
	}

	/**
	 * 为Tab导航栏设置数据源 传入的数据源中的每一项将作为导航栏中的每一项的标题内容
	 *
	 * @param titles
	 *            传入的数据源
	 */
	public void setTabTitles(List<String> titles) {
		if (titles != null && titles.size() > 0) {
			this.removeAllViews();
			mTitles = titles;
			for (String title : mTitles) {
				addView(generateTextView(title));
			}
		}
		setItemClickEvent();
	}

	/**
	 * 根据title创建Tab 返回包含Tab内容的TextView
	 */
	private View generateTextView(String title) {
		TextView tv = new TextView(getContext());
		LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.width = getScreenWidth() / mTabVisibleCount;
		tv.setText(title);
		tv.setPadding(0, 0, 0, TAB_LINE_HEIGHT);
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,SIZE_TEXT_DEFAULT);
		tv.setTextColor(Color.WHITE);
		tv.setSingleLine();
		tv.setLayoutParams(lp);
		return tv;
	}

	// Tab导航栏的默认大小以及改变的大小
	private static final int SIZE_TEXT_DEFAULT = 14;

	/**
	 * 为导航栏设置可见的Tab数量 传入的参数不可以小于零 由于在setTabTitles方法中使用了Tab栏的可见数目的属性，所以
	 * 此方法要在setTabTitles方法之前调用
	 *
	 * @param count
	 *            设置的可见标题栏的数目
	 */
	public void setVisibleTabCount(int count) {
		mTabVisibleCount = count;
	}

	private ViewPager mViewPager;

	/**
	 * 设置关联的ViewPager
	 *
	 * @param viewPager
	 * @param pos
	 *            设置默认指定第几个标题
	 */
	public void setViewPager(ViewPager viewPager, int pos) {
		mViewPager = viewPager;
		mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				if (mListener != null) {
					mListener.onPageSelected(arg0);
				}
				heightLightTextView(arg0);
				Constant.read_tab = arg0;
				MainMessage messageManager = MainMessage.getInstance(Constant.tabList.get(arg0));
				if (messageManager != null) {
					messageManager.resetVoice();
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				scroll(arg0, arg1);
				if (mListener != null) {
					mListener.onPageScrolled(arg0, arg1, arg2);
				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				if (mListener != null) {
					mListener.onPageScrollStateChanged(arg0);
				}
			}
		});
		mViewPager.setCurrentItem(pos);
		heightLightTextView(pos);
	}

	private PageOnChangeListener mListener;

	private interface PageOnChangeListener {
		void onPageSelected(int arg0);

		void onPageScrolled(int arg0, float arg1, int arg2);

		void onPageScrollStateChanged(int arg0);
	}

	/**
	 * 为ViewPager设置监听事件
	 * 监听器的类型为android.support.v4.view.ViewPager.OnPageChangeListener;
	 *
	 * @param listener
	 *            监听事件
	 */
	public void setOnPageChangeListener(PageOnChangeListener listener) {
		mListener = listener;
	}

	// 高亮某个Tab的文本
	private void heightLightTextView(int pos) {
		resetTextViewColor();
		View view = getChildAt(pos);
		if (view instanceof TextView) {
			((TextView) view).setTextColor(Color.RED);
		}
	}

	// 重置所有Tab项TextView的颜色
	@SuppressLint("NewApi")
	private void resetTextViewColor() {
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View view = getChildAt(i);
			if (view instanceof TextView) {
				((TextView) view).setTextColor(Color.WHITE);
			}
		}
	}

	// 为每个Tab设置点击事件
	private void setItemClickEvent() {
		int cCount = getChildCount();
		for (int i = 0; i < cCount; i++) {
			View view = getChildAt(i);
			final int currentCount = i;
			if (view instanceof TextView) {
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mViewPager.setCurrentItem(currentCount);
					}
				});
			}
		}
	}

}
