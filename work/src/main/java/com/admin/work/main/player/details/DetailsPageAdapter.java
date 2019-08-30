package com.admin.work.main.player.details;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.admin.core.app.Latte;
import com.admin.core.deleggate.LatteDelegate;
import com.admin.work.main.player.details.page.SongPageDetailsDelegate;
import com.admin.work.main.player.details.page.SongPageLyricsDelegate;
import com.admin.work.main.player.details.page.SongPagePhotoDelegate;
import com.admin.work.main.player.nativemusic.Song;

import java.util.List;

public class DetailsPageAdapter extends FragmentPagerAdapter {
    List<LatteDelegate> list ;
    public DetailsPageAdapter(FragmentManager fm, List<LatteDelegate> list) {
        super(fm);
        this.list = list;
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
