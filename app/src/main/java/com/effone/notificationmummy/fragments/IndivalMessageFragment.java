package com.effone.notificationmummy.fragments;

import android.database.ContentObserver;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;


import com.effone.notificationmummy.R;
import com.effone.notificationmummy.adapter.MessageUnreadInfoAdapter;
import com.effone.notificationmummy.common.AppInfo;
import com.effone.notificationmummy.database.DataBaseHandler;
import com.effone.notificationmummy.model.MessageUnreadInfo;

import java.util.ArrayList;

import static com.effone.notificationmummy.common.CustomContentProvider.URI_PERSONS;


public class IndivalMessageFragment extends Fragment implements SearchView.OnQueryTextListener {
    private ListView mLvAppDetails;
    private Toolbar mTbHeader;
    private     android.widget.Filter filter;
    private DataBaseHandler mDataBasehandler;
    private AppInfo mAppInfo;
    private MessageUnreadInfoAdapter messageUnreadInfoAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_indivual_message, container, false);
        setHasOptionsMenu(true);
        declarations(view);
        return view;
    }



    private void declarations(View view) {
        mDataBasehandler=DataBaseHandler.getInstance(getActivity());
        mTbHeader=view.findViewById(R.id.tb_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mTbHeader);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable backArrow = getResources().getDrawable(R.drawable.ic_back_arrow);
        backArrow.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(backArrow);
        mTbHeader.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        mLvAppDetails=view.findViewById(R.id.listview);
        mLvAppDetails.setTextFilterEnabled(false);
        mLvAppDetails.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        mTbHeader.setTitle(mAppInfo.getAppname());
        mTbHeader.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                getActivity().onBackPressed();
            }
        });
        mutliChoiceMode();
//        filter = sectionedBaseAdapter.getFilter();
        //gettingDataFromDB();
    }

    boolean selectAll=false;

    private void mutliChoiceMode() {
        mLvAppDetails.setMultiChoiceModeListener(
                new AbsListView.MultiChoiceModeListener() {
                    @Override
                    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                        try {
                            final int checkedCount = mLvAppDetails.getCheckedItemCount();
                            mode.setTitle(String.format(" %d", checkedCount));
                            messageUnreadInfoAdapter.select(position, checked);
                            messageUnreadInfoAdapter.notifyDataSetChanged();

                        }catch (Exception e){

                        }
                    }

                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    //   mTbHeader.setVisibility(View.GONE);
                        mode.getMenuInflater().inflate(R.menu.activity_main, menu);
                        messageUnreadInfoAdapter.clearSelection();
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                messageUnreadInfoAdapter.removeSelected();
                                mode.finish();
                                return true;
                            case R.id.selectAll:
                                selectAll=true;
                                mode.setTitle(String.format("%d", messageUnreadInfos.size()));
                                for (int i = 0; i < messageUnreadInfos.size(); i++) {
                                    messageUnreadInfoAdapter.select(i, true);
                                    messageUnreadInfoAdapter.notifyDataSetChanged();
                                }

                                return true;

                            default:
                                return false;
                        }

                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                      //  mTbHeader.setVisibility(View.VISIBLE);
                        int count = mLvAppDetails.getChildCount();
                        for (int i = 0; i < count; i++) {
                            View view = mLvAppDetails.getChildAt(i);
                            if (view != null)
                                view.setBackgroundResource(android.R.color.transparent);
                        }
                        messageUnreadInfoAdapter.notifyDataSetChanged();

                    }

                }
        );
    }



    ArrayList<MessageUnreadInfo>  messageUnreadInfos;

    private void gettingDataFromDB() {
        messageUnreadInfos= mDataBasehandler.fetchTheLastRecord(mAppInfo.getAppname());
        if (getActivity()!=null) {
            messageUnreadInfoAdapter = new MessageUnreadInfoAdapter(getActivity(), messageUnreadInfos);
            mLvAppDetails.setAdapter(messageUnreadInfoAdapter);
        }
        mLvAppDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MessageUnreadInfo clickedApp = (MessageUnreadInfo) adapterView.getAdapter().getItem(i);
                MessagesFragment appDetailFragment = new MessagesFragment();
                appDetailFragment.setTitle(clickedApp);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.fl_bottom, appDetailFragment).addToBackStack(null).commit();

            }
        });
        filter=messageUnreadInfoAdapter.getFilter();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_search) {
            /*Intent intent=new Intent(this,AppSearchDetailsActivity.class);
            intent.putExtra(AppDetail.EXTRA_PACKAGENAME,packageName);
            startActivity(intent);*/
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) searchItem.getActionView();
        SearchView.SearchAutoComplete theTextArea =searchView.findViewById(R.id.search_src_text);
        theTextArea.setTextColor(Color.WHITE);
        theTextArea.setHintTextColor(Color.GRAY);// I used the explicit layout ID of searchview's ImageView
        theTextArea.setTextSize(18);


        searchView.setQueryHint("Search People...");
        searchView.setOnQueryTextListener(this);
       searchView.setIconified(true);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filter.filter(newText);
        return false;
    }
    private MyDataObserver myObserver;



    @Override
    public void onResume() {
        super.onResume();
        gettingDataFromDB();
        myObserver = new MyDataObserver(new Handler());
        getActivity().getContentResolver().registerContentObserver(URI_PERSONS, true, myObserver);
    }

    public void setTitle(AppInfo appInfo) {
        this.mAppInfo=appInfo;
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
            gettingDataFromDB();
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        getActivity().getContentResolver().unregisterContentObserver(myObserver);
    }



}
