package com.hyphenate.easeui;

import android.content.Context;

/**
 * OKLine(luoxiuxiu) co.,Ltd.<br/>
 * Author  : luoxiuxiu <br/>
 * Email   : show@okline.cn <br/>
 * Date    : 2017/4/15 <br/>
 * Summary :  聊天公共類
 */

public class ChatContentUtil {
    private static ChatContentUtil instance;
    private static Context context;


    private ChatContentUtil(){}

    public static ChatContentUtil getInstance(){
        if(instance == null){
            instance = new ChatContentUtil();
        }
        return instance;
    }

    public static Context getContext() {
        return context;
    }

    public void setContext(Context context){
        this.context = context ;
    }



}
