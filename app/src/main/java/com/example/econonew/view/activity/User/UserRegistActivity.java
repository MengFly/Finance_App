package com.example.econonew.view.activity.User;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.example.econonew.R;
import com.example.econonew.databinding.ActUserRegistBinding;
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

    private ActUserRegistBinding mBinding;
    private EditText userNameEt, userPassWordEt, userPassWord2Et;
    private EditText userAnswerEt;

    @Override
    protected void initView(Bundle savedInstanceState) {
        mBinding = DataBindingUtil.setContentView(mContext, R.layout.act_user_regist);

        userNameEt = mBinding.actRegistUserNameLy.getEditText();
        userPassWordEt = mBinding.actRegistPasswordLy.getEditText();
        userPassWord2Et = mBinding.actRegistPassword2Ly.getEditText();
        userAnswerEt = mBinding.actUserRegistGetPassLy.getEditText();

        initSpinner();
        initListener();
    }

    //初始化选项列表
    private void initSpinner() {
        ArrayAdapter<CharSequence> questAdapter = ArrayAdapter.createFromResource(mContext, R.array.UserMiBaoQuestion, android.R.layout.simple_spinner_item);
        questAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.actUserRegistQuestSp.setAdapter(questAdapter);
        mBinding.actUserRegistQuestSp.setPrompt("请选择密保问题");
    }

    private void initListener() {
        mBinding.actRegistSureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRegistBtnClick();
            }
        });
        userNameEt.addTextChangedListener(new TextChangeListener(this));
        userPassWordEt.addTextChangedListener(new TextChangeListener(this));
        userPassWord2Et.addTextChangedListener(new TextChangeListener(this));
        userAnswerEt.addTextChangedListener(new TextChangeListener(this));
        mBinding.actUserRegistQuestSp.setOnItemSelectedListener(new SpinnerSelectAdapter() {
            @Override
            public void onItemSelected(String selectText) {
                // TODO: 2016/9/26 要实现的选择密保问题的方法
                userAnswerEt.setHint("请输入" + selectText);
            }
        });
    }

    @Override
    protected void initDatas() {
        initActionBar(false ,"用户注册", true);
        bindPresenter(new BaseUserPresenter(this));
    }

    // 用户注册按钮的点击事件处理逻辑
    private void userRegistBtnClick() {
        String username = userNameEt.getText().toString();
        String password = userPassWord2Et.getText().toString();
        String passWord2 = userPassWord2Et.getText().toString();
        mPresenter.userRegistThread(username, password, passWord2);
    }

    @Override
    public void onTextChange(CharSequence s, int start, int before, int count) {
        String userName = userNameEt.getText().toString();
        String pass1 = userPassWordEt.getText().toString();
        String pass2 = userPassWord2Et.getText().toString();
        if (!TextUtils.isEmpty(userName) && userName.length() != 11) {
            mBinding.actRegistUserNameLy.setErrorEnabled(true);
            mBinding.actRegistUserNameLy.setError("用户名长度不对");

        } else if (!TextUtils.isEmpty(pass1) && !pass1.equals(pass2)) {
            mBinding.actRegistPassword2Ly.setErrorEnabled(true);
            mBinding.actRegistPassword2Ly.setError("两次输入的密码不一致");
        }
        if(userName.length() == 11){
            mBinding.actRegistUserNameLy.setErrorEnabled(false);
        }
        if(pass1.equals(pass2) || TextUtils.isEmpty(pass1)) {
            mBinding.actRegistPassword2Ly.setErrorEnabled(false);
        }
    }
}
