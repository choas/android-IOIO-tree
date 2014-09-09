package net.choas.android.ioiotree;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class IOIOTreeActivity extends Activity {

    private static final String TAG = "IOIOTree";

    private IOIOTreeService mBoundService;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            mBoundService = ((IOIOTreeService.LocalBinder) service).getService();

            Log.i(TAG, "local service connected");
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            mBoundService = null;
            Log.i(TAG, "local service disconnected");
        }
    };
    private Switch record;
    private ToggleButton toggleButtons[] = new ToggleButton[7];
    private boolean mIsBound = false;


    private List<Recording> recording = new ArrayList<Recording>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_my);

        record = (Switch) findViewById(R.id.record);

        toggleButtons[0] = (ToggleButton) findViewById(R.id.toggleButton1);
        toggleButtons[1] = (ToggleButton) findViewById(R.id.toggleButton2);
        toggleButtons[2] = (ToggleButton) findViewById(R.id.toggleButton3);
        toggleButtons[3] = (ToggleButton) findViewById(R.id.toggleButton4);
        toggleButtons[4] = (ToggleButton) findViewById(R.id.toggleButton5);
        toggleButtons[5] = (ToggleButton) findViewById(R.id.toggleButton6);
        toggleButtons[6] = (ToggleButton) findViewById(R.id.toggleButton7);

        record.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Log.d(TAG, "isChecked=" + isChecked);
                for (ToggleButton toggleButton : toggleButtons) {
                    toggleButton.setEnabled(isChecked);
                    toggleButton.setChecked(false);
                }

                if (mBoundService == null) {
                    Log.i(TAG, "service not connected");
                    return;
                }

                if(isChecked) {
                    Log.d(TAG, "clean recording");
                    recording.clear();
//                   recording = null; //new ArrayList<Recording>();
                }

                mBoundService.setRecording(recording);
            }
        });


        int buttonNumber = 0;
for (ToggleButton toggleButton : toggleButtons) {
    final int finalButtonNumber = buttonNumber;
    toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        int buttonNumber = finalButtonNumber;
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.d(TAG, "button number " + buttonNumber + " is " + isChecked);
            recording.add(new Recording(this.buttonNumber, isChecked));
        }
    });

    buttonNumber++;
}

        startService(new Intent(this, IOIOTreeService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.doBindService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.doUnbindService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void doBindService() {
        // Establish a connection with the service.  We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).
        bindService(new Intent(IOIOTreeActivity.this,
                IOIOTreeService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    private void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    public class Recording {

        private int buttonNumber;
        private boolean state;
        private long time;

        private Recording(int buttonNumber, boolean state) {
            this.buttonNumber = buttonNumber;
            this.state = state;
            this.time = new Date().getTime();
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
}
