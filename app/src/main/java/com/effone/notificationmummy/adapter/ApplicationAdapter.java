package com.effone.notificationmummy.adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.effone.notificationmummy.R;
import com.effone.notificationmummy.database.DataBaseHandler;
import com.effone.notificationmummy.model.ApplicationInform;
import com.effone.notificationmummy.model.ListData;
import com.effone.notificationmummy.model.section;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sumanth.peddinti on 5/24/2018.
 */
public class ApplicationAdapter extends ArrayAdapter<ListData> {
    private List<ListData> appsList = null;
    private Context context;
    private PackageManager packageManager;
    private ArrayList<Boolean> checkList = new ArrayList<Boolean>();

    private DataBaseHandler mDataBasehandler;

    public ApplicationAdapter(Context context, int textViewResourceId,
                              List<ListData> appsList) {
        super(context, textViewResourceId, appsList);
        this.context = context;
        this.appsList = appsList;
        packageManager = context.getPackageManager();
        mDataBasehandler=DataBaseHandler.getInstance(context);
        for (int i = 0; i < appsList.size(); i++) {
            checkList.add(false);
        }
    }

    @Override
    public int getCount() {
        return ((null != appsList) ? appsList.size() : 0);
    }

    @Override
    public ListData getItem(int position) {
        return ((null != appsList) ? appsList.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getViewTypeCount() {
        return 2; // The number of distinct view types the getView() will return.
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof section){
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Object item = getItem(position);
        if (null == view) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.row, null);
        }

        ListData data = appsList.get(position);
        if (item instanceof ApplicationInform) {
            if (null != data) {
                TextView appName = (TextView) view.findViewById(R.id.app_name);
                TextView packageName = (TextView) view.findViewById(R.id.app_paackage);
                ImageView iconview = (ImageView) view.findViewById(R.id.app_icon);

                CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_app);

                checkBox.setTag(Integer.valueOf(position)); // set the tag so we can identify the correct row in the listener
                checkBox.setChecked(checkList.get(position)); // set the status as we stored it
                checkBox.setOnCheckedChangeListener(mListener); // set the listener
                if (((ApplicationInform) item).getPackageName().equals("JioMusic")) {
                    checkBox.setChecked(true);
                }
                appName.setText(((ApplicationInform) item).getAppName());
                //  packageName.setText(data.packageName);
               // iconview.setImageDrawable(data.loadIcon(packageManager));
            }
        }else{
            TextView appName = (TextView) view.findViewById(R.id.app_name);
            TextView packageName = (TextView) view.findViewById(R.id.app_paackage);
            ImageView iconview = (ImageView) view.findViewById(R.id.app_icon);
            packageName.setVisibility(View.GONE);
            iconview.setVisibility(View.GONE);
        }
        return view;
    }

    CompoundButton.OnCheckedChangeListener mListener = new CompoundButton.OnCheckedChangeListener() {

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            checkList.set((Integer)buttonView.getTag(),isChecked);
            // get the tag so we know the row and store the status
           // mDataBasehandler.insertTheUnBlockeAppList(ListData.((Integer)buttonView.getTag()));
        }
    };
}
