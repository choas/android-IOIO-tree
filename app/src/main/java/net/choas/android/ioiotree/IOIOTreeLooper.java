package net.choas.android.ioiotree;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ioio.lib.api.AnalogInput;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.IOIOLooper;

/**
 * Created by gregoril on 09.09.2014.
 */
public class IOIOTreeLooper implements IOIOLooper {

    private static final String TAG = "IOIOTreeLooper";

    DigitalOutput leds[] = new DigitalOutput[7];
    private boolean v = false;
    private List<Recording> recordings;

    private int pos = 0;
    private long startTime;
    private List<Recording> nextRecordings;
    private AnalogInput photoresistor;
    private long ct = System.currentTimeMillis();
    private Boolean lightIntensity = false;

    @Override
    public void setup(IOIO ioio) throws ConnectionLostException, InterruptedException {
        Log.d(TAG, "setup");

        leds[0] = ioio.openDigitalOutput(10);
        leds[1] = ioio.openDigitalOutput(11);
        leds[2] = ioio.openDigitalOutput(12);
        leds[3] = ioio.openDigitalOutput(13);
        leds[4] = ioio.openDigitalOutput(3);
        leds[5] = ioio.openDigitalOutput(4);
        leds[6] = ioio.openDigitalOutput(5);

        photoresistor = ioio.openAnalogInput(44);
    }

    @Override
    public void loop() throws ConnectionLostException, InterruptedException {

        if (this.lightIntensity) {
            lightIntensity();
            return;
        }

        if (this.nextRecordings != null) {
            this.recordings = new ArrayList<Recording>();
            for (Recording r : this.nextRecordings) {
                this.recordings.add(r);
            }
            this.pos = 0;
            this.nextRecordings = null;
        }
        if (this.recordings == null || this.recordings.isEmpty()) {
            for (DigitalOutput led : leds) {
                led.write(false);
            }
            return;
        }

        if (this.pos >= recordings.size()) {
            this.pos = 0;
        }

        if (this.pos == 0) {
            this.startTime = System.currentTimeMillis();
        }

        Recording recording = recordings.get(pos);

        long time = System.currentTimeMillis() - this.startTime;
        long timeNext = recording.getTime() - recordings.get(0).getTime();

        if (time < timeNext) {
            return;
        }

        leds[recording.getButtonNumber()].write(recording.isState());

        pos++;
    }

    private void lightIntensity() throws InterruptedException, ConnectionLostException {
        float voltage = photoresistor.getVoltage();
        int maxIntensity = (int)((voltage / 2.5) * leds.length) + 1;

        if (System.currentTimeMillis() > ct + 2000) {
            Log.d(TAG, "photoresistor voltage: " + voltage + " num:" + maxIntensity);
            ct = System.currentTimeMillis();
        }

        int intensity = 1;
        for (DigitalOutput led : leds) {
            if (intensity <= maxIntensity) {
                led.write(true);
            } else {
                led.write(false);
            }
            intensity++;
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

    public void setRecording(List<Recording> recordings) {

        Log.d(TAG, "recording " + recordings.size());

        this.pos = 0;
        this.nextRecordings = recordings;
    }

    public void setLightIntensity(Boolean lightIntensity) {
        this.lightIntensity = lightIntensity;
    }
}
