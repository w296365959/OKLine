package com.vboss.okline.ui.contact;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.vboss.okline.R;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.ContactRepository;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.WorkerThreadScheduler;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.data.entities.ContactGroup;
import com.vboss.okline.ui.auth.VerifyFailActivity;
import com.vboss.okline.ui.contact.bean.ContactItem;
import com.vboss.okline.utils.ToastUtil;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/3/28 15:36
 * Desc :
 */

public class ContactsPresenter extends ContentContract.ContentPresenter {
    private static final String TAG = "ContactsPresenter";
    ContentContract.ContentView mView;
    Context context;
    List<ContactItem> starList = new ArrayList<>();
    List<ContactItem> commonFriends = new ArrayList<>();
    List<ContactItem> contactItems = new ArrayList<>();
    Subscription sbpGetGroupList;
    Subscription subscription;
    private ContactRepository repository;
    private List<Subscription> subscriberList;
    private int entitieSize = 0;
    private UserRepository userRepository;
    private List<ContactItem> conList = new ArrayList<>();
    private List<ContactItem> groupList = new ArrayList<>();
    private Subscription sbpGetApplicantList;
    private Subscription sbpGetAll;

    public ContactsPresenter(ContentContract.ContentView view, Context context) {
        mView = view;
        this.context = context;
        onAttached();
    }

    @Override
    public void onAttached() {
        repository = ContactRepository.getInstance(context);
        userRepository = UserRepository.getInstance(context);
        subscriberList = new ArrayList<>();
        initUser();
    }

    @Override
    public void refreshContact(final List<ContactItem> localContactList) {
        final String userPhone = userRepository.getUser().getPhone();
        final List<ContactItem> addLst = new ArrayList<ContactItem>();
        //从数据库里拿出联系人集合
        //modify by shenyi 2017-5-25 修改通讯录比对方法 start
        sbpGetAll = repository.getAllContact()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<List<ContactEntity>>(TAG) {
                    @Override
                    public void onNext(final List<ContactEntity> contactEntities) {
                        WorkerThreadScheduler.getInstance().execute(new Runnable() {
                            @Override
                            public void run() {
                                //modify by shenyi 2017-5-25 修改通讯录比对方法 start
                                Timber.tag(TAG).i("refreshContact dataBaseContact:" + contactEntities.size());
                                String contactStr = "";
                                for (int i = 0, k = contactEntities.size(); i < k; i++) {
                                    contactStr = contactStr + contactEntities.get(i).phone() + ",";
                                }

                                for (int m = 0, n = localContactList.size(); m < n; m++) {
                                    if (!contactStr.contains(localContactList.get(m).getPhoneNum()) &&
                                            !localContactList.get(m).getPhoneNum().equals(userPhone)) {

                                        addLst.add(localContactList.get(m));
                                    }
                                }
                                if (addLst.size() != 0) {
                                    ContactsUtils.getInstance().testImportContacts(context, addLst);
                                }
                                Timber.tag(TAG).i("addList size : "+ addLst.size());
                            }
                        });
                        if (sbpGetAll != null && !sbpGetAll.isUnsubscribed()) {
                            sbpGetAll.unsubscribe();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (sbpGetAll != null && !sbpGetAll.isUnsubscribed()) {
                            sbpGetAll.unsubscribe();
                        }
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void showContent() {
        //TODO:这里拆分成两个,拿到本地的联系人去setadapter 拿到群组的之后再更新

       /* Subscription subscription = Observable.zip(repository.getAllContact(), repository.getGroupList(),
                new Func2<List<ContactEntity>, List<ContactGroup>, List<ContactItem>>() {
                    @Override
                    public List<ContactItem> call(List<ContactEntity> contactEntities,
                                                  List<ContactGroup> contactGroups) {
                        Timber.tag(TAG).i("getAllContact size:" + contactEntities.size());
//                        Set<ContactEntity> set = new HashSet<>();
//                        set.addAll(contactEntities);
//                        Timber.tag(TAG).i("set size:" + set.size());
                        final List<ContactItem> contactItems = new ArrayList<>();
                        final List<ContactItem> commonFriends = new ArrayList<>();
                        for (ContactEntity entity : contactEntities) {
                            ContactItem item = ContactsUtils.contactEtity2contactItem(entity);
                            if (item.isStar()) {
                                contactItems.add(item);
                            } else {
                                //被添加星标的也要显示在下方的所有分组中
                                commonFriends.add(item);
                            }
                        }
                        Collections.sort(contactItems, new ContactComparator());

                        //lzb edit 2017/5/14
                        if (!contactGroups.isEmpty()) {
                            for (ContactGroup group : contactGroups) {
                                ContactItem item = ContactsUtils.group2contactItem(group);
                                contactItems.add(item);
                            }
                        }
                        Collections.sort(commonFriends, new ContactComparator());
                        contactItems.addAll(commonFriends);
                        return contactItems;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<List<ContactItem>>(TAG) {
                    @Override
                    public void onNext(List<ContactItem> contactItems) {
                        mView.showConList(contactItems);
                    }

                    @Override
                    public void onCompleted() {
                        unsubscribeAll();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
//                        mView.requestTimeOut(throwable,0);
                        mView.showRefreshError();
                    }
                });
        addSubscription(subscription);*/

    }

    @Override
    public void showContactOnly() {
        sbpGetGroupList = repository.getGroupList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<List<ContactGroup>>(TAG) {
                    @Override
                    public void onNext(List<ContactGroup> contactGroups) {
                        groupList.clear();
                        for (ContactGroup group : contactGroups) {
                            ContactItem item = ContactsUtils.group2contactItem(group);
                            groupList.add(item);
                        }
                        addList();
                        if (sbpGetGroupList != null && !sbpGetGroupList.isUnsubscribed()) {
                            sbpGetGroupList.unsubscribe();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (sbpGetGroupList != null && !sbpGetGroupList.isUnsubscribed()) {
                            sbpGetGroupList.unsubscribe();
                        }
                    }
                });
        subscription = repository.getAllContact()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<List<ContactEntity>>(TAG) {
                    @Override
                    public void onNext(List<ContactEntity> list) {
                        Timber.tag(TAG).i("getContactPageBypage size:" + list.size());
                        starList.clear();
                        commonFriends.clear();
                        for (ContactEntity entity : list) {
                            ContactItem item = ContactsUtils.contactEtity2contactItem(entity);
//                            if (item.isStar()) {
//                                starList.add(item);
//                            } else {
//                                commonFriends.add(item);
//                            }

                            //add by linzhangbin 2017/6/29 星标好友也要显示在列表中 start
                            if ( item.isStar()){
                                starList.add(item);
                            }
                            commonFriends.add(item);
                            //add by linzhangbin 2017/6/29 星标好友也要显示在列表中 end
                        }
                        addList();
                        if (subscription != null && !subscription.isUnsubscribed()) {
                            subscription.unsubscribe();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (subscription != null && !subscription.isUnsubscribed()) {
                            subscription.unsubscribe();
                        }
                    }
                });

    }

    private void addList() {
        contactItems.clear();
        Collections.sort(starList, new ContactComparator());
        Collections.sort(commonFriends, new ContactComparator());
        contactItems.addAll(starList);
        contactItems.addAll(groupList);
        contactItems.addAll(commonFriends);

        mView.showContactOnly(contactItems,commonFriends,starList.size(),groupList.size());
    }

    @Override
    public void initUser() {
        mView.showUser();
    }

    @Override
    public void unSubscribe() {
        unsubscribeAll();
    }

    @Override
    public void refreshRedPoint() {
        sbpGetApplicantList = repository.getApplicantList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<List<ContactEntity>>(TAG) {
                    @Override
                    public void onNext(List<ContactEntity> entities) {
                        mView.showRedPoint(entities.size());
                        //add by linzhangbin 2017/6/15 取消订阅
                        if (sbpGetApplicantList != null && !sbpGetApplicantList.isUnsubscribed()) {
                            sbpGetApplicantList.unsubscribe();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mView.showRefreshError();
                        if (sbpGetApplicantList != null && !sbpGetApplicantList.isUnsubscribed()) {
                            sbpGetApplicantList.unsubscribe();
                        }
                        //add by linzhangbin 2017/6/15 取消订阅 end
                    }
                });
    }

    @Override
    public boolean hasReadContactPermission() {
        return Build.VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
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
