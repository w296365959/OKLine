package com.vboss.okline.ui.im;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hyphenate.easeui.Constant;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.vboss.okline.base.EventToken;

/**
 * OKLine(luoxiuxiu) co.,Ltd.<br/>
 * Author  : luoxiuxiu <br/>
 * Email   : show@okline.cn <br/>
 * Date    : 2017/4/24 <br/>
 * Summary : 聊天界面。参数(必填)：Constant.EXTRA_USER_ID(聊天对象ID),Constant.EXTRA_REAL_NAME(聊天对象昵称)
 * 以下参数可以为空 EaseConstant.CHATTYPE_GROUP 群聊  Constant.EXTRA_PHONE_NUM 手机号
 */
public class ChatActivity extends EaseBaseActivity {
    public static ChatActivity activityInstance;
    private EaseChatFragment chatFragment;
    String toChatUsername;
    String toNick;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.chat_activity);
        RxBus.get().register(this);
        activityInstance = this;
        //get user id or group id
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            toChatUsername = bundle.getString(Constant.EXTRA_USER_ID);
            toNick = bundle.getString(Constant.EXTRA_REAL_NAME);
            if(!TextUtils.isEmpty (toNick))
                setTitle (toNick);
        }
        //use EaseChatFratFragment
        chatFragment = new ChatFragment();
        //pass parameters to chat fragment
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.fl_chat_content, chatFragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
        activityInstance = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // make sure only one chat activity is opened
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }

    public String getToChatUsername() {
        return toChatUsername;
    }

//    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//        @NonNull int[] grantResults) {
//        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
//    }

    @Subscribe(tags = {@Tag(EventToken.GROUP_LEAVE)})
    public void onLeaveGroup(Boolean bool) {
        finish();
    }
}
