package com.example.econonew.view.activity.user;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.econonew.R;
import com.example.econonew.databinding.ActUserRegist2Binding;
import com.example.econonew.presenter.BaseUserPresenter;
import com.example.econonew.tools.adapter.SpinnerSelectAdapter;
import com.example.econonew.tools.listener.TextChangeListener;
import com.example.econonew.view.viewinterface.ActEditChangeable;


/**
 * UserRegistActivity 此类是管理用户注册的类
 *
 * @author Agnes
 */
public class UserRegistActivity extends BaseUserActivity<BaseUserPresenter<UserActivity>> implements ActEditChangeable {

    private ActUserRegist2Binding mBinding;

    @Override
    protected void initView(Bundle savedInstanceState) {
        mBinding = DataBindingUtil.setContentView(mContext, R.layout.act_user_regist2);

        initSpinner();
        initListener();
    }

    //初始化选项列表
    private void initSpinner() {
        ArrayAdapter<CharSequence> questAdapter = ArrayAdapter.createFromResource(mContext, R.array.UserMiBaoQuestion, android.R.layout.simple_spinner_item);
        questAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.SPMibao.setAdapter(questAdapter);
        mBinding.SPMibao.setPrompt("请选择密保问题");
    }

    private void initListener() {
        mBinding.btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRegistBtnClick();
            }
        });
        mBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBinding.etUsername.addTextChangedListener(new TextChangeListener(this));
        mBinding.etPassword.addTextChangedListener(new TextChangeListener(this));
        mBinding.etPasswordAgain.addTextChangedListener(new TextChangeListener(this));
        mBinding.etMibao.addTextChangedListener(new TextChangeListener(this));
        mBinding.SPMibao.setOnItemSelectedListener(new SpinnerSelectAdapter() {
            @Override
            public void onItemSelected(String selectText) {
                // TODO: 2016/9/26 要实现的选择密保问题的方法
                mBinding.etMibao.setHint("请输入" + selectText);
            }
        });
    }

    @Override
    protected void initDatas() {
        initActionBar(false, "用户注册", true);
        bindPresenter(new BaseUserPresenter(this));
    }

    // 用户注册按钮的点击事件处理逻辑
    private void userRegistBtnClick() {
        String username = mBinding.etUsername.getText().toString();
        String password = mBinding.etPassword.getText().toString();
        String passWord2 = mBinding.etPasswordAgain.getText().toString();
        mPresenter.userRegistThread(username, password, passWord2);
    }

    @Override
    public void onTextChange(CharSequence s, int start, int before, int count) {

        String userName = mBinding.etUsername.getText().toString();
        String pass1 = mBinding.etPassword.getText().toString();
        String pass2 = mBinding.etPasswordAgain.getText().toString();
        if (!TextUtils.isEmpty(userName) && userName.length() != 11) {
            mBinding.tilUsername.setErrorEnabled(true);
            mBinding.tilUsername.setError("用户名长度不对");
        } else if (!TextUtils.isEmpty(pass1) && !pass1.equals(pass2)) {
            mBinding.tilPasswordAgain.setErrorEnabled(true);
            mBinding.tilPasswordAgain.setError("两次输入的密码不一致");
        }
        if (userName.length() == 11) {
            mBinding.tilUsername.setErrorEnabled(false);
        }
        if (pass1.equals(pass2) || TextUtils.isEmpty(pass1)) {
            mBinding.tilPasswordAgain.setErrorEnabled(false);
        }
    }
}
