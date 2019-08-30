package com.admin.work.main.player;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.core.app.Latte;
import com.admin.core.app.MusicManager;
import com.admin.core.net.rx.RxRequest;
import com.admin.core.ui.recycler.MultipleFields;
import com.admin.core.ui.recycler.MultipleItemEntity;
import com.admin.core.ui.view.player.Player;
import com.admin.core.util.callback.CallbackManager;
import com.admin.core.util.callback.IGlobalCallback;
import com.admin.core.util.image.UrlToBitmap;
import com.admin.core.util.value.Resource;
import com.admin.work.R;
import com.admin.work.main.home.HomeDelegate;
import com.admin.work.main.home.icon_love.LoveSong;
import com.admin.work.main.home.icon_recently.RecentlySong;
import com.admin.work.main.home.search.NetWorkQQSong;
import com.admin.work.main.player.nativemusic.Song;
import com.admin.work.main.player.services.MusicService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.admin.core.app.MusicManager.PlayMode.ORDER_PLAY;
import static com.admin.core.app.MusicManager.PlayMode.RANDOM_PLAY;
import static com.admin.core.app.MusicManager.SourceCount.RECENTLY_COUNT;

/**
 * @author Lv
 * Created at 2019/7/7
 * 播放控制
 */
public class PlayerControl {

    private HomeDelegate delegate;
    private LinearLayout mLinearLayout;
    //自定义 播放控件
    private Player mPlayer;
    private Intent mServiceIntent;
    private MusicService.MusicBind mMusicBind;
    //服务标志
    private boolean isBinderService;
    private PlayerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    //动画
    private ObjectAnimator mObjectAnim;
    //接口，监听播放数量
    private HomeDelegate.OnUpdateCountListener listener;
    private ViewPagerLayoutManager mRecyclerManager;
    private OnNowSongDataListener mOnNowSongDataListener;
    public static String MUSIC_PATH;//当前播放的音乐路径
    public static Song play_below = null;//下一首
    public static Song play_song; //正在播放的

    static class PlayerHolder {
        @SuppressLint("StaticFieldLeak")
        public static final PlayerControl PLAYER_DELEGATE = new PlayerControl();
    }

    public static PlayerControl getInstence() {
        return PlayerHolder.PLAYER_DELEGATE;
    }

    public void onCreate(LinearLayout linearLayout) {
        this.mLinearLayout = linearLayout;
        //绑定service
        startMusicService();
    }

    /**
     * 从数据库加载音乐条目到 RecyclrView
     */
    public void start(HomeDelegate delegate) {
        this.delegate = delegate;
        initRecycler();
        init();
    }

    @SuppressLint("NewApi")
    private void init() {
        mPlayer = mLinearLayout.findViewById(R.id.bottom_player);
        mPlayer.setOnClickListener((view -> {
            setPlay();
        }));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setPlay() {
        if (mPlayer.isPlay()) {
            stopMusic();
            if (mObjectAnim != null) {
                mObjectAnim.pause();
            }
        } else {
            //获取 recyclerView 显示的条目,进行播放
            playMusic(getStateRecyItem(), getStateRecyPos());
        }
    }

    public void initRecycler() {
        mRecyclerView = mLinearLayout.findViewById(R.id.bottom_recycler);
        mRecyclerManager = new ViewPagerLayoutManager(Latte.getApplication(), LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(mRecyclerManager);
        ((DefaultItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        switch (MusicManager.getSource()) {
            case ICON_LOVE:
                mAdapter = new PlayerAdapter(new PlayerConverter().convert(LoveSong.class), delegate);
                break;
            case ICON_NATIVE:
                mAdapter = new PlayerAdapter(new PlayerConverter().convert(Song.class), delegate);
                break;
            case ICON_RECENTLY:
                mAdapter = new PlayerAdapter(new PlayerConverter().convert(RecentlySong.class), delegate);
                break;
            default:
                mAdapter = new PlayerAdapter(new PlayerConverter().convert(Song.class), delegate);
        }
        mRecyclerView.setAdapter(mAdapter);
        //滑动到指定位置
        MoveToPosition(mRecyclerManager, mRecyclerView, MusicManager.getPosition());
        //滑动事件
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int current = getStateRecyPos();
                Log.e("---------", "onScrollStateChanged: " + current);
                if (mAdapter.getData().size() == 0)
                    return;
                Song song = null;
                String playMode = MusicManager.getPlayMode();
                if (playMode.equals(ORDER_PLAY.name())) {
                    if (current == mAdapter.getData().size() - 1) {
                        current = 0;
                        song = mAdapter.getData().get(current).getField(MultipleFields.TAG);
                        mRecyclerView.scrollToPosition(current);
                    } else {
                        if (current >= 0) {
                            song = mAdapter.getData().get(current).getField(MultipleFields.TAG);
                        }
                    }
                } else {
                    randomPlay();
                    return;
                }
                addRecentlyData(song);
                if (MUSIC_PATH == null) {
                    MUSIC_PATH = song.path;
                } else {
                    if (MUSIC_PATH.equals(song.path)) {
                        return;
                    } else {
                        MUSIC_PATH = song.path;
                    }
                }
                mPlayer.setProgress(0);
                playMusic(song, current);
                addRecentlyData(song);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            }
        });
    }

    public void setNewRecyclerData() {
        mAdapter.getData().clear();
        switch (MusicManager.getSource()) {
            case ICON_LOVE:
                break;
            case ICON_NATIVE:
                mAdapter.setNewData(new PlayerConverter().convert(Song.class));
                break;
            case ICON_RECENTLY:
                mAdapter.setNewData(new PlayerConverter().convert(RecentlySong.class));
                break;
            default:
                mAdapter = new PlayerAdapter(new PlayerConverter().convert(Song.class), delegate);
        }
        if (MUSIC_PATH != null) {
            customPlay(MUSIC_PATH);
        }

    }

    /**
     * 播放音乐
     *
     * @param song 音乐对象
     */
    public void playMusic(Song song, int pos) {
        if (song != null) {
            play_song = song;
            MUSIC_PATH = song.path;
            mMusicBind.playMusic(song.path, new MusicService.OnPlayListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onContinue(boolean isContinue) {
                    if (isContinue && mObjectAnim != null) {
                        mObjectAnim.resume();
                        mPlayer.setProgress(mPlayer.getProgress());
                    } else {
                        mObjectAnim = null;
                    }
                }

                @Override
                public void onStart(int musicLength) {
                    //保存当前播放的位置
                    MusicManager.setPosition(pos);
                    //当前音乐位置发生变化，进行回调
                    song.duration = musicLength;
                    IGlobalCallback callBack = CallbackManager.getInstance().getCallBack(MusicManager.getSource());
                    if (callBack != null) {
                        callBack.executeCallBack(song);
                    }
                    setNowData();
                    startAnim(song.duration);
                }

                @Override
                public void onSave() {
                    mObjectAnim.clone();
                    play_song = null;
                    //继续播放下一个
                    setBelow();
                }
            });
            mPlayer.setPlay(true);
        }
    }

    /**
     * 播放网络音乐
     *
     * @param wrokSong
     */
    public void playNetWorkSong(NetWorkQQSong wrokSong) {
        if (wrokSong != null) {
            mMusicBind.playMusic(wrokSong.getUrl(), new MusicService.OnPlayListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onContinue(boolean isContinue) {
                    if (isContinue && mObjectAnim != null) {
                        mObjectAnim.resume();
                        mPlayer.setProgress(mPlayer.getProgress());
                    } else {
                        mObjectAnim = null;
                    }
                }

                @Override
                public void onStart(int musicLength) {
                    //开始播放动画
                    startAnim(musicLength);
                    //在recyclerview 中显示
                    int pos = getStateRecyPos();
                    if (pos == -1) {
                        List<MultipleItemEntity> list = new ArrayList<>();
                        list.add(NetWorkSongToSong(wrokSong, musicLength));
                        mAdapter.setNewData(list);
                    } else {
                        mAdapter.addData(pos, NetWorkSongToSong(wrokSong, musicLength));
                        mRecyclerView.scrollToPosition(pos);
                    }
                    setNowNetWorkData(wrokSong);
                    mPlayer.setPlay(true);
                    addRecentlyData(NetWorkSongToSong(wrokSong, musicLength).getField(MultipleFields.TAG));
                }

                @Override
                public void onSave() {
                    mObjectAnim.clone();
                    setBelow();
                }
            });
        }
    }

    private void startAnim(int duration) {
        //开始播放动画
        mObjectAnim = ObjectAnimator.
                ofInt(mPlayer, "Progress", 0, 360)
                .setDuration(duration);
        mObjectAnim.setInterpolator(new LinearInterpolator());
        mObjectAnim.start();
    }

    //监听
    public void addOnNowDataListener(OnNowSongDataListener nowSongDataListener) {
        mOnNowSongDataListener = nowSongDataListener;
    }

    /**
     * 如果 addOnNowDataListener方法设置了监听，则进行回调当前播放的音乐数据,
     * 每次切换音乐是，该方法被调用
     */
    private void setNowData() {
        Song song = mAdapter.getData().get(MusicManager.getPosition()).getField(MultipleFields.TAG);
        requestSongMessage(song, (netWorkQQSong -> {
            if (netWorkQQSong != null) {
                UrlToBitmap.urlToBitmap(netWorkQQSong.getPic(), bitmap -> {
                    //将数据保存起来
                    MultipleItemEntity build = MultipleItemEntity.builder()
                            .setField(PlayerItemFields.SONG, song)
                            .setField(PlayerItemFields.NETWORK_SONG, netWorkQQSong)
                            .setField(PlayerItemFields.BITMAP, bitmap).build();
                    if (mOnNowSongDataListener != null) {
                        mOnNowSongDataListener.nowSongData(build);
                    }
                    Log.e("----------", "setNowData: ");
                    mMusicBind.startNotif(bitmap);
                });
            } else {
                MultipleItemEntity build = MultipleItemEntity.builder()
                        .setField(PlayerItemFields.SONG, song).build();
                if (mOnNowSongDataListener != null) {
                    mOnNowSongDataListener.nowSongData(build);
                }
                mMusicBind.startNotif(null);
            }
        }));


    }

    private void setNowNetWorkData(NetWorkQQSong nowNetWorkData) {
        Song song = mAdapter.getData().get(MusicManager.getPosition()).getField(MultipleFields.TAG);
        play_song = song;
        if (nowNetWorkData != null) {
            UrlToBitmap.urlToBitmap(nowNetWorkData.getPic(), bitmap -> {
                //将数据保存起来
                MultipleItemEntity build = MultipleItemEntity.builder()
                        .setField(PlayerItemFields.SONG, song)
                        .setField(PlayerItemFields.NETWORK_SONG, nowNetWorkData)
                        .setField(PlayerItemFields.BITMAP, bitmap).build();
                if (mOnNowSongDataListener != null) {
                    mOnNowSongDataListener.nowSongData(build);
                }
                mMusicBind.startNotif(bitmap);
            });
        } else {
            mMusicBind.startNotif(null);
        }
    }


    /**
     * 上一首
     */
    public void setUp() {
        String playMode = MusicManager.getPlayMode();
        Song song;
        if (playMode.equals(ORDER_PLAY.name())) {
            int pos = MusicManager.getPosition();
            if (pos > 0) {
                int position = pos - 1;
                song = mAdapter.getData().get(position).getField(MultipleFields.TAG);
                mRecyclerView.scrollToPosition(position);
                playMusic(song, position);
            }
        } else if (playMode.equals(RANDOM_PLAY.name())) {
            randomPlay();
        } else {
            repetitionPlay();
        }
    }

    /**
     * 下一首
     */
    public void setBelow() {
        if (play_below != null) {
            customPlay(play_below);
            return;
        }
        Song song;
        String playMode = MusicManager.getPlayMode();
        if (playMode.equals(ORDER_PLAY.name())) {
            song = orderPlay();
        } else if (playMode.equals(RANDOM_PLAY.name())) {
            song = randomPlay();
        } else {
            song = repetitionPlay();
        }
        addRecentlyData(song);
    }

    /**
     * 单曲循环
     *
     * @return
     */
    private Song repetitionPlay() {
        Song song;
        int pos = MusicManager.getPosition();
        song = mAdapter.getData().get(pos).getField(MultipleFields.TAG);
        mRecyclerView.scrollToPosition(pos);
        playMusic(song, pos);
        return song;
    }

    /**
     * 随机播放
     *
     * @return
     */
    private Song randomPlay() {
        Song song;
        int count = mAdapter.getData().size();
        int pos = new Random().nextInt(count);
        song = mAdapter.getData().get(pos).getField(MultipleFields.TAG);
        mRecyclerView.scrollToPosition(pos);
        playMusic(song, pos);
        return song;
    }

    /**
     * 顺序播放
     *
     * @return
     */
    private Song orderPlay() {
        Song song;
        int pos = MusicManager.getPosition();
        int count = mAdapter.getData().size();
        if (pos == count - 1) {
            song = mAdapter.getData().get(0).getField(MultipleFields.TAG);
            mRecyclerView.scrollToPosition(0);
            playMusic(song, 0);
        } else {
            int position = pos + 1;
            song = mAdapter.getData().get(position).getField(MultipleFields.TAG);
            mRecyclerView.scrollToPosition(position);
            playMusic(song, position);
        }
        return song;
    }

    /**
     * 请求本地音乐的图片歌词等信息
     *
     * @param song
     */
    public void requestSongMessage(Song song, OnNowSongMessageListener listener) {
        RxRequest.onGetSongMessage(Resource.getString(R.string.music_url),
                song.getName(), song.singer, (flag, result) -> {
                    if (flag) {
                        JSONObject json = JSON.parseObject(result);
                        int code = json.getInteger("code");
                        if (code == 200) {
                            JSONArray data = JSON.parseArray(json.getString("data"));
                            NetWorkQQSong nowSong = data.getJSONObject(0).toJavaObject(NetWorkQQSong.class);
                            for (int i = 0; i < data.size(); i++) {
                                NetWorkQQSong netSong = data.getJSONObject(i).toJavaObject(NetWorkQQSong.class);
                                if (netSong.getName().equals(song.name) &&
                                        netSong.getAuthor().equals(song.singer)) {
                                    nowSong = netSong;
                                }
                            }
                            if (listener != null) {
                                listener.onSongMessage(nowSong);
                            }
                        }
                    } else {
                        if (listener != null) {
                            listener.onSongMessage(null);
                        }
                    }
                });
    }

    /**
     * 设置当前音乐的位置
     *
     * @param smec 毫秒为单位
     */
    public void seekTo(int smec) {
        mMusicBind.seekTo(smec);
    }

    /**
     * @return 返回当前是否播放
     */
    public boolean isPlay() {
        return mMusicBind.isPlay();
    }

    /**
     * @return 返回当前播放进度的位置
     */
    public int getCurrentPosition() {
        return mMusicBind.getCurrentPosition();
    }

    /**
     * 暂停播放
     */
    public void stopMusic() {
        mPlayer.setPlay(false);
        mMusicBind.pauseMusic();
    }

    /**
     * 开始播放
     */
    public void startMusic() {
        mPlayer.setPlay(true);
        int position = MusicManager.getPosition();
        Song song = mAdapter.getData().get(position).getField(MultipleFields.TAG);
        playMusic(song, position);
    }


    public void setUpdateListener(HomeDelegate.OnUpdateCountListener listener) {
        this.listener = listener;
    }

    public MultipleItemEntity NetWorkSongToSong(NetWorkQQSong workSong, int size) {
        Song song = new Song();
        song.name = workSong.getName();
        song.path = workSong.getUrl();
        song.singer = workSong.getAuthor();
        song.duration = size;
        return MultipleItemEntity.builder()
                .setItemType(PlayerItemType.PLAYER_LEFT_RIGHT)
                .setField(MultipleFields.TAG, song)
                .build();
    }

    /**
     * 判断当前的播放源是哪个，并且进行播放
     *
     * @param source 播放源
     * @param song   音乐对象
     */
    public void palayMusicSource(MusicManager.MusicSource source, Song song) {
        switch (source) {
            case ICON_LOVE:
                setAdapterData(source, LoveSong.class);
                break;
            case ICON_NATIVE:
                setAdapterData(source, Song.class);
                break;
            case ICON_RECENTLY:
                setAdapterData(source, RecentlySong.class);
                break;
            default:
        }
        customPlay(song);
    }

    /**
     * 从 adapter 中查找指定的音乐,
     *
     * @param song 指定的音乐
     */
    private void customPlay(Song song) {
        List<MultipleItemEntity> data = mAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            Song s = data.get(i).getField(MultipleFields.TAG);
            //如果路径相同，则直接定位到该音乐对象
            if (song.path.equals(s.path)) {
                MoveToPosition(mRecyclerManager, mRecyclerView, i);
                playMusic(s, i);
                Toast.makeText(Latte.getApplication(), song.name + "" + i, Toast.LENGTH_SHORT).show();
                break;
            }
        }
        play_below = null;
    }

    /**
     * 从 adapter 中查找指定的音乐地址,
     *
     * @param url 指定的音乐地址
     */
    private void customPlay(String url) {
        List<MultipleItemEntity> data = mAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            Song s = data.get(i).getField(MultipleFields.TAG);
            //如果路径相同，则直接定位到该音乐对象
            if (url.equals(s.path)) {
                MoveToPosition(mRecyclerManager, mRecyclerView, i);
                playMusic(s, i);
                break;
            }
        }
    }

    /**
     * RecyclerView 移动到当前位置，
     *
     * @param manager       设置RecyclerView对应的manager
     * @param mRecyclerView 当前的RecyclerView
     * @param n             要跳转的位置
     */
    public static void MoveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {
        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }
    }


    /**
     * 添加最近播放
     *
     * @param song
     */
    public void addRecentlyData(Song song) {
        if (MusicManager.getSource() != MusicManager.MusicSource.ICON_RECENTLY
                && song != null) {
            LitePal.deleteAll(RecentlySong.class, "path=?", song.path);
            RecentlySong recentlySong = new RecentlySong(song.name, song.singer, song.size,
                    song.duration, song.path, song.albumId, song.id);
            recentlySong.save();
            MusicManager.setMusicSize(RECENTLY_COUNT, LitePal.findAll(RecentlySong.class).size());
            //刷新最近播放的数量
            if (listener != null) {
                listener.onUpdate();
            }
        }
    }

    private Song getStateRecyItem() {
        int pos = getStateRecyPos();
        if (pos == -1) {
            return null;
        }
        return mAdapter.getData().get(pos).getField(MultipleFields.TAG);
    }

    /**
     * @return Recyclerview 当前显示的位置
     */
    private int getStateRecyPos() {
        int size = 0;
        if (MusicManager.getSource() == MusicManager.MusicSource.ICON_RECENTLY) {
            size = MusicManager.getMusicSize(MusicManager.SourceCount.RECENTLY_COUNT);
        } else if (MusicManager.getSource() == MusicManager.MusicSource.ICON_NATIVE) {
            size = MusicManager.getMusicSize(MusicManager.SourceCount.NATIVE_COUNT);
        } else if (MusicManager.getSource() == MusicManager.MusicSource.ICON_LOVE) {
            size = MusicManager.getMusicSize(MusicManager.SourceCount.LOVE_COUNT);
        }
        if (size == 0) {
            return -1;
        }
        return mRecyclerManager.findFirstVisibleItemPosition();
    }

    /**
     * 设置播放源
     *
     * @param source 播放源
     * @param table  表
     */
    private void setAdapterData(MusicManager.MusicSource source, Class table) {
        if (!source.equals(MusicManager.getSource())) {
            //设置新的播放源
            MusicManager.setSource(source);
            //更新数据
            mAdapter.setNewData(new PlayerConverter().convert(table));
        }
    }

    /**
     * 绑定service
     */
    private void startMusicService() {
        if (mServiceIntent == null) {
            mServiceIntent = new Intent(Latte.getApplication(), MusicService.class);
            Latte.getApplication().startService(mServiceIntent);
        }
        if (!isBinderService) {
            isBinderService = true;
            Latte.getApplication().bindService(mServiceIntent, conn, Context.BIND_AUTO_CREATE);
        }
    }

    /**
     * 解除绑定
     */
    public void destory() {
        if (isBinderService) {
            isBinderService = false;
            Latte.getApplication().stopService(mServiceIntent);
            Latte.getApplication().unbindService(conn);
        }
    }

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mMusicBind = (MusicService.MusicBind) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
}
