package com.vboss.okline.ui.contact.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.ui.contact.callPhone.CallLogNeedAllBean;
import com.vboss.okline.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.vboss.okline.R.id.item_underline;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/5/4 20:21
 * Desc : 通话记录adapter
 */

public class CallLogAdapter extends BaseAdapter {

    private List<CallLogNeedAllBean> mList;
    private Context mContext;

    public CallLogAdapter(List<CallLogNeedAllBean> list, Context context) {
        if (null != list && list.size() > 0) {
            this.mList = list;
        }
        mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        CallLogNeedAllBean bean = mList.get(position);
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.call_log_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        if (position!=0){
            holder.itemUnderline.setVisibility(View.VISIBLE);
        }else{
            holder.itemUnderline.setVisibility(View.GONE);
        }
        if (bean.isFriend()){
            holder.tvAdd.setVisibility(View.VISIBLE);
        }else{
            holder.tvAdd.setVisibility(View.GONE);
        }
        holder.tvPhoneNumber.setText(bean.getNumber());
        holder.tvLastDate.setText(bean.getLastDate());
        holder.tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContact(position);
            }


        });


        return view;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    private void addContact(int position) {
        ToastUtil.show(mContext,"position:"+position);
    }

    static class ViewHolder {
        @BindView(item_underline)
        TextView itemUnderline;
        @BindView(R.id.tv_phone_number)
        TextView tvPhoneNumber;
        @BindView(R.id.tv_last_date)
        TextView tvLastDate;
        @BindView(R.id.tv_add)
        TextView tvAdd;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
