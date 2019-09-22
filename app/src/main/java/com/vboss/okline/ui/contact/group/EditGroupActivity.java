package com.vboss.okline.ui.contact.group;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.Constant;
import com.hyphenate.exceptions.HyphenateException;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.EventToken;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.base.helper.ToolbarHelper;
import com.vboss.okline.data.local.UserLocalDataSource;
import com.vboss.okline.ui.contact.bean.ContactItem;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static android.content.ContentValues.TAG;
import static com.vboss.okline.R.id.toolbar;


/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Jiang Zhongyuan
 * Email : zhongyuan@okline.cn
 * Date : 2017/4/24 18:40
 * Desc : 群组成员管理
 */

public class EditGroupActivity extends BaseActivity implements EditGroupContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_exit)
    TextView tvExit;
    @BindView(R.id.toolbar_edit_group)
    FragmentToolbar toolbar;

    private EditGroupPresenter presenter;
    private MemberAdapter adapter;
    private int groupId;
    private String easeGroupId;
    private String groupName;
    private boolean isOwner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);
        ButterKnife.bind(this);
        initRecyclerView();
        groupId = getIntent().getIntExtra(ContactItem.EXTRA_OL_GROUP_ID, 0);
        easeGroupId = getIntent().getStringExtra(Constant.EXTRA_USER_ID);
        groupName = getIntent().getStringExtra(Constant.EXTRA_REAL_NAME);
        initToolbar();
        presenter = new EditGroupPresenter(this, new EditGroupModel(), this, groupId);
        presenter.initContent();

        showLocalGroupInfo();
        showRemoteGroupInfo();
    }

    private void showLocalGroupInfo() {
        String current = EMClient.getInstance().getCurrentUser();
        //根据群组ID从本地获取群组基本信息
        EMGroup group = EMClient.getInstance().groupManager().getGroup(easeGroupId);
        if (group != null) {
            String owner = group.getOwner();//获取群主
            isOwner = current.equals(owner);
            Timber.tag("EditGroupActivity").d("group info : " + easeGroupId + " , " + owner + " , " + current);
        }
    }

    private void showRemoteGroupInfo() {
        new Thread() {
            @Override
            public void run() {
                try {
                    //根据群组ID从服务器获取群组基本信息
                    EMGroup group = EMClient.getInstance().groupManager().getGroupFromServer(easeGroupId);
                    String owner = group.getOwner();//获取群主
                    String current = EMClient.getInstance().getCurrentUser();
                    Timber.tag("EditGroupActivity").d("group info remote : " + easeGroupId + ", " + owner + " , " + current);
                    List<String> members = group.getMembers();//获取内存中的群成员
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void initToolbar() {
        toolbar.setActionTitle(groupName);
        toolbar.setActionMenuVisible(View.GONE);
        toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
            @Override
            public void onNavigation(View v) {
                finish();
            }
        });
    }

    private void initRecyclerView() {
        adapter = new MemberAdapter();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 5);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showContent(List<ContactItem> list) {
        List<ContactItem> itemList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            itemList.addAll(list);
        }
        ContactItem contactItem = new ContactItem();
        itemList.add(contactItem);
        if (isOwner) {
            itemList.add(contactItem);
        }
        adapter.refreshDatas(itemList);
    }

    private void membersOption(String easeGroupId, List<ContactItem> members, int option) {
        List<ContactItem> memberList = new ArrayList<>();
        for (ContactItem item : members) {
            if (!TextUtils.isEmpty(item.getOlNo())) {
                if (option == CreateGroupActivity.OPTION_DELETE_MEMBERS) {
                    String olMy = UserLocalDataSource.getInstance().getOlNo();
                    if (!olMy.equals(item.getOlNo())) {
                        memberList.add(item);
                    }
                } else {
                    memberList.add(item);
                }
            }
        }
        Intent intent = new Intent(this, CreateGroupActivity.class);
        intent.putExtra(ContactItem.EXTRA_OL_GROUP_ID, groupId);
        intent.putExtra(Constant.EXTRA_USER_ID, easeGroupId);
        intent.putParcelableArrayListExtra(ContactItem.EXTRA_MEMBERS, new ArrayList<Parcelable>(memberList));
        intent.putExtra("option", option);
        startActivity(intent);
    }

    @OnClick(R.id.tv_exit)
    public void onClick() {
        new AlertDialog.Builder(this)
                .setMessage("确定退出群聊？")
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.leaveGroup(easeGroupId);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();

    }

    @Subscribe(tags = {@Tag(EventToken.GROUP_LEAVE)})
    public void onLeaveGroup(Boolean bool) {
        ToastUtil.show(this, R.string.exit_group_success);
        finish();
    }

    @Subscribe(tags = {
            @Tag(EventToken.GROUP_ADD_MEMBERS),
            @Tag(EventToken.GROUP_DELETE_MEMBER)})
    public void onAddMembers(Boolean bool) {
        presenter.initContent();
    }

    public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {
        List<ContactItem> dataList = new ArrayList<>();

        public MemberAdapter() {
        }

        public void refreshDatas(List<ContactItem> datas) {
            dataList.clear();
            dataList.addAll(datas);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_group_member_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            if (position >= dataList.size()) {
                return;
            }
            final ContactItem item = dataList.get(position);
            holder.tvName.setVisibility(View.GONE);
            holder.tvRemarkName.setVisibility(View.GONE);
            if (position == dataList.size() - 1) {
                if (isOwner) {
                    holder.ivAvatar.setImageResource(R.mipmap.icon_member_delete);
                } else {
                    holder.ivAvatar.setImageResource(R.mipmap.icon_member_add);
                }
            } else if (position == dataList.size() - 2 && isOwner) {
                holder.ivAvatar.setImageResource(R.mipmap.icon_member_add);
            } else {
                holder.tvName.setVisibility(View.VISIBLE);
                holder.tvRemarkName.setVisibility(View.VISIBLE);
                holder.tvName.setText(item.getRealName());
                holder.tvRemarkName.setText(item.getRemark());
                holder.ivAvatar.setImageURI(item.getAvatar());
            }
            holder.llContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isOwner) {
                        if (position == dataList.size() - 2) {
                            membersOption(easeGroupId, dataList, CreateGroupActivity.OPTION_ADD_MEMBERS);
                        } else if (position == dataList.size() - 1) {
                            membersOption(easeGroupId, dataList, CreateGroupActivity.OPTION_DELETE_MEMBERS);
                        }
                    } else {
                        if (position == dataList.size() - 1) {
                            membersOption(easeGroupId, dataList, CreateGroupActivity.OPTION_ADD_MEMBERS);
                        }
                    }
                }
            });
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.iv_avatar)
            SimpleDraweeView ivAvatar;
            @BindView(R.id.tv_name)
            TextView tvName;
            @BindView(R.id.tv_remark_name)
            TextView tvRemarkName;
            @BindView(R.id.ll_container)
            View llContainer;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}
