package net.choas.android.ioiotree;

import android.util.Log;

import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.IOIOLooper;

/**
 * Created by gregoril on 09.09.2014.
 */
public class IOIOTreeLooper implements IOIOLooper {

    private static final String TAG = "IOIOTreeLooper";

    @Override
    public void setup(IOIO ioio) throws ConnectionLostException, InterruptedException {
        Log.d(TAG, "setup");
    }

    @Override
    public void loop() throws ConnectionLostException, InterruptedException {
        Log.d(TAG, "loop");
    }

    @Override
    public void disconnected() {
        Log.d(TAG, "disconnected");
    }

    @Override
    public void incompatible() {
        Log.d(TAG, "incompatible");
    }
}
