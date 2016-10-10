package myapp.alex.com.businessassistant.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import myapp.alex.com.businessassistant.R;

/**
 * 日期时间选择控件 使用方法： private EditText inputDate;//需要设置的日期时间文本编辑框 private String
 * initDateTime="2012年9月3日 14:44",//初始日期时间值 在点击事件中使用：
 * inputDate.setOnClickListener(new OnClickListener() {
 *
 * @author
 * @Override public void onClick(View v) { DateTimePickDialogUtil
 * dateTimePicKDialog=new
 * DateTimePickDialogUtil(SinvestigateActivity.this,initDateTime);
 * dateTimePicKDialog.dateTimePicKDialog(inputDate);
 * <p>
 * } });
 */
public class DateTimePickDialogUtil implements OnDateChangedListener,
        OnTimeChangedListener {
    private DatePicker datePicker;
    private TimePicker timePicker;
    private AlertDialog ad;
    private String dateTime;
    private String initDateTime;
    private Activity activity;
    private boolean mNeedHMS=true;
    //防止被重复调用  单例模式会出现不明原因的崩溃
//    public static DateTimePickDialogUtil util=null;
//    public static DateTimePickDialogUtil getInstance(Activity activity,Boolean needHMS){
//        if (util==null){
//            util=new DateTimePickDialogUtil(activity,needHMS);
//        }
//        return util;
//    };

    /**
     * 日期时间弹出选择框构造函数
     * @param activity     ：调用的父activity
     //* @param initDateTime 初始日期时间值，作为弹出窗口的标题和日期时间初始值
     @param needHMS 是否显示时分秒
     */
    public DateTimePickDialogUtil(Activity activity, Boolean needHMS) {
        this.activity = activity;
        this.mNeedHMS=needHMS;

    }

    public void init(DatePicker datePicker, TimePicker timePicker) {
        Calendar calendar = Calendar.getInstance();
        if (!(null == initDateTime || "".equals(initDateTime))) {
            calendar = this.getCalendarByInintData(initDateTime);
        } else {
            if (mNeedHMS){
                initDateTime = calendar.get(Calendar.YEAR) + "-"
                        + calendar.get(Calendar.MONTH) + "-"
                        + calendar.get(Calendar.DAY_OF_MONTH) + " "
                        + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                        + calendar.get(Calendar.MINUTE) + ":"
                        + calendar.get(Calendar.SECOND);
            }else {
                initDateTime = calendar.get(Calendar.YEAR) + "-"
                        + calendar.get(Calendar.MONTH) + "-"
                        + calendar.get(Calendar.DAY_OF_MONTH);
            }

        }
        datePicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), this);
        if (mNeedHMS){
            timePicker.setIs24HourView(true);
            timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        }
    }

    /**
     * 弹出日期时间选择框方法
     *
     * @param inputDate :为需要设置的日期时间文本编辑框
     * @return
     */
    public AlertDialog dateTimePicKDialog(final EditText inputDate,final String time) {
        this.initDateTime=time;
//        this.mNeedHMS=needHMS;
        LinearLayout dateTimeLayout = (LinearLayout) activity
                .getLayoutInflater().inflate(R.layout.common_datetime, null);
        datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);
        timePicker = (TimePicker) dateTimeLayout.findViewById(R.id.timepicker);
        if (mNeedHMS){
            timePicker.setVisibility(View.VISIBLE);
        }else{
            timePicker.setVisibility(View.GONE);
        }
        init(datePicker, timePicker);
//        timePicker.setIs24HourView(false); 在init方法前声明才可生效
        timePicker.setOnTimeChangedListener(this);

        while (activity.getParent()!=null){
            activity=activity.getParent();
        }
        ad = new AlertDialog.Builder(activity)
                .setTitle(initDateTime)
                .setView(dateTimeLayout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

//						datePicker.clearFocus();
//						onDateChanged(null, 0, 0, 0);
//                        if(inputDate.getText().toString().equals("")){
//                        	inputDate.setText("");
//						}
                        datePicker.clearFocus();
                        onDateChanged(null, 0, 0, 0);
                        inputDate.setText(dateTime);

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
//						datePicker.clearFocus();
//						onDateChanged(null, 0, 0, 0);
//						inputDate.setText(dateTime);
                        datePicker.clearFocus();
                        onDateChanged(null, 0, 0, 0);
                        if (inputDate.getText().toString().equals("")) {
                            inputDate.setText("");
                        }


                    }
                }).show();

        return ad;
    }

    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        onDateChanged(null, 0, 0, 0);
    }

    public void onDateChanged(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
        // 获得日历实例
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (mNeedHMS){
            calendar.set(datePicker.getYear(), datePicker.getMonth(),
                    datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
                    timePicker.getCurrentMinute());
        }else{
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            calendar.set(datePicker.getYear(), datePicker.getMonth(),
                    datePicker.getDayOfMonth());
        }

        dateTime = sdf.format(calendar.getTime());
        ad.setTitle(dateTime);
    }

    /**
     * 实现将初始日期时间2012-07-02 拆分成2012 07 02,并赋值给calendar
     * 2016-09-14 16:41:08
     *
     * @param initDateTime 初始日期时间值 字符串型
     * @return Calendar
     */
    private Calendar getCalendarByInintData(String initDateTime) {
        Calendar calendar = Calendar.getInstance();
        int currentYear = Integer.parseInt(initDateTime.substring(0, 4));
        int currentMonth = Integer.parseInt(initDateTime.substring(5, 7)) - 1;
        int currentDay = Integer.parseInt(initDateTime.substring(8, 10));
        if (mNeedHMS){
            int currentHour = Integer.parseInt(initDateTime.substring(11, 13));
            int currentMinute = Integer.parseInt(initDateTime.substring(14, 16));
            int currentSecond = Integer.parseInt(initDateTime.substring(17, initDateTime.length()));
            calendar.set(currentYear, currentMonth, currentDay, currentHour, currentMinute, currentSecond);
        }else{
            calendar.set(currentYear, currentMonth, currentDay);
        }
        return calendar;
    }

}
