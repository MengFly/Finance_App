package com.example.econonew.presenter;

import com.example.econonew.view.activity.BaseActivity;

/**
 * 所有Presenter的基类
 * Created by mengfei on 2016/9/14.
 */
public class BasePresenter<T extends BaseActivity> {

    protected T mActivity;

    public BasePresenter(T activity) {
        this.mActivity = activity;
    }

    public void onDestroy() {
        mActivity = null;
    }

}
