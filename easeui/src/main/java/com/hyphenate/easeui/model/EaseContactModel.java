package com.hyphenate.easeui.model;

import android.content.Context;
import android.text.TextUtils;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.data.local.ContactLocalDataSource;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import rx.functions.Action1;

/**
 * OKLine(luoxiuxiu) co.,Ltd.<br/>
 * Author  : luoxiuxiu <br/>
 * Email   : show@okline.cn <br/>
 * Date    : 2017/6/14 <br/>
 * Summary : 获取DB中所有的联系人，放入map容器中
 * 用户通过key去取值
 */

public class EaseContactModel {
    private static EaseContactModel instance =null;
    private Map<String, ContactEntity> map=null;

    public static EaseContactModel getInstance(){
        if(instance == null){
            instance = new EaseContactModel();
        }
        return instance;
    }

    public Map<String, ContactEntity> getContactMap(){
        return map;
    }

    public void localContact(Context context){
        ContactLocalDataSource.getInstance(context).getAllContact().subscribe(new Action1<List<ContactEntity>>() {
                    @Override
                    public void call(List<ContactEntity> contactEntities) {
                        map = new Hashtable<String, ContactEntity>();
                        if(contactEntities!=null){
                            for (ContactEntity entity:contactEntities){
                                if(!TextUtils.isEmpty(entity.friendOlNo()))
                                    map.put(entity.friendOlNo(),entity);
                            }
                        }
                    }
                });;

    }

    public void updateItem(String key,ContactEntity entity){
        if(map!=null)
        {
            map.put(key,entity);
        }
    }
}
