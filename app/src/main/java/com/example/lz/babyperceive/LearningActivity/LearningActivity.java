package com.example.lz.babyperceive.LearningActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.lz.babyperceive.Activity.BaseActivity;
import com.example.lz.babyperceive.Activity.TranslateActivity;
import com.example.lz.babyperceive.R;
import com.example.lz.babyperceive.View.TitleView;

import java.util.ArrayList;

/**
 * 学习的主界面
 */
public class LearningActivity extends BaseActivity implements View.OnClickListener {

    private Button bt1, bt2, bt3, bt4, bt5; //1:跟我读 2:学英语 3:认事物 4:学成语 5:练词组
    private TitleView titleView;

    @Override
    public void widgetClick(View v) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_learning);
      //  getSupportActionBar().hide();//隐藏掉整个ActionBar，包括下面的Tabs
       // changeStatusBarTextColor(true);
        initPermission();  //初始化权限
        initView();//初始化View
    }

    //改变状态栏字体颜色
    private void changeStatusBarTextColor(boolean isBlack) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (isBlack) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏黑色字体
            } else {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//恢复状态栏白色字体
            }
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
        return R.layout.activity_learning;
    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void setListener() {

    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String permissions[] = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                Manifest.permission.READ_PHONE_STATE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                //进入到这里代表没有权限.

            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
    }

    private void initView() {
        bt1 = (Button) findViewById(R.id.bt1);
        bt2 = (Button) findViewById(R.id.bt2);
        bt3 = (Button) findViewById(R.id.bt3);
        bt4 = (Button) findViewById(R.id.bt4);
        bt5 = (Button) findViewById(R.id.bt5);
        titleView = (TitleView) findViewById(R.id.titleview);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        bt4.setOnClickListener(this);
        bt5.setOnClickListener(this);
        titleView.setCustomOnClickListener(new TitleView.ClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.mote_bt:
                        showPopupMenu(v);
                        break;
                    case R.id.back_bt:
                        finish();
                        break;
                }
            }
        });

        titleView.setTitle_tv("  ");
    }

    //1:跟我读 2:学英语 3:认事物 4:学成语 5:练词组
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt1:
                Intent intent1 = new Intent(this, SpeakingActivity.class);
                intent1.putExtra("title", "跟我读");
                startActivity(intent1);
                break;
            case R.id.bt2:
                Intent intent2 = new Intent(this, AnimalActivity.class);
                intent2.putExtra("data", "english.txt");
                intent2.putExtra("title", "学英语");
                startActivity(intent2);
                break;
            case R.id.bt3:
                Intent intent3 = new Intent(this, ObjectActivity.class);
                intent3.putExtra("title", "认事物");
                startActivity(intent3);
                break;
            case R.id.bt4:
                Intent intent4 = new Intent(this, AnimalActivity.class);
                intent4.putExtra("data", "idiom.txt");
                intent4.putExtra("title", "学成语");
                startActivity(intent4);
                break;
            case R.id.bt5:
                Intent intent5 = new Intent(this, GroupActivity.class);
                intent5.putExtra("data", "idiom.txt");
                intent5.putExtra("title", "练词组");
                startActivity(intent5);
                break;
        }
    }

    @Override
    public void doBusiness(Context mContext) {

    }


    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.info, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.quit1:
                        Intent intent1 = new Intent(LearningActivity.this, TranslateActivity.class);
                        startActivity(intent1);
                        break;

                }
                return false;
            }
        });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                //Toast.makeText(getApplicationContext(), "关闭PopupMenu", Toast.LENGTH_SHORT).show();
            }
        });
        popupMenu.show();
    }
}
