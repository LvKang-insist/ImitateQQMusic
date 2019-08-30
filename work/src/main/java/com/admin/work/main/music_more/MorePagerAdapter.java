package com.admin.work.main.music_more;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

class MorePagerAdapter extends FragmentStatePagerAdapter {

    List<PagerDelegate> list = new ArrayList<>();
    public MorePagerAdapter(FragmentManager fm, List<String> list) {
        super(fm);
        for (int i = 0; i < list.size(); i++) {
            this.list.add(new PagerDelegate(list.get(i)));
        }
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
