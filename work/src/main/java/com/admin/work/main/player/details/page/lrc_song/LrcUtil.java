package com.admin.work.main.player.details.page.lrc_song;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class LrcUtil {
    public static  List<LrcBaen> parseStrLrc(String lrc) {
        String[] split = lrc.split("\\n");
        List<LrcBaen> lrcBaens = new ArrayList<>();
        for (int i = 5; i < split.length; i++) {
            String str = split[i];
            String min = str.substring(str.indexOf("[") + 1, str.indexOf(":"));
            String seconds = str.substring(str.indexOf(":") + 1, str.indexOf("."));
            String mils = str.substring(str.indexOf(".") + 1, str.indexOf("]"));
            long startTime = Long.valueOf(min) * 60 * 1000 + Long.valueOf(seconds) * 1000 + Long.valueOf(mils) * 10;
            String text = str.substring(str.indexOf("]") + 1);
            LrcBaen baen = new LrcBaen();
            baen.setStart(startTime);
            baen.setLrc(text);
            lrcBaens.add(baen);
            if (lrcBaens.size() > 1) {
                //将第二段歌词的开始时间设为 第一段的结束时间
                lrcBaens.get(lrcBaens.size() - 2).setEnd(startTime);
            }
            //如果是最后一段歌词
            if (i == split.length - 1) {
                lrcBaens.get(lrcBaens.size()-1).setEnd(100000);
            }
        }
        return lrcBaens;
    }
}
