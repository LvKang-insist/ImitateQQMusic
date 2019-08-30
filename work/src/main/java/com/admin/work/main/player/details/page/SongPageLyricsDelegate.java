package com.admin.work.main.player.details.page;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.admin.core.deleggate.LatteDelegate;
import com.admin.work.R;
import com.admin.work.R2;
import com.admin.work.main.player.PlayerControl;
import com.admin.work.main.player.details.page.lrc_song.LrcBaen;
import com.admin.work.main.player.details.page.lrc_song.LrcUtil;
import com.admin.work.main.player.details.page.lrc_song.LrcView;
import com.admin.work.main.player.nativemusic.Song;

import butterknife.BindView;

public class SongPageLyricsDelegate extends LatteDelegate {

    @BindView(R2.id.lrc_view)
    LrcView mLrcView = null;

    private Song mSong;
    private String lrc;

    private SongPageLyricsDelegate(Song song, String lrc) {
        this.mSong = song;
        this.lrc = lrc;
    }

    public static SongPageLyricsDelegate getInstance(Song s, String lrc) {
        return new SongPageLyricsDelegate(s, lrc);
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_song_page_lvrics;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        init();
    }

    public SongPageLyricsDelegate setLrc(String lrc) {
        this.lrc = lrc;
        return this;
    }

    public void init() {
        if (mLrcView != null && lrc != null) {
            mLrcView.init();
            mLrcView.setLrc(lrc);
        }
    }
}
