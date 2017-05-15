// IBinderPool.aidl
package me.jifengzhang.aidlserver;


interface IBinderPool {
   IBinder queryBinder(int binderCode);
}
