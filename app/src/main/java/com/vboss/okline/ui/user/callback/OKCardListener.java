package com.vboss.okline.ui.user.callback;

import com.vboss.okline.data.entities.OKCard;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/4/10
 * Summary : 在这里描述Class的主要功能
 */

public interface OKCardListener {
    void onFetched(OKCard okCard);
    void onNull();
}
