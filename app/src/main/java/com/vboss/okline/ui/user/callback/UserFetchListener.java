package com.vboss.okline.ui.user.callback;

import com.vboss.okline.data.entities.User;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/4/12
 * Summary : 在这里描述Class的主要功能
 */

public interface UserFetchListener {
    void onFetch(User user);
    void onFail(String message);
}
