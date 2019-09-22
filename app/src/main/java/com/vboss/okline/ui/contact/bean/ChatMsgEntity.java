package com.vboss.okline.ui.contact.bean;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/4/7 20:29
 * Desc :
 */

public class ChatMsgEntity {
    private String name;  //消息来自
    private String date;  //消息日期
    private String message;  //消息内容
    private boolean isComMsg = true;  //是否收到的消息

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isComMsg() {
        return isComMsg;
    }

    public void setComMsg(boolean comMsg) {
        isComMsg = comMsg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
