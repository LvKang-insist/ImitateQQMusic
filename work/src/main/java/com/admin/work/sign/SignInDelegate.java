package com.admin.work.sign;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.admin.core.deleggate.LatteDelegate;
import com.admin.core.net.rx.RxRequest;
import com.admin.core.util.value.Resource;
import com.admin.work.R;
import com.admin.work.R2;
import com.admin.work.litepal.AccountTable;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import io.reactivex.internal.operators.single.SingleHide;

/**
 * Copyright (C)
 *
 * @file: SignInDelegate
 * @author: 345
 * @Time: 2019/4/22 14:34
 * @description: 登录界面
 */
public class SignInDelegate extends LatteDelegate {

    private static final String TAG = "SignInDelegate";


    @BindView(R2.id.edit_sign_in_phone)
    TextInputEditText mPhone = null;
    @BindView(R2.id.edit_sign_in_password)
    TextInputEditText mPassword = null;

    ISignListener mIsignListener = null;
    private AccountTable account;

    /**
     * @param activity 当前的Activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //判断 当前的 activity 有没有实现这个接口
        if (activity instanceof ISignListener) {
            //向上转型
            mIsignListener = (ISignListener) activity;
        }
    }

    @OnClick(R2.id.btn_sign_in)
    void onClickSignIn() {
        if (checkForm()) {
            //上传头像
            upLoadingImage(account.getPhone(),"error");
            SignHandler.onSignIn(getContext(), account, mIsignListener);
        }
    }

    /**
     * 微信登录
     */
    @OnClick(R2.id.icon_sign_in_wechat)
    void onClickWeiChart() {
        Toast.makeText(_mActivity, "调起 微信登录失败", Toast.LENGTH_SHORT).show();
    }

    /**
     * qq 登录
     */
    @OnClick(R2.id.icon_sign_in_qq)
    void onClickQQ() {
        Platform plat = ShareSDK.getPlatform(QQ.NAME);
        plat.removeAccount(true);
        plat.showUser(null);
        plat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                try {
                    final JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name", hashMap.get("nickname"));
                    jsonObject.put("gender", hashMap.get("gender"));
                    jsonObject.put("figureurl_qq", hashMap.get("figureurl_qq"));
                    //上传头像
                    upLoadingImage((String) hashMap.get("nickname"),(String) hashMap.get("figureurl_qq"));
                    SignHandler.onQqSignIn(getContext(), jsonObject.toJSONString(), mIsignListener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
    }

    /**
     * 短信验证
     */
    @OnClick(R2.id.icon_sign_in_SMS)
    void onClickSMS() {
        RegisterPage page = new RegisterPage();
        page.setTempCode(null);
        page.setRegisterCallback(new EventHandler() {
            @Override
            public void afterEvent(int i, int i1, Object o) {
                if (i1 == SMSSDK.RESULT_COMPLETE) {
                    try {
                        HashMap<String, Object> phoneMap = (HashMap<String, Object>) o;
                        JSONObject jsonObject = new JSONObject();
                        String phone = (String) phoneMap.get("phone");
                        jsonObject.put("phone", phone);
                        //上传头像
                        upLoadingImage(phone,"error");
                        SignHandler.onPhoneSignIn(getContext(), jsonObject.toJSONString(), mIsignListener);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext(), "验证码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        page.show(getActivity());
    }

    @OnClick(R2.id.tv_link_sign_up)
    void onClickLink() {
        getSupportDelegate().start(new SignUpDelegate());
    }

    private boolean checkForm() {
        final String phone = mPhone.getText().toString();
        final String password = mPassword.getText().toString();
        boolean isPass = true;

        //判断邮箱格式是否正确
        if (phone.isEmpty() || phone.length() != 11) {
            mPhone.setError("错误的手机格式");
            isPass = false;
        } else {
            mPhone.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            mPassword.setError("请至少填写 6位数的密码");
            isPass = false;
        } else {
            mPassword.setError(null);
        }
        if (isPass) {
            account = new AccountTable();
            account.setPhone(phone);
            account.setPassword(password);
        }
        return isPass;
    }

    public void upLoadingImage(String name,String image){
        Map<String,String> map = new HashMap<>();
        map.put("name",name);
        map.put("icon",image);
        String url = Resource.getString(R.string.upLoadingImage);
        RxRequest.onPostRx(getContext(), url, JSON.toJSONString(map), (flag, result) -> {
            if (flag){
                JSONObject jsonObject = JSON.parseObject(result);
                if (jsonObject.getString("result").equals("success")){
                    Log.d(TAG, "upLoadingImage: SUCCESS");
                }else {
                    Log.d(TAG, "upLoadingImage: ERROR");
                }
            }
        });
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_sign_in;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
    }
}
