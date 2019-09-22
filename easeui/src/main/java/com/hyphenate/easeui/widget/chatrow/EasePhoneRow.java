package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.R;
import com.hyphenate.exceptions.HyphenateException;

/**
 * Okline(Hangzhou)co,Ltd<br/>
 * Author: Mengyupeng<br/>
 * Email:  mengyupeng@okline.cn</br>
 * Date : $(DATE) </br>
 * Summary:
 */

public class EasePhoneRow extends EaseChatRow {

    TextView tv_phone;

    public EasePhoneRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }
    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_phone : R.layout.ease_row_sent_phone, this);
    }

    @Override
    protected void onFindViewById() {
          tv_phone=(TextView) findViewById(R.id.tv_phone_message);
    }

    @Override
    protected void onUpdateView() {
         adapter.notifyDataSetChanged();
    }

    @Override
    protected void onSetUpView() {
        tv_phone.setText(message.getStringAttribute(EaseConstant.MESSAGE_PHONE_TIME,""));
        handleTextMessage();
    }
    protected void handleTextMessage() {
        if (message.direct() == EMMessage.Direct.SEND) {
            setMessageSendCallback();
            switch (message.status()) {
                case CREATE:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.GONE);
                    break;
                case FAIL:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    break;
                case INPROGRESS:
                    progressBar.setVisibility(View.VISIBLE);
                    statusView.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }else{
            if(!message.isAcked() && message.getChatType() == EMMessage.ChatType.Chat){
                try {
                    EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    protected void onBubbleClick() {

    }
}
