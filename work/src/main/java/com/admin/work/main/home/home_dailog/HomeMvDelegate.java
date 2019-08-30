package com.admin.work.main.home.home_dailog;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.admin.core.deleggate.LatteDelegate;
import com.admin.work.R;
import com.admin.work.R2;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;


public class HomeMvDelegate extends LatteDelegate {

    String pic;
    String mvUrl;

    @BindView(R2.id.delegate_home_jzvd)
    JzvdStd mJzvdStd = null;

    private HomeMvDelegate(String pic ,String mvUrl){
        this.pic = pic;
        this.mvUrl = mvUrl;
    }

    public static HomeMvDelegate getInstence(String pic ,String mvUrl){
        return new HomeMvDelegate(pic,mvUrl);
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_home_mv;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        mJzvdStd.setUp(mvUrl, "");
//        JzvdStd.startFullscreenDirectly(getContext(), JzvdStd.class,mvUrl, "饺子辛苦了");
        Glide.with(getContext())
                .load(pic)
                .into(mJzvdStd.thumbImageView);
    }

    @Override
    public void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    public boolean onBackPressedSupport() {
        getSupportDelegate().pop();
        Jzvd.releaseAllVideos();
        return true;
    }
}
