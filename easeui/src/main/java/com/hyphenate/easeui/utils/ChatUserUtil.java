package com.hyphenate.easeui.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.present.ChatPresent;

/**
 * OKLine(luoxiuxiu) co.,Ltd.<br/>
 * Author  : luoxiuxiu <br/>
 * Email   : show@okline.cn <br/>
 * Date    : 2017/4/24 <br/>
 * Summary :
 */

public class ChatUserUtil  implements ChatPresent.GroupPresent{
    private static final String TAG = ChatUserUtil.class.getSimpleName ();
    private Context context;
    private ChatPresent.GroupPresent present;


    public ChatUserUtil(Context context, ChatPresent.GroupPresent present) {
        this.context = context;
        this.present = present;
    }
    public ChatUserUtil(Context context){
        this.context =context;
    }

    public void getGoupNick(String groupOL) {
//        ContactRepository repository = ContactRepository.getInstance (context);
//        Subscription subscribe = repository.getContactByOlNo (groupOL).observeOn (AndroidSchedulers.mainThread ()).subscribeOn (Schedulers.io ())
//                .subscribe (new DefaultSubscribe<ContactEntity> (TAG) {
//                    @Override
//                    public void onNext(ContactEntity contactEntity) {
//                        super.onNext (contactEntity);
//                        present.getEntity (contactEntity);
//                    }
//                });
    }

    /**
     * 解散群组
     *
     */
    @Override
    public void deleteGrop(final String groupId) {
        final String st5 = context.getString(R.string.Dissolve_group_chat_tofail);
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMClient.getInstance().groupManager().destroyGroup(groupId);
                } catch (final Exception e) {
                    ((Activity)context).runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(context, st5 + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

}
