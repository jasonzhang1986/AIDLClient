package me.jifengzhang.aidlserver;

import android.os.RemoteException;

/**
 * Author: Jifeng Zhang
 * Email : jifengzhang.barlow@gmail.com
 * Date  : 2017/5/15
 * Desc  : IRect.aidl的实现
 */

public class IRectImpl extends IRect.Stub {
    @Override
    public int area(int length, int width) throws RemoteException {
        return length * width;
    }
}
