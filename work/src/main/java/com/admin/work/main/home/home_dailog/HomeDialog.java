package com.admin.work.main.home.home_dailog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.admin.core.app.Latte;
import com.admin.core.app.MusicManager;
import com.admin.core.deleggate.LatteDelegate;
import com.admin.core.net.rx.RxRequest;
import com.admin.core.ui.view.player.Player;
import com.admin.core.util.callback.CallBackType;
import com.admin.core.util.callback.CallbackManager;
import com.admin.core.util.callback.IGlobalCallback;
import com.admin.core.util.value.Resource;
import com.admin.work.R;
import com.admin.work.main.home.icon_native.NativeDelegate;
import com.admin.work.main.home.search.NetWorkQQSong;
import com.admin.work.main.player.OnNowSongMessageListener;
import com.admin.work.main.player.PlayerControl;
import com.admin.work.main.player.nativemusic.Song;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.joanzapata.iconify.widget.IconTextView;

import java.io.File;

import cn.sharesdk.onekeyshare.OnekeyShare;
import me.yokeyword.fragmentation.ISupportFragment;

public class HomeDialog implements View.OnClickListener {
    private final AlertDialog DIALOG;
    private final LatteDelegate DELEGATE;
    private PlayerControl playerContro = PlayerControl.getInstence();
    private Song song;

    public HomeDialog(LatteDelegate delegate) {
        this.DELEGATE = delegate;
        DIALOG = new AlertDialog.Builder(DELEGATE.getContext()).create();
    }

    public final void beginMoreDialog(Song song) {
        this.song = song;
        DIALOG.show();
        Window window = DIALOG.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_song_more);
            window.setGravity(Gravity.BOTTOM);
            //设置动画
            window.setWindowAnimations(R.style.anim_panel_up_form_bottom);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //设置属性
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.dimAmount = 0.5f;
            window.setAttributes(params);

            //设置监听
            AppCompatTextView name = window.findViewById(R.id.home_dialog_song_name);
            AppCompatTextView path = window.findViewById(R.id.home_dialog_song_path);
            window.findViewById(R.id.home_dialog_song_next_play).setOnClickListener(this);
            IconTextView textView = window.findViewById(R.id.home_dialog_song_download_icon);
            if (song.path.startsWith("http")) {
                textView.setTextColor(Color.parseColor("#ffffff"));
                textView.setEnabled(true);
            } else {
                textView.setTextColor(Color.parseColor("#999999"));
                textView.setEnabled(false);
            }

            window.findViewById(R.id.home_dialog_song_shard).setOnClickListener(this);
            window.findViewById(R.id.home_dialog_song_delete).setOnClickListener(this);
            window.findViewById(R.id.home_dialog_song_singer).setOnClickListener(this);
            window.findViewById(R.id.home_dialog_song_album).setOnClickListener(this);
            window.findViewById(R.id.home_dialog_song_play_video).setOnClickListener(this);
            window.findViewById(R.id.home_dialog_song_message).setOnClickListener(this);
            name.setText(song.name);
            path.setText(song.path);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.home_dialog_song_next_play) {
            if (song != null) {
                PlayerControl.play_below = song;
                Toast.makeText(Latte.getApplication(), "下一首播放" + song.name, Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.home_dialog_song_download) {
            String dir = "天天动听";
            if (song != null && !song.getPath().equals("")) {
                playerContro.requestSongMessage(song, netWorkQQSong -> {
                    Toast.makeText(Latte.getApplication(), "开始下载", Toast.LENGTH_SHORT).show();
                    RxRequest.dowloadFile(netWorkQQSong.getUrl(), dir,
                            netWorkQQSong.getName() + "-" + netWorkQQSong.getAuthor() + ".mp3",
                            (flag, file) -> {
                                if (flag) {
                                    Toast.makeText(Latte.getApplication(), "下载成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Latte.getApplication(), "下载失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                });
            } else {
                Toast.makeText(Latte.getApplication(), "找不到下载路径", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.home_dialog_song_shard) {
            OnekeyShare oks = new OnekeyShare();
            oks.setTitle("天天动听");
            oks.setTitleUrl("http://bbs.mob.com/forum.php?mod=viewthread&tid=24653&extra=page%3D2");
            oks.show(Latte.getApplication());
        } else if (id == R.id.home_dialog_song_delete) {
            File file = new File(song.path);
            if (file.isFile()) {
                if (file.delete()) {
                    DIALOG.cancel();
                    Toast.makeText(Latte.getApplication(), "删除成功", Toast.LENGTH_SHORT).show();
                    IGlobalCallback callBack = CallbackManager.getInstance().getCallBack(CallBackType.DELETE_MUSIC);
                    MusicManager.updateMedia(DELEGATE.getContext());
                    if (callBack != null) {
                        callBack.executeCallBack(song);
                    }
                } else {
                    Toast.makeText(Latte.getApplication(), "删除失败", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (id == R.id.home_dialog_song_singer) {
            Toast.makeText(Latte.getApplication(), "" + song.getSinger(), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.home_dialog_song_album) {
            Toast.makeText(Latte.getApplication(), "没有找到专辑信息", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.home_dialog_song_play_video) {
            //获取mv
            PlayerControl.getInstence().requestSongMessage(song, netWorkQQSong -> {
                if (netWorkQQSong != null) {
                    RxRequest.onGetRx(DELEGATE.getContext(),Resource.getString(R.string.music_details), new String[]{"id"},
                            new Object[]{netWorkQQSong.getSongid()}, (flag, result) -> {
                                if (flag) {
                                    JSONObject json = JSON.parseObject(result);
                                    if (json.getInteger("code") == 200) {
                                        json = json.getJSONArray("data").getJSONObject(0);
                                        JSONObject mv = json.getJSONObject("mv");
                                        String mvId = mv.getString("vid");
                                        requestMv(mvId);
                                    }
                                } else {
                                    DIALOG.cancel();
                                    Toast.makeText(Latte.getApplication(), "没有找到视频", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
        } else if (id == R.id.home_dialog_song_message) {
            Toast.makeText(Latte.getApplication(), "暂无信息", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestMv(String mvId) {
        //获取mv 信息
        RxRequest.onGetRx(DELEGATE.getContext(),Resource.getString(R.string.mv_qq_message), new String[]{"id"},
                new Object[]{mvId}, (flag, result) -> {
                    if (flag) {
                        JSONObject json = JSON.parseObject(result);
                        if (json.getInteger("code") == 200) {
                            JSONObject data = json.getJSONObject("data");
                            JSONObject mv = data.getJSONObject(mvId);
                            if (mv != null) {
                                String pic = mv.getString("cover_pic");
                                String mvUrl = Resource.getString(R.string.mv_qq_url) + "?id=" + mvId;
                                DIALOG.cancel();
                                DELEGATE.getParentDelegate().getSupportDelegate()
                                        .start(HomeMvDelegate.getInstence(pic, mvUrl));
                            }
                        }
                    } else {
                        Toast.makeText(Latte.getApplication(), "没有找到视频", Toast.LENGTH_SHORT).show();
                        DIALOG.cancel();
                    }
                });
    }
}
