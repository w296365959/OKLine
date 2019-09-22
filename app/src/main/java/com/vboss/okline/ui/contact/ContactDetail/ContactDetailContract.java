package com.vboss.okline.ui.contact.ContactDetail;

import android.content.Context;

import com.vboss.okline.base.BaseModel;
import com.vboss.okline.base.BasePresenter;
import com.vboss.okline.base.BaseView;
import com.vboss.okline.data.ContactRepository;
import com.vboss.okline.data.entities.ChatLog;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.data.entities.User;

import java.util.List;

import rx.Observable;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/3/31 11:48
 * Desc :
 */

public interface ContactDetailContract {

    interface Model extends BaseModel{
        /**
         * 获取联系人详情
         * @param context   上下文
         * @param contactId 联系人id
         * @return
         */
        Observable<ContactEntity> getDetail(Context context,int contactId);

        /**
         * 获取用户与当前联系人的往来记录
         */
        Observable<List<ChatLog>> getChatLog(Context context, int contactId, ContactRepository repository);

        void setStar(Context context,int contactID);
    }

    interface View extends BaseView{
        /**
         * show detail
         * @param entity 联系人对象
         */
        void showDetail(ContactEntity entity);

        void setStar();

        /**
         * 展现联系人名片详情
         * @param user
         */
        void showVisitingCard(User user);

    }

    abstract class DetailPresenter extends BasePresenter<Model,View> {
        /**
         * 初始化详情
         * @param contactID
         */
        public abstract void initDetail(int contactID);

        /**
         * 添加新表
         *
         * @param star
         */
        public abstract void addToStar(int contactID,boolean star);

        /**
         * 取消订阅
         */
        public abstract void unSubscribe();

        /**
         * 发送好友请求
         */
        public abstract void sendFriendRequest();

        /**
         * 查询联系人名片详情
         */
        public abstract void getContactVisitingCard(String phoneNum);
    }
}
