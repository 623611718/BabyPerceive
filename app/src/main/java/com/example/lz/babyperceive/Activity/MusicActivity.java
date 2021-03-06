package com.example.lz.babyperceive.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lz.Broadcast.IntentBroadReceiver;
import com.example.lz.babyperceive.Application.MyApplication;
import com.example.lz.babyperceive.Dialog.VerifyDialog;
import com.example.lz.babyperceive.R;
import com.example.lz.babyperceive.Utils.UtilsGetUrl;
import com.example.lz.babyperceive.Utils.Utils_play;
import com.example.lz.babyperceive.View.TitleView;

import java.util.ArrayList;
import java.util.List;

public class MusicActivity extends BaseActivity {
    protected static final int PROGRESS = 1;
    protected static final int isplaying = 2;
    private static final int TIME = 3;
    private TitleView titleView;
    private List<String> urls = new ArrayList<>();
    private UtilsGetUrl utilsGetUrl;
    private List<String> videos = new ArrayList<>();
    private MediaPlayer mediaPlayer = null;
    private SeekBar sb_main;
    private String path;
    private Button previous_bt, play_bt, next_bt;
    private int max;
    private TextView tv_begin, tv_end;
    private Utils_play utils_play;
    private int pro = 0;
    private int currenposition = 0;
    private TextView musicName_tv;
    private int number = 0;
    private MyApplication myApplication;

    private int time = 0;
    private ArrayAdapter<String> adapter1;
    ;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PROGRESS:
                    if (mediaPlayer != null) {
                      /*  time +=1;
                        if (!myApplication.status && !myApplication.isShow()) {  //如果娱乐状态为false 弹出验证
                            myApplication.setShow(true);
                            //myApplication.setStatus(false);
                            showDialog();
                            // myApplication.setYueleStatus(false);
                        }*/
                        // 1.得到当前的视频播放进度
                        currenposition = mediaPlayer.getCurrentPosition();
                        // 2.Seekbar.setprogress(当前进度);
                        sb_main.setProgress(currenposition);

                        tv_begin.setText(utils_play.formatTime(currenposition));
                        // 3.每秒更新一次
                        removeMessages(PROGRESS);
                        sendEmptyMessageDelayed(PROGRESS, 1000);
                        Log.i("url", "PROGRESS  " + currenposition);
                        break;
                    }
                    return;
                case isplaying:
                    // sk_linear.setVisibility(View.GONE);
                    //  play_title.setVisibility(View.GONE);
                    return;
            }
        }
    };
    private Handler handlerTime = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TIME:
                    if (!myApplication.status && !myApplication.isShow()) {  //如果娱乐状态为false 弹出验证
                        myApplication.setShow(true);
                        showDialog();
                    }
                    removeMessages(TIME);
                    sendEmptyMessageDelayed(TIME, 1000);
                    return;
            }
        }
    };


    //对快进 和快退进行异步处理
    class MyAsyncTask extends AsyncTask<String, Void, String> {

        //onPreExecute用于异步处理前的操作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //此处将progressBar设置为可见.

        }

        //在doInBackground方法中进行异步任务的处理.
        @Override
        protected String doInBackground(String... params) {
            Log.i("dsa", "doInBackground");
            mediaPlayer.seekTo(pro);               //改变播放进度
            Log.i("dsa", "doInBackground after");
            return null;
        }

        //onPostExecute用于UI的更新.此方法的参数为doInBackground方法返回的值.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i("dsa", "onPostExecute");
            //   mediaPlayer.seekTo(pro);
            // 1.得到当前的视频播放进度
            currenposition = mediaPlayer.getCurrentPosition();
            // 2.Seekbar.setprogress(当前进度);
            sb_main.setProgress(currenposition);
            // mediaPlayer.start();

        }
    }


    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.play_bt:
                if (currenposition > 0)
                    isPlayingVideo();
                break;
            case R.id.next_bt:
                if (number < videos.size() - 2) {
                    number += 1;
                    play(videos.get(number));
                }
                break;
            case R.id.previous_bt:
                if (number > 0) {
                    number -= 1;
                    play(videos.get(number));
                }
                break;
        }

    }

    private IntentBroadReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //断网提示退出
        receiver = new IntentBroadReceiver(MusicActivity.this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(receiver, filter);

        initUrl();
        utils_play = new Utils_play();
        intSpinner();
        myApplication = (MyApplication) getApplication();
        if (!myApplication.isStatus()) {
            showDialog();
            // myApplication.setYueleStatus(false);
        }else {
            myApplication.sendYuleEmptyMessage();
        }
        handlerTime.sendEmptyMessageDelayed(TIME, 0);
    }

    /**
     * 获取配置文件URL 名称
     */
    private void initUrl() {
        utilsGetUrl = new UtilsGetUrl(this, "MP3URIConfig.txt");
        videos = utilsGetUrl.getVideos();
        urls = utilsGetUrl.getUrls();
    }

    private void showDialog(){
        new VerifyDialog(this, R.style.dialog, "快让家长帮忙吧", new VerifyDialog.OnCloseListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }).setTitle("不能玩了").show();
    }
    // 播放的时候调用
    public void play(String path) {
        // 重置mediaPaly,建议在初始化mediaplay立即调用
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer =null;
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.reset();
        // 设置声音效果
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        // 设置播放完成的监听
        mediaPlayer.setOnCompletionListener(new CompletionListener());
        // 设置媒体加载完成后的监听
        mediaPlayer.setOnPreparedListener(new PreparedListener());
        // 设置错误监听回调函数
        mediaPlayer.setOnErrorListener(new ErrorListener());
        // 设置缓存变化监听
        //mediaPlayer.setOnBufferingUpdateListener(new BufferingUpdateListener());
        // 设置拖动监听事件
        sb_main.setOnSeekBarChangeListener(new SeekBarChangeListener());

        try {
            //http://lz.free.idcfengye.com  http://codingwang.free.idcfengye.com
            //path = "http://lz.free.idcfengye.com/" + name;
            mediaPlayer.setDataSource(path);
            Toast.makeText(this,"url:"+path,Toast.LENGTH_LONG).show();
            Log.i("play", "url  " + path);
            // 设置异步加载视频，包括两种方式 prepare()同步，prepareAsync()异步
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(this, "加载错误！", Toast.LENGTH_LONG).show();
        }

    }

    public void isPlayingVideo() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                mediaPlayer.pause();
                Log.i("localplayer", "暂停");

                play_bt.setBackgroundResource(R.drawable.ico_next_big_n);
                // play_bt.setVisibility(View.VISIBLE);
                Log.i("TAG", "STOP");

            } else {
                Log.i("TAG", "PLAY");
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                mediaPlayer.start();

                play_bt.setBackgroundResource(R.drawable.ico_pause_n);

            }
        }
    }

    // 设置播放完成的监听
    class CompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            //IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            //PlayerActivity.this.registerReceiver(receiver,filter);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            if (mediaPlayer != null) {
                //mediaPlayer.release();
                //mediaPlayer=null;
                mediaPlayer.start();
                mediaPlayer.pause();
                play_bt.setBackgroundResource(R.drawable.btn_start_selector);
                play_bt.setVisibility(View.VISIBLE);
                // playing.setBackgroundResource(R.drawable.btn_start_selector);
                Log.i("url", "CompletionListener  ");
            }
        }
    }

    class PreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
            {
                // TODO Auto-generated method stub
                //设置屏幕常亮
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                //    // 当视频加载完毕以后，隐藏加载进度条
                //   pb_main.setVisibility(View.GONE);
                // play_bt.setVisibility(View.GONE);
                // 设置控制条,放在加载完成以后设置，防止获取getDuration()错误
                sb_main.setMax(mediaPlayer.getDuration());
                // 设置播放时间
                max = mediaPlayer.getDuration();
                tv_end.setText(utils_play.formatTime(max));
                // 播放视频
                mediaPlayer.start();
                //设置循环播放
                mediaPlayer.setLooping(true);
                mediaPlayer.seekTo(currenposition);
                // 发消息
                handler.sendEmptyMessage(PROGRESS);
            }
        }
    }

    class ErrorListener implements MediaPlayer.OnErrorListener {
        @Override
        public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
            // TODO Auto-generated method stub
            Log.i("url", "ErrorListener  " + arg1 + "  " + arg2);
            if (mediaPlayer != null) {
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
                Log.i("url", "ErrorListener mediaPlayer!=null ");
            }
            return false;
        }
    }

    // 给seekbar设置监听
    class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            if (progress > 0) {
                if (fromUser) {
                    Log.i("dsa", "onProgressChanged");
                    pro = progress;
                    MyAsyncTask asynctask = null;
                    asynctask = new MyAsyncTask();
                    asynctask.execute();

                }
            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            Log.i("dsa", "onProgressChanged");
            int progress = seekBar.getProgress();

            pro = progress;
            MyAsyncTask asynctask = null;
            asynctask = new MyAsyncTask();
            asynctask.execute();

        }

    }


    /**
     * 初始化下拉框
     */
    private void intSpinner() {
        videos.add("  ");
        urls.add(" ");
        Spinner spinner = findViewById(R.id.spinner);
        // 为下拉列表定义一个适配器，使用到上面定义的turtleList
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, videos);
        // 为适配器设置下拉列表下拉时的菜单样式，有好几种样式，请根据喜好选择
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 将适配器添加到下拉列表上
        spinner.setAdapter(adapter);
        if (videos.size() > 0) {
            spinner.setSelection(videos.size() - 1, false);
        } else {
            spinner.setSelection(0, false);
        }
        // 为下拉框设置事件的响应
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             *
             * @param adapterView
             * @param view   显示的布局
             * @param i      在布局显示的位置id
             * @param l      将要显示的数据
             */
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapter1 = (ArrayAdapter<String>) adapterView.getAdapter();
                number = i;
                musicName_tv.setText(adapter1.getItem(number));
                play(urls.get(number));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_music;
    }

    @Override
    public void initView(View view) {
        titleView = $(R.id.titleview);
        titleView.setTitle_tv("听儿歌");
        titleView.setCustomOnClickListener(new TitleView.ClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.back_bt:
                        finish();
                        break;
                }
            }
        });
        previous_bt = $(R.id.previous_bt);
        play_bt = $(R.id.play_bt);
        next_bt = $(R.id.next_bt);
        previous_bt.setOnClickListener(this);
        play_bt.setOnClickListener(this);
        play_bt.setOnClickListener(this);
        sb_main = $(R.id.sb_main);
        tv_begin = $(R.id.tv_begin);
        tv_end = $(R.id.tv_end);
        musicName_tv = $(R.id.musicName_tv);
        musicName_tv.requestFocus();
    }

    @Override
    public void setListener() {

    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        handler.removeMessages(PROGRESS);
        handlerTime.removeMessages(TIME);
        myApplication.removeYuleEmptyMessage();
        unregisterReceiver(receiver);//注销广播
        super.onDestroy();
    }
}
