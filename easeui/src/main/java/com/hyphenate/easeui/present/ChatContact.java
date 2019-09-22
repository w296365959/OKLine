package com.hyphenate.easeui.present;

/**
 * Okline(Hangzhou)co,Ltd<br/>
 * Author: Mengyupeng<br/>
 * Email:  mengyupeng@okline.cn</br>
 * Date : $(DATE) </br>
 * Summary:
 */

public interface ChatContact {


    public interface  ChatPresent{
         void registerPhoneReceiver();
         void unRegisterPhoneReceiver();
    }

    public  interface ChatView{


        void sendPhoneMessage(String time);
    }
}
