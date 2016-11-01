package com.example.econonew.tools.listener;

import android.text.Editable;
import android.text.TextWatcher;

import com.example.econonew.view.viewinterface.ActEditChangeable;

/**
 * 检测EditText中文字变化的监听事件
 * 这个类接受一个ActEditChangeable对象，便于和Activity联合使用
 * Created by mengfei on 2016/11/1.
 */
public class TextChangeListener implements TextWatcher {

    private ActEditChangeable mActivity;

    public TextChangeListener(ActEditChangeable mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mActivity.onTextChange(s, start, before, count);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
