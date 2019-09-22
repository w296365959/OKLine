package com.ICE.VOIP.ui;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Jiang Zhongyuan <br/>
 * Email   : jiangzhongyuan@okline.cn <br/>
 * Date    : 17/5/4 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public interface PhoneListener {

    /**
     * 登陆成功
     */
    public void onLoginResult(boolean isSuccess);

    /**
     * 拨出成功时
     */
    public void onCallOut(String phoneNumber);

    /**
     * 响铃时
     *
     * @param active      是否主动发起
     * @param phoneNumber
     */
    public void onRinging(boolean active, String phoneNumber);

    /**
     * 挂断电话时
     *
     * @param active      是否主动发起
     * @param phoneNumber
     */
    public void onHangUp(boolean active, String phoneNumber);

    /**
     * 电话接通时
     *
     * @param active 是否主动发起
     */
    public void onCalling(boolean active, String phoneNumber);
}
