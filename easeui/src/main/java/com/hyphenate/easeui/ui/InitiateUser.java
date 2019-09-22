package com.hyphenate.easeui.ui;

import android.content.Context;

import com.hyphenate.easeui.ChatContentUtil;

/**
 * OKLine(luoxiuxiu) co.,Ltd.<br/>
 * Author  : luoxiuxiu <br/>
 * Email   : show@okline.cn <br/>
 * Date    : 2017/4/18 <br/>
 * Summary :  启动IM
 */

public class InitiateUser {

    private Context context;
    private String userName;
    private String pass;

    private InitiateUser(){
        context = ChatContentUtil.getContext().getApplicationContext();
    }

    public InitiateUser(String userName,String pass){
        this.userName = userName;
        this.pass = pass;
    }


    private void initIMRegister(){

    }

    private void initIMLogin(){

    }


}
