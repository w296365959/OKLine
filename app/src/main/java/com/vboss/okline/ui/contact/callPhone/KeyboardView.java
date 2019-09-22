package com.vboss.okline.ui.contact.callPhone;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vboss.okline.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/5/4 11:37
 * Desc :
 */

public class KeyboardView extends RelativeLayout {
    Context context;
    //用GrideView布局键盘，其实并不是真正的键盘，只是模拟键盘的功能
    GridView gridView;
    LinearLayout callPhone;
    LinearLayout keyboardReturn;
    LinearLayout keyboardBottom;
    EditText etKeyboardTop;
    LinearLayout tvDeletePhoneNum;
    RelativeLayout dialTop;

    //因为就6个输入框不会变了，用数组内存申请固定空间，比List省空间（自己认为）

    private ArrayList<Map<String, String>> valueList;
    //因为要用Adapter中适配，用数组不能往adapter中填充


    public KeyboardView(Context context) {
        this(context, null);
    }

    public KeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        View view = View.inflate(context, R.layout.layoutl_keyboard, null);
        gridView = (GridView) view.findViewById(R.id.gv_keyboard);
        callPhone = (LinearLayout) view.findViewById(R.id.call_phone);
        keyboardReturn = (LinearLayout) view.findViewById(R.id.keyboard_return);
        keyboardBottom = (LinearLayout) view.findViewById(R.id.keyboard_bottom);
        etKeyboardTop = (EditText) view.findViewById(R.id.et_keyboard_top);
        tvDeletePhoneNum = (LinearLayout) view.findViewById(R.id.tv_delete_phoneNum);
        dialTop = (RelativeLayout) view.findViewById(R.id.rl_dial_top);
        valueList = new ArrayList<>();

        initValueList();

        setupView();

        addView(view);      //必须要，不然不显示控件
    }

    public ArrayList<Map<String, String>> getValueList() {
        return valueList;
    }

    private void initValueList() {

        // 初始化按钮上应该显示的数字
        for (int i = 1; i < 13; i++) {
            Map<String, String> map = new HashMap<>();
            if (i < 10) {
                map.put("name", String.valueOf(i));
            } else if (i == 10) {
                map.put("name", "*");
            } else if (i == 11) {
                map.put("name", String.valueOf(0));
            } else if (i == 12) {
                map.put("name", "#");
            }
            valueList.add(map);
        }
    }

    public GridView getGridView() {
        return gridView;
    }

    public EditText getEditText() {
        return etKeyboardTop;
    }

    public LinearLayout getCallPhone() {
        return callPhone;
    }

    public LinearLayout getBack() {
        return keyboardReturn;
    }

    public LinearLayout getTvDeletePhoneNum() {
        return tvDeletePhoneNum;
    }

    public RelativeLayout getDialTop() {
        return dialTop;
    }

    private void setupView() {

        KeyBoardAdapter keyBoardAdapter = new KeyBoardAdapter(context, valueList);
        gridView.setAdapter(keyBoardAdapter);
    }
}
