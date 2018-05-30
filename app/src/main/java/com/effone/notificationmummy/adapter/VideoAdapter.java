package com.effone.notificationmummy.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.effone.notificationmummy.R;
import com.effone.notificationmummy.model.ImageInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.media.ThumbnailUtils.createVideoThumbnail;

/**
 * Created by sumanth.peddinti on 5/3/2018.
 */

public class VideoAdapter extends BaseAdapter {
    private Context mContext;
    private List<ImageInfo> imageInfoList;
    public VideoAdapter(Context context, ArrayList<ImageInfo> near) {
        this.mContext=context;
        this.imageInfoList=near;

    }

    @Override
    public int getCount() {
        return imageInfoList.size();
    }

    @Override
    public Object getItem(int i) {
        return imageInfoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        ViewHolders holder;
     ImageView imageView = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_video, null);
            holder = new ViewHolders();
            holder.fileName= view.findViewById(R.id.textView);
            holder.thumbNail=view.findViewById(R.id.imageView);
            view.setTag(holder);

        } else {
            holder = (ViewHolders) view.getTag();
        }
        try {

            Bitmap ThumbnailUtils=createVideoThumbnail(imageInfoList.get(i).getPath(), MediaStore.Video.Thumbnails.MINI_KIND);
            holder.thumbNail.setImageBitmap(ThumbnailUtils);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        File file=new File(imageInfoList.get(i).getPath());

        holder.fileName.setText(file.getName());
        return view;
    }
    private static class ViewHolders {
         TextView fileName;
         ImageView thumbNail;
    }
    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
}
