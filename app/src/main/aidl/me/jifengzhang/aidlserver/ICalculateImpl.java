package me.jifengzhang.aidlserver;

import android.os.RemoteException;
import android.util.Log;

/**
 * Author: Jifeng Zhang
 * Email : jifengzhang.barlow@gmail.com
 * Date  : 2017/5/15
 * Desc  :ICalculate.aidl的实现类
 */

public class ICalculateImpl extends ICalculate.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        Log.i("AIDLDemo", "ICalculateImpl [" + a + " + " + b + "]");
        return a + b;
    }

    @Override
    public int sub(int a, int b) throws RemoteException {
        Log.i("AIDLDemo", "ICalculateImpl [" + a + " - " + b + "]");
        return a - b;
    }
}
