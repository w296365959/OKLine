package com.vboss.okline.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.TextView;

import com.vboss.okline.R;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/4/7 14:28 <br/>
 * Summary  : 在这里描述Class的主要功能
 */

public class CommonDialog extends Dialog {

    TextView tv_cancel;
    TextView tv_ok;
    TextView tv_title;
    TextView tv_content;

    public CommonDialog(@NonNull Context context) {
        this(context, R.style.dialog);
    }

    public CommonDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_card_default);
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
    public CommonDialog setTilte(String title) {
        setTitleVisible(View.VISIBLE);
        tv_title.setText(title);
        return this;
    }

    public CommonDialog setNegativeColor(int color) {
        tv_cancel.setTextColor(color);
        return this;
    }

    /**
     * set title visible
     *
     * @param visible int
     */
    public CommonDialog setTitleVisible(int visible) {
        tv_title.setVisibility(visible);
        return this;
    }

    /**
     * set dialog content
     *
     * @param content
     */
    public CommonDialog setContent(String content) {
        setContentVisible(View.VISIBLE);
        tv_content.setText(content);
        return this;
    }

    /**
     * set content visible
     *
     * @param visible int
     */
    public CommonDialog setContentVisible(int visible) {
        tv_content.setVisibility(visible);
        return this;
    }

    /**
     * set ensure text
     *
     * @param button text
     */
    public CommonDialog setPositiveButton(String button) {
        tv_ok.setText(button);
        return this;
    }

    /**
     * set cancel text
     *
     * @param button text
     */
    public CommonDialog setNegativeButton(String button) {
        tv_cancel.setText(button);
        return this;
    }


    /**
     * set dialog click listener
     *
     * @param listener
     */
    public CommonDialog setOnDialogClickListener(final OnDialogInterface listener) {
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.cancel(v, CommonDialog.this);
                }
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.ensure(v, CommonDialog.this);
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
        void cancel(View view, CommonDialog dialog);

        /**
         * ensure
         *
         * @param view   View
         * @param dialog CommonDialog
         */
        void ensure(View view, CommonDialog dialog);
    }

}
