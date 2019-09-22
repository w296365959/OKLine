package com.vboss.okline.ui.contact.editRemark;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hwangjr.rxbus.RxBus;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.base.EventToken;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.data.ContactRepository;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.ui.contact.ContactsFragment;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.TextUtils;
import com.vboss.okline.utils.ToastUtil;
import com.vboss.okline.view.widget.ClearEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static com.hyphenate.chat.EMGCMListenerService.TAG;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/4/6 14:56
 * Desc :
 */

public class EditRemarkFragment extends BaseFragment {
    @BindView(R.id.et_remark)
    ClearEditText etRemark;
    Unbinder unbinder;
    /**
     * ActionBar bind View
     */
    @BindView(R.id.toolbar_contact_edit)
    FragmentToolbar toolbar;
    private List<Subscription> subscriberList;
    private MainActivity activity;
    private ContactRepository repository;
    private ContactEntity entity;

    public static Fragment newInstance(ContactEntity entity){
        EditRemarkFragment fragment = new EditRemarkFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("entity",entity);
//        bundle.putString("remark",remark);
//        bundle.putInt("friendId",friendId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_remark, null);
        unbinder = ButterKnife.bind(this, view);
        activity = (MainActivity) getActivity();
        activity.hideTabs(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG,"initToolbar");
        initToolbar();
        Bundle bundle = getArguments();
        repository = ContactRepository.getInstance(activity);
        entity = (ContactEntity) bundle.getSerializable("entity");
        Timber.tag(TAG).i("onViewCreated:"+entity.toString());
        //modify by linzhangbin 2017/7/6 nickName为备注 start
//        final String remark = entity.remarkName();
        final String remark = entity.nickName();
        if (null != remark){
            etRemark.setText(remark);
        }
        //modify by linzhangbin 2017/7/6 nickName为备注 end

        /*etRemark.setTextChangeListener(new ClearEditText.AfterTextChangedListener() {
            @Override
            public void afterTextChanged(String s) {
                if (s.equals(remark)){
                    toolbar.setActionMenuIcon(R.mipmap.save_hide);
                    toolbar.setActionMenuClickable(false);
                    Timber.tag(TAG).i("clickable:false");
                }else{
                    toolbar.setActionMenuIcon(R.mipmap.save_show);
                    toolbar.setActionMenuClickable(true);
                    Timber.tag(TAG).i("clickable:true");
                }
            }
        });*/
        toolbar.setActionMenuClickable(false);
        etRemark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals(remark)){
                    toolbar.setActionMenuIcon(R.mipmap.save_hide);
                    toolbar.setActionMenuClickable(false);
                    Timber.tag(TAG).i("clickable:false");
                }else{
                    toolbar.setActionMenuIcon(R.mipmap.save_show);
                    toolbar.setActionMenuClickable(true);
                    Timber.tag(TAG).i("clickable:true");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden){
            //取消订阅
            unsubscribeAll();
        }
    }

    /**
     * 创建ContactEntity对象
     * @param remark 备注名
     * @return ContactEntity对象
     */
    private ContactEntity getNewEntity(String remark) {
        return ContactEntity.newBuilder()
                .friendId(entity.friendId())
                .friendOlNo(entity.friendOlNo())
                .imgUrl(entity.imgUrl())
                .isNote(entity.isNote())
                .nickName(entity.nickName())
                .operatType(0)
                .phone(entity.phone())
                .realName(entity.realName())
                .remarkName(remark)
                .build();
    }

    private void initToolbar() {

        toolbar.setActionTitle(getResources().getString(R.string.title_edit_remark));
        toolbar.setNavigationVisible(View.VISIBLE);
        toolbar.setActionMenuIcon(R.mipmap.save_hide);
        toolbar.setActionMenuVisible(View.VISIBLE);
        toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
            @Override
            public void onNavigation(View v) {
                activity.removeSecondFragment();
                TextUtils.showOrHideSoftIM(etRemark,false);
            }
        });
        toolbar.setOnActionMenuClickListener(new FragmentToolbar.OnActionMenuClickListener() {
            @Override
            public void onActionMenu(View v) {
//                toolbar.setActionMenuClickable(false);
                Timber.tag(TAG).i("被点击了");
                String text = etRemark.getText().toString();
                ContactEntity newEntity = getNewEntity(text);
                Log.i("EditRemarkFragment", "onClick: newEntity"+newEntity.toString());
                Subscription sbpAddOrUpdate = repository.addOrUpdateContact(newEntity)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new DefaultSubscribe<ContactEntity>(TAG){
                            @Override
                            public void onNext(ContactEntity entity) {
                                if (null != entity){
                                    ToastUtil.show(getActivity(),"保存成功!");
                                    TextUtils.showOrHideSoftIM(etRemark,false);
                                    toolbar.setActionMenuClickable(true);
                                    activity.removeSecondFragment();
                                    ContactsFragment.isRefresh = true;
                                    //lzb edit 2017/5/16 采用rxBux发送数据
                                    RxBus.get().post(EventToken.REMARK_CHANGED,entity.remarkName());
                                    RxBus.get().post(EventToken.REMARK_CHANGED,true);

                                }else{
                                    ToastUtil.show(activity,R.string.contact_save_failure);
                                }
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                if (throwable!=null){
                                    ToastUtil.show(activity,R.string.contact_save_failure);
                                    Timber.tag(TAG).i("throwable:"+throwable.getMessage());
                                    throwable.printStackTrace();
                                }

                            }
                        });
                addSubscription(sbpAddOrUpdate);
            }
        });

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        activity.hideTabs(false);
        unbinder.unbind();
    }

    /**
     * 添加订阅
     */
    protected void addSubscription(Subscription subscription) {
        if (subscriberList == null) {
            subscriberList = new ArrayList<>();
        }
        if (!subscriberList.contains(subscription)) {
            subscriberList.add(subscription);
        }
    }

    /**
     * 取消订阅
     */
    protected void unsubscribeAll() {
        if (subscriberList != null && subscriberList.size() != 0) {
            for (Subscription subscription : subscriberList) {
                if (!subscription.isUnsubscribed()) {
                    subscription.unsubscribe();
                }
            }
            subscriberList.clear();
        }
    }
}
