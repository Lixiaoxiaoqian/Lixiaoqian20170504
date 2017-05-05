package lixiaoqian.bwie.com.lixiaoqian20170504;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import xlistview.bawei.com.xlistviewlibrary.XListView;

/**
 * @类的用途：应用的主界面，展示数据
 * @author: 李晓倩
 * @date: 2017/5/4
 */

public class MainActivity extends AppCompatActivity {

    private XListView xlv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initControl();
    }

    private void initControl() {
        //网络请求工具类
        MyXutil myXutil = new MyXutil(this, xlv);
        myXutil.get(UrL.url1);
    }

    private void initView() {
        xlv = (XListView) findViewById(R.id.xlv);
    }


}
