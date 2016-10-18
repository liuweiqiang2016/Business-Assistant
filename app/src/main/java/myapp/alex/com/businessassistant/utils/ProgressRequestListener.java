package myapp.alex.com.businessassistant.utils;

/**
 * 请求体响应回调，用到上传
 * Created by yyw on 2016/1/20.
 */
public interface ProgressRequestListener {
    /**
     * 进度监听
     * @param allBytes 上传的全部文件长度
     * @param currentBytes 已上传的文件长度
     * @param done 是否完成
     */
    void onProgressRequest(long allBytes, long currentBytes, boolean done);
}
