package net.choas.android.ioiotree;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import ioio.lib.util.android.IOIOService;

public class IOIOTreeService  extends IOIOService {
    public IOIOTreeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    public class LocalBinder extends Binder {
        IOIOTreeService getService() {
            return IOIOTreeService.this;
        }
    }
}
