package com.vboss.okline.ui.home;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.ui.home.download.DownLoadManager;
import com.vboss.okline.ui.home.download.DownLoadService;
import com.vboss.okline.ui.home.download.ListAdapter;
import com.vboss.okline.ui.home.download.TaskInfo;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.view.widget.OKCardView;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class DownloadActivity extends BaseActivity {
    private static final String TAG = DownloadActivity.class.getSimpleName();

    @BindView(R.id.titleView)
    TextView titleView;
    @BindView(R.id.update_listView)
    ListView listview;
//    @BindView(R.id.progressBar)
//    ProgressBar progressBar;

    @BindView(R.id.download_logo)
    ImageView download_logo;

    //modify by yuanshaoyu 2017-06-21
//    @BindView(R.id.logoView)
//    LogoView logoView;
//    //Added by yuanshaoyu 2017-06-21
//    @BindView(R.id.ocardView)
//    OKCardView okCardView;

    @BindView(R.id.wait)
    TextView wait;

    private Handler handler = new Handler();
    public static String filepath = null;

    /*使用DownLoadManager时只能通过DownLoadService.getDownLoadManager()的方式来获取下载管理器，不能通过new DownLoadManager()的方式创建下载管理器*/
    private DownLoadManager manager;
    private ListAdapter adapter;
    String url;
    String versionName;
    private static final String ACTION_CONNECT_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ButterKnife.bind(this);
        registerNetWorkBroadcastReceiver();
        /*获取下载管理器*/
        manager = DownLoadService.getDownLoadManager();
        //progressBar.setProgress(50);
        String title = getIntent().getStringExtra("title");
        titleView.setText(title);
        url = getIntent().getStringExtra("url");
        versionName = getIntent().getStringExtra("versionName");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            //下载管理器需要启动一个Service,在刚启动应用的时候需要等Service启动起来后才能获取下载管理器，所以稍微延时获取下载管理器
            mHandler.sendEmptyMessageDelayed(1, 50);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //下载管理器需要启动一个Service,在刚启动应用的时候需要等Service启动起来后才能获取下载管理器，所以稍微延时获取下载管理器
                mHandler.sendEmptyMessageDelayed(1, 50);
            } else {
                finish();
            }
        }
    }

    public static void intentIn(Context context, String title, String url, String versionName) {
        context.startActivity(new Intent(context, DownloadActivity.class)
                .putExtra("title", title)
                .putExtra("url", url)
                .putExtra("versionName", versionName));
    }

    @Override
    public void onBackPressed() {
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            /*设置用户ID，客户端切换用户时可以显示相应用户的下载任务*/
            manager.changeUser("luffy");
            /*断点续传需要服务器的支持，设置该项时要先确保服务器支持断点续传功能*/
            manager.setSupportBreakpoint(true);
            adapter = new ListAdapter(DownloadActivity.this, manager);
            adapter.setActivity(DownloadActivity.this);
            listview.setAdapter(adapter);

            int index = url.lastIndexOf("/");
            final String fileName = url.substring(index + 1, url.length());
            if (manager.getTaskInfo(versionName) == null) {
                TaskInfo info = new TaskInfo();
                info.setFileName(versionName + "_" + fileName);
                /*服务器一般会有个区分不同文件的唯一ID，用以处理文件重名的情况*/
                info.setTaskID(versionName);
                info.setOnDownloading(true);
                /*将任务添加到下载队列，下载器会自动开始下载*/
                manager.addTask(versionName, url, versionName + "_" + fileName);
                adapter.addItem(info);
            } else {
                manager.getTaskInfo(versionName).setOnDownloading(true);
                manager.startTask(versionName);
                adapter.notifyDataSetChanged();
            }
        }
    };

    /**
     * Add by 2017-06-28 yuanshaoyu
     * <p>
     * the Method register NetWork change BroadcastReceiver
     */
    private void registerNetWorkBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter(ACTION_CONNECT_CHANGE);
        registerReceiver(noNetWorkBroadcastReceiver, intentFilter);
        Timber.tag(TAG).i("网络广播的注册");
    }

    /**
     * Add by 2017-06-28 yuanshaoyu
     * Toast No NetWork
     */
    private void noNetWorkToast() {
        Timber.tag(TAG).w("unlucky! your's network may be error");
        Utils.customToast(DownloadActivity.this, getResources().getString(R.string.no_network),
                Toast.LENGTH_SHORT).show();
    }

    BroadcastReceiver noNetWorkBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_CONNECT_CHANGE.equals(intent.getAction())) {
                ConnectivityManager manager1 = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = manager1.getActiveNetworkInfo();
                /* **** no network toast *****/
                if (networkInfo == null) {
                    noNetWorkToast();
                } else {
                    if (!networkInfo.isAvailable()) {
                        noNetWorkToast();
                    }
                }
                if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    int netWorkState = getNetWorkState();
                    Timber.tag(TAG).i("网络连接 ：%s", netWorkState);
                    //网络从新连接
                    try {
                        if (netWorkState >= 0) {
                            if (manager.getTaskInfo(versionName) != null) {
                                manager.getTaskInfo(versionName).setOnDownloading(true);
                                manager.startTask(versionName);
                                adapter.notifyDataSetChanged();
                            }

                        } else if (netWorkState == -1) {
                            //modified by yuanshaoyu 2017-7-4 :由wifi切换到4g网络继续下载的BUg修复
                            if (manager.getTaskInfo(versionName) != null) {
                                manager.getTaskInfo(versionName).setOnDownloading(false);
                                manager.stopTask(versionName);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        Timber.tag(TAG).i("activity onPause");

        //add by yuanshaoyu 2017-6-30 :解决杀掉进程重新升级从0开始下载的BUG
        if (manager.getTaskInfo(versionName) != null) {
            manager.getTaskInfo(versionName).setOnDownloading(false);
            manager.stopTask(versionName);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (noNetWorkBroadcastReceiver != null) {
            unregisterReceiver(noNetWorkBroadcastReceiver);
        }
    }

}
