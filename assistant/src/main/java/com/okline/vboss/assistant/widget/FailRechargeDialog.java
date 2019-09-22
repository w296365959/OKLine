package com.okline.vboss.assistant.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.TextView;

import com.okline.vboss.assistant.R;


/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/4/11 17:02 <br/>
 * Summary  : 充值失败并返回金额提示
 */

public class FailRechargeDialog extends Dialog {
    private TextView tv_dialog_content;
    private TextView tv_dialog_ok;

    public FailRechargeDialog(@NonNull Context context) {
        this(context, R.style.style_dialog);
    }

    public FailRechargeDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_single_button_assistant);
        setCanceledOnTouchOutside(false);

        tv_dialog_ok = (TextView) findViewById(R.id.tv_dialog_ensure);
        tv_dialog_content = (TextView) findViewById(R.id.tv_dialog_content);
    }


    /**
     * set dialog content
     *
     * @param content dialog content
     */
    public void setDialogContent(String content) {
        tv_dialog_content.setText(content);
    }


    /**
     * set dialog ensure button text
     *
     * @param button dialog button
     */
    public void setDialogButton(String button) {
        tv_dialog_ok.setText(button);
    }

    /**
     * set dialog ensure button click listener
     *
     * @param listener OnAuthDialogInterface
     */
    public void setOnAuthDialogButton(final OnAuthDialogInterface listener) {
        tv_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onEnsure(v, FailRechargeDialog.this);
                }
            }
        });
    }


    public interface OnAuthDialogInterface {
        void onEnsure(View v, DialogInterface dialog);
    }
}
