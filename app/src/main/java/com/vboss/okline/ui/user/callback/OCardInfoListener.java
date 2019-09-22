package com.vboss.okline.ui.user.callback;

import com.cosw.sdkblecard.DeviceInfo;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/6/13
 * Summary : 在这里描述Class的主要功能
 */

public interface OCardInfoListener {
    void onStart();
    void onFetch(DeviceInfo okCard);
    void onErr(String message);
}
