package com.effone.notificationmummy.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.effone.notificationmummy.R;
import com.effone.notificationmummy.adapter.VideoAdapter;
import com.effone.notificationmummy.database.DataBaseHandler;
import com.effone.notificationmummy.model.ImageInfo;
import com.effone.notificationmummy.model.MessageInfo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class VideoFragment extends Fragment implements View.OnClickListener {

    DataBaseHandler mDataBaseHandler;
    private String timestamp;
    private TextView mTvTitle;
    private ImageView mIvBackArrow;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ciew= inflater.inflate(R.layout.fragment_video, container, false);

        mIvBackArrow=ciew.findViewById(R.id.img_backArrow);
        mIvBackArrow.setOnClickListener(this);
        mTvTitle=ciew.findViewById(R.id.title);
        mTvTitle.setText("Video");
        mDataBaseHandler= DataBaseHandler.getInstance(getActivity());
        GridView gridView = (GridView) ciew.findViewById(R.id.list_notification_items);
        String selection= MediaStore.Video.Media.DATA +" like?";
        String[] selectionArgs=new String[]{"%WhatsApp Video%"};
        Uri uri= MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String condition=MediaStore.Video.Media.DATA +" like?";
        String[] selectionArguments=new String[]{"%WhatsApp Video%"};
        String sortOrder = MediaStore.Video.Media.DATE_TAKEN + " DESC";
        String[] projection = { MediaStore.Video.Media._ID, MediaStore.Video.Media.BUCKET_ID,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,MediaStore.Video.Media.DATA,MediaStore.Video.Media.DATE_ADDED };
        Cursor cursor = getActivity().getContentResolver().query(uri,projection, condition, selectionArguments, sortOrder);

        int pathColumn=cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);

        ArrayList<ImageInfo> videosList=new ArrayList<>();
        if(cursor!=null){
            while(cursor.moveToNext()){
                String filePath=cursor.getString(pathColumn);
                File file=new File(filePath);
                long value=file.lastModified();
                ImageInfo imageInfo = new ImageInfo();
                imageInfo.setPath(filePath);
                imageInfo.setTimestamp(value);
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String[] imageDate = formatter.format(new Date(file.lastModified())).split(" ");
                String[] selectedImageDate = formatter.format(new Date(Long.parseLong(timestamp))).split(" ");
                if (imageDate[0].equals(selectedImageDate[0]))
                    if (!file.getName().equals("Sent"))
                videosList.add(imageInfo);
            }
        }

        ArrayList<ImageInfo> near = new ArrayList<>();
        try {
            near = getNearestDate(videosList, timestamp);
        } catch (Exception e) {
            Log.e("VideoActivity", e.getMessage());
        }

        gridView.setAdapter(new VideoAdapter(getActivity(), near));
        return ciew;

    }
    @TargetApi(Build.VERSION_CODES.N)
    public static ArrayList<ImageInfo> getNearestDate(ArrayList<ImageInfo> imageInfo, String selectedImageDate) {
        ArrayList<ImageInfo> nearset = new ArrayList<>();
        Date selectedImage = new Date(Long.parseLong(selectedImageDate));
        try {

            for (int i = 0; i < imageInfo.size(); i++) {

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String[] date=formatter.format(new Date(Long.parseLong(selectedImageDate))).split(" GMT");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date before =
                        sdf.parse(formatter.format(new Date(imageInfo.get(i).getTimestamp())).split(" GMT")[0]);
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                calendar.setTime(before);
                calendar.add(java.util.Calendar.MINUTE, 1);
                before=calendar.getTime();
                calendar.add(java.util.Calendar.MINUTE ,-2);
                Date  after=calendar.getTime();


                Date toCheck =  sdf.parse(date[0]);
//is toCheck between the two?
                boolean isAvailable = (after.getTime() <= toCheck.getTime()) && (before.getTime() >= toCheck.getTime());
                if (isAvailable) {
                    nearset.add(imageInfo.get(i));
                }
            }

        }catch (Exception e){
            e.getMessage();
        }



        return nearset;
    }

    public void sendTimeStamp(String sender_message_time_stamp) {
        this.timestamp=sender_message_time_stamp;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_backArrow:
                getActivity().onBackPressed();
                break;
        }
    }
}
