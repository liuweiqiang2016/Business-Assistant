package myapp.alex.com.businessassistant.okhttp;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * 请求体
 * Created by yyw on 2016/1/20.
 */
public class ProgressRequestBody extends RequestBody{
    private RequestBody mRequestBody;
    private ProgressRequestListener mListener;
    private BufferedSink mSink;

    public ProgressRequestBody(ProgressRequestListener mListener, RequestBody mRequestBody) {
        this.mListener = mListener;
        this.mRequestBody = mRequestBody;
    }

    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (mSink == null){
            mSink = Okio.buffer(sink(sink));
        }
        mRequestBody.writeTo(mSink);
        mSink.flush();
    }

    /**
     * 写入，回调进度接口
     * @param sink
     * @return
     */
    private Sink sink(Sink sink){
        return new ForwardingSink(sink) {
            private long writeLength = 0l;
            private long contentLength = 0l;
            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength ==0){
                    contentLength = contentLength();
                }
                writeLength +=byteCount;
                mListener.onProgressRequest(contentLength,writeLength,contentLength == writeLength);
            }
        };
    }

    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }
}
