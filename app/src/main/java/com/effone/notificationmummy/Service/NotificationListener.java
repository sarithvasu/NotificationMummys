package com.effone.notificationmummy.Service;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.effone.notificationmummy.common.AppInfo;
import com.effone.notificationmummy.model.NotificaitionMessageInfo;
import com.effone.notificationmummy.database.DataBaseHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationListener extends NotificationListenerService {
    public static boolean isNotificationAccessEnabled = false;
    public static ArrayList<AppInfo> appName=new ArrayList<>();
    private String mPreviousKey;


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        if (!sbn.isOngoing()) {
            if (sbn.getTag() != null) {
                if (!sbn.getKey().equals(mPreviousKey)&&!sbn.getKey().contains("null")) {
                    mPreviousKey=sbn.getKey();
                    storeNotification(sbn);

                }
            }
        }

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void storeNotification(StatusBarNotification sbn) {
        String pack = sbn.getPackageName();
        if (pack.equals("com.whatsapp")) {
            if (sbn.getNotification() != null) {
                if (sbn.getNotification().getSortKey() != null)
                    if (sbn.getNotification().getSortKey().equals("1")) {
                        storingIntoData(sbn);
                    }
            }
        } else {
            storingIntoData(sbn);
        }

        //}

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void storingIntoData(final StatusBarNotification sbn) {
        String pack = sbn.getPackageName();
        DataBaseHandler dataBaseHandler = DataBaseHandler.getInstance(getApplicationContext());
        String message;
        Bundle extras = sbn.getNotification().extras;
        if (extras != null) {

            String title = extras.getString("android.title");
            String text = extras.getCharSequence("android.text").toString();
            try {
                String selfDisplayName = extras.getString("android.selfDisplayName").toString();
                if (title.contains("(")) {
                    title = title.replace(":", "");
                    int startIndex = title.indexOf("(");
                    int endIndex = title.indexOf(")");
                    String starttiltes = title.substring(0, startIndex);
                    String endTilte = title.substring(endIndex + 1, title.length()).trim();
                    if (endTilte.equals("")) {
                        title = starttiltes;
                    } else
                        title = starttiltes + "@" + endTilte;
                } else if (title.contains(":")) {
                    int startIndex = title.indexOf(":");
                    if (startIndex != title.length()) {
                        title = title.replace(":", "@");
                    }

                }
            } catch (Exception e) {
                Log.e("siplayTitle",e.getMessage());
            }
            try{
            int value = title.indexOf("@");
            if ((value + 2) == title.length()) {
                String[] name = title.split("@");
                title = name[0].trim();
            }}catch (Exception e){

            }
            try {
                String selfDisplayName = extras.getString("android.selfDisplayName").toString();
                title = selfDisplayName;
            } catch (Exception e) {
                Log.e("displayTitle",e.getMessage());
            }
            String strAppName = null;
            try {
                ApplicationInfo info = getPackageManager().getApplicationInfo(pack, 0);
                strAppName = getPackageManager().getApplicationLabel(info).toString();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if (title != null)
                message = "From " + title + " Msg :" + text;
            else
                message = "Msg :" + text;
            String[] number = sbn.getTag().split("@");
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String[] imageDate = formatter.format(new Date(sbn.getPostTime())).split(" ");
            try {
                NotificaitionMessageInfo notificaitionMessageInfo = new NotificaitionMessageInfo();
                notificaitionMessageInfo.setIs_read(0); // for unreaded messages
                notificaitionMessageInfo.setSender_message(message);
                notificaitionMessageInfo.setPackage_name(sbn.getPackageName());
                notificaitionMessageInfo.setStrAppName(strAppName);
                String[] da = text.split(" ");
                try {
                    if (da[1].equals("GIF") || da[1].equals("Photo") || da[1].equals("Video") || da[1].equals("Audio")) {
                        notificaitionMessageInfo.setMessage_type(da[1]);
                    } else {
                        notificaitionMessageInfo.setMessage_type("text");
                    }
                } catch (Exception e) {
                    notificaitionMessageInfo.setMessage_type("text");
                }
                notificaitionMessageInfo.setSender_message_formatted(text);
                notificaitionMessageInfo.setSender_name(title);
                notificaitionMessageInfo.setSender_message_time_stamp(sbn.getPostTime());
                notificaitionMessageInfo.setSender_ph_number(number[0].trim().replaceAll("@", ""));
                notificaitionMessageInfo.setSender_date(imageDate[0]);
                if (notificaitionMessageInfo.getPackage_name().equals("com.whatsapp"))
                    notificaitionMessageInfo.setIs_allowed(1);
                for(int i=0;i<appName.size();i++){
                    if(appName.get(i).getPname().equals(notificaitionMessageInfo.getStrAppName())){
                        notificaitionMessageInfo.setIs_allowed(1);
                    }
                }

                dataBaseHandler.insertNotificationInfo(notificaitionMessageInfo);
            } catch (Exception e) {
                Log.e("Main",e.getMessage());
            }
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        IBinder binder = super.onBind(intent);
        isNotificationAccessEnabled = true;
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        boolean onUnbind = super.onUnbind(intent);
        isNotificationAccessEnabled = false;
        return onUnbind;
    }


}
