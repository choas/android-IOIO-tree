package net.choas.android.ioiotree;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOService;

public class IOIOTreeService extends IOIOService {

    private static final String TAG = "IOIOTreeService";
    private IOIOTreeLooper looper = new IOIOTreeLooper();
    private final IBinder mBinder = new LocalBinder();

    public IOIOTreeService() {
    }

    @Override
    protected IOIOLooper createIOIOLooper() {
        return this.looper;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return mBinder;
    }

    public void setRun(boolean b) {
        looper.setRun(b);
    }


    public class LocalBinder extends Binder {
        IOIOTreeService getService() {
            return IOIOTreeService.this;
        }
    }
}
