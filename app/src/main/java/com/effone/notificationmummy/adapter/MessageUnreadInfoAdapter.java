package com.effone.notificationmummy.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.effone.notificationmummy.R;
import com.effone.notificationmummy.database.DataBaseHandler;
import com.effone.notificationmummy.model.MessageInfo;
import com.effone.notificationmummy.model.MessageUnreadInfo;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static com.effone.notificationmummy.common.NotificationAccessDialogFragment.getFormatedDateTime;

/**
 * Created by sumanth.peddinti on 5/15/2018.
 */

public class MessageUnreadInfoAdapter  extends BaseAdapter {
    private Context context;
    private ArrayList<MessageUnreadInfo> messageUnreadInfoArrayList;
    private ArrayList<MessageUnreadInfo> mFilterArrayList;
    private LayoutInflater layoutInflater;
    SparseBooleanArray selected = new SparseBooleanArray();
    public MessageUnreadInfoAdapter(Context context, ArrayList<MessageUnreadInfo> messageUnreadInfos) {
        this.context=context;
        this.messageUnreadInfoArrayList=messageUnreadInfos;
        this.mFilterArrayList=messageUnreadInfos;
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
                deleteTheRecord(mFilterArrayList.get(index-i));
                mFilterArrayList.remove(index-i);

                notifyDataSetChanged();
            }
        }

    }

    private void deleteTheRecord(MessageUnreadInfo messageInfo) {
        DataBaseHandler dataBaseHandler=DataBaseHandler.getInstance(context);
        dataBaseHandler.removeTheCompleteInfo(messageInfo.getSender_id());

    }

    @Override
    public int getCount() {
        return mFilterArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return mFilterArrayList.get(i);
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
            contentView = layoutInflater.inflate(R.layout.unreadmesage, viewGroup, false);
            itemViewHolder = new ItemViewHolder(contentView);
            contentView.setTag(itemViewHolder);
        }else {
            itemViewHolder = (ItemViewHolder) contentView.getTag();
        }
        MessageUnreadInfo messageUnreadInfo=mFilterArrayList.get(i);
        itemViewHolder.mTvSender_name.setText(messageUnreadInfo.getSender_name());
        long value = messageUnreadInfo.getTimestamp();
        Date date = new Date(value);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String[] dates = formatter.format(date).split(" ");
        String input = "<font color=#000000>" + dates[0] + "</font>" + " " + "<font color=#33BBFF>" + dates[1] + "</font>";


        itemViewHolder.mTvSender_mesage.setText( mFilterArrayList.get(i).getSender_message());
        int count=messageUnreadInfo.getUnread();
        if(count !=0) {
            itemViewHolder.mTvUnread.setText(Integer.toString(count));
            itemViewHolder.mTvTimeStamp.setText(getFormatedDateTime(dates[1],"HH:mm:ss","hh:mm a"));
        }else{
          ///  itemViewHolder.mTvUnread.setVisibility(View.GONE);
            Date todayDate = new Date();
                String[] ad=formatter.format(todayDate).split( " ");
                if(ad[0].equals(dates[0])){
                    itemViewHolder.mTvTimeStamp.setText(getFormatedDateTime(dates[1],"HH:mm:ss","hh:mm a"));
                }else
            itemViewHolder.mTvTimeStamp.setText(dates[0]);
        }
        byte[] bitmapImage=getImage(messageUnreadInfo.getSender_name());
        Glide.clear(itemViewHolder.mImgIcon);
        Glide.with(context)
                .load(bitmapImage)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(itemViewHolder.mImgIcon);

        if(selected.get(i)== true){
            contentView.setBackgroundResource(true ? R.color.holo_gray_light : android.R.color.transparent);
        }else{
            contentView.setBackgroundResource(false ? R.color.holo_gray_light : android.R.color.transparent);
        }
        return contentView;
    }

    private byte[] getImage(String sender_name) {
        Drawable icon = null;
        String name;
        name=firstTwo(sender_name.trim());
        if(name.equals("")){
            name="+";
        }
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color2 = generator.getColor(sender_name);
        TextDrawable drawable1 = TextDrawable.builder()
                .buildRound(""+name, color2);
        icon= new BitmapDrawable(drawableToBitmap(drawable1));
        Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }



    public String firstTwo(String str) {
        String name= str.substring(0, 2).trim();
            if( name.equals(Pattern.quote("+")))
        return str.length() < 1 ? str : str.substring(0, 2);
            else
                return str.length() < 1 ? str : str.substring(0, 1);
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<MessageUnreadInfo> results = new ArrayList<MessageUnreadInfo>();
                mFilterArrayList = messageUnreadInfoArrayList;
                for (int i = 0; i < mFilterArrayList.size(); i++) {
                    MessageUnreadInfo listData = (MessageUnreadInfo) getItem(i);
                        if ((listData).getSender_name().toLowerCase()
                                .contains(constraint.toString()))
                            results.add(listData);
                    }
                    oReturn.values = results;

                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                mFilterArrayList = (ArrayList<MessageUnreadInfo>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ItemViewHolder{
        public TextView mTvSender_name,mTvSender_mesage,mTvTimeStamp;
        public  TextView mTvUnread;
        public ImageView mImgIcon;
        public ItemViewHolder(View itemView) {
            mTvSender_name = (TextView) itemView.findViewById(R.id.tv_sender_name);
            mTvSender_mesage = (TextView) itemView.findViewById(R.id.tv_sender_message);
            mTvTimeStamp = (TextView) itemView.findViewById(R.id.tv_date);
            mTvUnread =  itemView.findViewById(R.id.tv_un_read);
            mImgIcon=itemView.findViewById(R.id.img_sender_name);
        }

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
