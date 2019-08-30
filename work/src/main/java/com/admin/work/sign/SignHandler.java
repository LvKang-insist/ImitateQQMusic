package com.admin.work.sign;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.admin.core.app.AccountManager;
import com.admin.core.app.Latte;
import com.admin.core.net.rx.RxRequest;
import com.admin.core.net.rx.RxRestClient;
import com.admin.core.ui.loader.LatteLoader;
import com.admin.core.util.value.Resource;
import com.admin.work.R;
import com.admin.work.litepal.AccountTable;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class SignHandler {
    /**
     * 进行注册
     */
    @SuppressLint("CheckResult")
    public static void onSignUp(Context context, AccountTable account, ISignListener signListener) {
        startLoader(context);
        String url = Resource.getString(R.string.Url_signUp);
        String json = JSON.toJSONString(account);
        RxRequest.onPostRx(context, url, json, (flag, result) -> {
            if (flag) {
                try {
                    JSONObject jsonObject = JSON.parseObject(result);
                    String string = jsonObject.getString(Resource.getString(R.string.result));
                    if (Resource.getString(R.string.success).equals(string)) {
                        signListener.onSignUpSuccess();
                    } else if (Resource.getString(R.string.failure).equals(string)) {
                        Toast.makeText(Latte.getApplication(), Resource.getString(R.string.Phone_existence), Toast.LENGTH_SHORT).show();
                    } else if (Resource.getString(R.string.error).equals(string)) {
                        Toast.makeText(Latte.getApplication(), Resource.getString(R.string.ServiceException), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(Latte.getApplication(), Resource.getString(R.string.NumAndPassError), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Latte.getApplication(), Resource.getString(R.string.NumAndPassError), Toast.LENGTH_SHORT).show();
            }
            LatteLoader.stopLoading();

        });
    }

    public static void onSignIn(Context context, AccountTable account, ISignListener signListener) {
        startLoader(context);
        String url = Resource.getString(R.string.Url_signIn);
        String json = JSON.toJSONString(account);
        RxRequest.onPostRx(context, url, json, ((flag, result) -> {
            if (flag) {
                try {
                    JSONObject jsonObject = JSON.parseObject(result);
                    String string = jsonObject.getString(Resource.getString(R.string.result));
                    String state = jsonObject.getString(Resource.getString(R.string.state));
                    String sign = jsonObject.getString(Resource.getString(R.string.sign));
                    if (Resource.getString(R.string.success).equals(string)) {
                        signListener.onSignInSuccess();
                        AccountManager.setSignState(true, AccountManager.SignIn.NUMBER_SIGNIN, sign);
                    } else if (Resource.getString(R.string.failure).equals(string)) {
                        Toast.makeText(Latte.getApplication(), Resource.getString(R.string.NumAndPassError), Toast.LENGTH_SHORT).show();
                    } else if (Resource.getString(R.string.error).equals(string)) {
                        Toast.makeText(Latte.getApplication(), Resource.getString(R.string.ServiceException), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(Latte.getApplication(), Resource.getString(R.string.NetWorkError), Toast.LENGTH_SHORT).show();
            }
            LatteLoader.stopLoading();

        }));
    }

    public static void onQqSignIn(Context context, String json, ISignListener iSignListener) {
        startLoader(context);
        String url = Resource.getString(R.string.Url_QqSignIn);
        RxRequest.onPostRx(context, url, json, ((flag, result) -> {
            if (flag) {
                JSONObject jsonObject = JSON.parseObject(result);
                String string = jsonObject.getString(Resource.getString(R.string.result));
                String state = jsonObject.getString(Resource.getString(R.string.state));
                String sign = jsonObject.getString(Resource.getString(R.string.sign));
                if (Resource.getString(R.string.success).equals(string)) {
                    iSignListener.onSignInSuccess();
                    AccountManager.setSignState(true, AccountManager.SignIn.QQ_SIGNIN, sign);
                } else if (Resource.getString(R.string.error).equals(string)) {
                    Toast.makeText(Latte.getApplication(), Resource.getString(R.string.ServiceException), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Latte.getApplication(), Resource.getString(R.string.NetWorkError), Toast.LENGTH_SHORT).show();
            }
            LatteLoader.stopLoading();
        }));
    }

    public static void onPhoneSignIn(Context context, String json, ISignListener iSignListener) {
        startLoader(context);
        String url = Resource.getString(R.string.Url_PhoneSignIn);
        RxRequest.onPostRx(context, url, json, ((flag, result) -> {
            if (flag) {
                JSONObject jsonObject = JSON.parseObject(result);
                String string = jsonObject.getString(Resource.getString(R.string.result));
                String state = jsonObject.getString(Resource.getString(R.string.state));
                String sign = jsonObject.getString(Resource.getString(R.string.sign));
                if (Resource.getString(R.string.success).equals(string)) {
                    iSignListener.onSignInSuccess();
                    AccountManager.setSignState(true, AccountManager.SignIn.PHONE_SIGNIN, sign);
                } else if (Resource.getString(R.string.error).equals(string)) {
                    Toast.makeText(Latte.getApplication(), Resource.getString(R.string.ServiceException), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Latte.getApplication(), Resource.getString(R.string.NetWorkError), Toast.LENGTH_SHORT).show();
            }
            LatteLoader.stopLoading();
        }));
    }

    public static void startLoader(Context context) {
        Latte.getHandler().post(() -> LatteLoader.showLoading(context));
    }

}
