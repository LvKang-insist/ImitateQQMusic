package com.admin.core.ui.date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Copyright (C)
 *
 * @file: DateDialogUtil
 * @author: 345
 * @Time: 2019/5/8 21:40
 * @description: 日期选择器
 */
public class DateDialogUtil {

    public interface IDateListener{
        void onDateChange(String date);
    }
    private String data = null;
    private IDateListener mDataListener = null;

    public void setDateListener(IDateListener listener){
        this.mDataListener = listener;
    }
    public void showDialog(final Context context){
        final LinearLayout ll = new LinearLayout(context);
        final DatePicker picker = new DatePicker(context);
        final LinearLayout.LayoutParams lp  = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        picker.setLayoutParams(lp);
        picker.init(1990, 1, 1, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                final Calendar calendar = Calendar.getInstance();
                //设置时间
                calendar.set(year,monthOfYear,dayOfMonth);
                //日期格式化,使用此Java虚拟机实例的默认区域设置的当前值
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日",Locale.getDefault());
                data = format.format(calendar.getTime());
            }
        });
        ll.addView(picker);
        new AlertDialog.Builder(context)
                .setView(ll)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mDataListener != null && data != null){
                            mDataListener.onDateChange(data);
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }
}
