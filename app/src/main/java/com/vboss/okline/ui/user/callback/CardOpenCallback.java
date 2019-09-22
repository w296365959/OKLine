package com.vboss.okline.ui.user.callback;

import com.vboss.okline.data.entities.CardEntity;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/4/13
 * Summary : 在这里描述Class的主要功能
 */

public interface CardOpenCallback {
    void onProcedureFinished(CardEntity flag);
    void onExceptionCaught(String message);
}
