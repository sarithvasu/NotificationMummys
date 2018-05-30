package com.effone.notificationmummy.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;


import com.effone.notificationmummy.Activity.FaqActivity;
import com.effone.notificationmummy.Activity.SettingActivity;
import com.effone.notificationmummy.R;
import com.effone.notificationmummy.adapter.ExpandableListAdapter;
import com.effone.notificationmummy.common.AppInfo;
import com.effone.notificationmummy.database.DataBaseHandler;
import com.effone.notificationmummy.model.ListData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.effone.notificationmummy.common.CustomContentProvider.URI_PERSONS;


public class RecieveNotificaitionFragment extends Fragment {
    private ExpandableListView mListView;
    private DataBaseHandler mDataBaseHandler;
    private Toolbar mToolBar;

    private TextView mTitle,mSubTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recieve_notificaition, container, false);
        mToolBar = view.findViewById(R.id.tb_toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolBar);
     setHasOptionsMenu(true);
        declaration(view);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.blockedAll) {


            Intent intent=new Intent(getActivity(),SettingActivity.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.faq){
            Intent intent=new Intent(getActivity(),FaqActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void declaration(View view) {
        mToolBar = view.findViewById(R.id.tb_toolbar);
        mToolBar.setTitle(R.string.app_name);
        mToolBar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        mListView = view.findViewById(R.id.lv_listView);
        mDataBaseHandler = DataBaseHandler.getInstance(getActivity());
        mTitle = view.findViewById(R.id.tv_layout);
        mSubTitle=view.findViewById(R.id.tv_subtitle);
        mSubTitle.setVisibility(View.GONE);
        mTitle.setVisibility(View.GONE);
        ;
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getAdapter().getItem(i) instanceof AppInfo) {
                    AppInfo appInfo = (AppInfo) adapterView.getAdapter().getItem(i);
                    IndivalMessageFragment indivalMessageFragment = new IndivalMessageFragment();
                    indivalMessageFragment.setTitle(appInfo);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.fl_bottom, indivalMessageFragment).addToBackStack(null).commit();
                }
            }
        });

    }

    ExpandableListAdapter.MyInterface myInterface;
    ArrayList<ListData> mListData;

    private void fetchInfo() {
        new insertDataIntoAdapter().execute("");
        myInterface = new ExpandableListAdapter.MyInterface() {
            @Override
            public void someEvent(int value) {
                new insertDataIntoAdapter().execute("");
            }

        };

    }


    public class MyDataObserver extends ContentObserver {
        public MyDataObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            this.onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            fetchInfo();
        }

    }

    MyDataObserver myDataObserver;

    @Override
    public void onResume() {
        super.onResume();

        myDataObserver = new MyDataObserver(new Handler());
        getActivity().getContentResolver().registerContentObserver(URI_PERSONS, true, myDataObserver);
        fetchInfo();


    }


    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getActivity().getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }


    @Override
    public void onPause() {
        super.onPause();
        getActivity().getContentResolver().unregisterContentObserver(myDataObserver);
    }
    public static String allowedTitle="Allowed App (S)";
    public static String denyTitle="Denied App (S)";


    private class insertDataIntoAdapter extends AsyncTask<String, Void, HashMap<String, List<AppInfo>>> {

        @Override
        protected HashMap<String, List<AppInfo>> doInBackground(String... params) {

            ArrayList<String> names = new ArrayList<String>();
            HashMap<String, List<AppInfo>> hashMap = new HashMap<String, List<AppInfo>>();
            names.add(allowedTitle);
            names.add(denyTitle);
            try {
                ArrayList<AppInfo> child1 = mDataBaseHandler.getTheEnabledList(1);
                ArrayList<AppInfo> child2 = mDataBaseHandler.getTheEnabledList(0);

                hashMap.put(names.get(0), child1);
                hashMap.put(names.get(1), child2);
            }catch (Exception e){
                Log.e("DataBase",e.getMessage());
            }
            return hashMap;
        }

        @Override
        protected void onPostExecute(final HashMap<String, List<AppInfo>> result) {
            final ArrayList<String> names = new ArrayList<String>();
            names.add(allowedTitle);
            names.add(denyTitle);

            if (result.get(allowedTitle).size() != 0 || result.get(denyTitle).size() != 0) {
                mListView.setVisibility(View.VISIBLE);
                ExpandableListAdapter notificationAppViewAdapter = new ExpandableListAdapter(getActivity(), result, names, myInterface);
        /*NotificationAppViewAdapter notificationAppViewAdapter = new NotificationAppViewAdapter(getActivity(), mListData);*/
                mListView.setAdapter(notificationAppViewAdapter);
            } else {
                if(appInstalledOrNot("com.whatsapp")) {
                    ArrayList<AppInfo> deniedApps = new ArrayList<>();
                    ArrayList<AppInfo> allowedApps = new ArrayList<>();
                    AppInfo appInfo = new AppInfo();
                    appInfo.setAppname("WhatsApp");
                    appInfo.setPname("com.whatsapp");
                    appInfo.setCount(0);
                    allowedApps.add(appInfo);
                    result.put(allowedTitle, allowedApps);
                    result.put(denyTitle, deniedApps);
                    ExpandableListAdapter notificationAppViewAdapter = new ExpandableListAdapter(getActivity(), result, names, myInterface);
        /*NotificationAppViewAdapter notificationAppViewAdapter = new NotificationAppViewAdapter(getActivity(), mListData);*/
                    mListView.setAdapter(notificationAppViewAdapter);
                }else{
                    mListView.setVisibility(View.GONE);
                    mTitle.setVisibility(View.VISIBLE);
                    mSubTitle.setVisibility(View.VISIBLE);
                    mTitle.setText("\n" +
                            "Welcome To WhatsUp Mommy !"
                           );
                    mSubTitle.setText( "Your app is now activated. Please wait for an alert to arrive.");
                }
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
