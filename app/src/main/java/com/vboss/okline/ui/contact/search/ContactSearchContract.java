package com.vboss.okline.ui.contact.search;

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
 * Date : 2017/4/5 10:04
 * Desc :
 */

public interface ContactSearchContract {
    interface Model extends BaseModel{
        Observable<List<ContactEntity>> getResult(String key, Context context);
    }

    interface View extends BaseView{
        void showResult(List<ContactItem> list);
    }

    abstract class SearchPresenter extends BasePresenter<Model,View>{
        abstract void search(String key2search);
    }
}
