package com.example.econonew.utils;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.econonew.R;
import com.example.econonew.server.URLManager;
import com.example.econonew.view.activity.BaseActivity;
import com.example.econonew.view.activity.FinanceApplication;

/**
 * Created by mengfei on 2016/9/14.
 */
public class URLSettingActivity extends BaseActivity {

    private TextView currentIIP;
    private EditText ip;

    private Button okBtn;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.test_act_set_ip);
        currentIIP = (TextView) findViewById(R.id.current_ip);
        ip = (EditText) findViewById(R.id.ip_et);
        okBtn = (Button) findViewById(R.id.test_act_set_ip_btn);
        currentIIP.setText(URLManager.getURL());
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentIp = ip.getText().toString();
                SharedPreferences spf = FinanceApplication.getInstance().getSharedPreferences("ip", MODE_PRIVATE);
                SharedPreferences.Editor editor = spf.edit();
                editor.putString("current_ip", currentIp);
                editor.apply();
                currentIIP.setText(URLManager.getURL());
            }
        });
    }

    @Override
    protected void initDatas() {

    }
}
