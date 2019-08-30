package com.admin.work.main.discover;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PageBannerAdapter extends FragmentPagerAdapter {
    private List<PagerBannerDelegate> pageBanner = new ArrayList<>();
    public PageBannerAdapter(FragmentManager fm, ArrayList<String> list) {
        super(fm);
        for (int i = 0; i < list.size(); i++) {
            pageBanner.add(new PagerBannerDelegate(list.get(i)));
        }
    }

    @Override
    public Fragment getItem(int position) {
        return pageBanner.get(position);
    }

    @Override
    public int getCount() {
        return pageBanner.size();
    }
}
