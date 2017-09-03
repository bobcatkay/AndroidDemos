package com.github.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.IntDef;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyService extends Service {
    public MyService() {
    }

    private long mServiceCreatTime;


    @Override
    public void onCreate() {
        super.onCreate();
        mServiceCreatTime = System.currentTimeMillis();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    private class MyBinder extends IMyAidlInterface.Stub{

        @Override
        public String getRunningTime() throws RemoteException {
            return MyService.this.getRunningTime();
        }
    }

    public String getRunningTime(){
        long runningTime = System.currentTimeMillis()-mServiceCreatTime;
        return String.format("服务已运行: %s",dateFormat(runningTime));
    }

    private String dateFormat(long timeMillions){
        Date date = new Date(timeMillions);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(date);
    }

}
