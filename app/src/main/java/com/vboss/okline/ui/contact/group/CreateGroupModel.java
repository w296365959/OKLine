package com.vboss.okline.ui.contact.group;

import android.content.Context;

import com.vboss.okline.data.ContactRepository;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.data.entities.ContactGroup;
import com.vboss.okline.ui.contact.bean.Contact;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/3/31 20:16
 * Desc :
 */

public class CreateGroupModel implements CreateGroupContract.Model {
    @Override
    public Observable<List<ContactEntity>> getList(Context context) {
        //此处只需要实名认证过的(欧乐会员)
        ContactRepository instance = ContactRepository.getInstance(context);


        return instance.getAllContact();
    }
}
