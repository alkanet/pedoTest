package com.ebs.test;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by yyh on 15-7-20.
 */
public class PedoService extends Service {

    private static String TAG="PedoService";
    private PedoBinder pBinder=new PedoBinder();


    private NotificationManager mNM;

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "[Service] onBind");
        return null;
    }
    @Override
    public void onCreate(){
        Log.i(TAG, "[Service] onCreate");
        super.onCreate();

        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        showNotification();
    }
    @Override
    public void onStart(Intent intent, int startId){
        Log.i(TAG, "[Service] onStart");
        super.onStart(intent,startId);
    }
    @Override
    public int onStartCommand(Intent intent,int flags, int startId){
        Log.i(TAG, "[Service] onStartCommand");
        return super.onStartCommand(intent,flags,startId);
    }
    public class PedoBinder extends Binder {
        PedoService getService() {
            return PedoService.this;
        }
    }

    private void showNotification() {
        CharSequence text = getText(R.string.app_name);
        //Notification notification = new Notification(R.drawable.ic_notification, null,
        //        System.currentTimeMillis());
        Notification.Builder builder = new Notification.Builder(this).setSmallIcon(R.drawable.ic_notification)
                .setTicker(text);
        Notification notification=builder.build();

        //notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
        notification.flags=Notification.FLAG_NO_CLEAR;

        Intent pedoIntent = new Intent();
        pedoIntent.setComponent(new ComponentName(this, PedoAct.class));
        pedoIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                pedoIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notification.setLatestEventInfo(this, text,
                getText(R.string.notification_subtitle), contentIntent);

        mNM.notify(R.string.app_name, notification);
    }
}
