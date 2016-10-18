package myapp.alex.com.businessassistant.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by yyw on 2016/1/20.
 */
public abstract class UIProgressResponseListener implements ProgressResponseListener{
    private static final int PROGRESS = 0x2001;
    private UIHandler mHandler = new UIHandler(Looper.getMainLooper(),this);

    public UIProgressResponseListener(){

    }
    private static class UIHandler extends Handler{
        private WeakReference<UIProgressResponseListener> mListener = null;

        public UIHandler(Looper looper, UIProgressResponseListener listener) {
            super(looper);
            this.mListener = new WeakReference<UIProgressResponseListener>(listener);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == PROGRESS){
                ProgressModel model = (ProgressModel)msg.obj;
                if (mListener != null) {
                    mListener.get().onUIProgressRequest(model.getContentLength(),model.getCurrentBytes(),model.isDone());
                }
            }else {
                super.handleMessage(msg);
            }

        }
    }

    @Override
    public void onProgressResponse(long allBytes, long currentBytes, boolean done) {
        Message msg = Message.obtain();
        msg.obj = new ProgressModel(currentBytes,allBytes,done);
        msg.what = PROGRESS;
        mHandler.sendMessage(msg);
    }

    public abstract void onUIProgressRequest(long allBytes, long currentBytes, boolean done);
}
