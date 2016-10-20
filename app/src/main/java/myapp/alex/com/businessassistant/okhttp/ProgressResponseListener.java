package myapp.alex.com.businessassistant.okhttp;

/**
 * 响应体进度回调接口，比如用于文件下载中
 * Created by yyw on 2016/1/20.
 */
public interface ProgressResponseListener {
    /**
     * 进度监听
     * @param allBytes 上传的全部文件长度
     * @param currentBytes 已上传的文件长度
     * @param done 是否完成
     */
    void onProgressResponse(long allBytes, long currentBytes, boolean done);
}
