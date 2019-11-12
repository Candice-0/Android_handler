package com.example.handler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TestHandlerActivity";
    //主线程中的handler
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //获得刚才发送的Message对象，然后在这里进行UI操作
            Log.e(TAG,"------------> msg.what = " + msg.what);
        }
    };
    //子线程中的handler
    private Handler mHandlerThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();

    }

    private void initData() {

        //开启一个线程模拟处理耗时的操作
        new Thread(new Runnable() {
            @Override
            public void run() {

                SystemClock.sleep(2000);
                //通过Handler发送一个消息切换回主线程（mHandler所在的线程）
                mHandler.sendEmptyMessage(0);

                //调用Looper.prepare（）方法
                Looper.prepare();

                //在子线程中创建Handler
                mHandlerThread = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        Log.e("sub thread","---------> msg.what = " + msg.what);
                    }
                };
                mHandlerThread.sendEmptyMessage(1);

                //调用Looper.loop（）方法
                Looper.loop();
            }
        }).start();

    }

}
