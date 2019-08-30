package com.admin.work.main.home.tab;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class TabPagerAdpter extends FragmentPagerAdapter {

    private List<String> mTabList;

    public TabPagerAdpter(FragmentManager fm, List<String> tabName) {
        super(fm);
        mTabList = tabName;
    }

    @Override
    public Fragment getItem(int position) {
        return TabDelegate.create(position);
    }

    @Override
    public int getCount() {
        return mTabList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTabList.get(position);
    }


}
