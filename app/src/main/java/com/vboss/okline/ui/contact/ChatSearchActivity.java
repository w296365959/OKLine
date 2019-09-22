package com.vboss.okline.ui.contact;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.data.ContactRepository;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.data.local.UserLocalDataSource;
import com.vboss.okline.ui.contact.adapter.ChatSearchAdapter;
import com.vboss.okline.ui.contact.callPhone.DialActivity;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.StringUtils;
import com.vboss.okline.utils.TextUtils;
import com.vboss.okline.utils.ToastUtil;
import com.vboss.okline.view.widget.ClearEditText;
import com.vboss.okline.view.widget.CommonDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.okline.icm.libary.StringUtil;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static android.content.ContentValues.TAG;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/5/9 11:52
 * Desc :
 */

public class ChatSearchActivity extends BaseActivity {
    private static final String TAG = "ChatSearchActivity";
    private static final int REFRESH_LIST = 12;

    /**
     * ActionBar bind View
     */
    @BindView(R.id.toolbar_contact)
    FragmentToolbar toolbar;
    @BindView(R.id.et_keyboard_top)
    ClearEditText etKeyboardTop;
    @BindView(R.id.lv_chat_content)
    ListView lvChatContent;
    @BindView(R.id.bt_chat_now)
    Button btChatNow;
    @BindView(R.id.ll_chat_current)
    LinearLayout llChatCurrent;
    @BindView(R.id.tv_noContact)
    TextView tvNoContact;
    private ContactRepository repository;
    private Subscription sbpAllContact;
    private ChatSearchAdapter adapter;
    private List<ContactEntity> filterList;
    private User user;
    private List<ContactEntity> conList;
    private String input;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_search_activity);
        ButterKnife.bind(this);
        conList = new ArrayList<>();
        user = UserLocalDataSource.getInstance().getUser();
        repository = ContactRepository.getInstance(this);
        lvChatContent.setVisibility(View.INVISIBLE);
        adapter = new ChatSearchAdapter(conList, this);
        Timber.tag(TAG).i("oncreate:" + conList.size());
        lvChatContent.setAdapter(adapter);
        initToolbar();
        getVipList();
        btChatNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //fix bug 1403
                if (input.trim().equals(user.getPhone())){
                    ToastUtil.show(ChatSearchActivity.this,R.string.can_not_talk_yourself);
                }else{
                    startChat();
                }
                //fix bug 1403
            }
        });
        lvChatContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                Timber.tag(TAG).i("OnItemClickListener:" + filterList.get(i).toString());
                //modify by linzhangbin 2017/6/15 聊天搜索匹配所有联系人
                if (filterList.get(i).relationState()<2){
                    CommonDialog commonDialog = new CommonDialog(ChatSearchActivity.this);
                    commonDialog.setTitleVisible(View.GONE);
                    commonDialog.setCanceledOnTouchOutside(true);
                    commonDialog.setContent(getString(R.string.notVip_call_chat_dial));
                    commonDialog.setNegativeButton(getString(R.string.notVip_negative_text_dial));
                    commonDialog.setPositiveButton(getString(R.string.notVip_local_text));
                    commonDialog.setOnDialogClickListener(new CommonDialog.OnDialogInterface() {
                        @Override
                        public void cancel(View view, CommonDialog dialog) {
                            ContactsUtils.normalChat(filterList.get(i).phone(), ChatSearchActivity.this, user.getRealName(), user.getPhone());
                            dialog.dismiss();
                        }

                        @Override
                        public void ensure(View view, CommonDialog dialog) {
                            //重试
                            ContactsUtils.normalChat(filterList.get(i).phone(), ChatSearchActivity.this);
                            dialog.dismiss();
                        }
                    });
                    commonDialog.show();
                }else{
                    String phone = filterList.get(i).phone();
                    String realName = filterList.get(i).realName();
                    String remarkName = filterList.get(i).remarkName();
                    String olNo = filterList.get(i).friendOlNo();
                    String avatar = filterList.get(i).imgUrl();
                    ContactsUtils.personalChat(ChatSearchActivity.this, realName, remarkName, olNo, phone, avatar);
                }
                //modify by linzhangbin 2017/6/15 聊天搜索匹配所有联系人
            }
        });


        etKeyboardTop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Timber.tag(TAG).i("onTextChanged:");
                input = s.toString();
                filterList = ContactsUtils.filterList(input, conList);
//                Timber.tag(TAG).i("input:" + input);
//                Timber.tag(TAG).i("conListSize:" + conList.size());
//                Timber.tag(TAG).i("list:" + filterList.size());
                adapter.refresh(filterList);
                if (TextUtils.isPhoneNumber(input) && filterList.isEmpty()) {
                    llChatCurrent.setVisibility(View.VISIBLE);
                    tvNoContact.setVisibility(View.GONE);
                } else if (!TextUtils.isPhoneNumber(input) && filterList.isEmpty() && !StringUtils.isNullString(s.toString())) {
                    llChatCurrent.setVisibility(View.GONE);
                    tvNoContact.setVisibility(View.VISIBLE);
                } else {
                    llChatCurrent.setVisibility(View.GONE);
                    tvNoContact.setVisibility(View.GONE);
                }
                if (TextUtils.isEmpty(input)){
                    lvChatContent.setVisibility(View.GONE);
                }else{
                    lvChatContent.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    /**
     * 发起聊天
     */
    private void startChat() {
        UserRepository.getInstance(ChatSearchActivity.this).getOKLineMember(input)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<User>(TAG) {
                    @Override
                    public void onNext(User user) {

                        ContactsUtils.personalChat(ChatSearchActivity.this, user.getRealName(), "",
                                user.getOlNo(), user.getPhone(), user.getAvatar());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        CommonDialog commonDialog = new CommonDialog(ChatSearchActivity.this);
                        commonDialog.setTitleVisible(View.GONE);
                        commonDialog.setCanceledOnTouchOutside(true);
                        commonDialog.setContent(getString(R.string.notVip_call_chat));
                        commonDialog.setNegativeButton(getString(R.string.notVip_negative_text));
                        commonDialog.setPositiveButton(getString(R.string.notVip_positive_chat_text));
                        commonDialog.setOnDialogClickListener(new CommonDialog.OnDialogInterface() {
                            @Override
                            public void cancel(View view, CommonDialog dialog) {
                                ContactsUtils.normalChat(input, ChatSearchActivity.this, user.getRealName(), user.getPhone());
                                dialog.dismiss();
                            }

                            @Override
                            public void ensure(View view, CommonDialog dialog) {
                                //重试
                                ContactsUtils.normalChat(input, ChatSearchActivity.this);
                                dialog.dismiss();
                            }
                        });
                        commonDialog.show();
                    }
                });
    }

    /**
     * 得到欧乐会员集合
     */
    private void getVipList() {
        sbpAllContact = repository.getAllContact()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<List<ContactEntity>>(TAG) {
                    @Override
                    public void onNext(List<ContactEntity> entities) {
//                        for (ContactEntity entity : entities) {
//                            if (entity.relationState() == 2 || entity.relationState() == 3) {

                                conList.addAll(entities);
//                            }
//                        }

                    }
                });

    }

    private void initToolbar() {
        toolbar.setActionTitle(getResources().getString(R.string.title_chat_search));
        toolbar.setNavigationVisible(View.VISIBLE);
        toolbar.setActionMenuVisible(View.GONE);
        toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
            @Override
            public void onNavigation(View v) {
                TextUtils.showOrHideSoftIM(etKeyboardTop, false);
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sbpAllContact.unsubscribe();
    }
}
