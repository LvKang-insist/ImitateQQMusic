package com.admin.work.main.home;

import com.admin.core.app.AccountManager;
import com.admin.core.app.MusicManager;
import com.admin.core.ui.recycler.DataConverter;
import com.admin.core.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class HomeConverter extends DataConverter {
    @Override
    public ArrayList<MultipleItemEntity> convert() {
        /*
         * 登陆信息
         */
        MultipleItemEntity account = MultipleItemEntity.builder()
                .setItemType(HomeItemType.HOME_ACCOUNT)
                .setField(HomeItemFields.NAME, AccountManager.getSignInNumber())
                .setField(HomeItemFields.TEXT1, "活动中心")
                .setField(HomeItemFields.TEXT2, "会员中心")
                .setField(HomeItemFields.Url_PHOTO, "")
                .build();
        ENTITLES.add(account);

        /*
         * icon
         */
        int icon_love = MusicManager.getMusicSize(MusicManager.SourceCount.LOVE_COUNT);
        int icon_native = MusicManager.getMusicSize(MusicManager.SourceCount.NATIVE_COUNT);
        int icon_recently = MusicManager.getMusicSize(MusicManager.SourceCount.RECENTLY_COUNT);
        Map<String, String> map = new LinkedHashMap<>();
        map.put("喜欢", String.valueOf(icon_love));
        map.put("最近", String.valueOf(icon_recently));
        map.put("本地", String.valueOf(icon_native));
        map.put("已购", "");
        map.put("关注", "");
        MultipleItemEntity icon = MultipleItemEntity.builder()
                .setItemType(HomeItemType.HOME_ICON)
                .setField(HomeItemFields.MAP, map)
                .build();
        ENTITLES.add(icon);

        /*
         * 智能分类
         */
        MultipleItemEntity sort = MultipleItemEntity.builder()
                .setItemType(HomeItemType.HOME_SORT)
                .build();
        ENTITLES.add(sort);
        /*
         * TabLayout 歌单
         */
        MultipleItemEntity tab = MultipleItemEntity.builder()
                .setItemType(HomeItemType.HOME_TABLAYOUT)
                .build();
        ENTITLES.add(tab);
        return ENTITLES;
    }
}
