package com.vboss.okline.ui.contact.addContact;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.data.ContactRepository;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.ui.contact.ContactsPresenter;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.StringUtils;
import com.vboss.okline.utils.TextUtils;
import com.vboss.okline.utils.ToastUtil;
import com.vboss.okline.view.widget.ClearEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static android.content.ContentValues.TAG;
import static com.vboss.okline.R.id.ll_create_con_phone;
import static com.vboss.okline.base.OKLineApp.context;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/5/3 15:15
 * Desc :
 */

public class AddContactActivity extends BaseActivity implements AddContactContract.View {
    private static final String TAG = "AddContactActivity";
    @BindView(R.id.toolbar_contact)
    FragmentToolbar toolbar;
    @BindView(R.id.et_createCon_phoneNum)
    ClearEditText etCreateConPhoneNum;
    @BindView(ll_create_con_phone)
    LinearLayout llCreateConPhone;
    @BindView(R.id.et_addCon_remark)
    ClearEditText etAddConRemark;
    @BindView(R.id.ll_create_con_name)
    LinearLayout llCreateConName;
    @BindView(R.id.tv_add_contact)
    TextView tvAddContact;
    //全部联系人集合
    List<ContactEntity> list;
    private AddContactPresenter presenter;
    private UserRepository userRepository;
    private List<Subscription> subscriberList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact_activity);
        ButterKnife.bind(this);
        initToolbar();
        presenter = new AddContactPresenter(this, new AddContactModel(), this);
        userRepository = UserRepository.getInstance(context);
        subscriberList = new ArrayList<>();
        //lzb edit 2017/5/10 默认弹出软键盘
        InputMethodManager inputManager = (InputMethodManager) etCreateConPhoneNum.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(etCreateConPhoneNum, 0);
        presenter.getAllContact();
    }

    /**
     * init toolbar
     */
    private void initToolbar() {
        toolbar.setActionTitle(getResources().getString(R.string.title_add_contact));
        toolbar.setNavigationVisible(View.VISIBLE);
        toolbar.setActionMenuVisible(View.GONE);
        toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
            @Override
            public void onNavigation(View v) {
                TextUtils.showOrHideSoftIM(tvAddContact, false);
                finish();
            }
        });

    }

    @OnClick(R.id.tv_add_contact)
    public void onViewClicked() {

        //第一步拿到姓名和输入的手机号码
        final String remarks = etAddConRemark.getText().toString().trim();
        final String phoneNum = etCreateConPhoneNum.getText().toString().trim();
        String phone = userRepository.getUser().getPhone();
        boolean isChineseName = TextUtils.isChineseName(remarks);
        final boolean isPhoneNumber = TextUtils.isPhoneNumber(phoneNum);
        final boolean nullName = StringUtils.isNullString(remarks);
        final boolean nullPhoneNum = StringUtils.isNullString(phoneNum);
        final boolean isYourSelf = phoneNum.equals(phone);
        ContactEntity build = ContactEntity.newBuilder()
                .remarkName(remarks)
                //modify by linzhangbin 2017/6/1 添加联系人operatType为3
                .operatType(3)
                //modify by linzhangbin 添加联系人operatType为3
                .phone(phoneNum)
                .build();
        boolean sameFriend = list.contains(build);


        if (nullName || nullPhoneNum) {
            ToastUtil.show(this, R.string.name_phone_isnull);
        } else if (isYourSelf) {
            ToastUtil.show(this, R.string.cannot_add_yourself);
        } else if (sameFriend) {
            ToastUtil.show(this, R.string.existed_contact);
        } else {
            //fix bug 1341
            tvAddContact.setClickable(false);
            //fix bug 1341
            presenter.addContact(build);
        }

    }


    @Override
    public void notice(int message) {
        ToastUtil.show(this, message);
        TextUtils.showOrHideSoftIM(tvAddContact, false);
        setResult(RESULT_OK);
        tvAddContact.setClickable(true);
        finish();
    }

    @Override
    public void getAllContact(List<ContactEntity> entities) {
        list = entities;
    }

    @Override
    public void onError() {
        ToastUtil.show(context, "创建好友失败");
        tvAddContact.setClickable(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.unsubscribeAll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
