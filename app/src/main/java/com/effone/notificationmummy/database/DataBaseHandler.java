package com.effone.notificationmummy.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.effone.notificationmummy.common.AppInfo;
import com.effone.notificationmummy.common.CustomContentProvider;
import com.effone.notificationmummy.model.MessageInfo;
import com.effone.notificationmummy.model.MessageUnreadInfo;
import com.effone.notificationmummy.model.NotificaitionMessageInfo;

import java.util.ArrayList;

/**
 * Created by sumanth.peddinti on 5/11/2018.
 */

public class DataBaseHandler extends SQLiteOpenHelper {
    SQLiteDatabase db;

    private Context context;

    public DataBaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        db = getWritableDatabase();
        this.context = context.getApplicationContext();
    }

    private static DataBaseHandler mInstance = null;
    public static String DATA_BASE_NAME = "notification.db";
    public static final int DATABASE_VERSION = 11;

    public static DataBaseHandler getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DataBaseHandler(context, DATA_BASE_NAME, null, DATABASE_VERSION);
        }
        return mInstance;
    }

    //table field Names
    private final static String USER_TABLE = "user_table";
    private final static String SENDER_ID = "sender_id";
    private final static String SENDER_NAME = "sender_name";
    private final static String SENDER_PH_NUMBER = "sender_ph_number";

    private final static String MESSAGE_TABLE = "message_table";
    private final static String MESSAGE_ID = "message_id";
    private final static String SENDER_MESSAGE = "sender_message";
    private final static String SENDER_MESSAGE_FORMATTED = "sender_message_formatted";
    private final static String SENDER_MESSAGE_TIME_STAMP = "sender_message_time_stamp";
    private final static String IS_READ = "is_read";

    private final static String USERXPACKAGE = "user_package";
    private final static String USER_PACKAGE_ID = "user_package_id";
    private final static String PACKAGE_NAME = "package_name";
    private final static String SENDER_DATE = "sender_date";
    private final static String SENDER_MESSAGE_TYPE = "message_type";
    private final static String UNBLOCK = "unblock";
    private final static String APPPACKAGE = "package_name";
    private final static String APP_NAME = "app_name";
    private final static String IS_ALLOWED = "is_allowed";
    private long BEFORE_TIME=0;
    private long AFTER_TIME=0;


    private String mUserXPackageTable = "CREATE TABLE if not exists " + USERXPACKAGE + "( " + USER_PACKAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
            SENDER_ID + " INTEGER, " + PACKAGE_NAME + " TEXT, " + APP_NAME + " TEXT, " + IS_ALLOWED + " INTEGER, FOREIGN KEY (sender_id) REFERENCES user_table(sender_id))";


    private String mMessageSenderTable = "CREATE TABLE if not exists " + MESSAGE_TABLE + "(" + MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + SENDER_ID + " INTEGER, " + USER_PACKAGE_ID + " INTEGER, " +
            SENDER_MESSAGE + " TEXT, " + SENDER_MESSAGE_FORMATTED + " TEXT," + SENDER_MESSAGE_TYPE + " TEXT," + SENDER_MESSAGE_TIME_STAMP
            + " long," + SENDER_DATE + " TEXT," + IS_READ
            + " INTEGER DEFAULT 0 ,FOREIGN KEY (sender_id) REFERENCES user_table(sender_id),FOREIGN KEY(" + USER_PACKAGE_ID + ") REFERENCES user_package(" + USER_PACKAGE_ID + "))";

    private String mUserTable = "CREATE TABLE if not exists " + USER_TABLE + "(" + SENDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            SENDER_NAME + " TEXT, " + SENDER_PH_NUMBER + " TEXT )";


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(mUserXPackageTable);
        sqLiteDatabase.execSQL(mMessageSenderTable);
        sqLiteDatabase.execSQL(mUserTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private PackageManager packageManager;


    public void insertNotificationInfo(NotificaitionMessageInfo notificaitionMessageInfo) {
        ContentValues mPackageContent = new ContentValues();
        mPackageContent.put(PACKAGE_NAME, notificaitionMessageInfo.getPackage_name());
        mPackageContent.put(APP_NAME, notificaitionMessageInfo.getStrAppName());
        mPackageContent.put(IS_ALLOWED, notificaitionMessageInfo.getIs_allowed());
        ContentValues mMessageContent = new ContentValues();
        mMessageContent.put(SENDER_MESSAGE, notificaitionMessageInfo.getSender_message_formatted());
        mMessageContent.put(SENDER_MESSAGE_FORMATTED, notificaitionMessageInfo.getSender_message());
        mMessageContent.put(SENDER_MESSAGE_TIME_STAMP, notificaitionMessageInfo.getSender_message_time_stamp());
        mMessageContent.put(SENDER_DATE, notificaitionMessageInfo.getSender_date());
        mMessageContent.put(SENDER_MESSAGE_TYPE, notificaitionMessageInfo.getMessage_type());
        mMessageContent.put(IS_READ, notificaitionMessageInfo.getIs_read());
        ContentValues mUserContent = new ContentValues();
        mUserContent.put(SENDER_NAME, notificaitionMessageInfo.getSender_name());
        mUserContent.put(SENDER_PH_NUMBER, notificaitionMessageInfo.getSender_ph_number());
        BEFORE_TIME=System.currentTimeMillis();
        Log.e("BEFORE",""+System.currentTimeMillis());
        insertDataIntoTables(mPackageContent, mUserContent, mMessageContent);
        AFTER_TIME=System.currentTimeMillis();
        Log.e("AFTER",""+System.currentTimeMillis());
        Log.e("","sadsa");

    }

    private void insertDataIntoTables(ContentValues mPackageContent, ContentValues mUserContent, ContentValues mMessageContent) {
        SQLiteDatabase db = this.getWritableDatabase();
        long value;

        db.beginTransaction();
        try {
            if (!checkingWhetherToinsertOrNot(mUserContent.getAsString(SENDER_NAME), mUserContent.getAsString(SENDER_PH_NUMBER)))
                value = db.insertWithOnConflict(USER_TABLE, null, mUserContent, SQLiteDatabase.CONFLICT_IGNORE);

            int sender_id = fetchTheSenderId(mUserContent.getAsString(SENDER_NAME), mUserContent.getAsString(SENDER_PH_NUMBER));
            mPackageContent.put(SENDER_ID, sender_id);
            mMessageContent.put(SENDER_ID, sender_id);
            if(!mPackageContent.getAsString(PACKAGE_NAME).equals("com.whatsapp")) {
                int is_allowed = checkingTheValue(mPackageContent.getAsString(PACKAGE_NAME));
                mPackageContent.put(IS_ALLOWED, is_allowed);
            }
            if (!checkingWhetherToinsertOrNot(mPackageContent.getAsString(PACKAGE_NAME), mPackageContent.getAsInteger(SENDER_ID))) {
                value = db.insertWithOnConflict(USERXPACKAGE, null, mPackageContent, SQLiteDatabase.CONFLICT_IGNORE);
            }


            int package_id = fetchThePackageId(mPackageContent.getAsString(PACKAGE_NAME));

            mMessageContent.put(USER_PACKAGE_ID, package_id);

            value = db.insertWithOnConflict(MESSAGE_TABLE, null, mMessageContent, SQLiteDatabase.CONFLICT_IGNORE);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("insertionError", e.getMessage());
        } finally {
            db.endTransaction();
        }
        notifyOnNewContact();
    }

    private int checkingTheValue(String asString) {
        int sender_id = 0;
        Cursor c = db.rawQuery("SELECT is_allowed FROM " + USERXPACKAGE + " WHERE " + PACKAGE_NAME + " = '" + asString + "'", null);

        while (c.moveToNext()) {
            sender_id = c.getInt(c.getColumnIndexOrThrow(IS_ALLOWED));
        }

        return sender_id;

    }

    private int fetchThePackageId(String asString) {
        int sender_id = 0;
        Cursor c = db.rawQuery("SELECT * FROM " + USERXPACKAGE + " WHERE " + PACKAGE_NAME + " = '" + asString + "'", null);

        while (c.moveToNext()) {
            sender_id = c.getInt(c.getColumnIndexOrThrow(USER_PACKAGE_ID));
        }
        return sender_id;
    }

    private int fetchTheSenderId(String sender_name, String sender_phone_number) {
        int sender_id = 0;
        Cursor c = db.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE " + SENDER_NAME + " = '" + sender_name + "' AND "
                + SENDER_PH_NUMBER + " = '" + sender_phone_number + "'", null);

        while (c.moveToNext()) {
            sender_id = c.getInt(c.getColumnIndexOrThrow(SENDER_ID));
        }
        return sender_id;
    }

    private boolean checkingWhetherToinsertOrNot(String sender_name, String sender_phone_number) {
        Cursor c = db.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE " + SENDER_NAME + " = '" + sender_name + "' AND "
                + SENDER_PH_NUMBER + " = '" + sender_phone_number + "'", null);

        if (c.getCount() != 0) {
            return true;
        } else
            return false;
    }

    private boolean checkingWhetherToinsertOrNot(String package_name, int sender_id) {
        Cursor c = db.rawQuery("SELECT * FROM " + USERXPACKAGE + " WHERE " + PACKAGE_NAME + " = '" + package_name + "' AND " + SENDER_ID + " = '" + sender_id + "'", null);
        if (c.getCount() != 0) {
            return true;
        } else
            return false;
    }

    public ArrayList<MessageUnreadInfo> fetchTheLastRecord(String appName) {
        ArrayList<MessageUnreadInfo> messageUnreadInfoArrayList = new ArrayList<>();
        String query="SELECT T1.sender_id,    " +
                "       T1.sender_name,    " +
                "        count( case when t2.is_read = 0 then 1 end )as  message_count,    " +
                "  (SELECT sender_message    " +
                "   FROM message_table    " +
                "   WHERE sender_id = T2.sender_id    " +
                "     AND user_package_id = T2.user_package_id    " +
                "     AND sender_message_time_stamp IN    " +
                "       (SELECT MAX(sender_message_time_stamp)    " +
                "        FROM message_table    " +
                "        WHERE sender_id = T2.sender_id    " +
                "          AND user_package_id = T2.user_package_id    " +
                "        LIMIT 1)    " +
                "   LIMIT 1) AS last_message,    " +
                "    " +
                "  (SELECT MAX(sender_message_time_stamp)    " +
                "   FROM message_table    " +
                "   WHERE sender_id = T2.sender_id    " +
                "     AND user_package_id = T2.user_package_id    " +
                "     AND sender_message_time_stamp    " +
                "   LIMIT 1) AS last_message_timestamp    " +
                " FROM user_table T1    " +
                "INNER JOIN message_table T2 ON T1.sender_id = T2.sender_id    " +
                "INNER JOIN user_package T3 ON T3.user_package_id = T2.user_package_id    " +
                "AND T3.app_name = '"+appName+"'    " +
                "GROUP BY T1.sender_name    " +
                "ORDER BY T2.sender_message_time_stamp DESC";


        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            MessageUnreadInfo messageUnreadInfo = new MessageUnreadInfo();
            messageUnreadInfo.setSender_id(cursor.getInt(cursor.getColumnIndex(SENDER_ID)));
            messageUnreadInfo.setSender_message(cursor.getString(cursor.getColumnIndex("last_message")));
            messageUnreadInfo.setSender_name(cursor.getString(cursor.getColumnIndex(SENDER_NAME)));
            messageUnreadInfo.setTimestamp(cursor.getLong(cursor.getColumnIndex("last_message_timestamp")));
            messageUnreadInfo.setUnread(cursor.getInt(cursor.getColumnIndex("message_count")));
            messageUnreadInfoArrayList.add(messageUnreadInfo);

        }
        return messageUnreadInfoArrayList;
    }

    public ArrayList<MessageInfo> fetchAllTheRecords(int sender_id) {
        ArrayList<MessageInfo> mMessage = new ArrayList<>();
        Cursor mCursor = db.rawQuery("select * from message_table where sender_id=" + sender_id + " ORDER BY " + SENDER_MESSAGE_TIME_STAMP + " DESC ", null);
        while (mCursor.moveToNext()) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMessage_id(mCursor.getInt(mCursor.getColumnIndex(MESSAGE_ID)));
            messageInfo.setSender_id(mCursor.getInt(mCursor.getColumnIndex(SENDER_ID)));
            messageInfo.setSender_message(mCursor.getString(mCursor.getColumnIndex(SENDER_MESSAGE)));
            messageInfo.setSender_message_format(mCursor.getString(mCursor.getColumnIndex(SENDER_MESSAGE_FORMATTED)));
            messageInfo.setSender_message_time_stamp(mCursor.getString(mCursor.getColumnIndex(SENDER_MESSAGE_TIME_STAMP)));
            messageInfo.setMessage_type(mCursor.getString(mCursor.getColumnIndex(SENDER_MESSAGE_TYPE)));
            messageInfo.setDate(mCursor.getString(mCursor.getColumnIndex(SENDER_DATE)));
            mMessage.add(messageInfo);
        }
        return mMessage;
    }

    private void notifyOnNewContact() {
        context.getContentResolver().notifyChange(
                CustomContentProvider.URI_PERSONS, null, false);
    }

    public void updateTheRecord(int sender_id) {
        ContentValues cv = new ContentValues();
        cv.put(IS_READ, 1);
        db.update(MESSAGE_TABLE, cv, SENDER_ID + " = " + sender_id, null);
    }

    public void removeTheCompletePackage(MessageInfo selecteditem) {
        deletePackageFromList(selecteditem.getSender_id());
    }

    private void deletePackageFromList(int sender_id) {

    }

    public boolean removeTheSelectedIndex(int message_id) {
        ContentValues cv = new ContentValues();
        cv.put(MESSAGE_ID, MESSAGE_ID);
        return db.delete(MESSAGE_TABLE, MESSAGE_ID + "=" + message_id, null) > 0;

    }

    public ArrayList<MessageInfo> fetchTheRecords(int sender_id) {
        ArrayList<MessageInfo> mMessage = new ArrayList<>();
        Cursor mCursor = db.rawQuery("select * from message_table where sender_id=" + sender_id + "  ORDER BY " + SENDER_MESSAGE_TIME_STAMP + " DESC limit 1 ", null);
        while (mCursor.moveToNext()) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMessage_id(mCursor.getInt(mCursor.getColumnIndex(MESSAGE_ID)));
            messageInfo.setSender_id(mCursor.getInt(mCursor.getColumnIndex(SENDER_ID)));
            messageInfo.setSender_message(mCursor.getString(mCursor.getColumnIndex(SENDER_MESSAGE)));
            messageInfo.setSender_message_format(mCursor.getString(mCursor.getColumnIndex(SENDER_MESSAGE_FORMATTED)));
            messageInfo.setSender_message_time_stamp(mCursor.getString(mCursor.getColumnIndex(SENDER_MESSAGE_TIME_STAMP)));
            messageInfo.setMessage_type(mCursor.getString(mCursor.getColumnIndex(SENDER_MESSAGE_TYPE)));
            mMessage.add(messageInfo);
        }
        return mMessage;
    }

    public ArrayList<AppInfo> getReceiveMessageInfo(String value) {
        Cursor mCursor = db.rawQuery(
                " select count(*),t1.package_name,t1.app_name,t1.blocked from UserXPackage as t1 left join user_table as t2  left join message_table as t3 where t1.blocked='" + value + "'and t1.sender_id=t2.sender_id and t1.sender_id=t3.sender_id group by t1.package_name and t1.blocked"
                , null);
        ArrayList<AppInfo> mAp = new ArrayList<>();
        while (mCursor.moveToNext()) {
            AppInfo appInfo = new AppInfo();
            appInfo.setAppname(mCursor.getString(mCursor.getColumnIndex(APP_NAME)));
            appInfo.setPname(mCursor.getString(mCursor.getColumnIndex(PACKAGE_NAME)));
            if (mCursor.getInt(mCursor.getColumnIndex(IS_ALLOWED)) == 1)
                appInfo.setChecked(true);
            else
                appInfo.setChecked(false);
            appInfo.setCount(mCursor.getInt(0));
            mAp.add(appInfo);
        }
        return mAp;
    }


    public ArrayList<AppInfo> getTheEnabledList(int aTrue) {
        db = this.getReadableDatabase();
        String query = "SELECT                  T1.user_package_id," +
                "                                                T1.package_name, T1.app_name," +
                "                                                COUNT(case WHEN T2.is_read = 0 then 1 end) AS message_count" +
                " FROM                   user_package T1 " +
                " INNER JOIN        message_table T2 " +
                " ON                             T1.is_allowed = "+aTrue+" and         T1.user_package_id = T2.user_package_id "+
                " GROUP BY T1.package_name" +
                " ORDER BY T1.user_package_id DESC";
        Cursor mCursor = db.rawQuery(
                query, null);
        ArrayList<AppInfo> mAp = new ArrayList<>();
        while (mCursor.moveToNext()) {
            AppInfo appInfo = new AppInfo();
            appInfo.setAppname(mCursor.getString(mCursor.getColumnIndex(APP_NAME)));
            appInfo.setPname(mCursor.getString(mCursor.getColumnIndex(PACKAGE_NAME)));
            if(aTrue== 1)
            appInfo.setChecked(true);
            else
                appInfo.setChecked(false);
            appInfo.setCount(mCursor.getInt(mCursor.getColumnIndex("message_count")));
            mAp.add(appInfo);
        }
        return mAp;


    }

    public void updateTheFlag(AppInfo values, int blocked) {

        ContentValues cv = new ContentValues();
        cv.put(IS_ALLOWED, blocked); //These Fields should be your String values of actual column names
        db.update(USERXPACKAGE, cv, PACKAGE_NAME + " = '" + values.getPname() + "'", null);


    }

    public boolean removeTheCompleteInfo(int sender_id) {
     //These Fields should be your String values of actual column names
      return db.delete(MESSAGE_TABLE, SENDER_ID + "=" + sender_id, null) > 0;

    }

    public void setClearDataBase() {
        db.delete(USERXPACKAGE, null ,null);
        db.delete(USER_TABLE, null,null);
        db.delete(MESSAGE_TABLE, null,null);

    }
}
