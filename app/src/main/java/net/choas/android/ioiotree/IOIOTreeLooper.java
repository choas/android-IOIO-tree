package net.choas.android.ioiotree;

import android.util.Log;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.IOIOLooper;

/**
 * Created by gregoril on 09.09.2014.
 */
public class IOIOTreeLooper implements IOIOLooper {

    private static final String TAG = "IOIOTreeLooper";


    DigitalOutput leds[] = new DigitalOutput[7];
    private boolean v = false;

    @Override
    public void setup(IOIO ioio) throws ConnectionLostException, InterruptedException {
        Log.d(TAG, "setup");

        leds[0] = ioio.openDigitalOutput(10);
        leds[1] = ioio.openDigitalOutput(11);
        leds[2] = ioio.openDigitalOutput(12);
        leds[3] = ioio.openDigitalOutput(13);
        leds[4] = ioio.openDigitalOutput(5);
        leds[5] = ioio.openDigitalOutput(4);
        leds[6] = ioio.openDigitalOutput(3);
    }

    @Override
    public void loop() throws ConnectionLostException, InterruptedException {
//        Log.d(TAG, "loop");

        for (DigitalOutput led : leds) {
            led.write(v);
        }
        v = !v;
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
