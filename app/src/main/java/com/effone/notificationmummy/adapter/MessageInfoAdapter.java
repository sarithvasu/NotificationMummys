package com.effone.notificationmummy.adapter;

import android.content.Context;
import java.util.Calendar;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import com.effone.notificationmummy.R;
import com.effone.notificationmummy.database.DataBaseHandler;
import com.effone.notificationmummy.model.MessageInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



import static android.view.ViewGroup.FOCUS_BLOCK_DESCENDANTS;
import static com.effone.notificationmummy.common.NotificationAccessDialogFragment.getFormatedDateTime;

/**
 * Created by sumanth.peddinti on 5/15/2018.
 */

public class MessageInfoAdapter extends BaseAdapter   {
    private Context mContext;
    private ArrayList<MessageInfo> messageInfos;
    private ArrayList<MessageInfo> mFilterMessageInfos;
    private LayoutInflater layoutInflater;

    SparseBooleanArray selected = new SparseBooleanArray();

    public MessageInfoAdapter(Context context, ArrayList<MessageInfo> messageInfos) {
        this.mContext=context;
        this.messageInfos=messageInfos;
        this.mFilterMessageInfos=messageInfos;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void select(int index, boolean checked){
        selected.put(index, checked);
    }

    public void clearSelection(){
        selected.clear();
    }

    public void removeSelected(){
        ArrayList<Integer> ints=new ArrayList<>();
        int size=selected.size();
        for (int i=0;i<size;i++) {
            boolean isChecked = selected.valueAt(i);
            if (isChecked) {
                int index=selected.keyAt(i);
                deleteTheRecord(mFilterMessageInfos.get(index-i));
                    mFilterMessageInfos.remove(index-i);

                notifyDataSetChanged();
            }
        }

    }

    private void deleteTheRecord(MessageInfo messageInfo) {
        DataBaseHandler dataBaseHandler=DataBaseHandler.getInstance(mContext);
        dataBaseHandler.removeTheSelectedIndex(messageInfo.getMessage_id());

    }


    @Override
    public int getCount() {
        return mFilterMessageInfos.size();
    }

    @Override
    public MessageInfo getItem(int i) {
        return mFilterMessageInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View contentView=view;
        ItemViewHolder itemViewHolder;
        if(contentView == null) {
            contentView = layoutInflater.inflate(R.layout.message_info, viewGroup, false);
            itemViewHolder = new ItemViewHolder(contentView);
            contentView.setTag(itemViewHolder);
        }else {
            itemViewHolder = (ItemViewHolder) contentView.getTag();
        }

       final MessageInfo messageInfo=mFilterMessageInfos.get(i);
        itemViewHolder.mTvMessage.setText(messageInfo.getSender_message());
        itemViewHolder.mTvMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (messageInfo.getMessage_type().equals("GIF") || messageInfo.getMessage_type().equals("Photo") || messageInfo.getMessage_type().equals("Video") || messageInfo.getMessage_type().equals("Audio")) {
                    Toast.makeText(mContext,"Can't Open",Toast.LENGTH_SHORT).show();
                }
            }
        });
        try {
            Date date = new Date(Long.parseLong(messageInfo.getSender_message_time_stamp()));
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String[] dates = formatter.format(date).split(" ");

            itemViewHolder.mTvDate.setText(getFormatedDateTime(dates[1],"HH:mm:ss","hh:mm a"));
        }catch (Exception e){

        }
        itemViewHolder.tv_header.setText(mFilterMessageInfos.get(i).getDate());

        if (i > 0) {
            if (mFilterMessageInfos.get(i).getDate().equalsIgnoreCase(mFilterMessageInfos.get(i-1).getDate())) {
                itemViewHolder.tv_header.setVisibility(View.GONE);
            } else {
                itemViewHolder.tv_header.setVisibility(View.VISIBLE);
            }
        } else {
            itemViewHolder.tv_header.setVisibility(View.VISIBLE);
        }
        if(selected.get(i)== true){
            contentView.setBackgroundResource(true ? R.color.holo_gray_light : android.R.color.transparent);
        }else{
            contentView.setBackgroundResource(false ? R.color.holo_gray_light : android.R.color.transparent);
        }


        return contentView;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<MessageInfo> results = new ArrayList<MessageInfo>();
                mFilterMessageInfos = messageInfos;
                for (int i = 0; i < mFilterMessageInfos.size(); i++) {
                    MessageInfo listData = getItem(i);
                        if (((MessageInfo) listData).getSender_message_format().toLowerCase()
                                .contains(constraint.toString()))
                            results.add((MessageInfo) listData);


                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                mFilterMessageInfos = (ArrayList<MessageInfo>) results.values;
                notifyDataSetChanged();
            }
        };
    }
    private class HeaderViewHolder {
        private TextView headerDate;
    }
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private SimpleDateFormat format1 = new SimpleDateFormat("ddMMyyyy");
    private Date headerDate = null;
    private String msgHeader = "";
    private SimpleDateFormat headerdateformat = new SimpleDateFormat("dd MMMM yyyy");

    public class ItemViewHolder{
        private TextView mTvMessage,mTvDate,tv_header;
   /*     private me.himanshusoni.chatmessageview.ChatMessageView mChat;*/
        public ItemViewHolder(View viewContext){
            mTvMessage=viewContext.findViewById(R.id.tv_message);
            mTvDate=viewContext.findViewById(R.id.tv_date);
            tv_header=viewContext.findViewById(R.id.tv_header);
      /*      mChat=viewContext.findViewById(R.id.chatMessageView);*/
        }

    }
}
