package com.vboss.okline.ui.user.callback;

import com.vboss.okline.data.entities.OKCard;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/3/30
 * Summary : 欧卡挂失回调接口
 */

public interface OCadAbsenceDeclarationListener {
    void onSuccess(OKCard okCard);
    void onFail(String message, OKCard okCard);
}