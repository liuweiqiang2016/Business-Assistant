package myapp.alex.com.businessassistant.utils;


import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 包装的响体，处理进度,用到下载
 * Created by yyw on 2016/1/20.
 */
public class ProgressResponseBody extends ResponseBody {
    /**待包装的响应体**/
    private ResponseBody mResponseBody;
    /**进度监听**/
    private ProgressResponseListener mListener;
    /**包装完成的BufferedSource**/
    private BufferedSource mSource ;

    public ProgressResponseBody(ProgressResponseListener mListener, ResponseBody mResponseBody) {
        this.mListener = mListener;
        this.mResponseBody = mResponseBody;
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    /**
     * 重写进行包装source
     * @return BufferedSource
     */
    @Override
    public BufferedSource source() {
        if (mSource == null){
            //包装
            mSource = Okio.buffer(source(mResponseBody.source()));
        }
        return mSource;
    }
    private Source source(Source source){
        return  new ForwardingSource(source) {
            //当前已经读取字节数
            private long totalBytesRead = 0L;
            private long contentLength = 0l;
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                //当前读取的字节
                long byteRead = super.read(sink, byteCount);
                if (contentLength == 0){
                    contentLength = contentLength();
                }
                if (byteRead != -1){
                    totalBytesRead += byteRead;
                    mListener.onProgressResponse(contentLength,totalBytesRead,false);
                }else {
                    mListener.onProgressResponse(contentLength,totalBytesRead,true);
                }
                return byteRead;
            }
        };
    }
}
