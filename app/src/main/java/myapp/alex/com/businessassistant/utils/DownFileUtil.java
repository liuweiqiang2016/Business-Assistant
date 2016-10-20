package myapp.alex.com.businessassistant.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import myapp.alex.com.businessassistant.okhttp.OkHttpHelper;
import myapp.alex.com.businessassistant.okhttp.UIProgressResponseListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liuweiqiang on 2016/10/19.
 */

public class DownFileUtil {

    private Handler mHandler;

    public DownFileUtil(Handler mHandler) {
        this.mHandler = mHandler;
    }

    /**
     * 获取文件。
     *
     * @param url   地址
     * @param path  下载的文件地址
     * @param check 是否为版本检测(下载xml文件)
     */
    public void getFile(final String url, final String path, final boolean check) {
        final String fileName;
        if (check) {
            //若下载xml文件
            fileName = url.substring(url.lastIndexOf("/") + 1);
        } else {
            //若下载apk文件
            fileName = FuncUtils.APP_DOWNFILE_NAME;
        }
        OkHttpClient client = new OkHttpClient();
        client = OkHttpHelper.getOkClient(client, new UIProgressResponseListener() {
            @Override
            public void onUIProgressRequest(long allBytes, long currentBytes, boolean done) {
                float progress = currentBytes * 100f / allBytes;
                Log.i("MAIN", "onUIProgressRequest: 总长度：" + allBytes + " 当前下载的长度：" + currentBytes + "是否下载完成：" + done + "下载进度：" + progress);

                if (!check) {
////
                    Message msg = mHandler.obtainMessage();
                    msg.what = 4;
                    msg.arg1 = (int) progress;
                    mHandler.sendMessage(msg);

                }
                if (done) {
                    if (check) {
                        //下载版本更新信息xml文件成功
                        mHandler.sendEmptyMessage(1);
                    } else {
                        //apk下载完成
                        mHandler.sendEmptyMessage(5);
                    }

                }

            }
        });
        Request request = new Request.Builder().url(url).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (check) {
                    //下载版本更新信息xml文件失败
                    mHandler.sendEmptyMessage(2);
                }

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    InputStream is = response.body().byteStream();
                    File file = new File(path);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File downLoad = new File(file, fileName);
                    FileOutputStream fos = new FileOutputStream(downLoad);
                    int len = -1;
                    byte[] buffer = new byte[1024];
                    while ((len = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    fos.flush();
                    fos.close();
                    is.close();
                }
            }
        });
    }
}





