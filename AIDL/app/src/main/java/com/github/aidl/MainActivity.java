package com.github.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView mTvRunningTime;
    private IMyAidlInterface mIMyAidlInterface;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvRunningTime = (TextView) findViewById(R.id.tv_running_time);

        //绑定服务
        Intent intent = new Intent();
        intent.setAction("com.github.aidl.action.MyService");
        intent.setPackage(getPackageName());
        MyServiceConnection myServiceConnection = new MyServiceConnection();
        bindService(intent,myServiceConnection, Context.BIND_AUTO_CREATE);

        refreshRunningTime();
    }

    private void refreshRunningTime(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //通过AIDL接口IMyAidlInterface调用MyService中的getRunningTime()方法
                        if (mIMyAidlInterface!=null) {
                            try {
                                mTvRunningTime.setText(mIMyAidlInterface.getRunningTime());
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        },0,1000);
    }

    private class MyServiceConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mIMyAidlInterface = IMyAidlInterface.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }

}
