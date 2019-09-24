package com.example.lz.babyperceive.Activity;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraActivity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lz.babyperceive.Bean.CharacterAsrJson;
import com.example.lz.babyperceive.Bean.CharacterBean;
import com.example.lz.babyperceive.CharacterRecognition.FileUtil;
import com.example.lz.babyperceive.CharacterRecognition.RecognizeService;
import com.example.lz.babyperceive.Dialog.CharacterDialog;
import com.example.lz.babyperceive.R;
import com.example.lz.babyperceive.Utils.ButtonUtils;
import com.example.lz.babyperceive.Utils.Speek;
import com.example.lz.babyperceive.Utils.Utils;
import com.example.lz.babyperceive.View.TitleView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharacterRecognitionActivity extends BaseActivity {

    private static final int REQUEST_CODE_GENERAL = 105;
    private static final int REQUEST_CODE_GENERAL_BASIC = 106;
    private static final int REQUEST_CODE_ACCURATE_BASIC = 107;
    private static final int REQUEST_CODE_ACCURATE = 108;
    private static final int REQUEST_CODE_GENERAL_ENHANCED = 109;
    private static final int REQUEST_CODE_GENERAL_WEBIMAGE = 110;
    private static final int REQUEST_CODE_BANKCARD = 111;
    private static final int REQUEST_CODE_VEHICLE_LICENSE = 120;
    private static final int REQUEST_CODE_DRIVING_LICENSE = 121;
    private static final int REQUEST_CODE_LICENSE_PLATE = 122;
    private static final int REQUEST_CODE_BUSINESS_LICENSE = 123;
    private static final int REQUEST_CODE_RECEIPT = 124;

    private static final int REQUEST_CODE_PASSPORT = 125;
    private static final int REQUEST_CODE_NUMBERS = 126;
    private static final int REQUEST_CODE_QRCODE = 127;
    private static final int REQUEST_CODE_BUSINESSCARD = 128;
    private static final int REQUEST_CODE_HANDWRITING = 129;
    private static final int REQUEST_CODE_LOTTERY = 130;
    private static final int REQUEST_CODE_VATINVOICE = 131;
    private static final int REQUEST_CODE_CUSTOM = 132;


    private boolean hasGotToken = false;

    private AlertDialog.Builder alertDialog;

    private TextView name_tv, spell_tv;
    private Button next_bt, previous_bt, play_bt;
    private TitleView titleView;
    private String result;
    private List<CharacterBean> list = new ArrayList<>();
    private Utils utils = new Utils(this);
    private String chinese, chineseSpell;
    private static int number = 0;
    private Speek speek;
    private FrameLayout iamage_layout;


    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.next_bt:
                if (!ButtonUtils.isFastDoubleClick(R.id.next_bt)) {
                    if (number < list.size() - 1) {
                        number += 1;
                        chinese = list.get(number).getWords();
                        name_tv.setText(chinese);
                        chineseSpell = getSpellChineseText(list.get(number).getWords());
                        spell_tv.setText(chineseSpell);
                        speek.Speeking(chinese);
                    } else {

                    }
                }

                break;
            case R.id.previous_bt:
                if (number > 0) {
                    number -= 1;
                    chinese = list.get(number).getWords();
                    name_tv.setText(chinese);
                    chineseSpell = getSpellChineseText(list.get(number).getWords());
                    spell_tv.setText(chineseSpell);
                    speek.Speeking(chinese);
                } else {

                }
                break;
            case R.id.play_bt:
                speek.Speeking(chinese);

                break;
            case R.id.iamage_layout:
                int i = 0;
                List<CharacterBean> list2 = new ArrayList<>();
                for (CharacterBean characterBean : list) {
                    characterBean.setWords(list.get(i).getWords());
                    characterBean.setWordsSpell(getSpellChineseText(list.get(i).getWords()));
                    list2.add(characterBean);
                    i += 1;
                }
                new CharacterDialog(this, R.style.dialog, list2, new CharacterDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {

                    }
                }).show();
                break;

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPermission();
        alertDialog = new AlertDialog.Builder(this);
        // 请选择您的初始化方式
        // initAccessToken();
        initAccessTokenWithAkSk();
        speek = new Speek(this);
    }

    private boolean checkTokenStatus() {
        if (!hasGotToken) {
            Toast.makeText(getApplicationContext(), "token还未成功获取", Toast.LENGTH_LONG).show();
        }
        return hasGotToken;
    }

    /**
     * 以license文件方式初始化
     */
    private void initAccessToken() {
        OCR.getInstance(this).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                hasGotToken = true;
                if (!checkTokenStatus()) {
                    //showToast("初始化失败");
                } else {
                   /* Intent intent = new Intent(CharacterRecognitionActivity.this, CameraActivity.class);
                    intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                            FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                    intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                            CameraActivity.CONTENT_TYPE_GENERAL);
                    startActivityForResult(intent, REQUEST_CODE_ACCURATE_BASIC);*/
                    showToast("初始化成功,可以进行识别");
                }
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                alertText("licence方式获取token失败,错误码", String.valueOf(error.getErrorCode()));
            }
        }, getApplicationContext());
    }

    /**
     * 用明文ak，sk初始化
     */
    private void initAccessTokenWithAkSk() {
        OCR.getInstance(this).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                alertText("AK，SK方式获取token失败", error.getMessage());
            }
        }, getApplicationContext(), "z97HOfAlY5Gn8kC68cUxpb9Q", "bc2XQT5Ga7h2PmbvEB2Ck2WkGi3RchPu");
    }

    /**
     * 自定义license的文件路径和文件名称，以license文件方式初始化
     */
    private void initAccessTokenLicenseFile() {
        OCR.getInstance(this).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                alertText("自定义文件路径licence方式获取token失败", error.getMessage());
            }
        }, "aip.license", getApplicationContext());
    }

    /**
     * 获取权限失败的弹出框
     *
     * @param title
     * @param message
     */
    private void alertText(final String title, final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog.setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("确定", null)
                        .show();
            }
        });
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String permissions[] = {Manifest.permission.RECORD_AUDIO,
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

    //用户权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initAccessToken();
        } else {
            //    Toast.makeText(getApplicationContext(), "需要android.permission.READ_PHONE_STATE", Toast.LENGTH_LONG).show();
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
        return R.layout.activity_character_recognition;
    }

    @Override
    public void initView(View view) {
        name_tv = $(R.id.name_tv);
        spell_tv = $(R.id.spell_tv);
        titleView = $(R.id.titleview);
        iamage_layout = $(R.id.iamage_layout);
        iamage_layout.setOnClickListener(this);
        titleView.setCustomOnClickListener(new TitleView.ClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.back_bt:
                        finish();
                        break;
                    case R.id.mote_bt:
                        showPopupMenu(v);
                        break;
                }
            }
        });
        titleView.setTitle_tv("  ");
        previous_bt = $(R.id.previous_bt);
        previous_bt.setOnClickListener(this);

        next_bt = $(R.id.next_bt);
        next_bt.setOnClickListener(this);


        play_bt = $(R.id.play_bt);
        play_bt.setOnClickListener(this);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void doBusiness(Context mContext) {

    }

    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.info2, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.quit1:  //右上角菜单,打开摄像机
                        try {
                            Intent intent = new Intent(CharacterRecognitionActivity.this, CameraActivity.class);
                            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                                    FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                                    CameraActivity.CONTENT_TYPE_GENERAL);
                            startActivityForResult(intent, REQUEST_CODE_ACCURATE_BASIC);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("test", "requestCode:" + requestCode + "  " + "resultCode:" + resultCode);
        // 识别成功回调，通用文字识别（高精度版）
        if (requestCode == REQUEST_CODE_ACCURATE_BASIC && resultCode == Activity.RESULT_OK) {
            RecognizeService.recAccurateBasic(this, FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            list.clear();
                            result = result.substring(result.indexOf("["), result.length() - 1);
                            CharacterAsrJson characterAsrJson = new CharacterAsrJson();
                            list = characterAsrJson.parseJSONobject(result);
                            if (list.size() != 0) {
                                // name_tv.setText(list.get(0).getWords());
                                Log.i("test", "识别成功:" + result);
                            } else {
                                CharacterBean cha = new CharacterBean();
                                cha.setWords("识别失败");
                                Log.i("test", "识别失败");
                                list.add(cha);
                            }
                            chinese = list.get(0).getWords();
                            name_tv.setText(chinese);

                            chineseSpell = getSpellChineseText(list.get(0).getWords());
                            spell_tv.setText(chineseSpell);
                            speek.Speeking(chinese);
                        }
                    });
        }

    }


    /**
     * 传入汉字 返回拼音
     *
     * @param chinese
     * @return
     */
    private String getSpellChineseText(String chinese) {

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        StringBuffer stringBuffer = new StringBuffer();
        if (chinese.length() > 0) {
            for (int i = 1; i <= chinese.length(); i++) {
                p = Pattern.compile("[\u4e00-\u9fa5]");
                Matcher m = p.matcher(chinese.substring(i - 1, i));
                if (m.matches()) {
                    stringBuffer.append(utils.getChineseSpell(chinese.substring(i - 1, i)));
                } else {
                    stringBuffer.append(chinese.substring(i - 1, i));
                }
                // stringBuffer.append(utils.getChineseSpell(chinese.substring(i - 1, i)));
                stringBuffer.append(" ");
            }
        }
        chineseSpell = stringBuffer.toString();
        return chineseSpell;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放内存资源
        OCR.getInstance(this).release();
        speek.Destory();
    }
}

