package com.effone.notificationmummy.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.ContentObserver;

import java.util.Calendar;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.RelativeLayout;
import android.widget.TextView;


import com.effone.notificationmummy.GalleryActivity;
import com.effone.notificationmummy.R;
import com.effone.notificationmummy.adapter.MessageInfoAdapter;
import com.effone.notificationmummy.adapter.SectionListDataAdapter;
import com.effone.notificationmummy.database.DataBaseHandler;
import com.effone.notificationmummy.model.ImageInfo;
import com.effone.notificationmummy.model.MessageInfo;
import com.effone.notificationmummy.model.MessageUnreadInfo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import static com.effone.notificationmummy.common.CustomContentProvider.URI_PERSONS;

public class MessagesFragment extends Fragment implements View.OnClickListener {
    private ImageView mIvBackArrow;
    private TextView mTvTitle, mTvMore;
    private String title;
    private boolean setVisible;
    private ListView mRvMessage;
    private MessageUnreadInfo messageUnreadInfo;
    private DataBaseHandler mDataBaseHandler;
    private LinearLayout mLvMediaHeader;
    private SearchView sv_search_view;

    private RelativeLayout mRlHeader;
    private RecyclerView mRecyclerView;

    private Toolbar mToolBar;
    private View mView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mesages, container, false);

        mDataBaseHandler = DataBaseHandler.getInstance(getActivity());
        setHasOptionsMenu(true);
        declaration(view);
        return view;
    }

    TextView mTvErrorMessage;
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }
    private void declaration(View view) {
        mToolBar=view.findViewById(R.id.tb_toolbar);
        mToolBar.setTitle(messageUnreadInfo.getSender_name());
        mToolBar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolBar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                getActivity().onBackPressed();
            }
        });
        mView=view.findViewById(R.id.View);
        Drawable backArrow = getResources().getDrawable(R.drawable.ic_back_arrow);
        backArrow.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(backArrow);
        mTvMore = view.findViewById(R.id.tv_more);
        mTvMore.setOnClickListener(this);
        mLvMediaHeader=view.findViewById(R.id.lv_layout);
        mRlHeader = view.findViewById(R.id.relativeLayout);
        mRvMessage = view.findViewById(R.id.rv_message);

                mTvErrorMessage=view.findViewById(R.id.tv_noMedia);
        mRecyclerView=view.findViewById(R.id.rv_media);
        fetchTheMessage(messageUnreadInfo.getSender_id());

        fetchTheTopHeader(messageUnreadInfo.getSender_id());
    }

    private void fetchTheTopHeader(int sender_id) {
        ArrayList<MessageInfo> messageImages=mDataBaseHandler.fetchTheRecords(sender_id);
        ArrayList<ImageInfo> imageDetails = new ArrayList<>();
        File path = null;
        switch (messageImages.get(0).getMessage_type()){
            case "Photo":
                path= new File(Environment.getExternalStorageDirectory(), "WhatsApp/Media/WhatsApp Images");
            break;
            case "Video":
                path= new File(Environment.getExternalStorageDirectory(), "WhatsApp/Media/WhatsApp Video.");
                break;
            case "GIF":
                path = new File(Environment.getExternalStorageDirectory(), "WhatsApp/Media/WhatsApp Animated Gifs");
                break;
            case "Audio":
                path=new File(Environment.getExternalStorageDirectory(),"WhatsApp/Media/WhatsApp Audio");
                break;
            default:
                path=null;
                break;

        }
     /*   if(messageImages.get(0).getMessage_type().equals("Photo")) {
            path= new File(Environment.getExternalStorageDirectory(), "WhatsApp/Media/WhatsApp Images");
        }else if{

        }*/
        String[] fileNames = new String[0];
        if(path!= null) {
            if (path.exists()) {
                fileNames = path.list();
            }
        }
        try {
            for (int i = 0; i < fileNames.length; i++) {
                try {
                    File file = new File(path.getPath() + "/" + fileNames[i]);
                    ImageInfo imageInfo = new ImageInfo();
                    imageInfo.setPath(path.getPath() + "/" + fileNames[i]);
                    imageInfo.setTimestamp(file.lastModified());
                        if (!fileNames[i].equals("Sent"))
                            imageDetails.add(imageInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Log.e("fileNames", e.getMessage());
        }
        ArrayList<ImageInfo> near = new ArrayList<>();
       near= getNearestDate(imageDetails,messageImages);
       if(near.size()!=0) {

           Set<ImageInfo> imafeInfo = new TreeSet<ImageInfo>(new TimeComapare());
           imafeInfo.addAll(near);
           final ArrayList<ImageInfo> newList = new ArrayList<ImageInfo>(imafeInfo);
           SectionListDataAdapter sectionListDataAdapter = new SectionListDataAdapter(getActivity(), newList,true,messageImages.get(0).getMessage_type());
           mRecyclerView.setAdapter(sectionListDataAdapter);
           mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

       }else{
           mRecyclerView.setVisibility(View.GONE);
           if(!messageImages.get(0).getMessage_type().equals("text")) {
               mTvErrorMessage.setVisibility(View.VISIBLE);
               mTvErrorMessage.setTextSize(18);
               mTvErrorMessage.setText("Oops! Unable to fetch "+messageImages.get(0).getSender_message());
           }else {
           mView.setVisibility(View.GONE);
         mLvMediaHeader.setVisibility(View.GONE);
           }
       }
    }


    public static class TimeComapare implements Comparator<ImageInfo>
    {
        public int compare(ImageInfo c1, ImageInfo c2)
        {
            return c1.getPath().compareTo(c2.getPath());
        }
    }



    MessageInfoAdapter messageInfoAdapter;
    ArrayList<MessageInfo> messageInfos;

    private void fetchTheMessage(int sender_id) {
        messageInfos = mDataBaseHandler.fetchAllTheRecords(sender_id);
        mDataBaseHandler.updateTheRecord(sender_id);
        messageInfoAdapter = new MessageInfoAdapter(getActivity(), messageInfos);
        mRvMessage.setAdapter(messageInfoAdapter);
        mRvMessage.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        mutliChoiceMode();



    }
    boolean selectAll=false;

    private void mutliChoiceMode() {
        mRvMessage.setMultiChoiceModeListener(
                new AbsListView.MultiChoiceModeListener() {
                    @Override
                    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                        try {
                            final int checkedCount = mRvMessage.getCheckedItemCount();
                            mode.setTitle(String.format(" %d", checkedCount));
                            messageInfoAdapter.select(position, checked);
                            messageInfoAdapter.notifyDataSetChanged();
                            View child = mRvMessage.getChildAt(position);
                          //  child.setBackgroundResource(checked ? R.color.holo_gray_light : android.R.color.transparent);

                        }catch (Exception e){

                        }
                    }

                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    //    mRlHeader.setVisibility(View.GONE);
                        mode.getMenuInflater().inflate(R.menu.activity_main, menu);
                        messageInfoAdapter.clearSelection();
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
                                messageInfoAdapter.removeSelected();
                                mode.finish();
                                return true;
                            case R.id.selectAll:
                                selectAll=true;
                                mode.setTitle(String.format("%d", messageInfos.size()));
                                for (int i = 0; i < messageInfos.size(); i++) {
                                    messageInfoAdapter.select(i, true);
                                    messageInfoAdapter.notifyDataSetChanged();
                                }

                                return true;

                            default:
                                return false;
                        }

                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {

                            int count = mRvMessage.getChildCount();
                            for (int i = 0; i < count; i++) {
                                View view = mRvMessage.getChildAt(i);
                                if (view != null)
                                    view.setBackgroundResource(android.R.color.transparent);
                            }
                         //   mRlHeader.setVisibility(View.VISIBLE);
                        }

                }
        );
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_backArrow:
                getActivity().onBackPressed();
                break;
            case R.id.tv_more:
                Intent timeStamp = new Intent(getActivity(), GalleryActivity.class);
                timeStamp.putExtra("BODY", messageUnreadInfo.getSender_id());
                startActivity(timeStamp);
                break;
        }
    }


    public void setTitle(MessageUnreadInfo clickedApp) {
        this.title = clickedApp.getSender_name();
        this.messageUnreadInfo = clickedApp;
    }


    @TargetApi(Build.VERSION_CODES.N)
    public static ArrayList<ImageInfo> getNearestDate(ArrayList<ImageInfo> imageInfo, ArrayList<MessageInfo> selectedImageDate) {
        ArrayList<ImageInfo> nearset = new ArrayList<>();
        try {
            for (int j = 0; j < selectedImageDate.size(); j++) {

                for (int i = 0; i < imageInfo.size(); i++) {

                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String[] date = formatter.format(new Date(Long.parseLong(selectedImageDate.get(j).getSender_message_time_stamp()))).split(" GMT");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date before =
                            sdf.parse(formatter.format(new Date(imageInfo.get(i).getTimestamp())).split(" GMT")[0]);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(before);
                    calendar.add(Calendar.MINUTE, 1);
                    before = calendar.getTime();
                    calendar.add(Calendar.MINUTE, -2);
                    Date after = calendar.getTime();


                    Date toCheck = sdf.parse(date[0]);
//is toCheck between the two?
                    boolean isAvailable = (after.getTime() <= toCheck.getTime()) && (before.getTime() >= toCheck.getTime());
                    if (isAvailable) {
                        nearset.add(imageInfo.get(i));
                    }
                }
            }

        } catch (Exception e) {
            e.getMessage();
        }
        return nearset;
    }

    private MyDataObserver myObserver;

    @Override
    public void onResume() {
        super.onResume();
        myObserver = new MyDataObserver(new Handler());
        getActivity().getContentResolver().registerContentObserver(URI_PERSONS, true, myObserver);
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
            fetchTheMessage(messageUnreadInfo.getSender_id());
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        getActivity().getContentResolver().unregisterContentObserver(myObserver);
    }

}
