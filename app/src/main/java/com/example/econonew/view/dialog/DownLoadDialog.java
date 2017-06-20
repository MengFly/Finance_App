package com.example.econonew.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.econonew.R;


/**
 * 下载的Dialog
 * Created by mengfei on 2017/6/20.
 */
public class DownLoadDialog extends Dialog {
    private TextView title;
    private ProgressBar progress;
    private TextView textPro;
    private int totalLength;
    private DownFinishListener listener;

    public DownFinishListener getListener() {
        return listener;
    }

    public void setListener(DownFinishListener listener) {
        this.listener = listener;
    }

    public DownLoadDialog(Context context, String title) {
        this(context, R.style.MyDialogStyle);
        if (title != null) {
            this.title.setText(title);
        }
    }

    private DownLoadDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView();
        setCancelable(false);
    }

    private void initView() {
        setContentView(R.layout.dialog_download);
        title = (TextView) findViewById(R.id.tv_title_download);
        progress = (ProgressBar) findViewById(R.id.pb_download);
        textPro = (TextView) findViewById(R.id.tv_download);
    }

    public int getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(int totalLength) {
        this.totalLength = totalLength;
        progress.setMax(totalLength);
    }

    public void setShowLength(int showLength) {
        setShowLength(showLength, totalLength);
    }

    public void setShowLength(int showLength, int totalLength) {
        if (showLength >= totalLength) {
            finish();
        } else {
            if (totalLength != progress.getMax()) {
                progress.setMax(totalLength);
            }
            progress.setProgress(showLength);
            textPro.setText(showLength + "/" + totalLength);
        }
    }

    private void finish() {
        if (listener != null) {
            listener.onFinish();
        }
        dismiss();
    }

    public interface DownFinishListener {
        void onFinish();
    }

}
