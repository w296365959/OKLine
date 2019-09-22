package com.vboss.okline.ui.contact.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.data.entities.ContactEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/6/3 17:13
 * Desc :
 */

public class DialListAdapter extends BaseAdapter {
    private List<ContactEntity> list = new ArrayList<>();
    private Context context;

    public DialListAdapter(List<ContactEntity> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void refresh(List<ContactEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.dial_list_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ContactEntity entity = list.get(position);
        if (entity.relationState()==3){
            holder.tvContactName.setText(entity.realName());
        }else{
            holder.tvContactName.setText(entity.remarkName());
        }
        holder.tvContactPhone.setText(entity.phone());


        return view;
    }

    static class ViewHolder {
        @BindView(R.id.tv_contact_name)
        TextView tvContactName;
        @BindView(R.id.tv_contact_phone)
        TextView tvContactPhone;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
