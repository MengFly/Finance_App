package com.example.econonew.view.activity.channel;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.econonew.view.activity.BaseActivity;
import com.example.econonew.R;
import com.example.econonew.tools.DialogTool;

/***
 * SpecialActivity
 * 在channelActivity中调用共同生成频道添加界面
 */

public class SpecialActivity extends BaseActivity implements OnClickListener {
    private Button expert_info;
    private TextView expert_info_show;
    private Button channel_sure;  //确认按钮

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.act_addchannel_special);
        channel_sure = (Button) findViewById(R.id.special_channel_sure);
        expert_info = (Button) findViewById(R.id.expert_info);
        expert_info_show = (TextView) findViewById(R.id.expert_info_show);

        channel_sure.setOnClickListener(this);
        expert_info.setOnClickListener(this);
    }

    @Override
    protected void initDatas() {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.special_channel_sure:

                break;
            case R.id.expert_info:
                DialogTool dialogName = new DialogTool(this, new String[]{"不选择", "张小二"}, expert_info_show);
                dialogName.setDialog();
                break;
            default:
                break;
        }
    }


}
