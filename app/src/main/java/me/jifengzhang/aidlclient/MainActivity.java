package me.jifengzhang.aidlclient;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import me.jifengzhang.aidlserver.BinderPool;
import me.jifengzhang.aidlserver.ICalculate;
import me.jifengzhang.aidlserver.ICalculateImpl;


public class MainActivity extends Activity {

    private BinderPool mBinderPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getBinderPool();

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBinderPool!=null) {
                    try {
                        ICalculate binder = ICalculateImpl.asInterface(mBinderPool.queryBinder(BinderPool.BINDER_CALCULATE));
                        int result = binder.add(1,2);
                        Log.i("AIDLDemo", "calculate add = " + result);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void getBinderPool() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mBinderPool = BinderPool.getInstance(MainActivity.this);
            }
        }).start();
    }


}
