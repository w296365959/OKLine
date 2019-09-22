package com.vboss.okline.ui.contact.search;

import android.content.Context;
import android.util.Log;

import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.ui.contact.ContactComparator;
import com.vboss.okline.ui.contact.ContactsUtils;
import com.vboss.okline.ui.contact.bean.ContactItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/4/5 10:12
 * Desc :
 */

public class ContactSearchPresenter extends ContactSearchContract.SearchPresenter{
    private static final String TAG = "ContactSearchPresenter";
    ContactSearchContract.Model mModel;
    ContactSearchContract.View mView;
    Context context;
    public ContactSearchPresenter(ContactSearchContract.View view, ContactSearchContract.Model model, Context context){
        mModel = model;
        mView = view;
        this.context = context;
    }
    @Override
    public void onAttached() {

    }

    @Override
    void search(String key2search) {
        mModel.getResult(key2search,context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<List<ContactEntity>>(TAG){
                    @Override
                    public void onNext(List<ContactEntity> list) {
                        Log.i("ContactSearchPresenter", "onNext: "+list.size());
                        List<ContactItem> itemList = new ArrayList<>();
                        //TODO 把contactEntities转换成contactItem 因为还要显示群组
                        for (int i = 0; i < list.size(); i++) {
                            ContactItem item = ContactsUtils.contactEtity2contactItem(list.get(i));
                            itemList.add(item);
                        }
                        Collections.sort(itemList,new ContactComparator());
                        mView.showResult(itemList);
                    }
                });
    }
}
