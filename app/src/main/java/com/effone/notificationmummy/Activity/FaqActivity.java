package com.effone.notificationmummy.Activity;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import com.effone.notificationmummy.R;
import com.effone.notificationmummy.adapter.FaqListAdapter;
import com.effone.notificationmummy.model.Faq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FaqActivity extends AppCompatActivity {
    private ExpandableListView mFaqList;
    ArrayList<Faq> faqs;
    private ArrayList<String> questions;
    private HashMap<String, List<String>> answers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable backArrow = getResources().getDrawable(R.drawable.ic_back_arrow);
        backArrow.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        toolbar.setTitle("FAQ");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                onBackPressed();
            }
        });
        mFaqList=findViewById(R.id.faq_list);
       

        addValu();
        FaqListAdapter faqListAdapter= new FaqListAdapter(this,questions,answers);
        mFaqList.setAdapter(faqListAdapter);
    }

    private void addValu() {
        questions = new ArrayList<String>();
        answers = new HashMap<String, List<String>>();

        // Adding child data
        questions.add("What is WhatsUp Mommy and how does it work?");
        questions.add("What is Allowed and Denied App(s)?");
        questions.add("Is it available for iOS and Android?");
        questions.add("When we delete message(s) from WhatsUp Mummy dose it also delete message(s) from WhatsApp App?");
        questions.add("Why my Images/ Media is not appearing?");
        questions.add("Why on tap Image is not opening?");
        questions.add("How to Delete Messages?");
        questions.add("Do I need to root the device?");
        questions.add("Is our data protected?");

        ArrayList<String> q1=new ArrayList<String>();
        q1.add("WhatsUp Mommy allows you to read your WhatsApp messages without opening WhatsApp application. It can also read deleted massages by user, open an Image/ GIF and play audio/ video file. WhatsUp Mommy is very helpful to monitor all incoming messages, coming from various applications like Facebook, gmail, sms, amazon, Flipkart etc. It helps to store the message and keep a record of it._ WhatsUp Mummy doesn’t contain any ads and it’s free.\n" +
                "Highlight features of WhatsUp Mummy :\n"+"- You can allow or deny messages for multiple apps.\n" +
                "- Can have the BackUp of message upto 30 days and  can be extended upto 1 year through setting.\n" +
                "- Search messages / contacts.\n" +
                "- Delete messages one by on or delete all messages on single tap.\n");
        ArrayList<String> q2=new ArrayList<String>();
        q2.add("Allowed Apps enable you view all the messages and Denied apps doesn’t. You can configure this list.");
        ArrayList<String> q3=new ArrayList<String>();
        q3.add("No, the app is not available in iOS platform.");
        ArrayList<String> q4=new ArrayList<String>();
        q4.add("No, It doesn’t delete your WhatsApp’s message(s).");
        ArrayList<String> q5=new ArrayList<String>();
        q5.add("This feature doesn’t work if WhatsApp auto download is disabled. Please enable auto download feature in WhatsApp. If you are not aware of it , follow the procedure :\n" +
                "- Open the whatsApp -> Got settings\n" +
                "- Select Data and storage usage\n" +
                "- In Media auto - download section , select for all the option to get auto- download feature .\n");
        ArrayList<String> q6=new ArrayList<String>();
        q6.add("Currently WhatsUp Mummy, can open only open the last media file. The media file could be an image/Gif or an Audio/Video file.");
        ArrayList<String> q7=new ArrayList<String>();
        q7.add("Long press the message(s) you want to delete, this will open up a new view with select and select all option to delete.");
        ArrayList<String> q8=new ArrayList<String>();
        q8.add("No, You don’t need to root the device.");
        ArrayList<String> q9=new ArrayList<String>();
        q9.add("Yes, your data is protected. They are stored in your phone local memory. There is no access of local data storage outside the device. Once you uninstall the app, whole data will be deleted automatically from your phone memory. Please go through our privacy policy to know more about it or you can reach us at contactmobitech@gmail.com");
        answers.put(questions.get(0),q1);
        answers.put(questions.get(1), q2);
        answers.put(questions.get(2), q3);
        answers.put(questions.get(3),q4);
        answers.put(questions.get(4),q5);
        answers.put(questions.get(5),q6);
        answers.put(questions.get(6),q7);
        answers.put(questions.get(7), q8);
        answers.put(questions.get(8),q9);







    }
}
