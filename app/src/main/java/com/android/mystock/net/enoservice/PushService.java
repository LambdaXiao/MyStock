package com.android.mystock.net.enoservice;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.mystock.R;
import com.android.mystock.application.App;
import com.android.mystock.common.log.Logs;
import com.android.mystock.ui.LaunchActivity;
import com.eno.base.utils.TCRS;
import com.eno.net.channel.ENO_Channel;
import com.eno.net.socket.manager.ENO_ConnectionManager;
import com.eno.net.socket.manager.ENO_NetConnectionNotifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

/**
 * Created by xmf on 16/3/22.
 * emil: 693559916@qq.com
 */
public class PushService extends Service {
    private static final int NOTIFY_ID = 1;
    private static Context mContext;
    private static SharedPreferences mSharedPreferences;
    private static NotificationManager mNotificationManager;
    private static Notification notification;
    private static Intent intent;
    private static PendingIntent contentIntent;
    private static List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
    private static String tel = "0";
    private static String tel_encrypt = "0";
    private static String psname = "";
    private static int subscribeFail = 0;
    private IBinder binder = new LocalBinder();
    private App app;
    private NetWorkInstance netWorkInstance;

    private static ENO_ConnectionManager connectionManager;// 长连接管理对象
    private static NetSocketWork netWorke;

    private String msgId = "";
    private String djId = "";

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private boolean isPush;

    public static String[] tsAddr = {"120.196.134.228", "125.93.53.234"};
    //public int addressIndex ;

    private String address;

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "starting", Toast.LENGTH_SHORT).show();
        System.out.println("service starting");
        app = (App) this.getApplication();
        netWorkInstance = NetWorkInstance.getInstance();
        Logs.e("服务重新运行！______________________onCreate____________________________");
        sp = getSharedPreferences("lxzq", Context.MODE_PRIVATE);
        editor = sp.edit();

        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Logs.e("服务重新运行！_______________________onStartCommand___________________________");
        order();
        return super.onStartCommand(intent, flags, startId);


    }

    @Override
    public void onDestroy() {
        System.out.println("服务关闭了！");
        Logs.e("服务关闭了！______________________onDestroy____________________________");
        if (connectionManager != null) {
            connectionManager.removeAllConnection();
        }
        super.onDestroy();
        try {
            if (mReceiver != null) {
                this.unregisterReceiver(mReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private void init() {
        if (app == null)
            app = (App) this.getApplication();

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (connectionManager == null) {
            connectionManager = new ENO_ConnectionManager();
            connectionManager.setNetNotifier(new NetConnectionNotifier());// 网络状态回调


            // conn=connectionManager.createSocketConnection("120.196.134.228",
            // 4516);//获取一个连接
        } else {
            connectionManager.removeAllConnection();
        }


//生产环境
//        String address = app.getAddressServer() ;
//
//        if(!enoSystem.servletAddr[2].equals(address)){
//            connectionManager.createSocketConnection(address, 4516);// 获取一个连接
//            connectionManager.start();// 开始维护长连接
//        }else{
//
//            NetTest test = new NetTest(tsAddr, enoSystem.port,new NetTestNotifier() {
//
//                @Override
//                public void onNetNotifier(int index, long time) {
//                    //this.address = tsAddr[index] ;
//                    System.out.println("   谁最快！！！ === tsAddr["+index+"] ==="+tsAddr[index]) ;
//
//                    connectionManager.createSocketConnection(tsAddr[index], 4516);// 获取一个连接
//                    connectionManager.start();// 开始维护长连接
//                }
//            }, enoSystem) ;
//            test.stratrTest(enoSystem) ;
//        }


//测试专用
        //connectionManager.createSocketConnection("219.143.202.27", 8002);// 获取一个连接  測試
        connectionManager.createSocketConnection("210.74.1.90", 8002);// 获取一个连接  測試
        connectionManager.start();// 开始维护长连接


    }


    /**
     * 订阅推送
     */
    public void order() {
        Logs.e("发送了推送订阅：");
        StringBuffer str1 = new StringBuffer();
        str1.append("tc_mfuncno=202&tc_sfuncno=1&s_ac=13148794461&imei=0000000");
        netWorke.sendRequest(1, str1.toString());

    }


    class NetMessage implements NetClientPushMessageible {

        @Override
        public boolean netParse(TCRS[] tcrs, Bundle bundle) {


            //isPush = sp.getBoolean(Fragment_seting_seting.openTs, true) ;


//            if(enoSystem.isLocked()){
//                Logger.d("锁屏状态下或者登录状态下，不弹出任何消息！！") ;
//                return true;
//            }


            if (tcrs != null && tcrs[0].IsError()) {

                Logs.e("推送出现错误：" + tcrs[0].getErrMsg());

            }


            Logs.e("收到消息推送");
            int ENOPackage = bundle.getInt("packageID");
            try {

                // boolean isBackgroundRunning = isBackgroundRunning()||!enoSystem.isLogin();  //离线状态
                boolean isBackgroundRunning = true;


                Message msg = Message.obtain();
                Bundle b = new Bundle();
                msg.obj = tcrs;
                b.putByteArray("rs", tcrs[0].ExportData());
                msg.setData(b);

                Logs.e("__________" + tcrs[0].getRecordInfo());

                if ("quitinfo".equals(tcrs[0].getRecordInfo())) {

                    Logs.e("_______2___" + tcrs[0].getRecordInfo() + "    " + isBackgroundRunning
                            + "    " + isBackgroundRunning());
                    if (isBackgroundRunning) {
                        //if(enoSystem.isLogin()){  //已经登录了，是可以收到消息的
                        sendPushMsg("消息通知！", "您的账号在其它地方登陆！",
                                "", "登陆退出", b);
                        // }

                    } else {
                        // if(enoSystem.isLogin()){  //已经登录了，是可以收到消息的
                        intent = new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //   intent.setClass(PushService.this, Quit_Dialog.class);
//				intent.setClass(PushMsgService.this, DJTX_Dialog.class);
                        intent.putExtras(b);
                        //intent.setAction(Activity_Base.Login);
                        startActivity(intent);
                        //  }

                    }

                    return false;
                }

                if (ENOPackage == 0) {
                    djId = tcrs[0].toString("contenttype");
                    if (djId.equals("999")) {


                    } else {

                        if (!isBackgroundRunning) {

                            if (isPush) {
                                intent = new Intent();
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                // intent.setClass(PushServer.this, Msg_Dialog.class);
                                intent.putExtras(b);
                                startActivity(intent);
                            }
                        } else {

                            sendPushMsg(tcrs[0].toString("typename"),
                                    tcrs[0].toString("msg"),
                                    tcrs[0].toString("data"), "消息中心", b);

                        }

                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        public boolean netError(String msg) {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public void netNotifier() {
            // TODO Auto-generated method stub

        }


    }


    /**
     * 判断当前程序是否在前台显示
     *
     * @return
     */
    private boolean isBackgroundRunning() {

        String processName = "com.eno.lx.mobile.page";
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

        if (activityManager == null)
            return false;
        // get running application processes
        List<ActivityManager.RunningAppProcessInfo> processList = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo process : processList) {
            if (process.processName.startsWith(processName)) {
                boolean isBackground = process.importance != IMPORTANCE_FOREGROUND
                        && process.importance != IMPORTANCE_VISIBLE;
                boolean isLockedState = keyguardManager
                        .inKeyguardRestrictedInputMode();
                if (isBackground || isLockedState)
                    return true;
                else
                    return false;
            }
        }
        return false;
    }


    /**
     * 监听消息推送长连接
     */
    class NetConnectionNotifier implements ENO_NetConnectionNotifier {

        @Override
        public boolean onConnectSusseed(ENO_Channel con) {
            // TODO Auto-generated method stub
            netWorke = new NetSocketWork(new NetMessage(), con, app);// 实例化一个工作线程
            netWorke.setParameter(netWorkInstance.getFixedUrl());
            netWorke.stratListenting();
            Logs.e("连接发送变化重新发送订阅请求！-------------");
            order();
            return true;
        }

        @Override
        public boolean onConnectError(int errorNumber, ENO_Channel con) {
            // TODO Auto-generated method stub
            Logs.e("长连接失败！-------------" + errorNumber);
            return true;
        }

    }

    private static final int NOTIFICATION_FLAG = 1;

    /**
     * 发送状态栏消息
     *
     * @param contentTitle
     * @param contentText
     * @param contentDate
     */
    public void sendPushMsg(String contentTitle, String contentText,
                            String contentDate, String action, Bundle b) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        CharSequence tickerText = "证券推送消息";
        long when = System.currentTimeMillis();
        PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0,
                new Intent(this, LaunchActivity.class), 0);
        // 通过Notification.Builder来创建通知，注意API Level
        // API11之后才支持
        Notification notify2 = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示，如果在那里需要更换更大的图片，可以使用setLargeIcon(Bitmap
                // icon)
                .setTicker("TickerText:" + "您有新短消息，请注意查收！")// 设置在status
                // bar上显示的提示文字
                .setContentTitle("Notification Title")// 设置在下拉status
                // bar后Activity，本例子中的NotififyMessage的TextView中显示的标题
                .setContentText("This is the notification message")// TextView中显示的详细内容
                .setContentIntent(pendingIntent2) // 关联PendingIntent
                .setNumber(1) // 在TextView的右方显示的数字，可放大图片看，在最右侧。这个number同时也起到一个序列号的左右，如果多个触发多个通知（同一ID），可以指定显示哪一个。
                .getNotification(); // 需要注意build()是在API level
        // 16及之后增加的，在API11中可以使用getNotificatin()来代替
        notify2.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(NOTIFICATION_FLAG, notify2);
    }

    private ConnectivityManager connectivityManager;
    private NetworkInfo info;
    /**
     * 监听网络状态
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                String action = intent.getAction();
                if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    Log.d("mark", "网络状态已经改变");
                    connectivityManager = (ConnectivityManager)

                            getSystemService(Context.CONNECTIVITY_SERVICE);
                    info = connectivityManager.getActiveNetworkInfo();
                    if (info != null && info.isAvailable()) {
                        String name = info.getTypeName();
                        Logs.e("mark", "-----------NO---------当前网络已连接：" + name);
                        init();
                    } else {
                        Logs.e("mark", "-----------OFF-----------没有可用网络停止长连接维护");
                        connectionManager.removeAllConnection();
                        connectionManager.stop();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };


    // 定义内容类继承Binder
    public class LocalBinder extends Binder {
        // 返回本地服务
        public PushService getService() {
            return PushService.this;
        }
    }


}
