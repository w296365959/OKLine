package com.vboss.okline.ui.contact.addContact;

import com.vboss.okline.data.ContactRepository;
import com.vboss.okline.data.entities.ContactEntity;

import java.util.List;

import rx.Observable;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/5/3 18:07
 * Desc :
 */

public class AddContactModel implements AddContactContract.Model {
    @Override
    public Observable<List<ContactEntity>> getContactList(ContactRepository repository) {
        return repository.getAllContact();
    }
}
