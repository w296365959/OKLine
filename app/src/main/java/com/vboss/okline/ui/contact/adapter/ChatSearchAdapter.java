package com.vboss.okline.ui.contact.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.ui.contact.ContactsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/5/10 21:36
 * Desc :
 */

public class ChatSearchAdapter extends BaseAdapter {
    private List<ContactEntity> list = new ArrayList<>();
    private Context context;

    public ChatSearchAdapter(List<ContactEntity> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void refresh(List<ContactEntity> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.contact_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        ContactEntity entity = list.get(position);
        int relationState = entity.relationState();

        //modify by linzhangbin 2017/6/15 修改搜索联系人页面展示
        if (relationState==3){
            holder.ivContactIscheck.setVisibility(View.VISIBLE);
            holder.ivContactIscheck.setImageResource(R.mipmap.main_trusted);
            //add by linzhangbin 2017/7/3 显示可信好友头像 start
            holder.ivContactAvatar.setImageURI(Uri.parse(entity.imgUrl()));
            //add by linzhangbin 2017/7/3 显示可信好友头像 end
            //add by linzhangbin 2017/7/6 显示姓名(备注)的格式 start
            holder.tvContactName.setText(ContactsUtils.realNameNremarkName(entity.realName(),entity.nickName()));
            holder.tvContactRealName.setText(entity.phone());
            //add by linzhangbin 2017/7/6 显示姓名(备注)的格式 end
        }else if(relationState==2){
            holder.ivContactIscheck.setVisibility(View.VISIBLE);
            holder.ivContactIscheck.setImageResource(R.mipmap.main_not_trusted);
            holder.ivContactAvatar.setImageResource(R.mipmap.default_avatar);
            //add by linzhangbin 2017/7/6 显示姓名(备注)的格式 start
            holder.tvContactName.setText(ContactsUtils.realNameNremarkName(entity.remarkName(),entity.nickName()));
            holder.tvContactRealName.setText(entity.phone());
            //add by linzhangbin 2017/7/6 显示姓名(备注)的格式 end
        }else{
            holder.ivContactIscheck.setVisibility(View.GONE);
            holder.ivContactAvatar.setImageResource(R.mipmap.default_avatar);
            //add by linzhangbin 2017/7/6 显示姓名(备注)的格式 start
            holder.tvContactName.setText(ContactsUtils.realNameNremarkName(entity.remarkName(),entity.nickName()));
            holder.tvContactRealName.setText(entity.phone());
            //add by linzhangbin 2017/7/6 显示姓名(备注)的格式 end
        }
        holder.tvCatalog.setVisibility(View.GONE);
        holder.tvGroupName.setVisibility(View.GONE);
        holder.itemUnderline.setVisibility(View.VISIBLE);
        //modify by linzhangbin 2017/6/15 修改搜索联系人页面展示 end


        return view;
    }

    static class ViewHolder {
        @BindView(R.id.tv_catalog)
        TextView tvCatalog;
        @BindView(R.id.item_underline)
        View itemUnderline;
        @BindView(R.id.checkbox_item)
        ImageView checkboxItem;
        @BindView(R.id.iv_contact_avatar)
        SimpleDraweeView ivContactAvatar;
        @BindView(R.id.iv_contact_ischeck)
        ImageView ivContactIscheck;
        @BindView(R.id.tv_contact_name)
        TextView tvContactName;
        @BindView(R.id.tv_group_name)
        TextView tvGroupName;
        @BindView(R.id.tv_contact_real_name)
        TextView tvContactRealName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
