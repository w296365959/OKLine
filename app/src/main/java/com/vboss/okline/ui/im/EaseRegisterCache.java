package com.vboss.okline.ui.im;

import android.app.Activity;
import android.text.TextUtils;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.exceptions.HyphenateException;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.data.local.AppConfig;
import com.vboss.okline.data.local.SPUtils;
import com.vboss.okline.utils.AppUtil;

import timber.log.Timber;

/**
 * OKLine(luoxiuxiu) co.,Ltd.<br/>
 * Author  : luoxiuxiu <br/>
 * Email   : show@okline.cn <br/>
 * Date    : 2017/4/14 <br/>
 * Summary : IM登录信息
 */

public class EaseRegisterCache {
    private final static String TAG = EaseRegisterCache.class.getSimpleName();
    private Activity activity;
    private String userName;
    private String pwd;

    /**
     * @param context
     * @param userName 用户名
     * @param pwd  密码
     */
    public EaseRegisterCache(Activity context, String userName, String pwd) {
        this.activity = context;
        this.userName = userName;
        this.pwd = pwd;
        register();
    }

    /**
     * IM 用户注册
     */
    public void register() {

        userName = userName.toLowerCase();
        if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(pwd)) {
            Timber.tag(TAG).d("==> userName or pass isEmpty  ");
            return;
        }
        pwd = pwd.replaceAll(" ", "");
        new Thread(new Runnable() {
            public void run() {
                try {
                    // call method in SDK
                    EMClient.getInstance().createAccount(userName, pwd);
                    Timber.tag(TAG).v("==> register success  ");
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            login();
                        }
                    });
                } catch (HyphenateException e) {
                    int errorCode=e.getErrorCode();
                    if(errorCode==EMError.NETWORK_ERROR){//网络异常
                    }else if(errorCode == EMError.USER_ALREADY_EXIST){//用户已存在
                        login();
                        Timber.tag(TAG).d("==> User already exist ! ...");
                    }else if(errorCode == EMError.USER_AUTHENTICATION_FAILED){//无权限！
                    }else if(errorCode == EMError.USER_ILLEGAL_ARGUMENT){//用户名不合法
                    }else{//注册失败
                    }

                }
            }
        }).start();
    }

    /**
     * IM 登录
     */
    public void login() {
        if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(pwd)) {
            Timber.tag(TAG).d("==> userName or pass isEmpty  ");
            return;
        }
        EMClient.getInstance().login(userName, pwd, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                try {
                  SPUtils.saveSp (activity, AppConfig.SP_EASE_NUMBER,userName);
                  Timber.tag(TAG).i( "登录聊天服务器成功！");
                  setEaseUser(userName);

                } catch (Exception e) {
                    e.printStackTrace();
                    Timber.tag(TAG).d("==> IM login fail...");
                }
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Timber.tag(TAG).d(code  +"》》"+message);
                Timber.tag(TAG).d("登录聊天服务器失败！");
            }
        });
    }

    private void setEaseUser(String userId){
        EaseUser easeUser = EaseUserUtils.getUserInfo(userId);
        User user = UserRepository.getInstance(activity).getUser();
        easeUser.setAvatar(user.getAvatar());
        easeUser.setPhone(user.getPhone());
        easeUser.setNick(user.getRealName());
        easeUser.setNickname(user.getRealName());
        easeUser.setRealName(user.getRealName());
    }


}
