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
import com.hyphenate.easeui.model.EaseContactModel;
import com.vboss.okline.R;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.ContactRepository;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.ui.contact.ContactsFragment;
import com.vboss.okline.utils.StringUtils;
import com.vboss.okline.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static android.content.ContentValues.TAG;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/5/3 21:36
 * Desc : 新的朋友adapter
 */

public class NewFriendAdapter extends BaseAdapter {
    private static final String TAG = "NewFriendAdapter";
    private List<ContactEntity> mList;
    private Context mContext;

    public NewFriendAdapter(List<ContactEntity> list, Context context) {
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
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ContactEntity entity = mList.get(position);

        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.new_friend_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (position != 0){
            holder.itemUnderline.setVisibility(View.VISIBLE);
        }
        holder.tvPhone.setText(entity.phone());
        holder.tvContactName.setText(entity.realName());
        if (StringUtils.isNullString(entity.imgUrl())){
            holder.ivContactAvatar.setImageResource(R.mipmap.default_avatar);
        }else{
            //lzb test for shenzhen
//            holder.ivContactAvatar.setImageResource(R.mipmap.default_avatar);
            holder.ivContactAvatar.setImageURI(Uri.parse(entity.imgUrl()));
        }
        holder.tvAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ContactsFragment.isRefresh = true;
                addNewFriend(entity);
            }
        });
        Timber.tag(TAG).i(entity.toString());
        return view;
    }


    private void addNewFriend(ContactEntity entity){

       ContactRepository.getInstance(mContext).agreeToApplicant(entity.friendOlNo())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<ContactEntity>(TAG){
                    @Override
                    public void onNext(ContactEntity entity) {
                        ToastUtil.show(mContext,"添加好友成功");
                        ContactsFragment.isRefresh = true;
                        mList.remove(entity);
                        notifyDataSetChanged();
                        Timber.tag(TAG).i("添加的好友:"+entity.toString());
                        //add by luoxx 170620 更新IM好友
                        EaseContactModel.getInstance().updateItem(entity.friendOlNo(),entity);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        ToastUtil.show(mContext,"添加好友失败");
                    }
                });
    }

    static class ViewHolder {
        @BindView(R.id.item_underline)
        TextView itemUnderline;
        @BindView(R.id.iv_contact_avatar)
        SimpleDraweeView ivContactAvatar;
        @BindView(R.id.iv_contact_ischeck)
        ImageView ivContactIscheck;
        @BindView(R.id.tv_contact_name)
        TextView tvContactName;
        @BindView(R.id.tv_phone)
        TextView tvPhone;
        @BindView(R.id.tv_group_name)
        TextView tvGroupName;
        @BindView(R.id.tv_add_new)
        TextView tvAddNew;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
