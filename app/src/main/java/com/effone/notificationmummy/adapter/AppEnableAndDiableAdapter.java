package com.effone.notificationmummy.adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.effone.notificationmummy.R;
import com.effone.notificationmummy.common.AppInfo;
import com.effone.notificationmummy.database.DataBaseHandler;
import com.effone.notificationmummy.fragments.IndivalMessageFragment;
import com.effone.notificationmummy.model.ListData;
import com.effone.notificationmummy.model.section;

import java.util.List;

/**
 * Created by sumanth.peddinti on 5/25/2018.
 */

public class AppEnableAndDiableAdapter extends BaseAdapter {
    private static final int VIEW_TYPE_NONE = 0;
    private static final int VIEW_TYPE_SECTION = 1;
    private static final int VIEW_TYPE_ITEM = 2;
    private SparseBooleanArray mSelectedItemsIds;

    private LayoutInflater layoutInflater;
    private List<ListData> dataList;
    private List<ListData> mDataList;
    private Context context;
    private DataBaseHandler mDataBaseHanfler;

    public AppEnableAndDiableAdapter(Context context, List<ListData> dataList) {
        this.context=context;
        this.dataList = dataList;
        this.mDataList = dataList;
        this.layoutInflater = LayoutInflater.from(context);
        mDataBaseHanfler=DataBaseHandler.getInstance(context);
        mSelectedItemsIds = new SparseBooleanArray();
    }
    @Override
    public int getCount() {
        return dataList != null ? dataList.size() : 0;
    }

    @Override
    public ListData getItem(int i) {
        if (dataList.isEmpty()) {
            return null;
        } else {
            return dataList.get(i);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (getItemViewType(i) == VIEW_TYPE_SECTION) {
            return getSectionView(i, view, viewGroup);
        } else if (getItemViewType(i) == VIEW_TYPE_ITEM) {
            return getItemView(i, view, viewGroup);
        }
        return null;
    }

    private View getSectionView(int i, View view, ViewGroup viewGroup) {
        View convertView=view;
        SectionViewHolder sectionViewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_list_section, viewGroup, false);
            sectionViewHolder = new SectionViewHolder(convertView);
            convertView.setTag(sectionViewHolder);
        } else {
            sectionViewHolder = (SectionViewHolder) convertView.getTag();
        }
        sectionViewHolder.setTitle(((section) getItem(i)).getName());
        return convertView;
    }

    @NonNull
    private View getItemView(final int position, View convertView, ViewGroup parent) {
        ItemViewHolder itemViewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_element, parent, false);
            itemViewHolder = new ItemViewHolder(convertView);
            convertView.setTag(itemViewHolder);
        } else {
            itemViewHolder = (ItemViewHolder) convertView.getTag();
        }
        PackageManager packageManager = context.getPackageManager();
        final AppInfo nv = (AppInfo) this.getItem(position);
        String str_appName = null;
        Drawable icon = null;
        final ApplicationInfo appInfo = null;
        if (str_appName != null) itemViewHolder.appName.setText(str_appName);
        else itemViewHolder.appName.setText(nv.getAppname());
        itemViewHolder.appCount.setText(""+nv.getCount());
        byte[] bitmapdata ;
        try {
            icon = packageManager.getApplicationIcon(nv.getPname());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        itemViewHolder.list_notification_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AppInfo appInfo = (AppInfo) getItem(position);
                IndivalMessageFragment indivalMessageFragment = new IndivalMessageFragment();
                indivalMessageFragment.setTitle(appInfo);
                FragmentTransaction ft =((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                ft.add(R.id.fl_bottom, indivalMessageFragment).addToBackStack(null).commit();
              //  Toast.makeText(context,"Suamnth",Toast.LENGTH_SHORT).show();
            }
        });

        itemViewHolder.mSwBar.setChecked(!nv.isChecked());
        itemViewHolder.mSwBar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
               /* appName.add(nv.getPname());*/
                DataBaseHandler dataBaseHandler=DataBaseHandler.getInstance(context);
               // dataBaseHandler.updateTheBlock(nv.getPname(),isChecked);
            }
        });


       /*Glide.clear(holder.imageView);
        Glide.with(context)
                .load(bitmapdata)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.imageView);*/
        itemViewHolder.imageView.setImageDrawable(icon);

        return convertView;
    }


    @Override
    public int getItemViewType(int position) {
        if (getCount() > 0) {
            ListData listData = getItem(position);
            if (listData instanceof section) {
                return VIEW_TYPE_SECTION;
            } else if (listData instanceof AppInfo) {
                return VIEW_TYPE_ITEM;
            } else {
                return VIEW_TYPE_NONE;
            }
        } else {
            return VIEW_TYPE_NONE;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }



    class SectionViewHolder {
        TextView tvTitle;

        public SectionViewHolder(View itemView) {
            tvTitle = (TextView) itemView.findViewById(R.id.text_view_title);
        }

        public void setTitle(String title) {
            tvTitle.setText(title);
        }
    }

    class ItemViewHolder {
        TextView appName, appCount;
        ImageView imageView;
        LinearLayout list_notification_item;
        Switch mSwBar;

        public ItemViewHolder(View view) {
            appName = (TextView) view.findViewById(R.id.app_name);
            appCount = (TextView) view.findViewById(R.id.app_count);
            imageView = (ImageView) view.findViewById(R.id.app_image);
            list_notification_item=view.findViewById(R.id.list_notification_item);
            mSwBar=view.findViewById(R.id.sw_bar);
        }
    }
}
