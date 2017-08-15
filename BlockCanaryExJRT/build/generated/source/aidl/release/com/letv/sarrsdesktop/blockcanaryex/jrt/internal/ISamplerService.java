/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Studio\\DEMO\\MyProject\\myChart\\MyStock\\BlockCanaryExJRT\\src\\main\\aidl\\com\\letv\\sarrsdesktop\\blockcanaryex\\jrt\\internal\\ISamplerService.aidl
 */
package com.letv.sarrsdesktop.blockcanaryex.jrt.internal;
public interface ISamplerService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.letv.sarrsdesktop.blockcanaryex.jrt.internal.ISamplerService
{
private static final java.lang.String DESCRIPTOR = "com.letv.sarrsdesktop.blockcanaryex.jrt.internal.ISamplerService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.letv.sarrsdesktop.blockcanaryex.jrt.internal.ISamplerService interface,
 * generating a proxy if needed.
 */
public static com.letv.sarrsdesktop.blockcanaryex.jrt.internal.ISamplerService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.letv.sarrsdesktop.blockcanaryex.jrt.internal.ISamplerService))) {
return ((com.letv.sarrsdesktop.blockcanaryex.jrt.internal.ISamplerService)iin);
}
return new com.letv.sarrsdesktop.blockcanaryex.jrt.internal.ISamplerService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_resetCpuSampler:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.resetCpuSampler(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getCurrentCpuInfo:
{
data.enforceInterface(DESCRIPTOR);
long _arg0;
_arg0 = data.readLong();
long _arg1;
_arg1 = data.readLong();
com.letv.sarrsdesktop.blockcanaryex.jrt.internal.CpuInfo _result = this.getCurrentCpuInfo(_arg0, _arg1);
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.letv.sarrsdesktop.blockcanaryex.jrt.internal.ISamplerService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void resetCpuSampler(int pid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(pid);
mRemote.transact(Stub.TRANSACTION_resetCpuSampler, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public com.letv.sarrsdesktop.blockcanaryex.jrt.internal.CpuInfo getCurrentCpuInfo(long startTime, long endTime) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
com.letv.sarrsdesktop.blockcanaryex.jrt.internal.CpuInfo _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeLong(startTime);
_data.writeLong(endTime);
mRemote.transact(Stub.TRANSACTION_getCurrentCpuInfo, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = com.letv.sarrsdesktop.blockcanaryex.jrt.internal.CpuInfo.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_resetCpuSampler = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getCurrentCpuInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public void resetCpuSampler(int pid) throws android.os.RemoteException;
public com.letv.sarrsdesktop.blockcanaryex.jrt.internal.CpuInfo getCurrentCpuInfo(long startTime, long endTime) throws android.os.RemoteException;
}
