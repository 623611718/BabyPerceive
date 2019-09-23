package com.example.lz.babyperceive.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lz.babyperceive.Adapter.ImageRecognitionAdapter;
import com.example.lz.babyperceive.Adapter.MoviesAdapter;
import com.example.lz.babyperceive.Application.MyApplication;
import com.example.lz.babyperceive.Bean.ImageRecognitionBean;
import com.example.lz.babyperceive.Bean.MoviesBean;
import com.example.lz.babyperceive.Dialog.SpeakingDialog;
import com.example.lz.babyperceive.R;
import com.example.lz.babyperceive.Speed.NetSpeed;
import com.example.lz.babyperceive.Speed.NetSpeedTimer;
import com.example.lz.babyperceive.Utils.UtilsGetUrl;
import com.example.lz.babyperceive.Utils.Utils_play;
import com.example.lz.babyperceive.View.Play_title;
import com.example.lz.babyperceive.View.TitleView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoviesActivity extends BaseActivity {
    private TitleView titleView;
    private ListView listViewl;
    private List<MoviesBean> list = new ArrayList<>();
    private MoviesAdapter adapter;
    private MoviesBean moviesBean;
    private List<String> urls = new ArrayList<>();
    private UtilsGetUrl utilsGetUrl;
    private List<String> videos = new ArrayList<>();
    private Button dispsize_bt;
    private LinearLayout Linear_layout;
    private FrameLayout Frame_layout;
    @Override
    public void widgetClick(View v) {

    }

    protected static final int PROGRESS = 1;
    protected static final int isplaying = 2;
    private static final int TIME = 3;
    private String path, name;
    private SurfaceView surfaceview;
    private TextView tv_begin;
    private SeekBar sb_main;
    private TextView tv_end;
    private Button btn_play, playing;
    private ProgressBar pb_main;
    private MediaPlayer mediaPlayer;
    private android.view.SurfaceHolder mSurfaceHolder;
    private int currenposition = 0;
    private int duration;
    private Utils_play utils_play;
    private int max;
    private LinearLayout sk_linear;
    private int pro = 0;
    private int errorcode = 0;
    private MyApplication myApplication;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PROGRESS:

                    if (mediaPlayer != null) {

                        // 1.得到当前的视频播放进度
                        currenposition = mediaPlayer.getCurrentPosition();
                        // 2.Seekbar.setprogress(当前进度);
                        sb_main.setProgress(currenposition);

                        tv_begin.setText(utils_play.formatTime(currenposition));
                        play_title.setTitle_time(utils_play.getDada()); //更新标题时间
                        // 3.每秒更新一次
                        removeMessages(PROGRESS);
                        sendEmptyMessageDelayed(PROGRESS, 1000);
                        Log.i("url", "PROGRESS  " + currenposition);
                        break;
                    }
                    return;
                case isplaying:
                    sk_linear.setVisibility(View.GONE);
                    play_title.setVisibility(View.GONE);
                    return;
            }
        }
    };
    private Handler handlerTime = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TIME:
                    int time = myApplication.getYuletime();
                    time += 1;
                    myApplication.setYuletime(time);
                    if (myApplication.getYuletime() >= myApplication.yuleTime_end && !myApplication.isShow()) {  //如果娱乐状态为false 弹出验证
                        myApplication.setShow(true);
                        showDialog();
                        // myApplication.setYueleStatus(false);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication = (MyApplication) getApplication();
        if (myApplication.getYuletime() <= myApplication.getYuleTime_end()) {
            showDialog();
           // myApplication.setYueleStatus(false);
        }
        handlerTime.sendEmptyMessageDelayed(TIME, 0);


        if (savedInstanceState != null) {
            currenposition = savedInstanceState.getInt("currenposition");
            Log.i("play", "onCreate currenposition:" + savedInstanceState.getInt("currenposition"));
        }
        utils_play = new Utils_play();
        initdata();
        initBroad();
        // 初始化Mediaplayer
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.reset();
        }

        initUrl();
        initList();
    }

    /**
     * 获取配置文件URL 名称
     */
    private void initUrl() {
        utilsGetUrl = new UtilsGetUrl(this, "URIConfig.txt");
        videos = utilsGetUrl.getVideos();
        urls = utilsGetUrl.getUrls();
    }

    /**
     * 初始化 list 用于listview
     */
    private void initList() {
        for (int i = 0; i < videos.size(); i++) {
            MoviesBean moviesBean = new MoviesBean();
            moviesBean.setName(videos.get(i));
            list.add(moviesBean);
        }
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
        return R.layout.activity_movies;
    }

    @Override
    public void initView(View view) {
        titleView = $(R.id.titleview);
        titleView.setCustomOnClickListener(new TitleView.ClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.mote_bt:
                        break;
                    case R.id.back_bt:
                        finish();
                        break;
                }
            }
        });
        titleView.setTitle_tv("视频列表");
        adapter = new MoviesAdapter(this, R.layout.movies_item, list);
        listViewl = $(R.id.listview);
        listViewl.setAdapter(adapter);
        listViewl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               /*Intent intent = new Intent(MoviesActivity.this, PlayerActivity.class);
                intent.putExtra("data", list.get(position).getName()); // URL
                intent.putExtra("url", urls.get(position));  //名称
                startActivity(intent);*/
                play(urls.get(position));
                name = list.get(position).getName();
                path = urls.get(position);

            }
        });
    }

    @Override
    public void setListener() {

    }

    @Override
    public void doBusiness(Context mContext) {

    }

    private void showDialog() {
        new SpeakingDialog(this, R.style.dialog, "快让家长帮忙吧", new SpeakingDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {

            }
        }).setTitle("不能玩了").show();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putInt("currentposition", currenposition);
        Log.v("play", "onSaveInstanceState:" + currenposition);
        super.onSaveInstanceState(outState);
    }

    private ScreenBroadcast mBatInfoReceiver = null;

    private void initBroad() {
        final IntentFilter filter = new IntentFilter();
        // 屏幕灭屏广播
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        // 屏幕亮屏广播
        filter.addAction(Intent.ACTION_SCREEN_ON);
        // 屏幕解锁广播
        filter.addAction(Intent.ACTION_USER_PRESENT);
        // 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播
        // example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
        // 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        mBatInfoReceiver = new ScreenBroadcast();
        registerReceiver(mBatInfoReceiver, filter);
    }

    public class ScreenBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                // mediaPlayer.start();
                //  sb_main.setProgress(currenposition);
                //  tv_begin.setText(utils.formatTime(currenposition));
                Log.i("play", "screen on currenposition:  " + currenposition);
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                mediaPlayer.pause();
                Log.i("play", "screen off");
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                Log.i("play", "screen unlock");
            } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
                Log.i("play", " receive Intent.ACTION_CLOSE_SYSTEM_DIALOGS");
            }
        }
    }

    private Play_title play_title;
    private float downX, downY;
    private int screenWidth;
    private int FACTOR = 100;

    private void initdata() {
        Linear_layout = (LinearLayout) findViewById(R.id.Linear_layout);
        Frame_layout = (FrameLayout) findViewById(R.id.Frame_layout);
        sk_linear = (LinearLayout) findViewById(R.id.sk_linear);
        surfaceview = (SurfaceView) findViewById(R.id.surfaceview);
        tv_begin = (TextView) findViewById(R.id.tv_begin);
        tv_end = (TextView) findViewById(R.id.tv_end);
        sb_main = (SeekBar) findViewById(R.id.sb_main);
        btn_play = (Button) findViewById(R.id.btn_play);
        pb_main = (ProgressBar) findViewById(R.id.pb_main);
        playing = (Button) findViewById(R.id.playing);
        tvSound = (TextView) findViewById(R.id.tv_sound); //音量管理
        dispsize_bt = (Button) findViewById(R.id.dispsize);
        dispsize_bt.setOnClickListener(this);
        playing.setOnClickListener(this);
        pb_main.setOnClickListener(this);
        play_title = (Play_title) findViewById(R.id.play_title);
        play_title.setTitle_name(name);
        surfaceview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            playLocalMedia(v);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        downX = event.getX();
                        downY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // TODO 音量
                        float distanceX = event.getX() - downX;
                        float distanceY = event.getY() - downY;
                        if (downX > screenWidth - 200
                                && Math.abs(distanceX) < 50
                                && distanceY > FACTOR) {
                            // TODO 减小音量
                            setVolume(false);
                        } else if (downX > screenWidth - 200
                                && Math.abs(distanceX) < 50
                                && distanceY < -FACTOR) {
                            // TODO 增加音量
                            setVolume(true);

                        }
                        // TODO 播放进度调节
                        if (Math.abs(distanceY) < 50 && distanceX > FACTOR) {
                            // TODO 快进
                            int currentT = mediaPlayer.getCurrentPosition();//播放的位置
                            mediaPlayer.seekTo(currentT + 15000);
                            downX = event.getX();
                            downY = event.getY();
                            Log.i("info", "distanceX快进=" + distanceX);
                        } else if (Math.abs(distanceY) < 50
                                && distanceX < -FACTOR) {
                            // TODO 快退
                            int currentT = mediaPlayer.getCurrentPosition();
                            mediaPlayer.seekTo(currentT - 15000);
                            downX = event.getX();
                            downY = event.getY();
                            Log.i("info", "distanceX=" + distanceX);
                        }
                        break;
                }
                return true;
            }
        });

        setSurfaceview();
        initNewWork();
    }

    private TextView tvSound, tvCurrentT, tvDuration;

    private void setVolume(boolean flag) {
        // 获取音量管理器
        AudioManager manager = (AudioManager) getSystemService(AUDIO_SERVICE);
        // 获取当前音量
        int curretnV = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (flag) {
            curretnV++;
        } else {
            curretnV--;
        }
        manager.setStreamVolume(AudioManager.STREAM_MUSIC, curretnV,
                AudioManager.FLAG_SHOW_UI);
        tvSound.setVisibility(View.VISIBLE);
        tvSound.setText("音量:" + curretnV);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvSound.setVisibility(View.GONE);
            }
        }, 1000);
        /**
         * 1.AudioManager.STREAM_MUSIC 多媒体 2.AudioManager.STREAM_ALARM 闹钟
         * 3.AudioManager.STREAM_NOTIFICATION 通知 4.AudioManager.STREAM_RING 铃音
         * 5.AudioManager.STREAM_SYSTEM 系统提示音 6.AudioManager.STREAM_VOICE_CALL
         * 电话
         *
         * AudioManager.FLAG_SHOW_UI:显示音量控件
         */
    }

    private void initNewWork() {
        //创建NetSpeedTimer实例
        NetSpeedTimer mNetSpeedTimer = new NetSpeedTimer(this, new NetSpeed(), mHandler).setDelayTime(1000).setPeriodTime(2000);
        //在想要开始执行的地方调用该段代码
        mNetSpeedTimer.startSpeedTimer();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == NetSpeedTimer.NET_SPEED_TIMER_DEFAULT) {
                String speed = (String) msg.obj;
                play_title.setTitle_speed(speed);
                //打印你所需要的网速值，单位默认为kb/s
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playing:
                isPlayingVideo();
                break;
            default:
                break;
            case R.id.play_back:
                finish();
                break;
            case R.id.pb_main:
                isPlayingVideo();
                break;
            case R.id.dispsize:         //缩放
                if (listViewl.getVisibility() == View.VISIBLE) {
                    listViewl.setVisibility(View.GONE);
                    titleView.setVisibility(View.GONE);
                    Frame_layout.setPadding(0, 0, 0, 0);
                    Linear_layout.setPadding(0, 0, 0, 0);
                } else {
                    listViewl.setVisibility(View.VISIBLE);
                    titleView.setVisibility(View.VISIBLE);
                    Frame_layout.setPadding(12, 12, 12, 12);
                    Linear_layout.setPadding(12, 12, 12, 12);
                }
              /*  int height = surfaceview.getLayoutParams().height;
                int width = surfaceview.getLayoutParams().width;
                if (height == -1 && width == -1) {
                    FrameLayout.LayoutParams params =new FrameLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT,);
                    params.gravity = Gravity.CENTER;
                    surfaceview.setLayoutParams(params);
                } else if (height == 540 && width == 960) {
                    height = 360;
                    width = 640;
                } else if (height == 360 && width == 640) {
                    height = 180;
                    width = 320;
                }else if (height == 180 && width == 320){
                    height =720;
                    width =1280;
                }

                FrameLayout.LayoutParams params =new FrameLayout.LayoutParams(width,height);
                params.gravity = Gravity.CENTER;
                surfaceview.setLayoutParams(params);*/
                break;
        }
    }

    private void setSurfaceview() {
        // 设置surfaceHolder
        mSurfaceHolder = surfaceview.getHolder();
        // 设置Holder类型,该类型表示surfaceView自己不管理缓存区
        //mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // 设置surface回调
        (mSurfaceHolder).addCallback(new SurfaceCallback());
    }

    class SurfaceCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(android.view.SurfaceHolder holder) {
            // 当surfaceview被创建的时候播放
            Log.i("tag", "surfaceCreated  ");
        }

        @Override
        public void surfaceChanged(android.view.SurfaceHolder holder, int format, int width,
                                   int height) {
            Log.i("tag", "surfaceChanged  ");

        }

        @Override
        public void surfaceDestroyed(android.view.SurfaceHolder holder) {
            Log.i("tag", "surfaceDestroyed  ");

        }

    }

    // 初次播放视屏的时候调用
    public void play(String path) {
        // 重置mediaPaly,建议在初始化mediaplay立即调用
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        // 重置mediaPaly,建议在初始化mediaplay立即调用
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

            mediaPlayer.setDataSource(path);
            Log.i("play", "url  " + path);
            // 设置异步加载视频，包括两种方式 prepare()同步，prepareAsync()异步
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "加载视频错误！", Toast.LENGTH_LONG).show();
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
                btn_play.setBackgroundResource(R.drawable.btn_start_selector);
                btn_play.setVisibility(View.VISIBLE);
                playing.setBackgroundResource(R.drawable.btn_start_selector);
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
                // 当视频加载完毕以后，隐藏加载进度条
                pb_main.setVisibility(View.GONE);
                btn_play.setVisibility(View.GONE);
                // 设置控制条,放在加载完成以后设置，防止获取getDuration()错误
                sb_main.setMax(mediaPlayer.getDuration());
                // 设置播放时间
                max = mediaPlayer.getDuration();
                tv_end.setText(utils_play.formatTime(max));

                // 播放视频
                mediaPlayer.start();

                //设置循环播放
                //mediaPlayer.setLooping(true);
                // 设置显示到屏幕
                mediaPlayer.setDisplay(mSurfaceHolder);
                // 设置surfaceView保持在屏幕上
                mediaPlayer.setScreenOnWhilePlaying(true);
                mSurfaceHolder.setKeepScreenOn(true);
                mediaPlayer.seekTo(currenposition);
                // 发消息
                handler.sendEmptyMessage(PROGRESS);
                handler.sendEmptyMessageDelayed(isplaying, 5000);
                Log.i("url", "PreparedListener  ");
            }
        }
    }

    class ErrorListener implements MediaPlayer.OnErrorListener {
        @Override
        public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
            // TODO Auto-generated method stub
            Log.i("url", "ErrorListener  " + arg1 + "  " + arg2);
            errorcode = arg1;
            if (mediaPlayer != null) {
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
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

    public void playLocalMedia(View v) throws IllegalArgumentException,
            SecurityException, IllegalStateException, IOException {
        Log.i("url", "playLocalMedia(View v)  ");

        if (sk_linear.getVisibility() == View.GONE) {
            sk_linear.setVisibility(View.VISIBLE);
            play_title.setVisibility(View.VISIBLE);
            handler.sendEmptyMessageDelayed(isplaying, 5000);
        } else if (sk_linear.getVisibility() == View.VISIBLE) {
            play_title.setVisibility(View.GONE);
            sk_linear.setVisibility(View.GONE);
            handler.removeMessages(isplaying);
        }


        // isPlayingVideo();
    }

    public void isPlayingVideo() {
        if (mediaPlayer.isPlaying()) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            mediaPlayer.pause();
            Log.i("localplayer", "暂停");
            btn_play.setBackgroundResource(R.drawable.play_play);
            btn_play.setVisibility(View.VISIBLE);
            playing.setBackgroundResource(R.drawable.play_play);
            playing.setVisibility(View.VISIBLE);
            Log.i("TAG", "STOP");

        } else {
            Log.i("TAG", "PLAY");
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            mediaPlayer.start();
            btn_play.setBackgroundResource(R.drawable.play_pause);
            playing.setBackgroundResource(R.drawable.play_pause);
            // 1秒过后自动消失
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btn_play.setVisibility(View.GONE);
                }
            }, 1000);
        }
    }

    /**
     * 给back键添加监听事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Log.i("dsa", "keyCode  " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            finish();
            return false;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (mediaPlayer.isPlaying()) {
                if (mediaPlayer.getCurrentPosition() > 0) {
                    int pos = mediaPlayer.getCurrentPosition();
                    if (pos < 5000) {
                        mediaPlayer.seekTo(pos);
                    } else {
                        // 毫秒 5秒
                        pos -= 5000;
                        mediaPlayer.seekTo(pos);
                        mediaPlayer.start();
                        btn_play.setBackgroundResource(R.drawable.btn_pause_selector);
                        // 1秒过后自动消失
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                btn_play.setVisibility(View.GONE);
                            }
                        }, 1000);
                        Log.i("TAG", "left！");
                    }
                }
            } else {
                Log.i("TAG", "按左键了！");
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (mediaPlayer.isPlaying()) {
                if (mediaPlayer.getCurrentPosition() > 0) {
                    int pos = mediaPlayer.getCurrentPosition();
                    int druation = mediaPlayer.getDuration();
                    Log.i("TAG", "pos" + pos);
                    Log.i("TAG", "druation" + druation);
                    if (pos < druation) {
                        // 5秒
                        pos += 5000;
                        mediaPlayer.seekTo(pos);
                        Log.i("TAG", "pos" + pos);
                        Log.i("TAG", "druation" + druation);
                    } else {
                        mediaPlayer.seekTo(druation);
                    }
                }
            } else {
                Log.i("TAG", "按右键了！");
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            if (errorcode != 100) {
                // isPlayingVideo();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void display(View v) throws IllegalArgumentException,
            SecurityException, IllegalStateException, IOException {
        playLocalMedia(v);

    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (mBatInfoReceiver != null) {
            unregisterReceiver(mBatInfoReceiver);
        }
        handlerTime.removeMessages(TIME);
        super.onDestroy();
    }
}


