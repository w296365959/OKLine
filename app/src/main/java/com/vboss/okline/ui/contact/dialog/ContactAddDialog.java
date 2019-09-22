package com.vboss.okline.ui.contact.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;



import com.vboss.okline.R;
import com.vboss.okline.utils.StringUtils;



/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/3/29 11:52
 * Desc : 添加联系人dialog
 */
public class ContactAddDialog extends Dialog {
    private static final String TAG = "ContactAddDialog";
    private Context context;
    private LinearLayout ll_refresh;
    private LinearLayout ll_create_group;
    private LinearLayout ll_gathering;
    private ClickListenerInterface clickListenerInterface;


    public ContactAddDialog(Context context, int theme) {
        super(context,theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    /**
     * 初始化
     */
    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_contacts_add, null);
        setContentView(view);
        ll_refresh = (LinearLayout)findViewById(R.id.ll_refresh);
        ll_create_group = (LinearLayout)findViewById(R.id.ll_create_group);
        ll_gathering = (LinearLayout)findViewById(R.id.ll_gathering);
        ll_refresh.setOnClickListener(new clickListener());
        ll_create_group.setOnClickListener(new clickListener());
        ll_gathering.setOnClickListener(new clickListener());
        /*int screenWidth = context.getWindowManager().getDefaultDisplay()
                .getWidth();*/
        //获取屏幕宽高信息
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;
        int heightPixels = displayMetrics.heightPixels;

        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.TOP | Gravity.LEFT);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = widthPixels - StringUtils.dip2px(context,350);
        lp.y = StringUtils.dip2px(context,44);
        Log.i("ContactAddDialog", "heightPixels = "+heightPixels+"---widthPixels = "+widthPixels+"---x = "+lp.x+"---y = "+lp.y);
        /*lp.x = 280;//设置x坐标
        lp.y = -440;//设置y坐标*/
        //dialogWindow.setAttributes(lp);
    }

    /**
     * 设置事件
     * @param clickListenerInterface
     */
    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    /**
     *
     */
    public interface ClickListenerInterface {
        //添加联系人
        void addContact();
        //创建群
        void createGroup();
        //收款
//        void gathering();
        //扫一扫
        void scaning();
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.ll_refresh:
                    clickListenerInterface.addContact();
                    break;
                case R.id.ll_create_group:
                    clickListenerInterface.createGroup();
                    break;
                case R.id.ll_gathering:
//                    clickListenerInterface.gathering();
                    clickListenerInterface.scaning();
                    break;
            }
        }
    }
}
