package com.effone.notificationmummy.Activity;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.effone.notificationmummy.R;
import com.effone.notificationmummy.database.DataBaseHandler;

public class SettingActivity extends AppCompatActivity {
    private TextView mTvSetting;
    private Switch mSw_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable backArrow = getResources().getDrawable(R.drawable.ic_back_arrow);
        backArrow.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        toolbar.setTitle("Settings");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                onBackPressed();
            }
        });
        mTvSetting = findViewById(R.id.clear_data);
        mSw_bar=findViewById(R.id.sw_clearing);
        mSw_bar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                               @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                                               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                                                   if (isChecked) {
                                                       AlertDialog alertDialog = new AlertDialog.Builder(SettingActivity.this).create();
                                                       alertDialog.setTitle("Alert");
                                                       alertDialog.setMessage("This will clear all the messages.");
                                                       alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                                               new DialogInterface.OnClickListener() {
                                                                   public void onClick(DialogInterface dialog, int which) {

                                                                       DataBaseHandler dataBaseHandler = DataBaseHandler.getInstance(SettingActivity.this);
                                                                       dataBaseHandler.setClearDataBase();
                                                                       dialog.dismiss();
                                                                   }
                                                               });
                                                       alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                                                               new DialogInterface.OnClickListener() {
                                                                   public void onClick(DialogInterface dialog, int which) {

                                                                       dialog.dismiss();
                                                                   }
                                                               });
                                                       alertDialog.show();

                                                   }
                                               }
                                           });


    }

}
