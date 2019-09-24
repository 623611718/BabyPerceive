package com.example.lz.babyperceive.Utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/9/20.
 */

public class UtilsGetUrl {
    private String strDefaulDir;
    private Context context;
    private File URL;
    private ArrayList<String> videos;                                        //显示listview 的视频名称
    private ArrayList<String> URIConfig;                                     // 配置文件读取url 列表
    private String url =  "/data/data/com.example.lz.babyperceive/URIConfig.txt";
    public UtilsGetUrl(Context context,String URIConfigName) {
        this.context = context;
        url = "/data/data/com.example.lz.babyperceive/"+URIConfigName;
        init();
    }

    public List<String> getUrls(){
        return URIConfig;
    }
    public List<String> getVideos(){
        return videos;
    }
    //初始化数据
    private void init() {
        // TODO Auto-generated method stub
        strDefaulDir = "/data/data/"+context.getPackageName();
        // 视频列表
        URL = new File(url);
        try {
            isExists(URL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        videos = new ArrayList();

            try {
                URIConfig = readFromConfig(URL);
                Log.i("tag","URIConfig.length  "+URIConfig.size());
            } catch (IOException e) {
                e.printStackTrace();
            }
            initChmod("chmod 777 /data/data/cpm.example.mediaplayer_video_on_demand_tvostest1/URIConfig.txt");

        for(int i=0;i<URIConfig.size();i++){
            videos.add(URIConfig.get(i).substring(URIConfig.get(i).lastIndexOf("/")+1));
            Log.i("tag","name  "+videos.get(i));
        }

    }
    /**
     * 修改文件读写权限,传入文件夹目录
     */
    private void initChmod(String command){
        Process process = null;
        DataOutputStream os = null;
        try{
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
        }catch(Exception e){
        }finally{
            try{
                if(os != null){
                    os.close();
                }
                process.destroy();
            }catch(Exception e){
            }
        }
    }
    // 从配置文件中一行一行的读
    public ArrayList<String> readFromConfig(File file) throws IOException {
        // 构建一个ArrayList存放文件中的内容
        ArrayList<String> array = new ArrayList<String>();
        // 构造一个BuffferReader来读取文件
        BufferedReader bf = new BufferedReader(new FileReader(file));
        String s = null;
        // 使用readline方法一次读一行
        while ((s = bf.readLine()) != null) {
            array.add(s);
        }
        bf.close();
        return array;
    }

    // 判断是否有配置文件的存在
    public void isExists(File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
            saveResourceToSdcard();
        }
    }


    public synchronized void saveResourceToSdcard() {
        String[] strConfigs;
        AssetManager assetManager =context.getAssets();
        try {
            String strTarPath = strDefaulDir; //this.getExternalFilesDir(null).getAbsolutePath();
            strConfigs = assetManager.list("");
            Log.i("tag", strConfigs.length + "");
            Log.i("tag", "copy config files");

            for (String string : strConfigs) {
                if (string.endsWith(".txt")) {
                    String strPath = strTarPath + "/" + string;
                    Log.i("tag", strPath);
                    InputStream inputStream = assetManager.open(string);
                    copyFileInput(inputStream, strPath);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void copyFileInput(InputStream sourInput, String strTarPath) {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            inBuff = new BufferedInputStream(sourInput);
            outBuff = new BufferedOutputStream(new FileOutputStream(strTarPath));
            byte[] b = new byte[4096];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            inBuff.close();
            outBuff.close();

        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
