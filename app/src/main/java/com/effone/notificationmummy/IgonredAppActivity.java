package com.effone.notificationmummy;

import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;


import com.effone.notificationmummy.adapter.ApplicationAdapter;
import com.effone.notificationmummy.model.ApplicationInform;
import com.effone.notificationmummy.model.ListData;
import com.effone.notificationmummy.model.section;

import java.util.ArrayList;
import java.util.List;

public class IgonredAppActivity extends AppCompatActivity {
    private PackageManager packageManager = null;
    private List<ListData> applist = null;
    private ApplicationAdapter listadaptor = null;
    private ExpandableListView mListView;
    private Toolbar mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_igonred_app);
        mTitle=findViewById(R.id.tb_toolbar);
        mTitle.setTitle("App List");
        setSupportActionBar(mTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTitle.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
               onBackPressed();
            }
        });

        packageManager = getPackageManager();
        mListView=findViewById(R.id.app_list);

    }



}
