package com.admin.work.main;

import android.os.Build;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;

import com.admin.core.deleggate.bottom.BaseBottomDelegate;
import com.admin.core.deleggate.bottom.BottomItemDelegate;
import com.admin.core.deleggate.bottom.BottomTabBean;
import com.admin.core.deleggate.bottom.ItemBuilder;
import com.admin.work.main.discover.DiscoverDelegate;

import com.admin.work.main.home.HomeDelegate;
import com.admin.work.main.music_more.MusicDelegate;
import com.admin.work.main.player.PlayerControl;

import java.util.LinkedHashMap;

/**
 * Copyright (C)
 *
 * @file: EcBottomDelegate
 * @author: 345
 * @Time: 2019/4/26 14:26
 * @description: ${DESCRIPTION}
 */
@SuppressWarnings("MapOrSetKeyShouldOverrideHashCodeEquals")
public class EcBottomDelegate extends BaseBottomDelegate {
    @Override
    public LinkedHashMap<BottomTabBean, BottomItemDelegate> setItems(ItemBuilder builder) {
        final LinkedHashMap<BottomTabBean, BottomItemDelegate> items = new LinkedHashMap<>();
        items.put(new BottomTabBean("{more-music}","音乐馆"),new MusicDelegate());
        items.put(new BottomTabBean("{fa-compass}","发现"),new DiscoverDelegate());
        items.put(new BottomTabBean("{fa-user}","我的"),new HomeDelegate());
        return builder.addItem(items).build();
    }

    @Override
    public int setIndexDelegate() {
        return 2;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int setClickedColor() {
        return getResources().getColor(com.admin.core.R.color.app_music_green,null);
    }

    @Override
    public void onPlayer(LinearLayout mLinearLayout) {
        PlayerControl.getInstence().onCreate(mLinearLayout);
    }

}
