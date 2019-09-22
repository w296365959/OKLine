package com.vboss.okline.data.entities;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/31 <br/>
 * Summary : 联系人群组
 */

@AutoValue
public abstract class ContactGroup {
    /**
     * 群组Id
     */
    public abstract int groupId();

    /**
     * 环信群组Id
     */

    @Nullable
    public abstract String easeGroupId();

    /**
     * 群组名称
     */
    public abstract String groupName();

    public static TypeAdapter<ContactGroup> typeAdapter(Gson gson) {
        return new AutoValue_ContactGroup.GsonTypeAdapter(gson);
    }
}
