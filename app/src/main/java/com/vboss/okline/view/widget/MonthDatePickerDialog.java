package com.vboss.okline.view.widget;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vboss.okline.R;

import java.util.Calendar;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/3/30 15:46 <br/>
 * Summary  : 日期选择dialog
 */

public class MonthDatePickerDialog extends DatePickerDialog {
    private static final String TAG = DatePickerDialog.class.getSimpleName();
    private TextView tv_dialog_title;

    public MonthDatePickerDialog(Context context, OnDateSetListener listener, int year, int month, int dayOfMonth) {
        this(context, 0, listener, year, month, dayOfMonth);
    }

    public MonthDatePickerDialog(Context context, int themeResId, OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth) {
        super(context, themeResId, listener, year, monthOfYear, dayOfMonth);
        update(context, getDatePicker(), year, monthOfYear, dayOfMonth);

    }

    private void update(Context context, DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        try {
            //Android 7.0 报错
            ViewGroup parent = (ViewGroup) view.getChildAt(0);
            LinearLayout container = null;
            if (parent != null) {
                container = (LinearLayout) parent.getChildAt(0);
            }
            if (container != null) {
                View view1 = container.getChildAt(0);
                if (view1 != null) {
                    view1.setVisibility(View.GONE);
                }
                View view2 = container.getChildAt(1);
                if (view2 != null) {
                    view2.setVisibility(View.GONE);
                }
            }

            View layout_title = LayoutInflater.from(context).inflate(R.layout.layout_date_title, container, false);
            tv_dialog_title = (TextView) layout_title.findViewById(R.id.tv_date_dialog_title);
            //调整月份和日期格式
            boolean month0 = (monthOfYear < 9);
            boolean day0 = (dayOfMonth <= 9);
            String text = year + "-" + (month0 ? "0" : "") + (monthOfYear + 1) + "-" + (day0 ? "0" : "") + dayOfMonth;
            tv_dialog_title.setText(text);
            // tv_dialog_title.setText(year + "- " + (monthOfYear + 1) + " - " + dayOfMonth);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                layout_title.setPadding(
                        0,
                        context.getResources().getDimensionPixelSize(R.dimen.dialog_date_margin_top),
                        0,
                        context.getResources().getDimensionPixelSize(R.dimen.dialog_date_margin_bottom));
            } else {
                layout_title.setPadding(
                        0,
                        context.getResources().getDimensionPixelSize(R.dimen.dialog_date_margin_top),
                        0,
                        context.getResources().getDimensionPixelSize(R.dimen.dialog_date_margin_top));
            }
            setCustomTitle(layout_title);
            Calendar calendar = Calendar.getInstance();
            view.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                    new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Log.i(TAG, year + "- " + monthOfYear + " - " + dayOfMonth);
                            //调整月份和日期格式
                            boolean month0 = (monthOfYear < 9);
                            boolean day0 = (dayOfMonth <= 9);
                            String text = year + "-" + (month0 ? "0" : "") + (monthOfYear + 1) + "-" + (day0 ? "0" : "") + dayOfMonth;
                            tv_dialog_title.setText(text);
                            // tv_dialog_title.setText(year + "- " + (monthOfYear + 1) + " - " + dayOfMonth);

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
