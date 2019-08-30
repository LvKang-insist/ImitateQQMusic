package com.admin.frame;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.admin.core.activitys.ProxyActivity;
import com.admin.core.app.Latte;
import com.admin.core.deleggate.LatteDelegate;
import com.admin.core.ui.loader.ILauncherListener;
import com.admin.core.ui.loader.OnLauncherFinishTag;
import com.admin.core.util.callback.CallBackType;
import com.admin.core.util.callback.CallbackManager;
import com.admin.core.util.callback.IGlobalCallback;
import com.admin.work.launcher.LauncherDelegate;
import com.admin.work.main.EcBottomDelegate;
import com.admin.work.main.player.PlayerControl;
import com.admin.work.sign.ISignListener;
import com.admin.work.sign.SignInDelegate;
import com.gyf.immersionbar.ImmersionBar;

public class ExampleActivity extends ProxyActivity implements
        ISignListener , ILauncherListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!= null){
            actionBar.hide();
        }
        //沉浸式状态栏
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    public LatteDelegate setRootDelegate() {
        return new LauncherDelegate();
    }

    @Override
    public void onLauncherFinish(OnLauncherFinishTag onLauncherFinishTag) {
        switch (onLauncherFinishTag) {
            case SIGNED:
                getSupportDelegate().startWithPop(new EcBottomDelegate());
                break;
            case NOT_SIGNED:
                getSupportDelegate().startWithPop(new SignInDelegate());
                break;
            default:
                break;
        }
    }


    @Override
    public void onSignInSuccess() {
        getSupportDelegate().startWithPop(new EcBottomDelegate());
    }
    @Override
    public void onSignUpSuccess() {
        Toast.makeText(this, "注册成功,请点击下方按钮进行登录", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        PlayerControl.getInstence().destory();
        IGlobalCallback callBack = CallbackManager.getInstance().getCallBack(CallBackType.STOP_NOTIF);
        if (callBack != null) {
            callBack.executeCallBack(null);
        }
        super.onDestroy();
    }
}
