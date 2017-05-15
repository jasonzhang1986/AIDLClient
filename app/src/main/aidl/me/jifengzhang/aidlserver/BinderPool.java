package me.jifengzhang.aidlserver;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.concurrent.CountDownLatch;

/**
 * Author: Jifeng Zhang
 * Email : jifengzhang.barlow@gmail.com
 * Date  : 2017/5/15
 * Desc  : Binder池
 */

public class BinderPool {
    public static final int  BINDER_CALCULATE = 0;
    public static final int  BINDER_RECT = 1;

    private volatile static BinderPool sInstance;
    private IBinderPool mBinderPool;
    private CountDownLatch mCountDownLatch;
    private Context mContext;

    private BinderPool(Context context) {
        mContext = context.getApplicationContext();
        connectBinderPoolService();
    }

    public static BinderPool getInstance(Context context) {
        if (sInstance==null){
            synchronized (BinderPool.class) {
                if (sInstance==null){
                    sInstance = new BinderPool(context);
                }
            }
        }
        return sInstance;
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("AIDLDemo", "BinderPool Service Connected");
            mBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                mBinderPool.asBinder().linkToDeath(deathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mCountDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBinderPool = null;
        }
    };

    private void connectBinderPoolService() {
        Log.i("AIDLDemo", "BinderPool connectBinderPoolService");
        mCountDownLatch = new CountDownLatch(1);
        Intent service = new Intent("me.jifengzhang.binder.BINDER_POOL_SERVICE");
        service.setPackage("me.jifengzhang.aidlserver");
        mContext.bindService(service, mServiceConnection, Context.BIND_AUTO_CREATE);
        try {
            mCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public IBinder queryBinder(int binderCode) {
        Log.i("AIDLDemo", "BinderPool queryBinder " + binderCode);
        if (mBinderPool != null) {
            try {
                return mBinderPool.queryBinder(binderCode);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mBinderPool != null) {
                mBinderPool.asBinder().unlinkToDeath(deathRecipient, 0);
                mBinderPool = null;
            }
            connectBinderPoolService();
        }
    };

    public static class IBinderPoolImpl extends IBinderPool.Stub {

        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder binder = null;
            switch (binderCode) {
                case BINDER_CALCULATE:
                    binder = new ICalculateImpl();
                    break;
                case BINDER_RECT:
                    binder = new IRectImpl();
                    break;
            }
            return binder;
        }
    }
}
