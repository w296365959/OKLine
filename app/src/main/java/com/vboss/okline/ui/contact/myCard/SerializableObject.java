package com.vboss.okline.ui.contact.myCard;

import java.io.Serializable;
import java.util.List;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/6/14 15:17
 * Desc :
 */

public class SerializableObject<T> implements Serializable {
    public T obj;

    public SerializableObject(T obj) {
        this.obj = obj;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }
}
