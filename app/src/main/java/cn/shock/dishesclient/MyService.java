package cn.shock.dishesclient;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyService extends Service {

    int busytable ;
    NotificationManager mNotificationManager;
    public MyService() {
    }

    public static final String TAG = "MyService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() executed");
        busytable = 0;

        getNewmenu();
        sleep(2500);

        /*
         * 新的菜单通知服务
         * */
        new Thread(new Runnable() {
            @Override
            public void run() {

                while(true){

                    int num = busytable;
                    getNewmenu();
                    sleep(1000);
                    if(busytable>num){
                        Notification();
                    }

                }
            }
        }).start();
        

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() executed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //访问网络获取是否有新的菜单
    public void getNewmenu(){
        String Url = "http://"+getResources().getString(R.string.localhost)+":8080/OrderWeb/GetMenuNum";
        HttpUtil.sendOkHttpRequest(Url, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responsebody = response.body().string();
                try {
                    busytable = new Integer(responsebody.trim()).intValue();
                }catch(Exception e){
                    busytable=0;
                }


                Log.i("shockc",responsebody);
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.i("shockc","网络出错");

            }
        });
    }
    //睡眠
    public void sleep(long time){
        try {
            new Thread().sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //通知栏方法
    public void Notification(){
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder=new Notification.Builder(this);
        Intent intent=new Intent(MyService.this, Tablelist.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,0);


        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);
        builder.setContentTitle("有新的菜单了");
        mNotificationManager.notify(1, builder.build());


    }
}
