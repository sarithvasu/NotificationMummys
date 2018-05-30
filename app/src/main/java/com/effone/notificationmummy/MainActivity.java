package com.effone.notificationmummy;

import android.Manifest;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.effone.notificationmummy.Service.NotificationListener;
import com.effone.notificationmummy.common.NotificationAccessDialogFragment;
import com.effone.notificationmummy.database.DataBaseHandler;
import com.effone.notificationmummy.fragments.RecieveNotificaitionFragment;

public class MainActivity extends AppCompatActivity implements NotificationAccessDialogFragment.NotificationAccessDialogFragmentListener {
    private DataBaseHandler dataBaseHandler;
    private Context mContext;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private final  int MY_PERMISSIONS_REQUEST_READ_MEDIA=101;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
          /*  appName.add("com.whatsapp");*/
        mContext = MainActivity.this;
        dataBaseHandler = DataBaseHandler.getInstance(mContext);
        if (!NotificationListener.isNotificationAccessEnabled) {
            DialogFragment dialog = new NotificationAccessDialogFragment();
            dialog.show(getSupportFragmentManager(), "notificationAccessDialog");
        }
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
        }
        if(savedInstanceState== null) {
            RecieveNotificaitionFragment appDetailFragment = new RecieveNotificaitionFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fl_bottom, appDetailFragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Something went wrong. Enable manually", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_MEDIA:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }


    @Override
    public void onBackPressed() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if (getSupportFragmentManager().getBackStackEntryCount() >1) {
            super.onBackPressed();
            FragmentManager manager = getSupportFragmentManager();
            if (manager != null) {
                manager.findFragmentById(R.id.fl_bottom).onResume();
            }
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            builder.setCancelable(false);
            builder.setMessage("Are you sure, you want to close?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.show();
            TextView messageText = (TextView)alert.findViewById(android.R.id.message);
            messageText.setGravity(Gravity.CENTER);
            alert.show();

        }

    }
}
