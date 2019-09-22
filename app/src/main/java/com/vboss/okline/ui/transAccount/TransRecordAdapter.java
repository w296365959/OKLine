package com.vboss.okline.ui.transAccount;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.ui.app.App;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: yuan shaoyu  <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/7/4 <br/>
 * summary  :在这里描述Class的主要功能
 */

public class TransRecordAdapter extends BaseAdapter{
    private static final String TAG = "TransRecordAdapter";

    private List<App> mList = new ArrayList<>();
    private Context mContext;
    private ViewHolder holder;
    public TransRecordAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public TransRecordAdapter(Context context, List<App> lists) {
        this.mContext = context;
        this.mList = lists;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * @param list
     */
    public void refresh(List<App> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        this.mList = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.trans_record_item_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(mList.get(position).getAppName());
        return convertView;
    }

    class OnAppIconClickListener implements View.OnClickListener{
        App app;

        public OnAppIconClickListener(int position) {
            app = mList.get(position);
        }

        @Override
        public void onClick(View v) {
            try {

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }


    static class ViewHolder {

        @BindView(R.id.trans_record_bankName)
        TextView bankName;

        @BindView(R.id.trans_record_name)
        TextView name;

        @BindView(R.id.trans_direction)
        ImageView directionIcon;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
