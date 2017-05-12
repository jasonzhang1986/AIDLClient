// IRemoteService.aidl
package me.jifengzhang.aidlserver;

import me.jifengzhang.aidlserver.RemoteData;

interface IRemoteService {
    String getRemoteValue(int input);
    void addData(in RemoteData data);
}
