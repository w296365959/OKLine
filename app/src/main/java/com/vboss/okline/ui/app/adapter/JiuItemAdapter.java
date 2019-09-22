package com.vboss.okline.ui.app.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.data.entities.AppEntity;
import com.vboss.okline.ui.app.App;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: Yuan shaoyu <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/3/28 9:51 <br/>
 * Summary  : 九宫格item列表项显示的adapter
 */
public class JiuItemAdapter extends BaseAdapter {
    private static final String TAG = "JiuItemAdapter";
    private List<AppEntity> mList = new ArrayList<>();
    private Context mContext;
    private ViewHolder holder;

    public JiuItemAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public JiuItemAdapter(Context context, List<AppEntity> lists) {
        this.mContext = context;
        this.mList = lists;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * @param list
     */
    public void refresh(List<AppEntity> list) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.jiugongge_small_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.jiugonggeSmallImg.setImageURI(mList.get(position).appIcon());

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.jiugongge_small_img)
        SimpleDraweeView jiugonggeSmallImg;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
