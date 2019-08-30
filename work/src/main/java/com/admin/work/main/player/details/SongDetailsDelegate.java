package com.admin.work.main.player.details;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.palette.graphics.Palette;
import androidx.viewpager.widget.ViewPager;

import com.admin.core.app.AccountManager;
import com.admin.core.app.Latte;
import com.admin.core.app.MusicManager;
import com.admin.core.deleggate.LatteDelegate;
import com.admin.core.net.rx.RxRequest;
import com.admin.core.net.rx.RxRestClient;
import com.admin.core.util.file.FileUtil;
import com.admin.core.util.image.UrlToBitmap;
import com.admin.core.util.time.TimeUtils;
import com.admin.core.util.value.Resource;
import com.admin.work.R;
import com.admin.work.R2;
import com.admin.work.main.home.search.NetWorkQQSong;
import com.admin.work.main.player.PlayerControl;
import com.admin.work.main.player.PlayerItemFields;
import com.admin.work.main.player.details.page.SongPageDetailsDelegate;
import com.admin.work.main.player.details.page.SongPageLyricsDelegate;
import com.admin.work.main.player.details.page.SongPagePhotoDelegate;
import com.admin.work.main.player.nativemusic.Song;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.joanzapata.iconify.widget.IconTextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SongDetailsDelegate extends LatteDelegate {

    @BindView(R2.id.dele_song_details_layout)
    LinearLayoutCompat mLinearLayout = null;
    @BindView(R2.id.dele_song_details_viewpager)
    ViewPager mViewpager = null;
    @BindView(R2.id.toolbar_first_textview)
    AppCompatTextView mTextName = null;
    @BindView(R2.id.dele_song_details_flag0)
    AppCompatButton flag0 = null;
    @BindView(R2.id.dele_song_details_flag1)
    AppCompatButton flag1 = null;
    @BindView(R2.id.dele_song_details_flag2)
    AppCompatButton flag2 = null;

    @BindView(R2.id.dele_song_details_seekbar)
    AppCompatSeekBar mSeekBar = null;
    @BindView(R2.id.dele_song_details_start)
    AppCompatTextView mSeekStart = null;
    @BindView(R2.id.dele_song_details_end)
    AppCompatTextView mSeekEnd = null;

    @BindView(R2.id.dele_song_details_pause)
    IconTextView mIconPause;
    @BindView(R2.id.dele_song_details_order)
    IconTextView mIconOrder;
    @BindView(R2.id.dele_song_details_dowload)
    IconTextView mDowload;
    @BindView(R2.id.dele_song_details_love)
    IconTextView mLove;

    private DetailsPageAdapter mPageAdapter;
    private Song mSong;
    private NetWorkQQSong qqSong;
    private Bitmap mBitmap;
    private SongPagePhotoDelegate mPhotoDelegate;
    private SongPageLyricsDelegate mLyricsDelegate;
    private SongPageDetailsDelegate mDetailsDelegate;
    private PlayerControl mPlayerControl;
    private ExecutorService mExecutorService;
    Map<String, String> map = new HashMap<>();

    @OnClick(R2.id.toolbar_first_back)
    void onBackClick(){
        getSupportDelegate().pop();
    }

    @OnClick(R2.id.dele_song_details_up)
    void onClickUp() {
        mPlayerControl.setUp();
    }

    @OnClick(R2.id.dele_song_details_pause)
    void onClickPause() {
        //暂停、播放
        if (mPlayerControl.isPlay()) {
            mPlayerControl.stopMusic();
            mIconPause.setText("{paly-start}");
            mPhotoDelegate.init();
        } else {
            mPlayerControl.startMusic();
            mIconPause.setText("{paly-pause}");
            mPhotoDelegate.init();
        }
    }

    @OnClick(R2.id.dele_song_details_below)
    void onClickBelow() {
        mPlayerControl.setBelow();
    }

    /**
     * 分享
     */
    @OnClick(R2.id.dele_song_details_shard)
    void onShardClick() {
        OnekeyShare oks = new OnekeyShare();
        oks.setTitleUrl("http://47.106.101.44:80/Frame_Api/ttdt.apk");
        oks.setTitle("天天动听");
        if (qqSong != null) {
            oks.setText(qqSong.getName()+" --"+qqSong.getAuthor());
            oks.setImageUrl(qqSong.getPic());
        }else {
            oks.setText(mSong.getName()+" --"+mSong.getSinger());
        }
        oks.show(getContext());
    }

    @SuppressLint("SetTextI18n")
    @OnClick(R2.id.dele_song_details_order)
    void onOrderClick() {
        String playMode = MusicManager.getPlayMode();
        if (playMode.equals(MusicManager.PlayMode.ORDER_PLAY.name())) {
            MusicManager.setPlayMode(MusicManager.PlayMode.RANDOM_PLAY);
            mIconOrder.setText("{play-random}");
            mIconOrder.setTextSize(20);
        } else if (playMode.equals(MusicManager.PlayMode.RANDOM_PLAY.name())) {
            MusicManager.setPlayMode(MusicManager.PlayMode.REPETITION);
            mIconOrder.setText("{paly-repetition}");
            mIconOrder.setTextSize(25);
        } else {
            MusicManager.setPlayMode(MusicManager.PlayMode.ORDER_PLAY);
            mIconOrder.setText("{play-order}");
            mIconOrder.setTextSize(40);
        }
    }

    @OnClick(R2.id.dele_song_details_dowload)
    void onDowloadClick() {
        String dir = "天天动听";
        if (qqSong != null && !qqSong.getUrl().equals("")) {
            Toast.makeText(Latte.getApplication(), "开始下载", Toast.LENGTH_SHORT).show();
            RxRequest.dowloadFile(qqSong.getUrl(), dir, qqSong.getName() + "-" + qqSong.getAuthor() + ".mp3",
                    (flag, file) -> {
                        if (flag) {
                            Toast.makeText(Latte.getApplication(), "成功", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(Latte.getApplication(), "找不到下载路径", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R2.id.dele_song_details_love)
    void onLoveClick() {
        map.clear();
        map.put("name", AccountManager.getSignInNumber());
        if (mSong != null) {
            map.put("song", mSong.getName());
            map.put("path", mSong.getPath());
            map.put("singer", mSong.singer);
        }
        String json = JSON.toJSONString(map);
        RxRequest.onPostRx(getContext(), Resource.getString(R.string.setlovesong),
                json, (flag, result) -> {
                    if (flag) {
                        Log.e("-------------", "onLoveClick: "+result );
                        JSONObject jsonObject = JSON.parseObject(result);
                        if (jsonObject.getString("result").equals("success")) {
                            mLove.setTextColor(Color.RED);
                        }
                        if (jsonObject.getString("result").equals("delete")) {
                            mLove.setTextColor(Color.WHITE);
                        }
                    }
                });
    }


    public SongDetailsDelegate(Song song, NetWorkQQSong qqSong, Bitmap bitmap) {
        mPlayerControl = PlayerControl.getInstence();
        this.mSong = song;
        this.qqSong = qqSong;
        this.mBitmap = bitmap;
    }

    public static SongDetailsDelegate getInstance(Song song, NetWorkQQSong qqSong, Bitmap bitmap) {
        return new SongDetailsDelegate(song, qqSong, bitmap);
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_song_details;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        setPlay();
        init();
        initViewPager();
        isFile();
        OnNowDataListener();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void OnNowDataListener() {
        if (mPlayerControl != null) {
            mPlayerControl.addOnNowDataListener(entity -> {
                Bitmap bitmap = entity.getField(PlayerItemFields.BITMAP);
                NetWorkQQSong qqSong = entity.getField(PlayerItemFields.NETWORK_SONG);
                this.mSong = entity.getField(PlayerItemFields.SONG);
                this.qqSong = qqSong;
                if (bitmap != null) {
                    new Palette.Builder(bitmap).generate(palette -> {
                        Palette.Swatch vibrant = palette.getMutedSwatch();
                        if (vibrant != null) {
                            mLinearLayout.setBackgroundColor(vibrant.getRgb());
                        }
                    });
                }
                if (qqSong != null) {
                    mLyricsDelegate.setLrc(qqSong.getLrc()).init();
                    mDetailsDelegate.setSongId(qqSong.getSongid()).init();
                    mPhotoDelegate.setSong(mSong).setBitmat(bitmap).init();
                    mTextName.setText(mSong.name);
                    setPlay();
                    setSeekTime();
                    mSeekBar.setMax(mSong.duration);
                    startThread();
                    isFile();
                    isLove();
                }
            });
        }
    }

    private void isLove() {
        map.clear();
        map.put("name", AccountManager.getSignInNumber());
        RxRequest.onPostRx(getContext(), Resource.getString(R.string.getlovesong),
                JSON.toJSONString(map), (flag, result) -> {
                    if (flag) {
                        JSONObject json = JSON.parseObject(result);
                        Log.e("-----", "isLove: " + json);
                        if (json.getString("result").equals("success")) {
                            JSONArray song = json.getJSONArray("song");
                            final int count = song.size();
                            for (int i = 0; i < count; i++) {
                                json = song.getJSONObject(i);
                                String name = json.getString("song");
                                String path = json.getString("path");
                                if (name.equals(qqSong.getName()) && path.equals(qqSong.getUrl())) {
                                    mLove.setTextColor(Color.RED);
                                    return;
                                } else {
                                    mLove.setTextColor(Color.WHITE);
                                }
                                if (name.equals(mSong.getName()) && path.equals(mSong.getPath())) {
                                    mLove.setTextColor(Color.RED);
                                    return;
                                } else {
                                    mLove.setTextColor(Color.WHITE);
                                }
                            }
                        }
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init() {
        mTextName.setText(mSong.name);
        mSeekBar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);//设置滑块颜色、样式
        setSeekTime();
        //设置最大值
        mSeekBar.setMax(mSong.duration);
        //设置当前位置
        if (mPlayerControl.isPlay()) {
            setSeekBarPos();
            startThread();
        }
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //参数 ： fromUser即b 是用来标识是否来自用户的手动操作  true 用户动过手动方式更改的进度条
                if (b) {
                    //seekto方法是异步方法
                    //seekto方法的参数是毫秒，而不是秒
                    mPlayerControl.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        String playMode = MusicManager.getPlayMode();
        if (playMode.equals(MusicManager.PlayMode.ORDER_PLAY.name())) {
            mIconOrder.setText("{play-order}");
            mIconOrder.setTextSize(40);
        } else if (playMode.equals(MusicManager.PlayMode.RANDOM_PLAY.name())) {
            mIconOrder.setText("{play-random}");
            mIconOrder.setTextSize(20);
        } else {
            mIconOrder.setText("{paly-repetition}");
            mIconOrder.setTextSize(25);
        }
    }

    private void startThread() {
        if (mExecutorService == null) {
            mExecutorService = Executors.newSingleThreadExecutor();
        }
        mExecutorService.execute(() -> {
            while (mPlayerControl.getCurrentPosition() < mSong.duration) {
                try {
                    //每隔 100 毫秒更新一次位置
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Latte.getHandler().post(this::setSeekBarPos);
            }
        });
    }

    private void setSeekBarPos() {
        if (mSeekBar != null) {
            int pos = mPlayerControl.getCurrentPosition();
            mSeekBar.setProgress(pos);
            mSeekStart.setText(TimeUtils.getTime(pos));
        }
    }

    private void setSeekTime() {
        mSeekStart.setText("00:00");
        mSeekEnd.setText(TimeUtils.getTime(mSong.duration));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initViewPager() {
        if (mBitmap != null) {
            new Palette.Builder(mBitmap).generate(palette -> {
                Palette.Swatch vibrant = palette.getMutedSwatch();
                if (vibrant != null) {
                    mLinearLayout.setBackgroundColor(vibrant.getRgb());
                }
            });
            setPage(qqSong);
        } else {
            mPlayerControl.requestSongMessage(mSong, netWorkQQSong -> {
                //设置当前页面的背景颜色
                if (netWorkQQSong != null) {
                    UrlToBitmap.urlToBitmap(netWorkQQSong.getPic(), bitmap -> {
                        new Palette.Builder(bitmap).generate(palette -> {
                            Palette.Swatch vibrant = palette.getMutedSwatch();
                            if (vibrant != null) {
                                mLinearLayout.setBackgroundColor(vibrant.getRgb());
                            }
                        });
                    });
                }
                setPage(netWorkQQSong);
            });
        }
    }


    private void setPlay() {
        if (mPlayerControl.isPlay()) {
            mIconPause.setText("{paly-pause}");
        } else {
            mIconPause.setText("{paly-start}");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setPage(NetWorkQQSong netWorkQQSong) {
        if (netWorkQQSong.getLrc() != null && netWorkQQSong.getSongid() != null) {
            mLyricsDelegate = SongPageLyricsDelegate.getInstance(mSong, netWorkQQSong.getLrc());
            mPhotoDelegate = SongPagePhotoDelegate.getInstance(mSong, mBitmap);
            mDetailsDelegate = SongPageDetailsDelegate.getInstance(mSong, netWorkQQSong.getSongid());
        } else {
            mLyricsDelegate = SongPageLyricsDelegate.getInstance(mSong, null);
            mPhotoDelegate = SongPagePhotoDelegate.getInstance(mSong, null);
            mDetailsDelegate = SongPageDetailsDelegate.getInstance(mSong, null);
        }
        List<LatteDelegate> delegates = new ArrayList<>();
        delegates.add(mLyricsDelegate);
        delegates.add(mPhotoDelegate);
        delegates.add(mDetailsDelegate);
        mPageAdapter = new DetailsPageAdapter(getChildFragmentManager(), delegates);
        mViewpager.setAdapter(mPageAdapter);
        mViewpager.setCurrentItem(1);
        mViewpager.setOffscreenPageLimit(2);
        setPointer();
    }

    private void isFile() {
        if (mSong.path.startsWith("http")) {
            mDowload.setTextColor(Color.parseColor("#ffffff"));
            mDowload.setEnabled(true);
        } else {
            mDowload.setTextColor(Color.parseColor("#999999"));
            mDowload.setEnabled(false);
        }
    }

    private void setPointer() {
        setflagColor();
        flag1.setBackgroundDrawable(getResources().getDrawable(R.drawable.delegate_btn_flag_start));
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setflagColor();
                switch (position) {
                    case 0:
                        flag0.setBackgroundDrawable(getResources().getDrawable(R.drawable.delegate_btn_flag_start));
                        break;
                    case 1:
                        flag1.setBackgroundDrawable(getResources().getDrawable(R.drawable.delegate_btn_flag_start));
                        break;
                    case 2:
                        flag2.setBackgroundDrawable(getResources().getDrawable(R.drawable.delegate_btn_flag_start));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setflagColor() {
        flag0.setBackgroundDrawable(getResources().getDrawable(R.drawable.delegate_btn_flag_out));
        flag1.setBackgroundDrawable(getResources().getDrawable(R.drawable.delegate_btn_flag_out));
        flag2.setBackgroundDrawable(getResources().getDrawable(R.drawable.delegate_btn_flag_out));
    }

    @Override
    public boolean onBackPressedSupport() {
        if (mExecutorService != null) {
            mExecutorService.shutdownNow();
        }
        if (mPlayerControl != null) {
            mPlayerControl.addOnNowDataListener(null);
        }
        if (mLyricsDelegate != null) {
            mLyricsDelegate.onDestroy();
        }
        if (mPhotoDelegate != null) {
            mPhotoDelegate.onDestroy();
        }
        if (mDetailsDelegate != null) {
            mDetailsDelegate.onDestroy();
        }
        getSupportDelegate().pop();
        return true;
    }
}
