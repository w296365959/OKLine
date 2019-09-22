package com.hyphenate.easeui.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.controller.EaseUI.EaseUserProfileProvider;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseContactModel;
import com.hyphenate.easeui.widget.DefaultSubscribe;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.data.entities.User;

import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EaseUserUtils {
    private final static String TAG = EaseUserUtils.class.getSimpleName();

    static EaseUserProfileProvider userProvider;

    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }

    /**
     * get EaseUser according username
     *
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username) {
        if (userProvider != null)
            return userProvider.getUser(username);

        return null;
    }

    /**
     * set user avatar
     *
     * @param username
     */
    public static void setUserAvatar(final Context context, String username, final ImageView imageView) {
        imageView.setImageResource(R.drawable.em_default_avatar);
    }

    public static void setAvatarNick(final Context context, final String username, final ImageView imageView, final TextView tvNick) {
        // update by luoxiuxiu 170614 好友信息在本地数据进行获取
        Map<String, ContactEntity> map = EaseContactModel.getInstance().getContactMap();
        if(map !=null)
        {
            ContactEntity entity = map.get(username.toUpperCase());
            if(entity !=null){
                String realName = entity.realName();
                String remarkName = entity.remarkName();
                String avatar = entity.imgUrl();
                if(!TextUtils.isEmpty(avatar) && entity.relationState() == 3){
                    Glide.with(context).load(avatar).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ol_default_avatar).into(imageView);
                }
                if (!TextUtils.isEmpty(realName))
                    tvNick.setText(realName);
                else
                    tvNick.setText(remarkName);
            }
//            else
//                setUserOL(context,username,imageView,tvNick);
        }
//        else
//            setUserOL(context,username,imageView,tvNick);
    }

    //欧乐会员，非好友，数据请求
    private static void setUserOL(final Context context, final String username, final ImageView imageView, final TextView tvNick){
        UserRepository.getInstance(context).getOKLineMember(username.toUpperCase()).observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<User>(TAG){
                    @Override
                    public void onNext(final User user) {
                        super.onNext(user);
                        if(user!=null){
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(user == null)return;
                                    tvNick.setText(user.getRealName());
                                    String avatar = user.getAvatar();
                                    if(!TextUtils.isEmpty(avatar)){
                                        Glide.with(context).load(avatar).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ol_default_avatar).into(imageView);
                                    }
                                }
                            });

                        }
                    }
                });
    }

    /**
     * set user's nickname
     */
    public static void setUserNick(String username, TextView textView) {
        if (textView != null) {
            EaseUser user = getUserInfo(username);
            if (user != null && user.getNick() != null) {
                textView.setText(user.getNick());
            } else {
                if (user != null && user.getNickname() != null)
                    textView.setText(user.getNickname());
                else
                    textView.setText(username);
            }
        }
    }

}
