package cn.cj.floatingwindow.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.june.floatingwindow.R;

import cn.cj.floatingwindow.ui.NetFlowFloatingView;
import cn.cj.floatingwindow.ui.PlayerView;
import cn.cj.floatingwindow.ui.MainActivity;

/**
 * 悬浮窗context，应用非前台应用时，仍可显示悬浮窗
 */
public class FloatingWindowService extends Service {
    private static final String TAG = FloatingWindowService.class.getSimpleName();
    public static final String PARAM_KEY = "param";
    public static final int PARAM_VALUE = 1;

    /**
     * a notification to keep the service alive when task manager try to kill it
     */
    private static Notification notification;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int ret = super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            Log.d(TAG, "---onStartCommand-" + intent.getIntExtra("PARAM", -1) + "-flags-" + flags + "---return-" + ret);
            if (intent.getIntExtra(PARAM_KEY, -1) == PARAM_VALUE) {
                showFloatingView(this);
                return START_STICKY;
            }
        }
        return ret;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "---onCreate");
        if (notification == null) {
            // build a notification to show nothing just to keep this service alive
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext());
            notification = builder.build();
            // 常駐起動
//            startForeground(, notification);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "---onCreate");
    }

    // 先权限检查
    private void showFloatingView(Context context) {
        boolean canShow = false;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            canShow = true;
        } else {
            canShow = Settings.canDrawOverlays(context);
        }

        if (canShow) {
            // 加载视频悬浮窗并显示
            String path = "http://lteby1.tv189.com/6/mobi/vod/ts01/TVM/video/3gp/TVM/HuNanHD/2016/08/09/4395129c-c98f-4683-b8bf-a0c707f97259/Q350/Q350.3gp?sign=9988c87f046c5e3a6a389ac95aa29e93&tm=57ad5b20&vw=2&ver=v1.1&t=57ad7740&qualityCode=452&version=1&guid=6a49a20a-d1c2-ff4b-c901-324114049a8f&app=115020310073&cookie=57ad5a70dfd97&session=57ad5a70dfd97&uid=104300052488646160804&uname=17721050902&time=20160812131408&videotype=2&cid=C38619987&cname=&cateid=&dev=000001&ep=14&isp=&etv=&os=30&ps=0099&clienttype=GT-I9500&deviceid=null&appver=5.2.19.7&res=1080%2A1920&channelid=059998&pid=1000000228&orderid=1100479128457&nid=&netype=11&cp=00000055&sp=00000014&ip=180.168.5.182&ipSign=d2c09011944e08b56027145f44c4827b&cdntoken=api_57ad5b203bab8&pvv=0";
            PlayerView playerView = new PlayerView(context);
            playerView.show(path);

            // 网速悬浮窗
            NetFlowFloatingView netFlowFloatingView = new NetFlowFloatingView(context);
            netFlowFloatingView.show();

        } else {
            // 申请悬浮窗权限
            final Intent i = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }

    protected void showWhat(){

    }

    private void noti(Context context) {
        Notification mNotification = new Notification(R.mipmap.ic_launcher, "aaaaaa", System.currentTimeMillis());
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //该通知能被状态栏的清除按钮给清除掉
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;
        // 设置提醒声
        mNotification.defaults |= Notification.DEFAULT_SOUND;
        Intent notificationIntent = new Intent(context, MainActivity.class); /* 建立PendingIntent */
        PendingIntent sender = PendingIntent.getBroadcast(context, 1111, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //要实现自定义布局，首先自己定义一个 布局文件notification.xml,如放一个ImageView 和 TextView，用这个布局文件生成 //RemoteViews的实例

        RemoteViews contentview = new RemoteViews(context.getPackageName(), R.layout.notification_view);
        mNotification.contentView = contentview;
        mNotification.contentIntent = sender;
        mNotificationManager.notify(1111, mNotification);
    }


}
