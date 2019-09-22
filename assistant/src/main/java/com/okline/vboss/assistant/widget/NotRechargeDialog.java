package com.okline.vboss.assistant.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.TextView;

import com.okline.vboss.assistant.R;


/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/4/7 14:28 <br/>
 * Summary  : 放弃充值
 */

public class NotRechargeDialog extends Dialog {

    TextView tv_cancel;
    TextView tv_ok;
    TextView tv_title;
    TextView tv_content;

    public NotRechargeDialog(@NonNull Context context) {
        this(context, R.style.style_dialog);
    }

    public NotRechargeDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_card_default_assistant);
        setCanceledOnTouchOutside(false);

        tv_cancel = (TextView) findViewById(R.id.tv_dialog_default_cancel);
        tv_title = (TextView) findViewById(R.id.tv_dialog_default_title);
        tv_content = (TextView) findViewById(R.id.tv_dialog_default_content);
        tv_ok = (TextView) findViewById(R.id.tv_dialog_default_ok);
    }

    /**
     * set dialog title
     *
     * @param title title
     */
    public NotRechargeDialog setTilte(String title) {
        setTitleVisible(View.VISIBLE);
        tv_title.setText(title);
        return this;
    }

    public NotRechargeDialog setNegativeColor(int color) {
        tv_cancel.setTextColor(color);
        return this;
    }

    /**
     * set title visible
     *
     * @param visible int
     */
    public NotRechargeDialog setTitleVisible(int visible) {
        tv_title.setVisibility(visible);
        return this;
    }

    /**
     * set dialog content
     *
     * @param content
     */
    public NotRechargeDialog setContent(String content) {
        setContentVisible(View.VISIBLE);
        tv_content.setText(content);
        return this;
    }

    /**
     * set content visible
     *
     * @param visible int
     */
    public NotRechargeDialog setContentVisible(int visible) {
        tv_content.setVisibility(visible);
        return this;
    }

    /**
     * set ensure text
     *
     * @param button text
     */
    public NotRechargeDialog setPositiveButton(String button) {
        tv_ok.setText(button);
        return this;
    }

    /**
     * set cancel text
     *
     * @param button text
     */
    public NotRechargeDialog setNegativeButton(String button) {
        tv_cancel.setText(button);
        return this;
    }


    /**
     * set dialog click listener
     *
     * @param listener
     */
    public NotRechargeDialog setOnDialogClickListener(final OnDialogInterface listener) {
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.cancel(v, NotRechargeDialog.this);
                }
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.ensure(v, NotRechargeDialog.this);
                }
            }
        });
        return this;
    }


    public interface OnDialogInterface {
        /**
         * cancel
         *
         * @param view   View
         * @param dialog CommonDialog
         */
        void cancel(View view, NotRechargeDialog dialog);

        /**
         * ensure
         *
         * @param view   View
         * @param dialog CommonDialog
         */
        void ensure(View view, NotRechargeDialog dialog);
    }

}
