package com.vboss.okline.ui.record.person;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.Constant;
import com.hyphenate.easeui.model.EaseContactModel;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.hyphenate.easeui.widget.DefaultSubscribe;
import com.vboss.okline.R;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.data.local.ContactLocalDataSource;
import com.vboss.okline.ui.contact.ContactsUtils;
import com.vboss.okline.ui.im.ChatActivity;
import com.vboss.okline.ui.user.DateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Jiang Zhongyuan <br/>
 * Email   : jiangzhongyuan@okline.cn <br/>
 * Date    : 17/5/11 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {

    public static final String TAG = PersonAdapter.class.getSimpleName();
    protected List<EMConversation> dataList = new ArrayList<>();
//    private List<ContactEntity> contactEntities = new ArrayList<>();
    private Map<String, ContactEntity> contactMap = null;
    private Context context;

    public PersonAdapter(Context context) {
        this.context = context;
        contactMap = EaseContactModel.getInstance().getContactMap();
    }

    public void refresh(@NonNull List<EMConversation> datas) {
        dataList.clear();
        dataList.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_person_item, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        final EMConversation conversation = dataList.get(position);
        final String username = conversation.conversationId();
        holder.phone.setTag(username);
        if (conversation.getType() == EMConversation.EMConversationType.GroupChat) {
            holder.avatar.setImageResource(R.drawable.ease_group_icon);
            EMGroup group = EMClient.getInstance().groupManager().getGroup(username);
            holder.name.setText(group != null ? group.getGroupName() : username);
            holder.remark.setText("");
            holder.phone.setVisibility(View.GONE);
            holder.ivOlVip.setVisibility(View.GONE);
        } else { //update by luoxx 170614 friends relation state
//            holder.avatar.setImageResource(com.hyphenate.easeui.R.drawable.ol_default_avatar);
            ContactEntity contactEntity = getContact(username.toUpperCase());
            if (contactEntity == null) {//非好友，欧乐会员
                holder.name.setText("");
                holder.remark.setText("");
                holder.ivOlVip.setImageResource(R.drawable.ol_vip_logo);
                requestStranger(username,holder.phone,holder.avatar);
                holder.llViewName.setVisibility(View.GONE);
            } else {
                contactMap.put(username, contactEntity);
                int olState = contactEntity.relationState();
                holder.phone.setText(contactEntity.phone());
                holder.avatar.setImageResource(R.drawable.ol_default_avatar);
                if (olState == 2) {//非好友，欧乐会员
                    holder.ivOlVip.setImageResource(R.drawable.ol_vip_logo);
                    holder.name.setVisibility(View.VISIBLE);
                    holder.name.setText(contactEntity.remarkName());
                } else if (olState == 3) {//好友，欧乐会员，
                    holder.ivOlVip.setImageResource(R.drawable.ol_friend_logo);
                    holder.avatar.setImageURI(contactEntity.imgUrl());
                    holder.name.setVisibility(View.VISIBLE);
                    holder.remark.setVisibility(View.VISIBLE);
                    holder.name.setText(contactEntity.realName());
                    holder.remark.setText(String.format(context.getString(R.string.remark_record),contactEntity.remarkName()));
                }
            }
        }
        if (conversation.getUnreadMsgCount() > 0) {
            // show unread message count
            int msgCount = conversation.getUnreadMsgCount();
            if (msgCount > 99)
                msgCount = 99;
            holder.unreadMsgNumber.setText(String.valueOf(msgCount));
            holder.unreadMsgNumber.setVisibility(View.VISIBLE);
        } else {
            holder.unreadMsgNumber.setVisibility(View.INVISIBLE);
        }
        EMMessage message = conversation.getLastMessage();
        if(message == null)return;
        holder.message.setText(EaseSmileUtils.getSmiledText(context,
                EaseCommonUtils.getMessageDigest(message, context)),
                TextView.BufferType.SPANNABLE);
        holder.time.setText(DateUtils.getOrgaListDate(message.getMsgTime()));
        if (message.direct() == EMMessage.Direct.SEND
                && message.status() == EMMessage.Status.FAIL) {
            holder.msgState.setVisibility(View.VISIBLE);
        } else {
            holder.msgState.setVisibility(View.GONE);
        }

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick(conversation);
            }
        });
    }

    private void itemClick(EMConversation conversation) {
        String userId = conversation.conversationId();
        ContactEntity entity = contactMap.get(userId.toUpperCase());
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constant.EXTRA_USER_ID, userId);
        if (conversation.isGroup()) {
            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
            context.startActivity(intent);
        } else if (entity != null) {
            ContactsUtils.personalChat(context,entity.realName(),entity.remarkName(),userId.toUpperCase(),entity.phone(),entity.imgUrl());
        }
    }

    private ContactEntity getContact(String olno) {
        if (contactMap!=null&&contactMap.get(olno) != null) {
            return contactMap.get(olno);
        }
//        for (ContactEntity contactEntity : contactEntities) {
//            if (olno.toUpperCase().equals(contactEntity.friendOlNo())) {
//                return contactEntity;
//            }
//        }
        return null;
    }

    private void requestStranger(final String olno, final TextView phoneView,final SimpleDraweeView avatar) {
        phoneView.setTag(olno);
        UserRepository.getInstance(context).getOKLineMember(olno.toUpperCase())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<User>(TAG) {
                    @Override
                    public void onNext(final User user) {
                        ContactEntity entity = ContactEntity.newBuilder()
                                .friendOlNo(user.getOlNo())
                                .realName(user.getRealName())
                                .phone(user.getPhone())
//                                .remarkName(user.getRemarkName())
                                .imgUrl(user.getAvatar())
                                .relationState(0)
                                .build();
                        notifyDataSetChanged();
                        contactMap.put(olno.toUpperCase(), entity);
                        if (olno.equals(phoneView.getTag())) {
//                            nameView.setText(entity.realName());
//                            remarkView.setText(entity.remarkName());
                            phoneView.setText(entity.phone());
                            avatar.setImageResource(R.drawable.ol_default_avatar);

                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class PersonViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.avatar)
        SimpleDraweeView avatar;
        @BindView(R.id.iv_ol_vip)
        ImageView ivOlVip;
        @BindView(R.id.avatar_container)
        RelativeLayout avatarContainer;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.remark)
        TextView remark;
        @BindView(R.id.phone)
        TextView phone;
        @BindView(R.id.unread_msg_number)
        TextView unreadMsgNumber;
        @BindView(R.id.msg_state)
        ImageView msgState;
        @BindView(R.id.message_icon)
        ImageView messageIcon;
        @BindView(R.id.message)
        TextView message;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.container)
        View container;
        @BindView(R.id.ll_view_name)
        View llViewName;

        PersonViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
