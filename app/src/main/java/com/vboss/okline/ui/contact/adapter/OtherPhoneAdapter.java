package com.vboss.okline.ui.contact.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vboss.okline.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/7/3 11:54
 * Desc :
 */

public class OtherPhoneAdapter extends BaseAdapter {
    private Context context;
    private List<String> phoneList;

    public OtherPhoneAdapter(Context context, List<String> phoneList) {
        this.context = context;
        this.phoneList = phoneList;
    }

    @Override
    public int getCount() {
        return phoneList.size();
    }

    @Override
    public Object getItem(int i) {
        return phoneList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.other_phone_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvOtherPhone.setText(phoneList.get(i));

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.tv_other_phone)
        TextView tvOtherPhone;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
