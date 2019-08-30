package com.admin.work.main.home.setting;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.admin.core.app.AccountManager;
import com.admin.core.app.IUserChecker;
import com.admin.core.app.Latte;
import com.admin.work.R;
import com.admin.work.main.home.list.ListBean;
import com.admin.work.sign.SignInDelegate;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;

public class SettingOnclickListener extends SimpleClickListener {
    private Context context;
    SettingDelegate delegate;

    public SettingOnclickListener(Context context, SettingDelegate delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ListBean list = (ListBean) baseQuickAdapter.getData().get(position);
        switch (list.getId()) {
            case 4:
                AlertDialog.Builder builder = new AlertDialog.Builder(delegate.getContext());
                builder.setTitle("警告");
                builder.setMessage("是否退出登录?");
                builder.setNegativeButton("否", null);
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AccountManager.checkAccount(new IUserChecker() {
                            @Override
                            public void onSignIn(String SignState, String SignNumber) {
                                Log.e(TAG, "onSignIn: "+SignNumber+"-----"+SignState );
                                AccountManager.setSignState(false);
                                delegate.getParentDelegate().getSupportDelegate().pop();
                                delegate.getParentDelegate().getSupportDelegate().replaceFragment(new SignInDelegate(), false);
                            }

                            @Override
                            public void onNoSignIn() {
                            }
                        });

                    }
                });
                builder.show();
                break;
            default:
        }
    }

    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
