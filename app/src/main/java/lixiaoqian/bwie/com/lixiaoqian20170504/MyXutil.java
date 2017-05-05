package lixiaoqian.bwie.com.lixiaoqian20170504;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.List;

import xlistview.bawei.com.xlistviewlibrary.XListView;


/**
 * @类的用途：xutils网络请求工具类
 * @author: 李晓倩
 * @date: 2017/5/4
 */

public class MyXutil {
    private Context mContext;
    private XListView mXListView;
    private Handler hander=new Handler(){

    };
    public MyXutil(Context context, XListView XListView) {
        mContext = context;
        mXListView = XListView;
    }

    public void  onLoad(){
        mXListView.stopLoadMore();
        mXListView.stopRefresh();
        mXListView.setRefreshTime("刚刚");
    }

    public void get(String s) {
        RequestParams params = new RequestParams(s);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                GsonBean gsonBean = GsonUtil.GsonToBean(result, GsonBean.class);
                final List<GsonBean.AppBean> app = gsonBean.getApp();
                Log.d("zzz", app.size() + "zzz");
                MyBaseAdapter adapter = new MyBaseAdapter(mContext, app);
                mXListView.setAdapter(adapter);


                mXListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                       chooseNet(app.get(position+1).getUrl());
                    }
                });
            }

            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                GsonBean gsonBean = GsonUtil.GsonToBean(UrL.json, GsonBean.class);
                final List<GsonBean.AppBean> app = gsonBean.getApp();
                Log.d("zzz", app.size() + "zzz");
                MyBaseAdapter adapter = new MyBaseAdapter(mContext, app);
                mXListView.setAdapter(adapter);
                mXListView.setPullRefreshEnable(true);
                mXListView.setPullLoadEnable(true);
                mXListView.setXListViewListener(new XListView.IXListViewListener() {
                    @Override
                    public void onRefresh() {
                        hander.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                onLoad();
                            }
                        }, 2000);
                        //onLoad();
                    }

                    @Override
                    public void onLoadMore() {
                        hander.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                onLoad();
                            }
                        }, 2000);
                    }
                });

                mXListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        chooseNet(app.get(position-1).getUrl());
                        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
                        checkBox.setChecked(true);
                    }
                });
            }

            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {

            }
        });
    }



    public void chooseNet(final String str){
            new AlertDialog.Builder(mContext)
                    .setTitle("网络选择")
                    .setIcon(R.mipmap.ic_launcher)
                    .setSingleChoiceItems(new String[]{"wifi", "手机流量"}, 0,
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0) {
                                        updateVersion(str);
                                    } else {
                                        setNetworkMethod(mContext);
                                    }
                                    dialog.dismiss();
                                }
                            }
                    )
                    .show();
        }

        private void download(String url){
            RequestParams params = new RequestParams(url);
            //自定义保存路径，Environment.getExternalStorageDirectory()：SD卡的根目录
            params.setSaveFilePath(Environment.getExternalStorageDirectory()+"/myapp/");
            //自动为文件命名
            params.setAutoRename(true);
            Log.d("zzz","zzz"+url);
            x.http().post(params, new Callback.ProgressCallback<File>() {

                @Override
                public void onSuccess(File result) {
                    //apk下载完成后，调用系统的安装方法
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(result), "application/vnd.android.package-archive");
                    mContext.startActivity(intent);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                   Log.d("zzz","zzz");
                }
                @Override
                public void onCancelled(CancelledException cex) {
                }
                @Override
                public void onFinished() {
                }
                //网络请求之前回调
                @Override
                public void onWaiting() {
                }
                //网络请求开始的时候回调
                @Override
                public void onStarted() {
                }
                //下载的时候不断回调的方法
                @Override
                public void onLoading(long total, long current, boolean isDownloading) {
                    //当前进度和文件总大小
                    Log.i("JAVA","current："+ current +"，total："+total);
                }
            });
        }

        public  void setNetworkMethod(final Context context){
        //提示对话框
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("网络设置提示").setMessage("是否打开wifi设置?").setPositiveButton("设置", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Intent intent=null;
                //判断手机系统的版本  即API大于10 就是3.0或以上版本
                if(android.os.Build.VERSION.SDK_INT>10){
                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                }else{
                    intent = new Intent();
                    ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                context.startActivity(intent);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        }).show();
    }

        public void updateVersion(final String uri){
            new AlertDialog.Builder(mContext)
                    .setTitle("版本更新")
                    .setMessage("现在检测到新版本,是否更新？")
                    .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            download(uri);
                        }
                    })
                    .show();

        }
}
