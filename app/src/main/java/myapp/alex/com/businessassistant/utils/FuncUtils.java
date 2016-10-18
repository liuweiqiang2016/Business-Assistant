package myapp.alex.com.businessassistant.utils;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by liuweiqiang on 2016/9/21.
 */
public class FuncUtils {
//    //查询跳转的tag
//    public static final String TAG_QUERY="queryTag";
//    public static final int TAG_ORDER=0;//订单查询
//    public static final String ID_COST="cost";//开销查询
//    public static final int TAG_CUSTOMER=2;//客户查询
//    public static final int TAG_NOTE=3;//笔记查询

    // 获取当前时间
    public static String getTime() {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式

        return df.format(new Date());// new Date()为获取当前系统时间

    }
    // 获取当前时间
    public static String getDate() {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式

        return df.format(new Date());// new Date()为获取当前系统时间

    }

    /**
     * 比较开始时间和结束时间
     *
     * @param startDate 开始时间 "yyyy-MM-dd HH:mm:ss"
     * @param endDate   结束时间 "yyyy-MM-dd HH:mm:ss"
     * @return true 表示开始时间大于结束时间
     */
    public static boolean compareDateHMS(String startDate, String endDate) {
        Date startTime = null, endTime = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        if (!startDate.equals("") && !endDate.equals("")) {
            try {
                startTime = dateFormat.parse(startDate);
                endTime = dateFormat.parse(endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (startTime.getTime() > endTime.getTime()) {
                // CommomFun.toastMsg(R.string.time_error);
                return true;
            }
        }
        return false;
    }

    /**
     * 比较开始时间和结束时间
     *
     * @param startDate 开始时间 "yyyy-MM-dd "
     * @param endDate   结束时间 "yyyy-MM-dd"
     * @return true 表示开始时间大于结束时间
     */
    public static boolean compareDate(String startDate, String endDate) {
        Date startTime = null, endTime = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");
        if (!startDate.equals("") && !endDate.equals("")) {
            try {
                startTime = dateFormat.parse(startDate);
                endTime = dateFormat.parse(endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (startTime.getTime() > endTime.getTime()) {
                // CommomFun.toastMsg(R.string.time_error);
                return true;
            }
        }
        return false;
    }

    /**
     *字符串的日期格式的计算
     */
    public static int daysBetween(String smdate,String bdate) throws ParseException{
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     2  * 获取版本号
     3  * @return 当前应用的版本号
     4  */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0";
        }
    }


}
