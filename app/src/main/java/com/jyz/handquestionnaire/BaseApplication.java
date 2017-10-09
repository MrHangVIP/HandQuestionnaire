package com.jyz.handquestionnaire;

import android.app.Application;

import com.jyz.handquestionnaire.util.SpfUtil;

/**
 * Created by Songzhihang on 2017/10/5.
 */
public class BaseApplication extends Application{

    private static BaseApplication instance;

    public static BaseApplication getAPPInstance(){
        return instance;
    }

//    private UserItem mUser;
//
//    private static List<EmojiItem> emojiItemList=new ArrayList<>();
//
//    {
//        PlatformConfig.setWeixin("wxf4b8773580a65693", "5dd54d261fe64389eb3c79b4de4b9319");
//        PlatformConfig.setSinaWeibo("1862317283", "dc5e758fd07b84055f949875ad001ffb","http://sns.whalecloud.com");
//        PlatformConfig.setQQZone("1106050992", "dBN4CCRba77fxp7k");
//
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        //初始化缓存
        SpfUtil.init(this);
//        Config.DEBUG = true;
//        UMShareAPI.get(this);
    }
}
