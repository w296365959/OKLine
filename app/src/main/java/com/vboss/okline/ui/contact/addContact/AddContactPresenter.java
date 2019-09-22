package com.vboss.okline.ui.contact.addContact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.vboss.okline.R;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.ContactRepository;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.ui.contact.ContactsUtils;
import com.vboss.okline.utils.StringUtils;
import com.vboss.okline.utils.TextUtils;
import com.vboss.okline.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;


/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/5/3 17:51
 * Desc :
 */

public class AddContactPresenter extends AddContactContract.AddContactPresenter {
    private static final String TAG = "AddContactPresenter";
    AddContactContract.View mView;
    AddContactContract.Model mModel;
    Context context;
    private ContactRepository repository;
    private List<Subscription> subscriberList;

    public AddContactPresenter(AddContactContract.View view, AddContactContract.Model model, Context context) {
        mView = view;
        mModel = model;
        this.context = context;
        onAttached();
    }

    @Override
    public void onAttached() {
        repository = ContactRepository.getInstance(context);
        subscriberList = new ArrayList<>();
    }


    @Override
    public void addContact(ContactEntity entity) {

        Timber.tag(TAG).i("addContact:"+entity.toString());
        Subscription sbpAdd = repository.addOrUpdateContact(entity)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<ContactEntity>(TAG) {
                    @Override
                    public void onNext(ContactEntity entity) {
                        Timber.tag(TAG).i("创建联系人成功");
                        if (entity.relationState()>1){
                            mView.notice(R.string.notice_addCon_success_forVip);
                        }else{
                            mView.notice(R.string.notice_addCon_success);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mView.onError();
                    }
                });
        addSubscription(sbpAdd);
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


    public void getAllContact() {
        repository.getAllContact()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<List<ContactEntity>>(TAG){
                    @Override
                    public void onNext(List<ContactEntity> list) {
                        mView.getAllContact(list);
                    }
                });
    }
}
