package com.admin.work.main.discover;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.admin.core.deleggate.LatteDelegate;
import com.admin.work.main.discover.tab.TabRecommendDelegate;
import com.admin.work.main.discover.tab.TabVideoDelegate;

import java.util.ArrayList;
import java.util.List;

public class PageAdapter extends FragmentStatePagerAdapter {

    private List<String> list;
    private List<LatteDelegate> delegates = new ArrayList<>();

    public PageAdapter(FragmentManager fm, List<String> list) {
        super(fm);
        this.list = list;
        for (int i = 0; i < list.size(); i++) {
            switch (i) {
                case 0:
                    delegates.add(new TabRecommendDelegate());
                    break;
                case 1:
                    delegates.add(new TabVideoDelegate());
                    break;
                default:
            }
        }
    }

    @Override
    public Fragment getItem(int position) {
        return delegates.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position);
    }
}
