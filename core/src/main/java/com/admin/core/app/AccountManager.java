package com.admin.core.app;

import com.admin.core.util.storage.LattePreference;

/**
 * Copyright (C)
 *
 * @file: AccountManager
 * @author: 345
 * @Time: 2019/4/23 17:54
 * @description: ${DESCRIPTION}
 */
public class AccountManager {

    public enum SignIn{
        /**
         *  qq 登录
         */
        QQ_SIGNIN,
        /**
         *  手机号 登录
         */
        PHONE_SIGNIN,
        /**
         *  账号登录
         */
        NUMBER_SIGNIN

    }

    private enum SignTag{
        /**
         * 登录的状态
         */
        SIGN_TAG,
        /**
         *  登录的方式
         */
        SIGN_IN,
        /**
         *  登录的账号
         */
        SIGN_NUMBER
    }

    /**
     * @param state 设置登陆的状态 ,登录的方式 ，登录的账号
     */
    public static void setSignState(boolean state,SignIn signIn,String number){
        LattePreference.setAppFlag(SignTag.SIGN_TAG.name(),state);
        LattePreference.setAppData(SignTag.SIGN_IN.name(),signIn.name());
        LattePreference.setAppData(SignTag.SIGN_NUMBER.name(),number);
    }

    /**
     * 设置登录的状态
     * @param state 状态
     */
    public static void setSignState(boolean state){
        LattePreference.setAppFlag(SignTag.SIGN_TAG.name(),state);
    }

    /**
     * @return 返回用户是否登陆
     */
    private static boolean isSignIn(){
        return LattePreference.getAppFlag(SignTag.SIGN_TAG.name());
    }



    /**
     * @return 返回用户的登录方式
     */
    private static String getSignIn(){
        return LattePreference.getAppData(SignTag.SIGN_IN.name());
    }

    /**
     * @return 返回用户登录的账号
     */
    public static String getSignInNumber(){
        return LattePreference.getAppData(SignTag.SIGN_NUMBER.name());
    }

    /**
     * @param checker 登录状态的回调
     */
    public static void checkAccount( IUserChecker checker){
        if (isSignIn()){
            checker.onSignIn(getSignIn(),getSignInNumber());
        }else {
            checker.onNoSignIn();
        }
    }
}
