package net.choas.android.ioiotree;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;


public class TreeActivity extends Activity {

    private static final String TAG = "IOIOActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_tree);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tree, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            Intent intent = new Intent(this, SettingsActivity.class);
//            EditText editText = (EditText) findViewById(R.id.edit_message);
//            String message = editText.getText().toString();
//            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);

//
////            if (savedInstanceState == null) {
//                getFragmentManager().beginTransaction()
//                        .replace(R.id.container, new SettingsFragment())
//                        .commit();
//
//
////            }


            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final String TAG = "IOIOFragment";
        private Switch record;
        private ToggleButton[] toggleButtons = new ToggleButton[7];

        private List<Recording> recording = new ArrayList<Recording>();

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tree, container, false);

            record = (Switch) rootView.findViewById(R.id.record);

            toggleButtons[0] = (ToggleButton) rootView.findViewById(R.id.toggleButton1);
            toggleButtons[1] = (ToggleButton) rootView.findViewById(R.id.toggleButton2);
            toggleButtons[2] = (ToggleButton) rootView.findViewById(R.id.toggleButton3);
            toggleButtons[3] = (ToggleButton) rootView.findViewById(R.id.toggleButton4);
            toggleButtons[4] = (ToggleButton) rootView.findViewById(R.id.toggleButton5);
            toggleButtons[5] = (ToggleButton) rootView.findViewById(R.id.toggleButton6);
            toggleButtons[6] = (ToggleButton) rootView.findViewById(R.id.toggleButton7);

            record.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
                    }

                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());


//                    ;

                    mBoundService.setRecording(recording, sharedPref.getString("pref_lightIntensity", "?"));
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

            getActivity().startService(new Intent(getActivity(), IOIOTreeService.class));

            return rootView;
        }

        private IOIOTreeService mBoundService;
        private boolean mIsBound = false;
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


        @Override
        public void onResume() {
            super.onResume();
            this.doBindService();
        }

        @Override
        public void onPause() {
            super.onPause();
            this.doUnbindService();
        }

        private void doBindService() {
            // Establish a connection with the service.  We use an explicit
            // class name because we want a specific service implementation that
            // we know will be running in our own process (and thus won't be
            // supporting component replacement by other applications).
            getActivity().getApplicationContext().bindService(new Intent(getActivity(),
                    IOIOTreeService.class), mConnection, Context.BIND_AUTO_CREATE);
            mIsBound = true;
        }

        private void doUnbindService() {
            if (mIsBound) {
                // Detach our existing connection.
                getActivity().getApplicationContext().unbindService(mConnection);
                mIsBound = false;
            }
        }
    }
}
