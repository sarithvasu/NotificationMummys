package com.effone.notificationmummy.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.effone.notificationmummy.R;
import com.effone.notificationmummy.model.ImageInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by sumanth.peddinti on 5/15/2018.
 */

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    private ArrayList<ImageInfo> itemsList;
    private Context mContext;
    private Boolean showText;
    private String messageType;

    public SectionListDataAdapter(Context context, ArrayList<ImageInfo> itemsList, boolean b) {
        this.itemsList = itemsList;
        this.mContext = context;
        showText = b;
    }

    public SectionListDataAdapter(Context context, ArrayList<ImageInfo> newList, boolean b, String message_type) {
        this.itemsList = newList;
        this.mContext = context;
        showText = b;
        this.messageType=message_type;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, final int i) {

        final ImageInfo singleItem = itemsList.get(i);


        File ifile = new File(singleItem.getPath());
        if(messageType.equals("Video")){
            holder.itemImage.setBackground(mContext.getResources().getDrawable(R.drawable.ic_video_call));
        }else
        if(messageType.equals("Audio")) {
            holder.itemImage.setBackground(mContext.getResources().getDrawable(R.drawable.ic_music));
        } else{
            holder.itemImage.setImageURI(Uri.parse(singleItem.getPath()));
   /*         Glide.with(mContext)
                    .load(singleItem.getPath())
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .error(R.drawable.android)
                    .into(holder.itemImage);*/
        }
        if (!showText)
            holder.tvTitle.setText(ifile.getName());
        else
            holder.tvTitle.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //shareImage(mContext, singleItem.getPath());
                if(messageType.equals("Video")){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(singleItem.getPath()));
                    intent.setDataAndType(Uri.parse(singleItem.getPath()), "video/mp4");
                    mContext.startActivity(intent);
                }else
               if(messageType.equals("Audio")){
                   Intent intent = new Intent();
                   intent.setAction(android.content.Intent.ACTION_VIEW);
                   File file = new File(singleItem.getPath());
                   intent.setDataAndType(Uri.fromFile(file), "audio/*");
                   mContext.startActivity(intent);
               }else {
                   Log.e("imagePath", singleItem.getPath());
                   Intent intent = new Intent();
                   intent.setAction(Intent.ACTION_VIEW);
                   intent.setDataAndType(Uri.fromFile(new File(singleItem.getPath())), "image/gif");
         /*       intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);*/
                   mContext.startActivity(intent);
               }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvTitle;

        protected ImageView itemImage;


        public SingleItemRowHolder(View view) {
            super(view);


            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);
            this.tvTitle = view.findViewById(R.id.tv_title);


        }

    }


    public static void shareImage(Context context, String pathToImage) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(pathToImage)));  //optional//use this when you want to send an image
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent, "send"));
    }

}