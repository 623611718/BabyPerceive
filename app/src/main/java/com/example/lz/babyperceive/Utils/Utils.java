package com.example.lz.babyperceive.Utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by lz on 2019/8/19.
 */

public class Utils {
    private final static String TAG="utils";
    private  String Chinesetext ="";
    private String englishtext = "";
    private String IdiomText;
    private Context context;
    public Utils(Context context) {
        this.context=context;
        this.Chinesetext = getAsstesTxt("chinese.txt");
     //   this.englishtext = getTextEnglish();
      //  this.IdiomText = getTextIdiom();

    }

    public String getAsstesTxt(String filename){

        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("assets/" + filename);
            byte[] buffer = new byte[is.available()];
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            int length = 1;
            while((length = is.read(buffer))!= -1 ){
                stream.write(buffer, 0, length);
            }
            String text = stream.toString();
            stream.close();
            is.close();
            return text;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Drawable getAssectImage(String filename){
        AssetManager assetManager=context.getAssets();
        try {
            InputStream inputStream =assetManager.open("image/"+filename);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            Drawable drawable = new BitmapDrawable(bitmap);
            return drawable;
        } catch (IOException e) {
            e.printStackTrace();
        }
            return null;
    }

    /**
     * 获取3500常用汉字随机一个
     * @return
     */
    public String getChineseRandom(int max){
        int position = getRandomNumber(max);
       // String chinses = Chinesetext.substring(position, position+1);      //随机获取的汉字
        String[] arr = Chinesetext.split(",");
        List<String> list = java.util.Arrays.asList(arr);
        String chinses = list.get(position);
        String chineseSpell = getChineseSpell(chinses);                          //获取拼音
        Log.i(TAG, "内容:" + chinses+"拼音:"+chineseSpell);
        return chinses;
    }
    public String getChinese(int position){

        // String chinses = Chinesetext.substring(position, position+1);      //随机获取的汉字
        String[] arr = Chinesetext.split(",");
        List<String> list = java.util.Arrays.asList(arr);
        String chinses = list.get(position);
       // String chineseSpell = getChineseSpell(chinses);                          //获取拼音
       // Log.i("test", "内容:" + chinses+"拼音:"+chineseSpell);
        return chinses;
    }
    private int getRandomNumber() {
        //random = new Random(3500);//指定种子数字
        //  random_number = random.nextInt();
        int max = 3500;
        int min = 1;
        Random random = new Random();
        int random_number = random.nextInt(max) % (max - min + 1) + min;
        return random_number;
    }
    /**
     * 获取指定区间的随机数
     */
    public int getRandomNumber(int max){
        int min = 1;
        Random random = new Random();
        int random_number = random.nextInt(max) % (max - min + 1) + min;
        return random_number;
    }

    /**
     * 获取传入汉字的拼音
     * @param chinese
     * @return
     */
    public String getChineseSpell(String chinese){
        Log.i("test","chinese:"+chinese);
        String[] spell = new String[0];
//-------------------指定格式转换----------------------------
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();

// UPPERCASE：大写  (ZHONG)
// LOWERCASE：小写  (zhong)
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);//输出大写

// WITHOUT_TONE：无音标  (zhong)
// WITH_TONE_NUMBER：1-4数字表示音标  (zhong4)
// WITH_TONE_MARK：直接用音标符（必须WITH_U_UNICODE否则异常）  (zhòng)
        format.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);

// WITH_V：用v表示ü  (nv)
// WITH_U_AND_COLON：用"u:"表示ü  (nu:)
// WITH_U_UNICODE：直接用ü (nü)
        format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);

        try {
            char c = chinese.charAt(0);
            spell = PinyinHelper.toHanyuPinyinStringArray(c, format);
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }
        String s = Arrays.toString(spell); //
        return spell[0];
    }

}
