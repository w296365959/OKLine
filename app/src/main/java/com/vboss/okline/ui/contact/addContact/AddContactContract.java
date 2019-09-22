package com.vboss.okline.ui.contact.addContact;

import com.vboss.okline.base.BaseModel;
import com.vboss.okline.base.BasePresenter;
import com.vboss.okline.base.BaseView;
import com.vboss.okline.data.ContactRepository;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.ui.contact.c2cPart.C2Contract;

import java.util.List;

import rx.Observable;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/5/3 17:52
 * Desc :
 */

public interface AddContactContract {

    interface Model extends BaseModel{
        /**
         * 获取联系人的集合
         */
        Observable<List<ContactEntity>> getContactList(ContactRepository repository);
    }

    interface View extends BaseView{

        /**
         * 提醒
         * @param message 提醒内容
         */
        void notice(int message);

        /**
         * 获取所有联系人
         * @param entities 联系人集合
         */
        void getAllContact(List<ContactEntity> entities);

        /**
         * 请求服务器失败
         */
        void onError();
    }

    abstract class AddContactPresenter extends BasePresenter<AddContactContract.Model,AddContactContract.View> {
        /**
         * 添加联系人
         * @param entity 联系人对象
         */
        public abstract void addContact(ContactEntity entity);

        /**
         * 获取所有联系人
         */
        public abstract void getAllContact();
    }
}
