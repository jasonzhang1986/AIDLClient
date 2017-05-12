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
import me.jifengzhang.aidlserver.RemoteData;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindRemote();

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mService!=null) {
                    try {
                        RemoteData data = new RemoteData(1, "Android");
                        mService.addData(data);
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

    private void bindRemote() {
        Intent intent = new Intent("me.jifengzhang.aidl.Remote");
        intent.setPackage("me.jifengzhang.aidlserver"); //Fixed:Service Intent must be explicit

        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private IRemoteService mService;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("AIDLDemo","onServiceConnected");
            mService = IRemoteService.Stub.asInterface(service);
            try {
                service.linkToDeath(deathRecipient,0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("AIDLDemo","onServiceDisconnected");
            mService = null;
        }
    };

    private final IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.i("AIDLDemo","binderDied");
            if (mService==null) {
                return;
            }
            mService.asBinder().unlinkToDeath(deathRecipient, 0);
            mService = null;
            //重新绑定服务
            bindRemote();
        }
    };
}
