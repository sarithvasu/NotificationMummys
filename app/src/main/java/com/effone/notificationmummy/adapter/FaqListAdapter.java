package com.effone.notificationmummy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.effone.notificationmummy.R;
import com.effone.notificationmummy.model.Faq;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sarith.vasu on 20-05-2016.
 */
public class FaqListAdapter extends ArrayAdapter<Faq> {
    private ArrayList<Faq> values;
    private LayoutInflater inflater;

    public FaqListAdapter(Context context, int resource, List<Faq> values) {
        super(context, resource, values);
        this.values =(ArrayList<Faq>) values;
        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final FilterViewHolder holder;

        if(convertView==null){
            vi = inflater.inflate(R.layout.faq_list_item, null);
            holder = new FilterViewHolder();
            holder.tv_Question = (TextView) vi.findViewById(R.id.tv_question);
            holder.tv_Answer = (TextView) vi.findViewById(R.id.tv_answer);


            vi.setTag( holder );
        }
        else
            holder = (FilterViewHolder) vi.getTag();

        if (values.size() <= 0) {
            holder.tv_Question.setText("No Data");

        } else {
            /***** Get each Model object from Arraylist ********/

            Faq value = (Faq) values.get(position);

            /************  Set Model values in Holder elements ***********/

            holder.tv_Question.setText(value.getQuestion());
            holder.tv_Answer.setText(value.getAnswer());

            holder.tv_Question.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.tv_Answer.getVisibility() == View.VISIBLE) {
                        holder.tv_Answer.setVisibility(View.GONE);
                    } else {
                        holder.tv_Answer.setVisibility(View.VISIBLE);
                    }
                }
            });
            holder.tv_Answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( holder.tv_Answer.getVisibility()== View.VISIBLE){
                        holder.tv_Answer.setVisibility(View.GONE);
                    }else{
                        holder.tv_Answer.setVisibility(View.VISIBLE);
                    }
                }
            });




        }

        return vi;



    }
    public static  class FilterViewHolder {
        TextView tv_Question;
        TextView tv_Answer;

    }


}