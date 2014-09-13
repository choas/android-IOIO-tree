package net.choas.android.ioiotree;

/**
 * Created by gregoril on 13.09.2014.
 */
public class Recording {

    private int buttonNumber;
    private boolean state;
    private long time;

    public Recording(int buttonNumber, boolean state) {
        this.buttonNumber = buttonNumber;
        this.state = state;
        this.time = System.currentTimeMillis();
    }

    public int getButtonNumber() {
        return buttonNumber;
    }

    public boolean isState() {
        return state;
    }

    public long getTime() {
        return time;
    }
}
