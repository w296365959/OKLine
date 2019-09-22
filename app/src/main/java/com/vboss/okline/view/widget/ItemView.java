package com.vboss.okline.view.widget;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vboss.okline.R;

import java.io.UnsupportedEncodingException;

import timber.log.Timber;


/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/6/5 10:11
 * Desc : 自定义管理条目
 */
public class ItemView extends FrameLayout {
    private static final String TAG = "ItemView";
    private int selectionStart;
    private int selectionEnd;
    private EditText mEtPhone;
    private TextView mTvLabel;
    private LinearLayout mDeletePhone;
    private String value;
    private DeleteCallBack deleteCallBack;

    private LabelClickListener labelClickListener;

    public ItemView(Context context) {
        this(context, null);
    }

    public ItemView(Context context, AttributeSet attrs) {

        super(context, attrs);

        // 将条目布局增加到自定义的布局中
        View view = View.inflate(context, R.layout.contacts_create_item, null);
        mTvLabel = (TextView) view.findViewById(R.id.tv_custom_label);
        mEtPhone = (EditText) view.findViewById(R.id.et_contact_phone);
        mDeletePhone = (LinearLayout) view.findViewById(R.id.ll_delete_phone);
        this.addView(view);
        init();
    }



    private void init() {
        // 删除按钮事件
        mDeletePhone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteCallBack != null) {
                    // 通过接口回调
                    deleteCallBack.deleteLayout(ItemView.this);
                }
            }
        });

        //按钮点击事件
        mTvLabel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != labelClickListener) {
                    labelClickListener.toLabelFragment(ItemView.this);
                }
            }
        });

        //modify by linzhangbin 2017/6/19 产品要求此处不设置字数限制
//         输入框的内容改变事件
       /* mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                byte[] gbks = null;
                    try {
                        gbks = s.toString().getBytes("GBK");

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    selectionStart = mEtPhone.getSelectionStart();
                    selectionEnd = mEtPhone.getSelectionEnd();
                    Log.i("MainActivity", "afterTextChanged: length = " + gbks.length);
                    //如果内容大于16个字节把后面输入的全部去掉
                    if (gbks.length > 16) {
                        Log.i("MainActivity", "onTextChanged: gbks = " + gbks);
                        s.delete(selectionStart - 1, selectionEnd);
                        int tempSelection = selectionEnd;
                        mEtPhone.setText(s);
                        mEtPhone.setSelection(tempSelection);//设置光标在最后
                    }
                // 改变当前条目的值
                value = s.toString();
            }
        });*/
        //modify by linzhangbin 2017/6/19 产品要求此处不设置字数限制
    }

    public DeleteCallBack getDeleteCallBack() {
        return deleteCallBack;
    }

    /**
     * 设置删除监听
     *
     * @param deleteCallBack
     */
    public void setDeleteCallBack(DeleteCallBack deleteCallBack) {
        this.deleteCallBack = deleteCallBack;
    }

    /**
     * 设置标签点击监听
     */
    public void setLabelClickListener(LabelClickListener labelClickListener){
        this.labelClickListener = labelClickListener;
    }

    /*****************************************************************/
    /*改变和获取单个phoneBean对象中的字段，前提是必须调用过setData(User user)方法，否则报 null 异常*/
    /*****************************************************************/
    public String getLabelContent() {
        return mTvLabel.getText().toString();
    }

    public void setLabelContent(String tvContent) {
        this.mTvLabel.setText(tvContent);
    }

    public String getEtContent() {
        return this.mEtPhone.getText().toString();
    }

    public void setEtContent(String etContent) {
        this.mEtPhone.setText(etContent);
    }
    /*****************************************************************/

    /**
     * 设置数据，直接设置User类型
     *
     */
    public void setData(String label,String phone) {
        mTvLabel.setText(label);
        mEtPhone.setText(phone);
    }

    public void setPhoneUI() {
        mEtPhone.setHint("请输入电话号码");
        mEtPhone.setInputType(InputType.TYPE_CLASS_PHONE);
    }

    public void setEmailUI() {
        mEtPhone.setHint("请输入邮箱地址");
        mEtPhone.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    }


    /**
     * 定义删除接口
     */
    public interface DeleteCallBack {
        void deleteLayout(ItemView itemView);
    }

    /**
     * 定义标签被点击接口
     */
    public interface LabelClickListener {
        void toLabelFragment(ItemView itemView);
    }
}
