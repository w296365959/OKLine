package com.vboss.okline.ui.app.jiugongge;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;

import com.vboss.okline.R;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: yuan shao yu  <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2016/10/8 <br/>
 * Desc  :
 */

public class AnimationDialog extends Dialog {

    private Window window = null;

    public AnimationDialog(Context context)
    {
        super(context, R.style.firstpagedialog);
    }

    public void showDialog(View view){
        setContentView(view);

        window = getWindow(); //得到对话框
        //window.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画

        //设置触摸对话框意外的地方取消对话框
        //setCanceledOnTouchOutside(true);
        show();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
