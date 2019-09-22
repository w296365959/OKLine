package com.vboss.okline.ui.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/6/19 <br/>
 * Summary : Fragment跳转接口
 */
public interface IFragmentSwitcher {

    /**
     * 进入下一级
     *
     * @param fClass  目标Fragment的class
     * @param args    要携带的参数
     * @param current
     */
    void goNext(Class<? extends Fragment> fClass, Bundle args, Fragment current);

    /**
     * 返回上一级
     */
    void goBack();

    /**
     * 跳到纵深的顶部
     */
    void goTop();

}
