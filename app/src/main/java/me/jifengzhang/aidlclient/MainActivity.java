package me.jifengzhang.aidlclient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import me.jifengzhang.aidlserver.IRemoteService;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent("me.jifengzhang.aidl.Remote");
        intent.setPackage("me.jifengzhang.aidlserver"); //Fixed:Service Intent must be explicit

        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mService!=null) {
                    try {
                        String msg = mService.getRemoteValue(1);
                        Log.i("AIDLDemo","Client msg = "+ msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }

    private IRemoteService mService;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("AIDLDemo","onServiceConnected");
            mService = IRemoteService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("AIDLDemo","onServiceDisconnected");
            mService = null;
        }
    };


}
