package com.example.econonew.tools.adapter;

import android.view.View;
import android.widget.AdapterView;

/**
 * 适配器：用于Spinner选择
 * Created by mengfei on 2016/9/26.
 */

public abstract class SpinnerSelectAdapter implements AdapterView.OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String itemText = parent.getItemAtPosition(position).toString();
        onItemSelected(itemText);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //ignore
    }

    public abstract void onItemSelected(String selectText);
}
