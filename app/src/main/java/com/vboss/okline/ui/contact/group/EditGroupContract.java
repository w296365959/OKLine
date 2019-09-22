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
 * Author : Jiang Zhongyuan
 * Email : zhongyuan@okline.cn
 * Date : 2017/4/24 18:40
 * Desc :
 */

public interface EditGroupContract {

    interface Model extends BaseModel {
        /**
         * 获取群聊成员
         *
         * @param groupId
         * @return
         */
        Observable<List<ContactEntity>> getGroupMembers(Context context, int groupId, int index, int size);
    }

    interface View extends BaseView {
        void showContent(List<ContactItem> list);
    }

    abstract class GroupPresenter extends BasePresenter<Model, View> {
        public abstract void initContent();

        public abstract void leaveGroup(final String easeId);

    }
}
