package lixiaoqian.bwie.com.lixiaoqian20170504;

import android.app.Application;

import org.xutils.x;

/**
 * @类的用途：应用的入口
 * @author: 李晓倩
 * @date: 2017/5/4
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
    }
}
