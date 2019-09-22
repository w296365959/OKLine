package com.vboss.okline.ui.user.callback;

import com.vboss.okline.data.entities.OKCard;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/3/30
 * Summary : 欧卡重启回调接口
 */

public interface OCardResumeListener {
    void onSuccess(OKCard okCard);
    void onFail();
}
