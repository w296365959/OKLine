package com.vboss.okline.ui.user.callback;

import com.vboss.okline.data.entities.CardEntity;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/4/12
 * Summary : 获取卡片信息回调
 */

public interface CardEntityFetchListener {
    void onFetch(CardEntity cardEntity);
    void onFail(String message);
}
