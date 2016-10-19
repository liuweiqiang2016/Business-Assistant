package myapp.alex.com.businessassistant.utils;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by liuweiqiang on 2016/9/21.
 */
public class FuncUtils {

    public static final String APP_DIR= Environment.getExternalStorageDirectory()+"/BusinessAssistant/";
    public static final String APP_UPDATE_URL= "https://raw.githubusercontent.com/liuweiqiang2016/Business-Assistant/master/app/versioninfo.xml";

    public static final String APP_DOWNFILE_NAME ="BusinessAssistant.apk";

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

    /**
     * 解析SDcard xml文件
     * @param fileName
     * @return 返回xml文件的inputStream
     */
    public static InputStream getInputStreamFromSDcard(String fileName){
        try {
            // 路径根据实际项目修改
            String path = FuncUtils.APP_DIR + "/";

            Log.v("", "path : " + path);

            File xmlFlie = new File(path+fileName);

            InputStream inputStream = new FileInputStream(xmlFlie);

            return inputStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 比较软件当前版本与服务器最新版本
     *
     * @param localVersion
     *            软件当前版本为四位 如：1.0.0.0
     * @param serviceVersion
     *            服务器最新版本 为四位 如：1.0.11.2
     * @return
     */
    public static boolean compareVersion(String localVersion, String serviceVersion)
    {
        try
        {
            localVersion = localVersion.replace(".", ",");
            serviceVersion = serviceVersion.replace(".", ",");

            String[] local = localVersion.trim().split(",");
            String[] service = serviceVersion.trim().split(",");
            if (local.length != service.length)
                return false;
            for (int i = 0; i < local.length; i++)
            {
                int lv = Integer.valueOf(local[i]);
                int sv = Integer.valueOf(service[i]);
                if (lv == sv)
                    continue;
                if (lv < sv)
                    return true;
                else
                    return false;

            }
            return false;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 弹出toast消息
     * @param context  context
     * @return string 展示的内容
     */
    public static void showToast(Context context,String string){

        Toast.makeText(context,string,Toast.LENGTH_SHORT).show();

    }


}
