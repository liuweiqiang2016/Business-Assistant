package myapp.alex.com.businessassistant.okhttp;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by yyw on 2016/1/20.
 */
public class OkHttpHelper {
    /**
     * 包装OkHttpClient，用于下载文件的回调
     *
     * @param client   待包装的OkHttpClient
     * @param listener 进度回调接口
     * @return 包装后的OkHttpClient，使用clone方法返回
     */
    public static OkHttpClient getOkClient(OkHttpClient client, final ProgressResponseListener listener) {
        //克隆
        OkHttpClient.Builder builder= client.newBuilder();
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());

                return response.newBuilder().body(new ProgressResponseBody(listener, response.body())).build();
            }
        };

        builder.networkInterceptors().add(interceptor);
        return builder.build();
    }

    /**
     * 包装请求体用于上传文件的回调
     *
     * @param body     请求体RequestBody
     * @param listener 进度回调接口
     * @return 包装后的进度回调请求体
     */
    public static RequestBody getRequestBody(ProgressRequestListener listener, RequestBody body) {
        return new ProgressRequestBody(listener, body);
    }

    /**
     * 获取缓存的请求客户端
     * @param client 带包装的请求客户端
     * @param cacheFile 缓存的文件
     * @param cacheSize 缓存的大小
     * @return
     */
    public static OkHttpClient getCacheClient(OkHttpClient client,File cacheFile,int cacheSize){
        OkHttpClient.Builder builder = client.newBuilder();
        Cache cache = new Cache(cacheFile,cacheSize);
        return builder.cache(cache).build();
    }
/***
 * Public指示响应可被任何缓存区缓存。
 * Private指示对于单个用户的整个或部分响应消息，不能被共享缓存处理。这允许服务器仅仅描述当用户的部分响应消息，此响应消息对于其他用户的请求无效。
 * no-cache指示请求或响应消息不能缓存，该选项并不是说可以设置”不缓存“，容易望文生义~
 * no-store用于防止重要的信息被无意的发布。在请求消息中发送将使得请求和响应消息都不使用缓存，完全不存下來。
 * max-age指示客户机可以接收生存期不大于指定时间（以秒为单位）的响应。
 * min-fresh指示客户机可以接收响应时间小于当前时间加上指定时间的响应。
 * max-stale指示客户机可以接收超出超时期间的响应消息。如果指定max-stale消息的值，那么客户机可以接收超出超时期指定值之内的响应消息。
 * only-if-cached，表示只接受是被缓存的内容
 */
    /**
     * 这种缓存方式
     * max-stale
     *
     * 在缓存时间内不会去联网请求请求,但是Response返回有数据，上次缓存的数据。
     * @param time 缓存时间
     * @return
     */
    public static Request getCacheRequest_ONE(int time,String getUrl){
        return new Request.Builder()
                .cacheControl(new CacheControl.Builder()
                        .maxStale(time, TimeUnit.SECONDS)
                        .build())
                .url(getUrl)
                .build();
    }
    /**
     * 这种缓存方式
     * only-if-cached，表示只接受是被缓存的内容
     * 在缓存时间内不会去联网请求请求,但是Response返回有数据，上次缓存的数据。
     * @return
     */
    public static Request getCacheRequest_TWO(String getUrl){
        return new Request.Builder()
                .cacheControl(new CacheControl.Builder()
                        .onlyIfCached()
                        .build())
                .url(getUrl)
                .build();
    }

    /**
     * no-store用于防止重要的信息被无意的发布。在请求消息中发送将使得请求和响应消息都不使用缓存，完全不存下來。
     * @param getUrl
     * @return
     */
    public static Request getCacheRequest_NOT_STORE(String getUrl){
        return new Request.Builder()
                .cacheControl(new CacheControl.Builder()
                        .noStore()
                        .build())
                .url(getUrl)
                .addHeader("apikey","1f536bb6767d4012d1169360c88323b0")
                .build();
    }
}
