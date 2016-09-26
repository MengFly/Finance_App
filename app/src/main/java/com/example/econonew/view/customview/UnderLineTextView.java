package com.example.econonew.view.customview;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 带下划线的TextView
 * Created by mengfei on 2016/9/24.
 */

public class UnderLineTextView extends TextView {

    public UnderLineTextView(Context context) {
        this(context,null, 0);
    }

    public UnderLineTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UnderLineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);//为Text添加下划线
    }
}
