package com.example.econonew.view.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.econonew.R;
import com.example.econonew.databinding.FragSetPassBinding;
import com.example.econonew.presenter.BaseUserPresenter;
import com.example.econonew.view.activity.User.BaseUserActivity;
import com.example.econonew.view.activity.User.UserActivity;

/**
 * 用于设置密码的Fragment
 * Created by mengfei on 2016/10/7.
 */

public class UserSetPassFragment extends Fragment {

    private FragSetPassBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.frag_set_pass, container, false);
        initListener();
        return mBinding.getRoot();
    }

    private void initListener() {
        mBinding.fragSetPass1Et.addTextChangedListener(new ContentExchangeListener());
        mBinding.fragSetPass2Et.addTextChangedListener(new ContentExchangeListener());
        mBinding.userSetpwdSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
		        final String new_pwd1 = mBinding.fragSetPass1Et.getText().toString();
		        final String new_pwd2 = mBinding.fragSetPass2Et.getText().toString();
		        new BaseUserPresenter((BaseUserActivity<BaseUserPresenter<UserActivity>>) getActivity()).userSetPassThread(new_pwd1, new_pwd2);
            }
        });
    }

    private class ContentExchangeListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
			String pass1 = mBinding.fragSetPass1Et.getText().toString();
			String pass2 = mBinding.fragSetPass2Et.getText().toString();
			if (!TextUtils.isEmpty(pass1)) {
				if (TextUtils.isEmpty(pass2)) {
                    mBinding.fragSetPwdIsOkTv.setText("请再次输入新密码");
				} else if (pass1.equals(pass2)) {
                    mBinding.fragSetPwdIsOkTv.setText("两次输入的密码一致");
				} else {
                    mBinding.fragSetPwdIsOkTv.setText("两次输入密码不一致");
				}
			}
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

    }

}
