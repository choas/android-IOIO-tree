package net.choas.android.ioiotree;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private List<IOIOTreeActivity.Recording> recordings;

    private int pos = 0;
    private long startTime;
    private List<IOIOTreeActivity.Recording> nextRecordings;

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
    }

    @Override
    public void loop() throws ConnectionLostException, InterruptedException {

        if (this.nextRecordings != null) {
            this.recordings  = new ArrayList<IOIOTreeActivity.Recording>();
            for(IOIOTreeActivity.Recording r : this.nextRecordings) {
                this.recordings.add(r);
            }
            this.pos = 0;
            this.nextRecordings = null;
        }
        if (this.recordings == null || this.recordings.isEmpty()) {
            for(DigitalOutput led : leds) {
                led.write(false);
            }
            return;
        }

        if (pos >= recordings.size()) {
            pos = 0;

        }
        if (pos == 0) {
            this.startTime = new Date().getTime();
        }

        IOIOTreeActivity.Recording recording = recordings.get(pos);

        long time = new Date().getTime() - this.startTime;
        long timeNext = recording.getTime() - recordings.get(0).getTime();

        if (time < timeNext)
        {
            return;
        }

        leds[recording.getButtonNumber()].write(recording.isState());

        pos++;
    }

    @Override
    public void disconnected() {
        Log.d(TAG, "disconnected");
    }

    @Override
    public void incompatible() {
        Log.d(TAG, "incompatible");
    }

    public void setRecording(List<IOIOTreeActivity.Recording> recordings) {

        Log.d(TAG, "recording " + recordings.size());

        this.pos = 0;
        this.nextRecordings = recordings;
    }
}
