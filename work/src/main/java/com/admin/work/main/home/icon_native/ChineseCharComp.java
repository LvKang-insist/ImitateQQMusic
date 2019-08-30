package com.admin.work.main.home.icon_native;

import com.admin.work.main.player.nativemusic.Song;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;

public class ChineseCharComp implements Comparator<Song> {


    @Override
    public int compare(Song song, Song t1) {
        Collator myCollator = Collator.getInstance(java.util.Locale.CHINA);
        if (myCollator.compare(song.getName(),t1.getName())<0){
            return -1;
        }else if (myCollator.compare(song.getName(),t1.getName())>0){
            return 1;
        }else {
            return 0;
        }
    }
}
