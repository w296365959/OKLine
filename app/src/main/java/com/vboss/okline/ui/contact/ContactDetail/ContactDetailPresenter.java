package com.vboss.okline.ui.contact.ContactDetail;

import android.content.Context;
import android.util.Log;

import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.base.OKLineApp;
import com.vboss.okline.data.ContactRepository;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.ChatLog;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static com.vboss.okline.R.string.me;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/3/31 11:53
 * Desc :
 */

public class ContactDetailPresenter extends ContactDetailContract.DetailPresenter {
    private static final String TAG = "ContactDetailPresenter";
    Context context;
    ContactDetailContract.View view;
    ContactDetailContract.Model mModel;
    private ContactRepository repository;
    private int favorite;
    private List<Subscription> subscriberList;

    public ContactDetailPresenter(ContactDetailContract.View view,ContactDetailContract.Model model,Context context){
        this.mModel = model;
        this.view = view;
        this.context = context;
        subscriberList = new ArrayList<>();
        onAttached();
    }
    @Override
    public void onAttached() {
        repository = ContactRepository.getInstance(context);
    }

    @Override
    public void initDetail(int contactID) {

        Subscription sbpGetDetail = mModel.getDetail(context,contactID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<ContactEntity>(TAG){
                    @Override
                    public void onNext(ContactEntity entity) {
                        Log.i("ContactDetailPresenter", "onNext: "+entity);
                        view.showDetail(entity);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtil.show(context,"获取数据失败,请返回重试");
                    }
                });
        addSubscription(sbpGetDetail);
    }

    @Override
    public void addToStar(int contactID,boolean star) {
        //请求服务器 如果成功就改变图标状态
//        mModel.setStar(context,contactID);

        if (star){
            favorite = 0;
        }else{
            favorite = 1;
        }
        Subscription sbpFavor = repository.favoriteContact(contactID,favorite)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<Boolean>(TAG){
                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);
                        if (aBoolean){
                            view.setStar();
                        }else{
                            ToastUtil.show(context,"添加关心不成功,请稍候再试");
                        }

                    }
                });
        addSubscription(sbpFavor);

//        mModel.setStar();

    }

    @Override
    public void unSubscribe() {
        unsubscribeAll();
    }

    @Override
    public void sendFriendRequest() {

    }

    @Override
    public void getContactVisitingCard(String phoneNum) {
        Subscription sbpCVC = UserRepository.getInstance(OKLineApp.context)
                .getContactVisitingCard(phoneNum)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<User>(TAG){
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtil.show(context,"获取名片详情失败,请稍候再试");
                    }

                    @Override
                    public void onNext(User user) {
                        view.showVisitingCard(user);
                    }
                });
        addSubscription(sbpCVC);
    }

    /**
     * 添加订阅
     */
    private void addSubscription(Subscription subscription) {
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
    private void unsubscribeAll() {
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
