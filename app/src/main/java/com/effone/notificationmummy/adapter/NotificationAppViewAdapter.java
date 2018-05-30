package com.effone.notificationmummy.adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.effone.notificationmummy.R;
import com.effone.notificationmummy.common.AppInfo;
import com.effone.notificationmummy.model.ListData;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sumanth.peddinti on 5/24/2018.
 */


public class NotificationAppViewAdapter extends ArrayAdapter<AppInfo> {
    List<AppInfo> notificationCounts;
    private Context context;
    public NotificationAppViewAdapter(Context context, ArrayList<AppInfo> objects) {
        super(context, 0, objects);
        this.context=context;
        mSelectedItemsIds = new SparseBooleanArray();
        this.notificationCounts=objects;

    }


    public void toggleSelection(int position) {
        selectView(position-1, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }
    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
    private SparseBooleanArray mSelectedItemsIds;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_element, null);
            holder = new ViewHolder();
            holder.appName = (TextView) view.findViewById(R.id.app_name);
            holder.appCount = (TextView) view.findViewById(R.id.app_count);
            holder.imageView = (ImageView) view.findViewById(R.id.app_image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        PackageManager packageManager = getContext().getPackageManager();
        AppInfo nv = this.getItem(position);
        String str_appName = null;
        Drawable icon = null;
        ApplicationInfo appInfo = null;
        if (str_appName != null) holder.appName.setText(str_appName);
        else holder.appName.setText(nv.getAppname());
        holder.appCount.setText(""+nv.getCount());
        byte[] bitmapdata ;
        try {
            icon = packageManager.getApplicationIcon(nv.getPname());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = drawableToBitmap(icon);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            bitmapdata = stream.toByteArray();



       /*Glide.clear(holder.imageView);
        Glide.with(context)
                .load(bitmapdata)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.imageView);*/
        holder.imageView.setImageDrawable(icon);
        return view;
    }

    private static class ViewHolder {
        TextView appName;
        TextView appCount;
        TextView msg;
        ImageView imageView;
    }

    public Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }


        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 96; // Replaced the 1 by a 96
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 96; // Replaced the 1 by a 96

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }



}
