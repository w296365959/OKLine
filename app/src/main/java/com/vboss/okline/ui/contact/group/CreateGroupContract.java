package com.vboss.okline.ui.contact.group;

import android.content.Context;

import com.vboss.okline.base.BaseModel;
import com.vboss.okline.base.BasePresenter;
import com.vboss.okline.base.BaseView;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.ui.contact.bean.ContactItem;

import java.util.List;

import rx.Observable;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/3/31 19:59
 * Desc :
 */

public interface CreateGroupContract {

    interface Model extends BaseModel{
        Observable<List<ContactEntity>> getList(Context context);
    }
    interface View extends BaseView {
        void showContent(List<ContactItem> list);

        void enterGroup(ContactItem item);

        //服务器创建群失败
        void createGroupError();

        void addMemberResult(Boolean result);
    }

    abstract class GroupPresenter extends BasePresenter<Model,View> {
        public abstract void initContent();

        public abstract void createGroup(List<ContactItem> checkedList);

        public abstract void addMembers(final int groupId,  String easeID,final List<ContactItem> checkedList);

        abstract void unSubscribe();
    }
}
