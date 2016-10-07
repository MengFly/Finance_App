package com.example.econonew.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.econonew.R;
import com.example.econonew.presenter.UserPresenter;
import com.example.econonew.view.activity.User.BaseUserActivity;

/**
 * 用于设置密码的Fragment
 * Created by mengfei on 2016/10/7.
 */

public class UserSetPassFragment extends Fragment {

    private EditText passWord1Et;
    private EditText passWord2Et;
    private TextView checkPassTv;
    private Button sureBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View setPassLayout = inflater.inflate(R.layout.frag_set_pass, container, false);
        passWord1Et = (EditText) setPassLayout.findViewById(R.id.frag_set_pass_1_et);
        passWord2Et = (EditText) setPassLayout.findViewById(R.id.frag_set_pass_2_et);
        checkPassTv = (TextView) setPassLayout.findViewById(R.id.frag_set_pwd_is_ok_tv);
        sureBtn = (Button) setPassLayout.findViewById(R.id.user_setpwd_sure);
        initListener();
        return setPassLayout;
    }

    private void initListener() {
        passWord1Et.addTextChangedListener(new ContentExchangeListener());
        passWord2Et.addTextChangedListener(new ContentExchangeListener());
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
		        final String new_pwd1 = passWord1Et.getText().toString();
		        final String new_pwd2 = passWord2Et.getText().toString();
		        new UserPresenter((BaseUserActivity) getActivity()).userSetPassThread(new_pwd1, new_pwd2);
            }
        });
    }

    private class ContentExchangeListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
			String pass1 = passWord1Et.getText().toString();
			String pass2 = passWord2Et.getText().toString();
			if (!TextUtils.isEmpty(pass1)) {
				if (TextUtils.isEmpty(pass2)) {
                    checkPassTv.setText("请再次输入新密码");
				} else if (pass1.equals(pass2)) {
                    checkPassTv.setText("两次输入的密码一致");
				} else {
                    checkPassTv.setText("两次输入密码不一致");
				}
			}
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

    }

}
