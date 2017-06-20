package com.example.econonew.tools.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.econonew.R;
import com.example.econonew.db.DBHelperFactory;
import com.example.econonew.entity.MsgItemEntity;
import com.example.econonew.tools.ImageLoader;
import com.example.econonew.tools.ShareTool;
import com.example.econonew.view.activity.FinanceApplication;

import java.util.List;

/**
 * MyListViewAdapter
 *
 * @author agnes 是显示信息的ListView的适配器 在MyFragment中引用
 */
public class MsgListViewAdapter extends BaseAdapter {

    private List<MsgItemEntity> msgItems;
    private LayoutInflater inflater;
    private ImageLoader mImageLoader;
    private String msgName;
    private Context mContext;

    public MsgListViewAdapter(Context context, List<MsgItemEntity> list, ListView listView, String msgName) {
        this.msgItems = list;
        inflater = LayoutInflater.from(context);
        this.mImageLoader = new ImageLoader(context, listView);
        this.msgName = msgName;
        mContext = context;
    }

    @Override // 得到listView的长度
    public int getCount() {
        return msgItems.size();
    }

    @Override
    public Object getItem(int arg0) {
        return msgItems.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int arg0, View view, ViewGroup arg2) {
        if (msgItems.size() == 0) {
            return null;
        }
        ViewHolder holder;
        final MsgItemEntity item = msgItems.get(arg0);
        if (view == null) {
            view = inflater.inflate(R.layout.item_msg_list, null);
            holder = new ViewHolder();
            holder.titleTv = (TextView) view.findViewById(R.id.itemText);
            holder.titleIv = (ImageView) view.findViewById(R.id.itemImage);
            holder.isVipTv = (TextView) view.findViewById(R.id.vip_tv);
            holder.isLoveCb = (CheckBox) view.findViewById(R.id.item_is_love_cb);
            holder.shareTv = (ImageView) view.findViewById(R.id.item_share_tv);
            holder.pdfFL = (LinearLayout) view.findViewById(R.id.fl_pdf);
            holder.pdfTitleTv = (TextView) view.findViewById(R.id.tv_pdf_title);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        initUi(holder, item);
        return view;
    }

    private void initUi(ViewHolder holder, final MsgItemEntity item) {
        holder.titleTv.setText(item.getMsgTitle());
        holder.titleIv.setTag(item.getImageTitleUrl());
        //放置界面刷新的时候出现错位
        holder.pdfFL.setVisibility(View.GONE);
        holder.titleIv.setVisibility(View.VISIBLE);
        //这一段代码是确定标题图片是显示图片还是PDF图片
        if (item.getMsgContentUrl() != null && item.getMsgContentUrl().toLowerCase().endsWith("pdf")) {
            holder.titleIv.setVisibility(View.GONE);
            holder.pdfFL.setVisibility(View.VISIBLE);
            holder.pdfTitleTv.setText(getPdfTitleSpannableString(item.getMsgTitle().substring(0, 2)));
        } else {
            mImageLoader.showImageFromURL(holder.titleIv, item.getImageTitleUrl());
        }
        holder.isLoveCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (item.isLove() != isChecked) {
                    item.setLove(isChecked);
                    Toast.makeText(FinanceApplication.getInstance(), "设置成功", Toast.LENGTH_SHORT).show();
                    ContentValues values = new ContentValues(1);
                    values.put("isLove", isChecked);
                    DBHelperFactory.getDBHelper().updateItemById(MsgItemEntity.class, values, item.getId());
                }
            }
        });
        holder.shareTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ShareTool.shareText(mContext, "我在Fiance_app中发现了这条消息，挺不错的，发送给你" + item.getMsgContentUrl());
            }
        });
        holder.isVipTv.setVisibility(item.isVip() ? View.VISIBLE : View.GONE);
        holder.isLoveCb.setChecked(item.isLove());
    }

    /**
     * 获取到显示PDF的标题
     */
    private SpannableString getPdfTitleSpannableString(String substring) {
        SpannableString str = new SpannableString(substring);
        str.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.btn_default_back)),
                0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new RelativeSizeSpan(2.5f), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return str;
    }

    // 存放控件的viewHolder
    private class ViewHolder {
        TextView titleTv;
        ImageView titleIv;
        TextView isVipTv;
        CheckBox isLoveCb;
        ImageView shareTv;
        LinearLayout pdfFL;
        TextView pdfTitleTv;
    }
}
