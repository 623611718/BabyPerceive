package com.example.lz.babyperceive.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.ocr.ui.camera.CameraActivity;
import com.example.lz.babyperceive.Adapter.ImageRecognitionAdapter;
import com.example.lz.babyperceive.Bean.ImageRecognitionBean;
import com.example.lz.babyperceive.CharacterRecognition.FileUtil;
import com.example.lz.babyperceive.MainActivity;
import com.example.lz.babyperceive.R;
import com.example.lz.babyperceive.View.TitleView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ImageRecognitionActivity extends BaseActivity {

    private Handler handler = null;
    private String content = null;
    private static int RESULT_LOAD_IMAGE = 1;
    private String TAG = "vehicle_detect";
    private String base64 = null;
    private String httpArg = null;
    private String access_token = null;
    public static final String APP_ID = "17214990";
    public static final String API_KEY = "0mF4fP2iR50fMCIRTvWhmAlD";
    public static final String SECRET_KEY = "Fb6GeR4rYLIqdFH6sye1hFpko6aDr1TR ";
    private TitleView titleView;
    private ListView listView;
    private List<ImageRecognitionBean> list = new ArrayList<>();
    private ImageRecognitionAdapter adapter;
    private String title;
    private Button bt1;
    private WebView webView;

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.bt1:
                list.clear();
                Intent intent = new Intent(ImageRecognitionActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, 2);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        //进入此界面自动打开相机 CameraActivity
     /*   Intent intent = new Intent(ImageRecognitionActivity.this, CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(getApplication()).getAbsolutePath());
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL);
        startActivityForResult(intent, 2);*/
        //  ImageRecognitionAdapter adapter = new ImageRecognitionAdapter(this,R.layout.image_recognition_item);
    }

    @Override
    public void initParms(Bundle parms) {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");

    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_image_recognition;
    }

    @Override
    public void initView(View view) {
        bt1 = $(R.id.bt1);
        bt1.setOnClickListener(this);
        titleView = $(R.id.titleview);
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
        titleView.setTitle_tv(title);
        adapter = new ImageRecognitionAdapter(this, R.layout.image_recognition_item, list);
        listView = $(R.id.listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ImageRecognitionActivity.this, WebViewActivity.class);
                intent.putExtra("data", list.get(position).getName());
                startActivity(intent);
            }
        });
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
                    case R.id.quit1:
                        try {
                           /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, 2);*/
                            list.clear();
                            Intent intent = new Intent(ImageRecognitionActivity.this, CameraActivity.class);
                            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                                    FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                                    CameraActivity.CONTENT_TYPE_GENERAL);
                            startActivityForResult(intent, 2);
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


    ImageView.OnClickListener imageCarDetect = new ImageView.OnClickListener() {
        public void onClick(View v) {
            if (httpArg != null)
                sendRequestWithHttpURLConnection();
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("test", "requestCode:" + requestCode + "  " + "resultCode:" + resultCode + "  ");
        String picturePath = null;
        Bitmap bitmap = null;
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            Log.d(TAG, picturePath);
            cursor.close();
         /*   if (!picturePath.isEmpty())
                imageView.setImageURI(selectedImage);*/
        } else if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
            //  Bundle bundle = data.getExtras();
            //bitmap = (Bitmap) bundle.get("dsa");
            //获取返回的baty数据  key：data   函数的数据处理在CameraActivity.java 中的doConfirmResult()处理
            byte[] bis = data.getByteArrayExtra("data");
            bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
            //   imageView.setImageBitmap(bitmap);
            imgToBase64(null, bitmap);
        }

        if (bitmap != null || picturePath != null) {
            base64 = imgToBase64(picturePath, bitmap);
            base64 = base64.replace("\r\n", "");
            try {
                base64 = URLEncoder.encode(base64, "utf-8");
                httpArg = "imagetype=1" + "&image=" + base64 + "&top_num=4";
                sendRequestWithHttpURLConnection();
            } catch (Exception e) {
                return;
            }
        }
    }

    private void get_access_token() {
        HttpURLConnection connection = null;
        String uri = "https://aip.baidubce.com/oauth/2.0/token?" +
                "grant_type=client_credentials" + "&client_id=" + API_KEY + "&client_secret=" + SECRET_KEY;
        try {
            URL url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.connect();

            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            content = response.toString();
            Log.e(TAG, "get_access_token: " + content);

            JSONObject jsonObject = new JSONObject(content);
            access_token = jsonObject.getString("access_token");
            Log.e(TAG, "get_access_token: access_token: " + access_token);
        } catch (Exception e) {
            e.printStackTrace();
            content = "网络连接超时，请点击图片重试...";
            handler.post(runnableUi);
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    private void sendRequestWithHttpURLConnection() {
        //开启线程来发起网络请求
        new Thread(new Runnable() {
            public void run() {
                if (access_token == null)
                    get_access_token();
                HttpURLConnection connection = null;
                content = "查询中...";
                // bt1.setText(content);
                handler.post(runnableUi);
                try {
                    Intent intent = getIntent();
                    String uri = " ";
                    if (intent.getStringExtra("type").equals("advanced_general")) {
                        uri = "https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general?access_token=" + access_token;
                    } else if (intent.getStringExtra("type").equals("ingredient")) {
                        uri = "https://aip.baidubce.com/rest/2.0/image-classify/v1/classify/" + intent.getStringExtra("type") + "?" + "access_token=" + access_token;

                    } else {
                        uri = "https://aip.baidubce.com/rest/2.0/image-classify/v1/" + intent.getStringExtra("type") + "?" + "access_token=" + access_token;
                    }
                    Log.i("test", "url:" + uri);
                    // https://aip.baidubce.com/rest/2.0/image-classify/v1/classify/ingredient?access_token=
                    URL url = new URL(uri);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setDoOutput(true);
                    connection.getOutputStream().write(httpArg.getBytes("UTF-8"));
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.connect();

                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();

                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    content = response.toString();
                    Log.e(TAG, "run: " + content);
                    parseJSONWithJSONObject(content);
                } catch (Exception e) {
                    e.printStackTrace();
                    content = "网络连接超时，请点击图片重试...";
                    handler.post(runnableUi);
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    Runnable runnableUi = new Runnable() {
        @Override
        public void run() {
            //   name_tv.setText(content);
            if (bt1.getText().equals("查询中...")) {
                bt1.setVisibility(View.GONE);
            } else {
                bt1.setText(content);
            }
            adapter.refresh(list);
        }
    };

    Button.OnClickListener selectPicture = new Button.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(
                    Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        }
    };


    public static String imgToBase64(String imgPath, Bitmap bitmap) {
        if (imgPath != null && imgPath.length() > 0) {
            bitmap = readBitmap(imgPath);
        }
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            out.flush();
            out.close();

            byte[] imgBytes = out.toByteArray();
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

    private static Bitmap readBitmap(String imgPath) {
        try {
            return BitmapFactory.decodeFile(imgPath);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

    //方法一：使用JSONObject
    private void parseJSONWithJSONObject(String jsonData) {
        Log.i("test", "11111111::::::" + jsonData);
        content = "";
        try {
            float score1 = 1;
            String score = String.format("%.2f", score1);
            String name = " ";
            Intent intent = getIntent();
            JSONObject jsonObject = new JSONObject(jsonData);
            //如果是地标,特殊处理
            if (intent.getStringExtra("type").equals("landmark")) {
                name = jsonData.substring(jsonData.lastIndexOf(":") + 3, jsonData.lastIndexOf("") - 3);
                Log.i("test","11"+name+"1");
                if (name.length() == 0) {
                    name = "无法识别";
                }
                score1 = score1 * 100;
                score = String.format("%.2f", score1);
                Log.i("test", "name:" + name);
                Log.i("test", "11111111::::::" + name);
                content += "\n 名称: " + name + " 可信度: " + score + "%";
                ImageRecognitionBean imageRecognitionBean = new ImageRecognitionBean();
                imageRecognitionBean.setName(name);
                imageRecognitionBean.setScore(score);
                list.add(imageRecognitionBean);
                handler.post(runnableUi);
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    score = String.valueOf(Float.parseFloat(object.getString("score")) * 100);
                    if (intent.getStringExtra("type").equals("advanced_general")) {
                        name = object.getString("keyword");
                    } else {
                        name = object.getString("name");
                    }
                    Log.i("test", "11111111::::::" + name);
                    content += "\n 名称: " + name + " 可信度: " + score + "%";
                    ImageRecognitionBean imageRecognitionBean = new ImageRecognitionBean();
                    imageRecognitionBean.setName(name);
                    imageRecognitionBean.setScore(score);
                    list.add(imageRecognitionBean);
                    handler.post(runnableUi);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
