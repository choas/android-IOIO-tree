package net.choas.android.ioiotree;

import android.util.Log;

import ioio.lib.api.IOIO;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.IOIOLooper;

/**
 * Created by gregoril on 09.09.2014.
 */
public class IOIOTreeLooper implements IOIOLooper {

    private static final String TAG = "IOIOTreeLooper";


    PwmOutput led;
    private int v = 0;

    @Override
    public void setup(IOIO ioio) throws ConnectionLostException, InterruptedException {
        Log.d(TAG, "setup");

        led = ioio.openPwmOutput(10, 100);

    }

    @Override
    public void loop() throws ConnectionLostException, InterruptedException {
        Log.d(TAG, "loop");

        led.setPulseWidth(v++);

        if (v > 255) {
            v = 0;
        }
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
