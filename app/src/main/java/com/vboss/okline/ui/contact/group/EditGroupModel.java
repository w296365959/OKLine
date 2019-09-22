package com.vboss.okline.ui.contact.group;

import android.content.Context;

import com.vboss.okline.data.ContactRepository;
import com.vboss.okline.data.entities.ContactEntity;

import java.util.List;

import rx.Observable;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Jiang Zhongyuan
 * Email : zhongyuan@okline.cn
 * Date : 2017/4/24 18:40
 * Desc :
 */

public class EditGroupModel implements EditGroupContract.Model {

    @Override
    public Observable<List<ContactEntity>> getGroupMembers(Context context, int groupId, int index, int size) {
        ContactRepository instance = ContactRepository.getInstance(context);
        return instance.getGroupMember(groupId, index, size);
    }
}
