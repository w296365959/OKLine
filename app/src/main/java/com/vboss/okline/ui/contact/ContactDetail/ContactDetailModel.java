package com.vboss.okline.ui.contact.ContactDetail;

import android.content.Context;

import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.ContactRepository;
import com.vboss.okline.data.entities.ChatLog;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.data.entities.ContactGroup;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.vboss.okline.R.id.view;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/4/5 09:28
 * Desc :
 */

public class ContactDetailModel implements ContactDetailContract.Model {
    private static final String TAG = "ContactDetailModel";
    @Override
    public Observable<ContactEntity> getDetail(Context context,int contactId) {
        ContactRepository instance = ContactRepository.getInstance(context);
        return instance.getContact(contactId);
    }

    @Override
    public Observable<List<ChatLog>> getChatLog(Context context, int contactId, ContactRepository repository) {

        return /*repository.getChatLogToMe(contactId)*/Observable.empty();
    }

    @Override
    public void setStar(Context context, final int contactID) {
        final ContactRepository instance = ContactRepository.getInstance(context);
        instance.getContact(contactID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<ContactEntity>(TAG){

                });
    }
}
