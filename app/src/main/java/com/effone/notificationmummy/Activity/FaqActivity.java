package com.effone.notificationmummy.Activity;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.effone.notificationmummy.R;
import com.effone.notificationmummy.adapter.FaqListAdapter;
import com.effone.notificationmummy.model.Faq;

import java.util.ArrayList;

public class FaqActivity extends AppCompatActivity {
    private ListView mFaqList;
    ArrayList<Faq> faqs;

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
       faqs=new ArrayList<Faq>();
        addValu();
        FaqListAdapter faqListAdapter= new FaqListAdapter(this,R.layout.faq_list_item,faqs);
        mFaqList.setAdapter(faqListAdapter);
    }

    private void addValu() {
        Faq faq1=new Faq("What is WhatsUp Mommy and how does it work?","WhatsUp Mommy allows you to read your WhatsApp messages without opening WhatsApp application. It can also read deleted massages by user, open an Image/ GIF and play audio/ video file. WhatsUp Mommy is very helpful to monitor all incoming messages, coming from various applications like Facebook, gmail, sms, amazon, Flipkart etc. It helps to store the message and keep a record of it._ WhatsUp Mummy doesn’t contain any ads and it’s free.\n" +
                "Highlight features of WhatsUp Mummy :\n"+"- You can allow or deny messages for multiple apps.\n" +
                "- Can have the BackUp of message upto 30 days and  can be extended upto 1 year through setting.\n" +
                "- Search messages / contacts.\n" +
                "- Delete messages one by on or delete all messages on single tap.\n");
        Faq faq3=new Faq("What is Allowed and Denied App(s)?","Allowed Apps enable you view all the messages and Denied apps doesn’t. You can configure this list.");
        Faq faq4=new Faq("Is it available for iOS and Android?","No, the app is not available in iOS platform.");
        Faq faq5=new Faq("When we delete message(s) from WhatsUp Mummy dose it also delete message(s) from WhatsApp App?","No, It doesn’t delete your WhatsApp’s message(s).");
        Faq faq6=new Faq("Why my Images/ Media is not appearing?","This feature doesn’t work if WhatsApp auto download is disabled. Please enable auto download feature in WhatsApp. If you are not aware of it , follow the procedure :\n" +
                "- Open the whatsApp -> Got settings\n" +
                "- Select Data and storage usage\n" +
                "- In Media auto - download section , select for all the option to get auto- download feature .\n");
        Faq faq7=new Faq("Why on tap Image is not opening?","Currently WhatsUp Mummy, can open only open the last media file. The media file could be an image/Gif or an Audio/Video file.");
        Faq faq8=new Faq("How to Delete Messages?","Long press the message(s) you want to delete, this will open up a new view with select and select all option to delete.");
        Faq faq9=new Faq("Do I need to root the device?","No, You don’t need to root the device.");
        Faq faq10=new Faq("Is our data protected?","Yes, your data is protected. They are stored in your phone local memory. There is no access of local data storage outside the device. Once you uninstall the app, whole data will be deleted automatically from your phone memory. Please go through our privacy policy to know more about it or you can reach us at contactmobitech@gmail.com");
        faqs.add(faq1);
        faqs.add(faq3);
        faqs.add(faq4);
        faqs.add(faq5);
        faqs.add(faq6);
        faqs.add(faq7);
        faqs.add(faq8);
        faqs.add(faq9);
        faqs.add(faq10);



    }
}
