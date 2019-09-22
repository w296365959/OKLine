package com.vboss.okline.ui.contact;

import android.view.View;

import com.hyphenate.easeui.ui.EaseChatFragment;
import com.vboss.okline.ui.im.ChatFragment;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Jiang Zhongyuan <br/>
 * Email   : jiangzhongyuan@okline.cn <br/>
 * Date    : 17/4/25 <br/>
 * Summary : 好友聊天记录
 */
public class HistoryChatFragment extends ChatFragment {

    @Override
    protected void setUpView() {
        super.setUpView();
        inputMenu.setVisibility(View.GONE);
        hideTitleBar();
    }
}
