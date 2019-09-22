package com.vboss.okline.ui.contact.callPhone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.data.local.ContactLocalDataSource;
import com.vboss.okline.data.local.UserLocalDataSource;
import com.vboss.okline.ui.contact.ContactsUtils;
import com.vboss.okline.ui.contact.adapter.CallLogAdapter;
import com.vboss.okline.ui.contact.adapter.DialListAdapter;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.AppUtil;
import com.vboss.okline.utils.TimeUtils;
import com.vboss.okline.utils.ToastUtil;
import com.vboss.okline.view.widget.CommonDialog;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/5/3 20:07
 * Desc :
 */

public class DialActivity extends BaseActivity implements DialContract.View {
    private static final String TAG = "DialActivity";

    @BindView(R.id.toolbar_contact)
    FragmentToolbar toolbar;
    @BindView(R.id.lv_newContact)
    ListView lvNewContact;
    @BindView(R.id.keyboardView)
    KeyboardView keyboardView;
    //拨打电话筛选出来的本地联系人集合
    List<ContactEntity> filterList;
    private ArrayList<Map<String, String>> valueList;
    //电话按钮
    private LinearLayout callPhone;
    //回退按钮
    private LinearLayout back;
    //显示电话号码的编辑框
    private EditText textPhone;
    //进入动画
    private Animation enterAnim;
    //退出动画
    private Animation exitAnim;
    //电话编辑栏的删除按钮
    private LinearLayout deleteBottun;
    private GridView gridView;
    private RelativeLayout dialTop;
    private List<ContactEntity> allContactList = new ArrayList<>();
    /**
     * 电话号码
     */
    private String phoneNum;
    private DialListAdapter dialListAdapter;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dial_activity);
        ButterKnife.bind(this);
        initToolbar();
        initView();
        user = UserLocalDataSource.getInstance().getUser();
        lvNewContact.setVisibility(View.INVISIBLE);
        dialListAdapter = new DialListAdapter(allContactList, this);
        lvNewContact.setAdapter(dialListAdapter);
        valueList = keyboardView.getValueList();
//        testData();
        ContactLocalDataSource.getInstance(this)
                .getAllContact()
                .subscribe(new DefaultSubscribe<List<ContactEntity>>(TAG) {
                    @Override
                    public void onNext(List<ContactEntity> list) {
                        allContactList = list;
                    }
                });
        //add by linzhangbin 2017/6/19 listView条目点击打电话
        lvNewContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ContactEntity item = (ContactEntity) dialListAdapter.getItem(i);
                String phoneNum = item.phone();
                toMakeCall(phoneNum);
            }
        });
        //add by linzhangbin 2017/6/19 listView条目点击打电话 end
    }

    /**
     * 数字键盘显示动画
     */
    private void initAnim() {

        enterAnim = AnimationUtils.loadAnimation(this, R.anim.push_bottom_in);
        exitAnim = AnimationUtils.loadAnimation(this, R.anim.push_bottom_out);
    }

    private void testData() {
        List<CallLogNeedAllBean> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            CallLogNeedAllBean bean = new CallLogNeedAllBean();
            bean.setLastDate(TimeUtils.formatDate(System.currentTimeMillis()));
            if (i % 2 == 0) {
                bean.setFriend(true);
            } else {
                bean.setFriend(false);
            }
            bean.setNumber("1363678663" + i);
            list.add(bean);
        }
        CallLogAdapter adapter = new CallLogAdapter(list, this);
        lvNewContact.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timber.tag(TAG).i("amount: %s", textPhone.getText().toString().trim());
    }

    private void initView() {
        gridView = keyboardView.getGridView();
        callPhone = keyboardView.getCallPhone();
        back = keyboardView.getBack();
        deleteBottun = keyboardView.getTvDeletePhoneNum();
        textPhone = keyboardView.getEditText();
        dialTop = keyboardView.getDialTop();
        // 设置不调用系统键盘
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            textPhone.setInputType(InputType.TYPE_NULL);
        } else {
            this.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus",
                        boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(textPhone, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /**
         * 打电话
         */
        callPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String callNumber = textPhone.getText().toString();

                toMakeCall(callNumber);

            }
        });


        /**
         * 删除按钮
         */
        deleteBottun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //modify by linzhangbin 2017/6/15 数字键盘根据光标位置删除
                int index = textPhone.getSelectionStart();
                Editable editable = textPhone.getText();
                //add by linzhangbin 2017/06/19 从中间删除删到没有数据数组越界的问题
                if (index > 0){
                    editable.delete(index - 1, index);
                }
                //add by linzhangbin 2017/06/19 从中间删除删到没有数据数组越界的问题 end
//                if (amount.length() > 0) {
//                    amount = amount.substring(0, amount.length() - 1);
//                    textPhone.setText(amount);
//
//                    Editable ea = textPhone.getText();
//                    textPhone.setSelection(ea.length());
//                }
                String amount = textPhone.getText().toString().trim();
                Timber.tag(TAG).i("amount: " + amount);
                if (amount.length() < 1) {
                    dialTop.setVisibility(View.GONE);
                }
                //modify by linzhangbin 2017/6/15 数字键盘根据光标位置删除 end
            }
        });

        /**
         * 右下方按钮点击关闭页面
         */
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /**
         * 键盘数字点击事件
         */
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //modify by linzhangbin 2017/6/15 数字键盘根据光标位置增加号码
                int selectionStart = textPhone.getSelectionStart();
                phoneNum = textPhone.getText().insert(selectionStart, valueList.get(position).get("name")).toString();
                Timber.tag(TAG).i("name : " + phoneNum);

//                phoneNum = textPhone.getText().toString().trim();
//                phoneNum = phoneNum + valueList.get(position).get("name");
//
//                textPhone.setText(phoneNum);
//
//                Editable ea = textPhone.getText();
//                textPhone.setSelection(ea.length());
                dialTop.setVisibility(View.VISIBLE);
                //modify by linzhangbin 2017/6/15 数字键盘根据光标位置增加号码 end
            }
        });

        /**
         * 最上方显示电话号码Edittext
         */
        textPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input = charSequence.toString();
                //根据输入内容刷新adapter
                filterList = ContactsUtils.filterList(charSequence.toString(), allContactList);
                dialListAdapter.refresh(filterList);
                if (TextUtils.isEmpty(input)) {
                    lvNewContact.setVisibility(View.GONE);
                } else {
                    lvNewContact.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    //modify by linzhangbin 2017/6/22 打电话点击匹配出来的联系人拨打的号码不对的问题
    /**
     * 打电话的方法
     */
    private void toMakeCall(final String phoneNum) {
        if (TextUtils.isEmpty(phoneNum)) {
            Utils.customToast(DialActivity.this, "号码无效", Toast.LENGTH_SHORT).show();
            return;
        }

        //modify by linzhangbin 非本用户的欧乐会员都可以拨打安全电话 start
        if (user.getPhone().equals(phoneNum)){
            ToastUtil.show(this,"不可拨打本机号码");
        }else{
            UserRepository.getInstance(DialActivity.this).getOKLineMember(phoneNum)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new DefaultSubscribe<User>(TAG) {
                        @Override
                        public void onError(Throwable throwable) {
                            super.onError(throwable);
                            showLocalPhoneDialog(phoneNum);
                        }

                        @Override
                        public void onNext(User user) {
                            Intent intent = new Intent(DialActivity.this, CallingActivity.class);
                            intent.putExtra("phoneNum", phoneNum);
                            Timber.tag(TAG).i("user:" + user.toString());
                            startActivity(intent);
                        }
                    });
        }
        //modify by linzhangbin 非本用户的欧乐会员都可以拨打安全电话 end

    }
    //modify by linzhangbin 2017/6/22 打电话点击匹配出来的联系人拨打的号码不对的问题 end

    private void showLocalPhoneDialog(final String phone) {

        CommonDialog commonDialog = new CommonDialog(DialActivity.this);
        commonDialog.setCanceledOnTouchOutside(true);
        commonDialog.setTitleVisible(View.GONE);
        //lzb edit 2017/5/14 更改dialog显示内容
        commonDialog.setContent(getString(R.string.notVip_call_chat_dial));
        commonDialog.setNegativeButton(getString(R.string.notVip_negative_text_dial));
        commonDialog.setPositiveButton(getString(R.string.notVip_local_dial));
        //lzb edit 2017/5/14 更改dialog显示内容
        commonDialog.setOnDialogClickListener(new CommonDialog.OnDialogInterface() {
            @Override
            public void cancel(View view, CommonDialog dialog) {
                dialog.dismiss();
                ContactsUtils.normalChat(phone, DialActivity.this, user.getRealName(), user.getPhone());
            }

            @Override
            public void ensure(View view, CommonDialog dialog) {
                dialog.dismiss();
                ContactsUtils.requestCallPhonePermission(DialActivity.this);
                AppUtil.call(DialActivity.this, phone);
            }
        });
        commonDialog.show();

    }

    private void initToolbar() {
        toolbar.setActionTitle(R.string.title_dial);
        toolbar.setNavigationVisible(View.GONE);
        toolbar.setActionMenuVisible(View.GONE);
    }

}
