package com.vboss.okline.ui.contact.group;

import android.app.ProgressDialog;
import android.content.Context;

import com.hwangjr.rxbus.RxBus;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.exceptions.HyphenateException;
import com.vboss.okline.R;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.base.EventToken;
import com.vboss.okline.data.ContactRepository;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.data.entities.ContactGroup;
import com.vboss.okline.ui.contact.ContactsUtils;
import com.vboss.okline.ui.contact.bean.ContactItem;
import com.vboss.okline.utils.StringUtils;
import com.vboss.okline.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/3/31 20:17
 * Desc :
 */

public class CreateGroupPresenter extends CreateGroupContract.GroupPresenter {
    public static final String TAG = CreateGroupPresenter.class.getSimpleName();
    CreateGroupContract.View mView;
    CreateGroupContract.Model mModel;
    Context context;
    private List<Subscription> subscriberList;
    private String easeGroupId;
    private String userRealName;
    ContactRepository contactRepository;

    public CreateGroupPresenter(CreateGroupContract.View view, CreateGroupContract.Model model, Context context) {
        onAttached();
        mView = view;
        mModel = model;
        this.context = context;
        contactRepository = ContactRepository.getInstance(context);
    }

    @Override
    public void onAttached() {
        subscriberList = new ArrayList<>();
        try {
            UserRepository repository = UserRepository.getInstance(context);
            userRealName = repository.getUser().getRealName();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initContent() {
        Subscription sbpGetList = mModel.getList(context)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<List<ContactEntity>>(TAG) {
                    @Override
                    public void onNext(List<ContactEntity> list) {
                        List<ContactItem> starList = new ArrayList<>();
                        for (ContactEntity entity : list) {
                            if (!StringUtils.isNullString(entity.friendOlNo())) {
                                ContactItem item = ContactsUtils.contactEtity2contactItem(entity);
                                starList.add(item);
                            }
                        }
                        mView.showContent(starList);
                    }
                });
//        mView.showContent(new ArrayList<Contact>());
        addSubscription(sbpGetList);
    }

    /**
     * 创建群组
     */
    @Override
    public void createGroup(final List<ContactItem> checkedList) {
        final String createGroupFail = context.getString(R.string.Failed_to_create_groups);
        String createGroupSuccess = context.getString(R.string.Is_to_create_a_group_chat);
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(createGroupSuccess);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        Subscription subscription = Observable.create(new Observable.OnSubscribe<EMGroup>() {
            @Override
            public void call(Subscriber<? super EMGroup> subscriber) {
                EMGroupManager.EMGroupOptions option = new EMGroupManager.EMGroupOptions();
                option.maxUsers = 200;
                option.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin; //公开群，任何人都能加入此群
                try {
                    List<String> nameList = new ArrayList<>();
                    if (!StringUtils.isNullString(userRealName)) {
                        nameList.add(userRealName);
                    }
                    for (ContactItem item : checkedList) {
                        if (!StringUtils.isNullString(item.getRealName())) {
                            nameList.add(item.getRealName());
                        }
                    }
                    String groupName = org.apache.commons.lang.StringUtils.join(nameList, ",");
                    if (StringUtils.getChineseLength(groupName) > 20) {
                        groupName = StringUtils.tosubstring(groupName, 20, "...");
                    }
                    List<String> olList = new ArrayList<>();
                    for (ContactItem item : checkedList) {
                        olList.add(item.getOlNo());
                    }
                    String[] members = new String[olList.size()];
                    EMGroup emGroup = EMClient.getInstance().groupManager().createGroup(groupName, null, olList.toArray(members), null, option);
                    easeGroupId = emGroup.getGroupId();
                    subscriber.onNext(emGroup);
                } catch (final HyphenateException e) {
                    subscriber.onError(e);
                }
            }
        }).flatMap(new Func1<EMGroup, Observable<ContactGroup>>() {
            @Override
            public Observable<ContactGroup> call(EMGroup emGroup) {
                List<String> olList = new ArrayList<>();
                for (ContactItem item : checkedList) {
                    olList.add(item.getOlNo());
                }
                //TODO 这里要把用户的欧乐号也放进去
//                olList.add("OLHZ310571000000000436");
                UserRepository repository = UserRepository.getInstance(context);
                olList.add(repository.getUser().getOlNo());
                return contactRepository.createGroup(olList, emGroup.getGroupName(), emGroup.getGroupId());
            }
        }).onErrorResumeNext(new Func1<Throwable, Observable<? extends ContactGroup>>() {
            @Override
            public Observable<? extends ContactGroup> call(Throwable throwable) {
                Timber.tag(TAG).d("create group error : " + easeGroupId + " , " + throwable.getMessage());
                if (!StringUtils.isNullString(easeGroupId)) {
                    try {
                        EMClient.getInstance().groupManager().destroyGroup(easeGroupId);
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                }
                return Observable.error(throwable);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<ContactGroup>(TAG) {
                    @Override
                    public void onNext(ContactGroup group) {
                        progressDialog.dismiss();
                        mView.enterGroup(ContactsUtils.group2contactItem(group));
                        RxBus.get().post(EventToken.GROUP_CREATE, true);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        progressDialog.dismiss();
                        ToastUtil.show(context, createGroupFail + throwable.getLocalizedMessage());
                        mView.createGroupError();
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void addMembers(final int groupId, final String easeID, final List<ContactItem> checkedList) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.refresh_footer));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        Subscription subscription = addEaseMembers(easeID, checkedList)
                .flatMap(new Func1() {
                    @Override
                    public Object call(Object o) {
                        List<String> olList = new ArrayList<>();
                        for (ContactItem item : checkedList) {
                            olList.add(item.getOlNo());
                        }

                        return contactRepository.addGroupMember(groupId, olList);
                    }
                })
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends ContactGroup>>() {
                    @Override
                    public Observable<? extends ContactGroup> call(Throwable throwable) {
                        Timber.tag(TAG).d("add member error : " + easeGroupId + " , " + throwable.getMessage());
                        for (ContactItem item : checkedList) {
                            String olNo = item.getOlNo();
                            try {
                                EMClient.getInstance().groupManager().removeUserFromGroup(easeGroupId, olNo);
                                Timber.tag(TAG).d("remove ease group member : " + olNo);
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(throwable);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<Boolean>(TAG) {
                    @Override
                    public void onNext(Boolean success) {
                        progressDialog.dismiss();
                        mView.addMemberResult(success);
                        RxBus.get().post(EventToken.GROUP_ADD_MEMBERS, true);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        progressDialog.dismiss();
                        mView.addMemberResult(false);
                    }
                });
        addSubscription(subscription);
    }

    Observable addEaseMembers(final String easeGroupIds, final List<ContactItem> checkedList) {
        return Observable.create(
                new Observable.OnSubscribe<Object>() {
                    @Override
                    public void call(Subscriber<? super Object> subscriber) {
                        List<String> olList = new ArrayList<>();
                        for (ContactItem item : checkedList) {
                            olList.add(item.getOlNo());
                        }
                        String[] members = new String[olList.size()];
                        try {
                            EMClient.getInstance().groupManager().addUsersToGroup(easeGroupIds, olList.toArray(members));
                            subscriber.onNext(null);
                        } catch (HyphenateException e) {
                            subscriber.onError(e);
                        }
                    }
                }
        );
    }

    public void removeMemers(final int groupId, final String easeID, final List<ContactItem> checkedList) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.refresh_footer));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        Observable.merge(removeEaseMembers(easeID, checkedList),
                removeOlMembers(groupId, checkedList))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe(TAG) {
                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);
                        Timber.tag(TAG).d("removeMemers onNext : " + o);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Timber.tag(TAG).d("removeMemers onError : " + throwable.getMessage());
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        Timber.tag(TAG).d("removeMemers onCompleted : ");
                        progressDialog.dismiss();
                        mView.addMemberResult(true);
                        RxBus.get().post(EventToken.GROUP_DELETE_MEMBER, true);
                    }
                });
    }

    Observable removeOlMembers(final int groupId, final List<ContactItem> checkedList) {
        return Observable.from(checkedList).flatMap(new Func1<ContactItem, Observable<?>>() {
            @Override
            public Observable<?> call(ContactItem item) {
                return contactRepository.removeGroupMember(groupId, item.getOlNo());
            }
        });
    }

    Observable removeEaseMembers(final String easeGroupIds, final List<ContactItem> checkedList) {
        return Observable.from(checkedList).map(new Func1<ContactItem, Boolean>() {
            @Override
            public Boolean call(ContactItem item) {
                try {
                    EMClient.getInstance().groupManager().removeUserFromGroup(easeGroupIds, item.getOlNo());
                    return true;
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        });
    }

    @Override
    void unSubscribe() {
        unsubscribeAll();
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