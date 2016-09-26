package com.example.econonew.view.activity.User;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.econonew.R;
import com.example.econonew.presenter.UserPresenter;
import com.example.econonew.tools.adapter.SpinnerSelectAdapter;


/**
 * UserRegistActivity 此类是管理用户注册的类
 *
 * @author Agnes
 */
public class UserRegistActivity extends BaseUserActivity {

    private EditText userRegistNameEt;
    private EditText userRegistPassEt;
    private EditText userRegistPassTwoEt;
    private Button userRegistSureBtn;

    private Spinner userMiBaoQuestSp;//Todo 这里要添加Spinner的处理逻辑
    private EditText userAnswerEt;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.act_user_regist);
        userRegistNameEt = (EditText) findViewById(R.id.act_regist_user_name_et);
        userRegistPassEt = (EditText) findViewById(R.id.act_regist_user_password_et);
        userRegistPassTwoEt = (EditText) findViewById(R.id.act_regist_user_password2_et);
        userRegistSureBtn = (Button) findViewById(R.id.act_regist_sure_btn);

        userMiBaoQuestSp = (Spinner) findViewById(R.id.act_user_regist_quest_Sp);
        userAnswerEt = (EditText) findViewById(R.id.act_user_regist_get_pass_et);

        initSpinner();
        initListener();
        
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> questAdapter = ArrayAdapter.createFromResource(mContext, R.array.UserMiBaoQuestion,  android.R.layout.simple_spinner_item);
        questAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userMiBaoQuestSp.setAdapter(questAdapter);
        userMiBaoQuestSp.setPrompt("请选择密保问题");
    }

    private void initListener() {
        userRegistSureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {userRegistBtnClick();}
        });
        userMiBaoQuestSp.setOnItemSelectedListener(new SpinnerSelectAdapter() {
            @Override
            public void onItemSelected(String selectText) {
                // TODO: 2016/9/26 要实现的选择密保问题的方法
                userAnswerEt.setHint("请输入" + selectText);
            }
        });
    }

    @Override
    protected void initDatas() {
        initActionBar("用户注册", true);
        bindPresenter(new UserPresenter(this));
    }

    // 用户注册按钮的点击事件处理逻辑
    private void userRegistBtnClick() {
        String regist_username = userRegistNameEt.getText().toString();
        String regist_password = userRegistPassEt.getText().toString();
        String regist_passWord2 = userRegistPassTwoEt.getText().toString();
        mPresenter.userRegistThread(regist_username, regist_password, regist_passWord2);
    }

}
