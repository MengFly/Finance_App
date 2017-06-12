package com.example.econonew.view.activity.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.econonew.R;
import com.example.econonew.databinding.ActMsgItemContentBinding;
import com.example.econonew.tools.CallBack;
import com.example.econonew.tools.FileTools;
import com.example.econonew.view.activity.BaseActivity;
import com.example.econonew.view.customview.DefaultWebView;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;

import java.io.File;

public class MsgContentActivity extends BaseActivity {

    private ActMsgItemContentBinding mBinding;
    private String msgContentUrl;//用于显示界面的URL地址
    private String msgContent;
    private DefaultWebView showContentDW;
    private PDFView showContentPDFV;


    @Override
    protected void initView(Bundle savedInstanceState) {
        mBinding = DataBindingUtil.setContentView(mContext, R.layout.act_msg_item_content);
    }

    @Override
    protected void initDatas() {
        Intent intent = getIntent();
        msgContentUrl = intent.getStringExtra("msgContentUrl");
        initActionBar(false, intent.getStringExtra("msgTitle"), true);
        msgContent = intent.getStringExtra("msgContext");
        if (msgContentUrl != null && msgContentUrl.toLowerCase().endsWith("pdf")) {
            showContentPDFV = new PDFView(mContext, null);
            mBinding.progressBar1.setVisibility(View.GONE);
            mBinding.svMessage.addView(showContentPDFV);
        } else {
            showContentDW = new DefaultWebView(mContext);
            showContentDW.setProgressBar(mBinding.progressBar1);
            mBinding.svMessage.addView(showContentDW);
        }
        load(msgContentUrl);

    }

    private void load(String url) {
        if (showContentDW != null) {
            showContentDW.loadUrl(msgContentUrl, msgContent);
        } else {
            FileTools.downloadFileFromNet(msgContentUrl, new CallBack<String>() {
                @Override
                public void callBack(final String... t) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (t[0] != null) {
                                if (t.length <= 1) {
                                    showToast("文件已保存" + t[0]);
                                }
                                File file = new File(t[0]);
                                Uri uri = Uri.fromFile(file);
                                showContentPDFV.fromUri(uri)
                                        .enableSwipe(true) // allows to block changing pages using swipe
                                        .swipeHorizontal(false)
                                        .enableDoubletap(true)
                                        .defaultPage(0)
                                        .swipeHorizontal(false)
                                        .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                                        .password(null)
                                        .scrollHandle(null)
                                        .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                                        .onError(new OnErrorListener() {
                                            @Override
                                            public void onError(Throwable t) {
                                                showToast("文件解析失败" + t.getMessage());
                                            }
                                        })
                                        .load();
                            } else {
                                showToast(t[1]);
                            }
                        }
                    });
                }
            }, new CallBack<Integer>() {
                @Override
                public void callBack(final Integer... t) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            showToast("已下载" + t[1] + "/" + t[0] + "K");
                        }
                    });
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.act_msg_content_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                load(msgContentUrl);
                return true;
            case R.id.menu_open_with_browser:
                if (msgContentUrl != null) {
                    openWithBrowser();
                } else {
                    showToast("这个页面不支持在浏览器中打开");
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openWithBrowser() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(msgContentUrl));
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding.svMessage.removeAllViews();
        showContentDW = null;
        showContentPDFV = null;
    }

    // 重写返回按键的方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && showContentDW != null && showContentDW.canGoBack()) {
            showContentDW.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}

