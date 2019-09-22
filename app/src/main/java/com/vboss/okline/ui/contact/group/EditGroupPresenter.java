package com.vboss.okline.ui.contact.group;

import android.app.ProgressDialog;
import android.content.Context;

import com.hwangjr.rxbus.RxBus;
import com.hyphenate.chat.EMClient;
import com.vboss.okline.R;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.base.EventToken;
import com.vboss.okline.data.ContactRepository;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.data.local.UserLocalDataSource;
import com.vboss.okline.ui.contact.ContactsUtils;
import com.vboss.okline.ui.contact.bean.ContactItem;
import com.vboss.okline.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static android.content.ContentValues.TAG;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Jiang Zhongyuan
 * Email : zhongyuan@okline.cn
 * Date : 2017/4/24 18:40
 * Desc :
 */

public class EditGroupPresenter extends EditGroupContract.GroupPresenter {
    EditGroupContract.View mView;
    EditGroupContract.Model mModel;
    Context mContext;
    int mGroupId;
    ContactRepository instance;

    public EditGroupPresenter(EditGroupContract.View view,
                              EditGroupContract.Model model,
                              Context context,
                              int groupId) {
        onAttached();
        mView = view;
        mModel = model;
        mContext = context;
        mGroupId = groupId;
        instance = ContactRepository.getInstance(mContext);
    }

    @Override
    public void onAttached() {

    }

    @Override
    public void initContent() {
        mModel.getGroupMembers(mContext, mGroupId, 0, 200)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<List<ContactEntity>>(TAG) {
                    @Override
                    public void onNext(List<ContactEntity> list) {
                        List<ContactItem> starList = new ArrayList<>();
                        User user = UserLocalDataSource.getInstance().getUser();
                        for (ContactEntity entity : list) {
                            if (!StringUtils.isNullString(entity.friendOlNo())) {
                                ContactItem item = ContactsUtils.contactEtity2contactItem(entity);
                                if (item.getOlNo().equals(user.getOlNo())) {
                                    item.setRealName(user.getRealName());
                                    item.setAvatar(user.getAvatar());
                                }
                                starList.add(item);
                            }
                        }
                        mView.showContent(starList);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mView.showContent(null);
                    }
                });
    }

    @Override
    public void leaveGroup(final String easeId) {
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage(mContext.getString(R.string.refresh_footer));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        String olNoMy = UserLocalDataSource.getInstance().getOlNo();
        instance.removeGroupMember(mGroupId, olNoMy)
                .flatMap(new Func1<Boolean, Observable<Object>>() {
                    @Override
                    public Observable<Object> call(Boolean aBoolean) {
                        RxBus.get().post(EventToken.GROUP_LEAVE, true);
                        return leaveByEase(easeId);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<Object>(TAG) {
                    @Override
                    public void onNext(Object object) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        progressDialog.dismiss();
                    }
                });
    }

    Observable leaveByEase(final String easeId) {
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                try {
                    removeConversation(easeId);
                    String current = EMClient.getInstance().getCurrentUser();
                    Timber.tag("EditGroupActivity").d("leaveByEase " + easeId + " , " + current);
                    EMClient.getInstance().groupManager().leaveGroup(easeId);//需异步处理
                    subscriber.onNext(null);
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    private void removeConversation(String easeId) {
        EMClient.getInstance().chatManager().deleteConversation(easeId, true);
    }
}