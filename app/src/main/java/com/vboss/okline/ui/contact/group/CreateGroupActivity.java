package com.vboss.okline.ui.contact.group;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.easeui.Constant;
import com.hyphenate.easeui.present.ChatPresent;
import com.hyphenate.easeui.utils.ChatUserUtil;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.base.OKLineApp;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.data.ContactRepository;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.ui.contact.ContactComparator;
import com.vboss.okline.ui.contact.ContactsFragment;
import com.vboss.okline.ui.contact.ContactsUtils;
import com.vboss.okline.ui.contact.bean.ContactItem;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.StringUtils;
import com.vboss.okline.utils.TextUtils;
import com.vboss.okline.utils.ToastUtil;
import com.vboss.okline.view.widget.SideBar;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static android.content.ContentValues.TAG;
import static android.widget.ImageView.ScaleType.FIT_CENTER;
import static com.vboss.okline.R.id.iv_search;
import static com.vboss.okline.R.id.tv_search_key;


/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/3/31 17:01
 * Desc :
 */

public class CreateGroupActivity extends BaseActivity implements CreateGroupContract.View, ChatPresent {
    public static final int OPTION_CREATE = 0;
    public static final int OPTION_ADD_MEMBERS = 1;
    public static final int OPTION_DELETE_MEMBERS = 2;
    private static final String TAG = "CreateGroupActivity";
    public List<ContactItem> allUserList;
    public List<String> addList = new ArrayList<String>();
    //    @BindView(R.id.recycleView_top)
//    RecyclerView recycleViewTop;
    @BindView(R.id.ll_top)
    LinearLayout llTop;
    @BindView(R.id.lv_group_choose)
    ListView lvGroupChoose;
    @BindView(R.id.sidebar)
    SideBar sidebar;
    @BindView(iv_search)
    ImageView ivSearch;
    @BindView(R.id.linearLayoutMenu)
    LinearLayout linearLayoutMenu;
    @BindView(R.id.horizonMenu)
    HorizontalScrollView horizonMenu;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_search_count)
    TextView tvSearchCount;
    @BindView(R.id.layout_count)
    LinearLayout layoutCount;
    @BindView(R.id.toolbar_contact)
    FragmentToolbar toolbar;
    @BindView(R.id.no_result)
    RelativeLayout noReslt;
    @BindView(R.id.search_tv)
    TextView searchTv;
    private List<ContactItem> checkedList = new ArrayList<>();
    private CreateGroupPresenter presenter;
    private ChatUserUtil chatUserUtil;
    //    private ContactAdapter adapter;
    private GroupAdapter adapter;
    private int total = 0;
    private int editTextWidth;
    private int groupId;
    private ArrayList<ContactItem> oldMembers = new ArrayList<>();
    private int itemWidth;
    private int marginLeft;
    private String easeGroupId;
    private int option = OPTION_CREATE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        ButterKnife.bind(this);
        option = getIntent().getIntExtra("option", OPTION_CREATE);
        groupId = getIntent().getIntExtra(ContactItem.EXTRA_OL_GROUP_ID, 0);
        easeGroupId = getIntent().getStringExtra(Constant.EXTRA_USER_ID);
        oldMembers = getIntent().getParcelableArrayListExtra(ContactItem.EXTRA_MEMBERS);
        initToolbar();
        sidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                if (adapter != null) {
                    int position = adapter.getPositionForSection(s.charAt(0));
                    if (position != -1) {
                        lvGroupChoose.setSelection(position);
                    }
                }
            }
        });
        presenter = new CreateGroupPresenter(this, new CreateGroupModel(), this);
        chatUserUtil = new ChatUserUtil(this);
        presenter.initContent();
        testTopAvatar();
        initTopView();
        itemWidth = getResources().getDimensionPixelSize(R.dimen.contact_item_avatar_width);
        marginLeft = StringUtils.dip2px(this, 4);

    }

    private void initTopView() {

    }

    private void testTopAvatar() {
        //搜索栏搜索
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //add by linzhangbin 2017/6/20 统一搜索结果显示 start
                if (s.length() > 0){
                    final String str_s = etSearch.getText().toString().trim();
                    ContactRepository.getInstance(OKLineApp.context).searchContact(str_s)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new DefaultSubscribe<List<ContactEntity>>(TAG){
                                @Override
                                public void onNext(List<ContactEntity> list) {
                                    Timber.tag(TAG).i("ContactSearchPresenter: "+list.size());
                                    List<ContactItem> itemList = new ArrayList<>();
                                    for (ContactEntity entity : list) {
                                        if (entity.relationState()>1){
                                            ContactItem item = ContactsUtils.contactEtity2contactItem(entity);
                                            itemList.add(item);
                                        }
                                    }
                                    if (itemList.size() == 0){
                                        noReslt.setVisibility(View.VISIBLE);
                                        String str = String.format(getResources().getString(R.string.card_search_key), str_s);
                                        SpannableString ss = new SpannableString(str);
                                        ss.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(
                                                CreateGroupActivity.this, R.color.color_card_search_blue)),
                                                str.indexOf("：") + 1, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        searchTv.setText(ss);
                                        lvGroupChoose.setVisibility(View.GONE);
                                    }else{
                                        lvGroupChoose.setVisibility(View.VISIBLE);
                                        noReslt.setVisibility(View.GONE);

                                        adapter.refresh(itemList);
                                    }
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    super.onError(throwable);
                                }
                            });


                }else{
                    adapter.refresh(allUserList);
                    lvGroupChoose.setVisibility(View.VISIBLE);
                    noReslt.setVisibility(View.GONE);
                }

                //add by linzhangbin 2017/6/20 统一搜索结果显示 end
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lvGroupChoose.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView<?> parent, View view,
                                    final int position, long id) {

                if (lvGroupChoose.isItemChecked(position)) {
                    showCheckImage(adapter.getItem(position).getAvatar(), adapter.getItem(position));
                    checkedList.add(adapter.getItem(position));
                    Timber.tag(TAG).i("%s checked, checkedList size: %s", position, checkedList.size());
                } else {
                    deleteImage(adapter.getItem(position));
                    checkedList.remove(adapter.getItem(position));
                    Timber.tag(TAG).i("%s notChecked, checkedList size: %s", position, checkedList.size());
                }
                adapter.notifyDataSetChanged();
            }
        });
    }


    private void initToolbar() {
        toolbar.setActionTitle(getResources().getString(R.string.choose_contact));
        toolbar.setNavigationVisible(View.VISIBLE);
        toolbar.setActionMenuVisible(View.VISIBLE);
        toolbar.setActionMenuIcon(R.mipmap.confirm);
        toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
            @Override
            public void onNavigation(View v) {
                TextUtils.showOrHideSoftIM(etSearch, false);
                finish();
            }
        });
        toolbar.setOnActionMenuClickListener(new FragmentToolbar.OnActionMenuClickListener() {
            @Override
            public void onActionMenu(View v) {
                Timber.tag(TAG).i("create group clicked...");
                toolbar.setClickable(false);
                switch (option) {
                    case OPTION_CREATE:
                        createGroup();
                        break;
                    case OPTION_ADD_MEMBERS:
                        presenter.addMembers(groupId, easeGroupId, checkedList);
                        break;
                    case OPTION_DELETE_MEMBERS:
                        presenter.removeMemers(groupId, easeGroupId, checkedList);
                        break;
                }
            }
        });
    }

    private void createGroup() {
        try {
            //如果选中的是单个人走单人聊天,两人以上走创建群聊
            if (checkedList.isEmpty()) {
                Utils.customToast(getApplicationContext(), getString(R.string.please_choose_member)
                        , Toast.LENGTH_SHORT).show();
            } else if (checkedList.size() == 1) {
                String realName = adapter.getItem(lvGroupChoose.getCheckedItemPosition()).getRealName();
                String remark = adapter.getItem(lvGroupChoose.getCheckedItemPosition()).getRemark();
                String olNo = adapter.getItem(lvGroupChoose.getCheckedItemPosition()).getOlNo();
                if (!StringUtils.isNullString(remark) && !StringUtils.isNullString(olNo)) {
                    ContactsUtils.personalChat(CreateGroupActivity.this, realName,remark, olNo, null, null);
                } else {
                    ToastUtil.show(getApplicationContext(), R.string.text_contact_chat);
                }
            } else {
                presenter.createGroup(checkedList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showContent(List<ContactItem> list) {
        switch (option) {
            case OPTION_ADD_MEMBERS:
                list.removeAll(oldMembers);
                break;
            case OPTION_DELETE_MEMBERS:
                list.clear();
                list.addAll(oldMembers);
                break;
        }
        Collections.sort(list, new ContactComparator());
        allUserList = list;

        //TEST
       /* for (int i = 0;i<11;i++){
            ContactItem item = new ContactItem();
            item.setSortLetters("VSESE");
            item.setOlNo("OL1"+i);
            item.setRelationState(3);
            item.setRealName("OL2321real"+i);
            item.setRemark("OL2321"+i);
            list.add(item);
        }*/
        Timber.tag(TAG).i("创建群组界面好友数量:::::::::" + list.size());
        adapter = new GroupAdapter(this, list);
        lvGroupChoose.setAdapter(adapter);
    }

    @Override
    public void enterGroup(ContactItem item) {
        finish();
        ContactsFragment.isRefresh = true;
        toolbar.setClickable(true);
        ContactsUtils.groupChat(this, item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

    @Override
    public void createGroupError() {
        toolbar.setClickable(true);
    }

    @Override
    public void addMemberResult(Boolean result) {
        if (result) {
            setResult(RESULT_OK);
            finish();
        }
    }

    //显示选择的头像
    private void showCheckImage(String url, ContactItem glufineid) {
        Timber.tag(TAG).i("showCheckImage");
        total++;
        // 包含TextView的LinearLayout
        // 参数设置
        View view = LayoutInflater.from(this).inflate(R.layout.header_item, null);
        SimpleDraweeView images = (SimpleDraweeView) view.findViewById(R.id.iv_avatar);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(itemWidth, itemWidth);
        layoutParams.setMarginStart(marginLeft);
        view.setLayoutParams(layoutParams);
        images.setScaleType(FIT_CENTER);
        linearLayoutMenu.addView(view);
        int length;
        if (total > 5) {
            length = 6;
        } else {
            length = total;
        }
        Timber.tag(TAG).i("length %s", length);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (itemWidth + marginLeft) * length,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        Timber.tag(TAG).i("param width %s", params.width);
        horizonMenu.setLayoutParams(params);
        horizonMenu.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
        // 设置id，方便后面删除
        view.setTag(glufineid);
            //add by linzhangbin 2017/6/9 不可信的好友不显示头像
        if (/*bitmap == null*/StringUtils.isNullString(url) || glufineid.getRelationState() != 3) {
            //add by linzhangbin 2017/6/9 不可信的好友不显示头像
            images.setImageResource(R.mipmap.default_avatar);
        } else {
            //modify by linzhangbin 上方与下方选择的头像统一
            images.setImageURI(Uri.parse(url));
            //modify by linzhangbin 上方与下方选择的头像统一 end
        }

        if (total > 0) {
            if (ivSearch.getVisibility() == View.VISIBLE) {
                ivSearch.setVisibility(View.GONE);
            }
        }
        addList.add(glufineid.getRemark());
        if (total == 6) {
            editTextWidth = etSearch.getWidth();
            Timber.tag(TAG).i("editTextWidth" + editTextWidth);

        }
    }

    //删除选择的头像
    public void deleteImage(ContactItem contact) {
        Timber.tag(TAG).i("deleteImage");
        View view = (View) linearLayoutMenu.findViewWithTag(contact);

        linearLayoutMenu.removeView(view);
        total--;
        addList.remove(contact.getRemark());
        int length = total;

        //add by linzhangbin 取消勾选时上方也最多显示6个头像 start
        if (total > 5) {
            length = 6;
        } else {
            length = total;
        }
        //add by linzhangbin 取消勾选时上方也最多显示6个头像 end
        LinearLayout.LayoutParams params;
        if (total < 1) {
            if (ivSearch.getVisibility() == View.GONE) {
                ivSearch.setVisibility(View.VISIBLE);
            }
            params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
        } else {
            params = new LinearLayout.LayoutParams(
                    (itemWidth + marginLeft) * length,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
        }

        Timber.tag(TAG).i("length %s", length);

        Timber.tag(TAG).i("param width %s", params.width);
        horizonMenu.setLayoutParams(params);
    }


    /**
     * adapter
     */
    public class GroupAdapter extends BaseAdapter {

        private Context context;
        private List<ContactItem> list = new ArrayList<>();


        GroupAdapter(Context context, List<ContactItem> list) {
            this.context = context;
            if (null != list && list.size() > 0) {
                this.list = list;
            }
        }

        public void refresh(List<ContactItem> list) {
            if (list == null) {
                list = new ArrayList<>();
            }
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return this.list.size();
        }

        @Override
        public ContactItem getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ContactItem item = getItem(position);
            Timber.tag(TAG).i(item.toString());
//            String firstLetter = ContactsUtils.filledData(item.getRemark());
            String firstLetter = item.getSortLetters();

            Timber.tag(TAG).i("firstLetter:" + firstLetter);

            int section = firstLetter.charAt(0);
            Timber.tag(TAG).i("section:" + section);
            final ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.contact_item, null);
                holder = new ViewHolder(convertView);

                //排序,如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
                if (position == getPositionForSection(section)) {
                    holder.tvCatalog.setVisibility(View.VISIBLE);
                    //下划线隐藏
                    holder.itemUnderline.setVisibility(View.GONE);

                    holder.tvCatalog.setText(firstLetter);
                } else {
                    holder.itemUnderline.setVisibility(View.VISIBLE);
                    holder.tvCatalog.setVisibility(View.GONE);

                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position == list.size()){
                holder.bottomLine.setVisibility(View.VISIBLE);
            }

            holder.checkBox.setVisibility(View.VISIBLE);
            //lzb edit 2017/5/9 如果非可信好友显示灰标 并且只显示
            if (item.getRelationState() == 2) {
                holder.ivContactIscheck.setImageResource(R.mipmap.main_not_trusted);
                holder.tvGroupName.setVisibility(View.GONE);
                holder.tvGroupName.setText(item.getRemark());
                holder.tvUserName.setVisibility(View.VISIBLE);
                holder.tvUserName.setText(item.getRemark());
                holder.tvRealName.setVisibility(View.VISIBLE);
                holder.tvRealName.setText(item.getPhoneNum());
                holder.avatar.setImageResource(R.mipmap.default_avatar);
            } else {
                holder.ivContactIscheck.setImageResource(R.mipmap.main_trusted);
                holder.tvGroupName.setVisibility(View.INVISIBLE);
                holder.tvUserName.setVisibility(View.VISIBLE);
                holder.tvRealName.setVisibility(View.VISIBLE);
                holder.tvUserName.setText(item.getRealName());
                holder.tvRealName.setText(item.getRemark());
                if (null != item.getAvatar()) {
                    holder.avatar.setImageURI(Uri.parse(item.getAvatar()));
                } else {
//                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.mipmap.default_avatar));
                    holder.avatar.setImageResource(R.mipmap.default_avatar);
                }
            }
            changeState(position, holder.checkBox);
            return convertView;
        }


        /**
         * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
         */
        public int getPositionForSection(int section) {

            for (int i = 0, m = getCount(); i < m; i++) {
//            char firstChar = getFirstLetter(mList.get(i)).matches("[A-Z]") ? getFirstLetter(mList.get(i)).charAt(0) : '#';
                char firstChar = list.get(i).getSortLetters().charAt(0);
                if (firstChar == section) {
                    return i;
                }
            }
            return -1;
        }

        public void changeState(int position, ImageView imageView) {
            if (lvGroupChoose.isItemChecked(position)) {
                imageView.setBackground(getResources().getDrawable(R.drawable.checkbox_checked));
//                checkedList.add(getItem(position).getOlNo());
//                showCheckImage(getItem(position).getAvatar(), getItem(position));
            } else {
                imageView.setBackground(getResources().getDrawable(R.drawable.checkbox_normal));
//                checkedList.remove(getItem(position).getOlNo());
//                deleteImage(getItem(position));
            }
        }


        class ViewHolder {
            @BindView(R.id.tv_catalog)
            TextView tvCatalog;
            @BindView(R.id.item_underline)
            View itemUnderline;
            @BindView(R.id.iv_contact_avatar)
            SimpleDraweeView avatar;
            @BindView(R.id.iv_contact_ischeck)
            ImageView ivContactIscheck;
            @BindView(R.id.tv_contact_name)
            TextView tvUserName;
            @BindView(R.id.tv_contact_real_name)
            TextView tvRealName;
            @BindView(R.id.tv_group_name)
            TextView tvGroupName;
            @BindView(R.id.checkbox_item)
            ImageView checkBox;
            @BindView(R.id.item_bottom_line)
            View bottomLine;
            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
