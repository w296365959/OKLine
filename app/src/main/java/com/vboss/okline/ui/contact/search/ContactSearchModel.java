package com.vboss.okline.ui.contact.search;

import android.content.Context;

import com.vboss.okline.base.OKLineApp;
import com.vboss.okline.data.ContactRepository;
import com.vboss.okline.data.entities.ContactEntity;

import java.util.List;

import rx.Observable;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/4/5 10:12
 * Desc :
 */

public class ContactSearchModel implements ContactSearchContract.Model {

    @Override
    public Observable<List<ContactEntity>> getResult(String key, Context context) {
        return ContactRepository.getInstance(OKLineApp.context).searchContact(key);
    }
}
