package com.vboss.okline.ui.contact;


import com.baidu.platform.comapi.map.C;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.vboss.okline.base.BaseModel;
import com.vboss.okline.base.BasePresenter;
import com.vboss.okline.base.BaseView;
import com.vboss.okline.data.ContactRepository;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.data.entities.ContactGroup;
import com.vboss.okline.ui.contact.bean.Contact;
import com.vboss.okline.ui.contact.bean.ContactItem;

import java.util.List;

import rx.Observable;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/3/29 14:52
 * Desc :
 */

public interface ContentContract  {

    interface ContentView extends BaseView{
        /**
         * show dialog
         */
        void dialogShow();

        /**
         * show Contact list
         * @param list contact list
         */
        void showConList(List<ContactItem> list);


        /**
         * show user
         */
        void showUser();

        /**
         * show toast
         * @param size 刷新个数
         */
        void showRefreshToast(int size);

        /**
         * 请求超时
         * @param throwable 异常
         * @param methodFlag 标记
         */
        void requestTimeOut(Throwable throwable, int methodFlag);

        /**
         * 刷新失败页面展示信息
         */
        void showRefreshError();

        /**
         * 展现小红点
         * @param size 小红点消息数
         */
        void showRedPoint(int size);

        /**
         * 先展示数据库里的联系人
         */
        void showContactOnly(List<ContactItem> items,List<ContactItem> commonList,int starSize,int groupSize);


        void showSyncComplete();
    }

    abstract class ContentPresenter extends BasePresenter<ContentModel,ContentView>{

        /**
         * 刷新联系人
         * @param localList 本地联系人集合
         */
        public abstract void refreshContact(List<ContactItem> localList);
//        public abstract void refreshContact(List<ContactEntity> localList);

        /**
         * 展现主界面
         */
        public abstract void showContent();

        /**
         * ShowContact Only
         */
        public abstract void showContactOnly();

        /**
         * 初始化user
         */
        public abstract void initUser();

        /**
         * 取消订阅
         */
        public abstract void unSubscribe();

        /**
         * 获取小红点
         */
        public abstract void refreshRedPoint();

        /**
         * 检查联系人权限
         */
        public abstract boolean hasReadContactPermission();
    }


}
