package com.vboss.okline.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.TextView;

import com.vboss.okline.R;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/4/11 17:02 <br/>
 * Summary  : 在这里描述Class的主要功能
 */

public class CommonAuthDialog extends Dialog {
    private TextView tv_dialog_title;
    private TextView tv_dialog_content;
    private TextView tv_dialog_sub_content;
    private TextView tv_dialog_ok;

    public CommonAuthDialog(@NonNull Context context) {
        this(context, R.style.dialog);
    }

    public CommonAuthDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_single_button);
        setCanceledOnTouchOutside(false);

        tv_dialog_title = (TextView) findViewById(R.id.tv_dialog_title);
        tv_dialog_ok = (TextView) findViewById(R.id.tv_dialog_ensure);
        tv_dialog_content = (TextView) findViewById(R.id.tv_dialog_content);
        tv_dialog_sub_content = (TextView) findViewById(R.id.tv_dialog_sub_content);
    }


    /**
     * set dialog title
     *
     * @param title dialog title
     */
    public void setDialogTitle(String title) {
        tv_dialog_title.setText(title);
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
     * set dialog sub content
     *
     * @param content dialog sub content
     */
    public void setDialogSubContent(String content) {
        tv_dialog_sub_content.setVisibility(View.VISIBLE);
        tv_dialog_sub_content.setText(content);
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
                    listener.onEnsure(v, CommonAuthDialog.this);
                }
            }
        });
    }


    public interface OnAuthDialogInterface {
        void onEnsure(View v, DialogInterface dialog);
    }
}
