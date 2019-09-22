package com.vboss.okline.ui.contact.search;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.ContactRepository;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.ui.card.widget.SliderListView;
import com.vboss.okline.ui.contact.ContactDetail.ContactDetailFragment;
import com.vboss.okline.ui.contact.adapter.ContactListAdapter;
import com.vboss.okline.ui.contact.addContact.AddCardFragment;
import com.vboss.okline.ui.contact.bean.ContactItem;
import com.vboss.okline.ui.contact.dialog.ContactAddDialog;
import com.vboss.okline.ui.contact.group.CreateGroupActivity;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.scanner.QRCodeActivity;
import com.vboss.okline.utils.TextUtils;
import com.vboss.okline.view.widget.ClearEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static android.R.attr.gravity;
import static com.vboss.okline.R.id.action_back;
import static com.vboss.okline.R.id.edt_card;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/6/6 17:37
 * Desc :
 */

public class ContactsSearchFragment extends DialogFragment implements ContactSearchContract.View, TextView.OnEditorActionListener {
    private static final String TAG = "ContactsSearchFragment";
    @BindView(action_back)
    ImageButton actionBack;
    @BindView(R.id.action_back_layout)
    RelativeLayout actionBackLayout;
    @BindView(R.id.ib_more)
    ImageButton ibMore;
    @BindView(R.id.logo_animation)
    ImageView logoAnimation;
    @BindView(R.id.rl_logo)
    RelativeLayout rlLogo;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(edt_card)
    ClearEditText edtCard;
    @BindView(R.id.rl_toolbar)
    RelativeLayout rlToolbar;
    @BindView(R.id.listView)
    SliderListView listView;
    @BindView(R.id.fl_data)
    FrameLayout frameLayout;
    @BindView(R.id.rl_card_log_query)
    RecyclerView rlCardLogQuery;
    @BindView(R.id.refreshFrameLayout)
    PtrClassicFrameLayout refreshFrameLayout;
    @BindView(R.id.fl_log_data)
    FrameLayout frameLayout1;
    @BindView(R.id.empty)
    View empty;
    @BindView(R.id.fl_no_data)
    RelativeLayout emptyLayout;
    @BindView(R.id.tv_search_key)
    TextView tv_search_key;
    //modify by linzhangbin 2017-06-21  logoView
    @BindView(R.id.logoView)
    LogoView logoView;
    Unbinder unbinder;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switchLogo();
        }
    };
    private MainActivity activity;
    private ContactSearchPresenter presenter;
    private InputMethodManager imm;
    private ContactAddDialog addDialog;
    private String keyWord;
    private Subscription sbpAllContact;
    private List<ContactEntity> conList = new ArrayList<>();
    private List<ContactEntity> filterList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
        getAllContact();
        presenter = new ContactSearchPresenter(this, new ContactSearchModel(), activity);
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private void getAllContact() {
        sbpAllContact = ContactRepository.getInstance(activity).getAllContact()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<List<ContactEntity>>(TAG) {
                    @Override
                    public void onNext(List<ContactEntity> entities) {

                        conList.addAll(entities);

                    }
                });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Timber.tag(TAG).i("onCreateDialog ");
        View convertView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_card_search, null);
        unbinder = ButterKnife.bind(this, convertView);
        edtCard.setHint(getString(R.string.edittext_hint));
        actionBack.setImageResource(R.mipmap.ic_contacts_add);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dialog);
        builder.setView(convertView);
        initView();
        //add by linzhangbin 2017/6/21 欧卡显示状态
        switchLogo();
        //add by linzhangbin 2017/6/21 欧卡显示状态
        //modify by linzhangbin 增加左上角logo按欧卡状态显示 2017/6/9
//        onOcardStateChanged();
        //modify by linzhangbin 增加左上角logo按欧卡状态显示 2017/6/9
        return builder.create();
    }

    //modify by linzhangbin 增加左上角logo按欧卡状态显示 2017/6/9
    /*private void onOcardStateChanged() {
        int ocardState = activity.getOcardState();
        switch (ocardState) {
            case BaseActivity.OCARD_STATE_BOND:
                iv_ol_logo.setImageResource(R.mipmap.logo2);
                Utils.showLog(TAG, "欧卡已连接");
                break;
            case BaseActivity.OCARD_STATE_IPSS_INVALID:
                Utils.showLog(TAG, "安全电话不可用");
                break;
            case BaseActivity.OCARD_STATE_NOT_BOND:
                iv_ol_logo.setImageResource(R.mipmap.logo);
                Utils.showLog(TAG, "欧卡未绑定");
                break;
            case BaseActivity.OCARD_STATE_NOT_CONNECTED:
                Utils.showLog(TAG, "欧卡未连接");
                break;
        }
    }*/
    //modify by linzhangbin 增加左上角logo按欧卡状态显示 2017/6/9 end

    private void initView() {
        //Added by linzhangbin 2017-06-09 registerReceiver broadcast
        IntentFilter filter = new IntentFilter(BaseActivity.ACTION_OCARD_STATE_CHANGED);
        getActivity().registerReceiver(receiver, filter);
        edtCard.setOnEditorActionListener(this);
        edtCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                keyWord = s.toString().trim();
                Timber.tag(TAG).i("onTextChanged:" + keyWord);
                if (s.length() > 0) {
                    frameLayout.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);
//                    filterList = ContactsUtils.filterList(keyWord, conList);
                    presenter.search(keyWord);

                } else {
                    empty.setVisibility(View.VISIBLE);
                    frameLayout.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void showResult(List<ContactItem> list) {
        if (list.size() == 0) {
            emptyLayout.setVisibility(View.VISIBLE);
            String str = String.format(getResources().getString(R.string.card_search_key), keyWord);
            SpannableString ss = new SpannableString(str);
            ss.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(getActivity(), R.color.color_card_search_blue)),
                    str.indexOf("：") + 1, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_search_key.setText(ss);
        } else {
            emptyLayout.setVisibility(View.GONE);
            final ContactListAdapter contactListAdapter = new ContactListAdapter(list, activity, 3);
            listView.setAdapter(contactListAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    ContactItem item = (ContactItem) contactListAdapter.getItem(position);
                    TextUtils.showOrHideSoftIM(listView, false);
                    activity.addSecondFragment(ContactDetailFragment.newInstance(item.getContactID(),item));
                    //add by linzhangbin 2017/6/8 搜索进入三级界面返回关闭搜索界面直接返回一级界面
                    dismiss();
                    //end
                }
            });
        }

    }


    private void switchLogo() {
        switch (BaseActivity.getOcardState()) {
            case BaseActivity.OCARD_STATE_BOND:
                //modify by linzhangbin 2017-06-21
                logoView.setOCardState(LogoView.OCARD_BIND);
                break;
            case BaseActivity.OCARD_STATE_NOT_BOND:
                //modify by linzhangbin 2017-06-21
                logoView.setOCardState(LogoView.OCARD_NO_BIND);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Added by linzhangbin 2017-06-09 unregisterReceiver broadcast
        try {
            getActivity().unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.tag(TAG).i(" onResume ");
        Window mWindow = getDialog().getWindow();
        if (mWindow != null) {
            Timber.tag(TAG).i(" onResume ");
            WindowManager.LayoutParams mLayoutParams = mWindow.getAttributes();
            mLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            mLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mLayoutParams.gravity = gravity;
            mWindow.setAttributes(mLayoutParams);
            //modify by wangshuai 2017-06-06 solve show soft input
            mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
        //modify by wangshuai 2017-06-06 solve show soft input
        edtCard.setFocusable(true);
        edtCard.setFocusableInTouchMode(true);
        edtCard.requestFocus();
        //modify by wangshuai 2017-06-06 solve show soft input
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @OnClick({R.id.action_back_layout, R.id.ib_more, R.id.tv_cancel, R.id.empty})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.action_back_layout:

                break;
            case R.id.ib_more:
                //modify by linzhangbin 2017/6/7 点击加号弹出框
                dialogShow();
                //modify by linzhangbin 2017/6/7 点击加号弹出框 end
                break;
            case R.id.tv_cancel:
                hideSoftInput();
                break;
            case R.id.empty:
                hideSoftInput();
                break;
        }
    }

    private void dialogShow() {
        addDialog = new ContactAddDialog(getActivity(), R.style.dialog);
        addDialog.show();
        addDialog.setClicklistener(new ContactAddDialog.ClickListenerInterface() {
            @Override
            public void addContact() {
                //modify by linzhangbin 2017/6/9 更换添加名片页面

                activity.addSecondFragment(new AddCardFragment());
                addDialog.dismiss();
                //modify by linzhangbin 2017/6/9 更换添加名片页面 end
            }

            @Override
            public void createGroup() {
                Intent intent = new Intent(getActivity(), CreateGroupActivity.class);

                startActivity(intent);
                addDialog.dismiss();
            }

            @Override
            public void scaning() {
                startActivity(new Intent(activity, QRCodeActivity.class));
                addDialog.dismiss();
            }

        });
    }

    private void hideSoftInput() {
        imm.hideSoftInputFromWindow(edtCard.getWindowToken(), 0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 100);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return false;
    }
}
