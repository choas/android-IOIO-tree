package net.choas.android.ioiotree;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;

import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOService;

public class IOIOTreeService extends IOIOService {

    private static final String TAG = "IOIOTreeService";
    private static final String KEY_PREF_LIGHT_INTENSITY = "pref_lightIntensity";
    private IOIOTreeLooper looper = new IOIOTreeLooper();
    private final IBinder mBinder = new LocalBinder();

    public IOIOTreeService() {
    }

    @Override
    protected IOIOLooper createIOIOLooper() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        prefs.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Log.d(TAG, "shared preferences changed " + key);
                if (KEY_PREF_LIGHT_INTENSITY.equals(key)) {
                    looper.setLightIntensity(sharedPreferences.getBoolean(KEY_PREF_LIGHT_INTENSITY, false));
                }
            }
        });

        this.looper.setLightIntensity(prefs.getBoolean(KEY_PREF_LIGHT_INTENSITY, false));

        return this.looper;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return mBinder;
    }

    public void setRecording(List<Recording> recordings) {

        looper.setRecording(recordings);
    }


    public class LocalBinder extends Binder {
        IOIOTreeService getService() {
            return IOIOTreeService.this;
        }
    }
}
