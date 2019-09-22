package com.hyphenate.easeui.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.hyphenate.easeui.R;

/**
 * OKLine(luoxiuxiu) co.,Ltd.<br/>
 * Author  : luoxiuxiu <br/>
 * Email   : show@okline.cn <br/>
 * Date    : 2017/4/11 <br/>
 * Summary : dialog工具类
 */

public class DialogUtil extends Dialog implements View.OnClickListener{


    private Context context;
    private TextView tvTitle;
    private TextView tvContent;
    private Button btnLeft;
    private Button btnRight;
    private String title;
    private String content;
    private DialogClick dialogClick;

    public DialogUtil(Context context, String title, String content) {
        super(context, R.style.dialog);
        this.context = context;
        this.title = title;
        this.content = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        init();
    }

    public void setScreen(){
        // 将对话框的大小按屏幕大小的百分比设置
        WindowManager windowManager = ((Activity)context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int)(display.getWidth() * 0.95); //设置宽度
        getWindow().setAttributes(lp);
    }

    /**
     * 初始化dialog布局
     */
    private void init() {
        tvTitle = (TextView)findViewById(R.id.dialog_title);
        tvContent = (TextView)findViewById(R.id.dialog_content);
        btnLeft = (Button)findViewById(R.id.btn_left);
        btnRight = (Button)findViewById(R.id.btn_right);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        setTit(title);
        tvTitle.setText(title);
        tvContent.setText(content);
    }

    /**
     * 提交按钮文本(左边)
     * @param submit
     */
    public void setSubmitLeft(String submit){
        btnLeft.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(submit)){
            btnLeft.setText(submit);
        }
    }
    /**
     * 提交按钮文本(右边)
     * @param submit
     */
    public void setSubmitRight(String submit){
        btnRight.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(submit)){
            btnRight.setText(submit);
        }
    }

    /**
     * title设置
     * @param title
     */
    public void setTitle(String title){
        tvTitle.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(title)){
            tvTitle.setText(title);
        }
    }

    /**
     * 设置title
     * @param tit
     */
    public void setTit(String tit) {
        if(TextUtils.isEmpty(tit)){
            tvTitle.setVisibility(View.GONE);
        }else{
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(tit);
        }
    }

    @Override
    public void onClick(View v) {
        int id  = v.getId();
        if (id == R.id.btn_right) {
            v.setTag(context.getString(R.string.yes));
            if(dialogClick == null){
                dismiss();
                return;
            }
            dialogClick.dialogSumbit(v);
        }
        dismiss();
    }

    /**
     * 提交监控
     */
    public void clickSumbit(DialogClick dialogClick){
            this.dialogClick = dialogClick;
    }

}
