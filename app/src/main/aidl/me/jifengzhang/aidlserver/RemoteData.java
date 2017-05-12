package me.jifengzhang.aidlserver;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: Jifeng Zhang
 * Email : jifengzhang.barlow@gmail.com
 * Date  : 2017/5/12
 * Desc  : 自定义的类
 */

public class RemoteData implements Parcelable{
    public int id;
    public String name;
    public RemoteData(int id, String name) {
        this.id = id;
        this.name = name;
    }
    protected RemoteData(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<RemoteData> CREATOR = new Creator<RemoteData>() {
        @Override
        public RemoteData createFromParcel(Parcel in) {
            return new RemoteData(in);
        }

        @Override
        public RemoteData[] newArray(int size) {
            return new RemoteData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

    @Override
    public String toString() {
        return "["+id+":"+name+"]";
    }
}
