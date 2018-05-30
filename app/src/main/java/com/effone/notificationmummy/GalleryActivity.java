package com.effone.notificationmummy;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.effone.notificationmummy.fragments.AudioFragment;
import com.effone.notificationmummy.fragments.GifFragment;
import com.effone.notificationmummy.fragments.ImageFragment;
import com.effone.notificationmummy.fragments.VideoFragment;
import com.effone.notificationmummy.model.MessageInfo;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {
  int  messageInfos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        messageInfos=  getIntent().getIntExtra("BODY",0);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),messageInfos);
        adapter.addFragment(new ImageFragment(), "Images");
        adapter.addFragment(new VideoFragment(), "Videos");
        adapter.addFragment(new AudioFragment(), "Audios");
        adapter.addFragment(new GifFragment(), "GIFs");
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private  int messageInfos;
        Bundle bundle=new Bundle();
        public ViewPagerAdapter(FragmentManager manager, int messageInfos) {
            super(manager);
            bundle.putSerializable("data",messageInfos);
            this.messageInfos=messageInfos;
        }

        @Override
        public Fragment getItem(int position) {
            mFragmentList.get(position).setArguments(bundle);
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
