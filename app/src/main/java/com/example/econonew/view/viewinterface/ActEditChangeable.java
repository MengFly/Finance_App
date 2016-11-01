package com.example.econonew.view.viewinterface;

/**
 * 回调接口，当一个Activity里面存在EditText想要为这个Activity绑定EditText的文字改变时间的时候可以实现该接口
 * Created by mengfei on 2016/11/1.
 */

public interface ActEditChangeable {
    void onTextChange(CharSequence s, int start, int before, int count);
}
