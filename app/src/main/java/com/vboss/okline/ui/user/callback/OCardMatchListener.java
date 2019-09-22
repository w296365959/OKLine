package com.vboss.okline.ui.user.callback;

import com.vboss.okline.data.entities.OKCard;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/3/30
 * Summary : 匹配欧卡回调接口
 */

public interface OCardMatchListener {
    void onMatchSuccess(OKCard okCard);
    void onMatchFail(String message);
}
