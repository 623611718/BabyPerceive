package com.example.lz.babyperceive.Utils;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;

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
import java.util.Locale;
import java.util.Random;

/**
 * Created by lz on 2019/8/19.
 */

public class Utils {
    private  String text ="";
    private String englishtext = "";
    public Utils() {
        this.text = getTextHanzi();
        this.englishtext = getTextEnglish();
    }

    public String getTextHanzi(){
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("assets/" + "hanzi.txt");
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
    public String getTextEnglish(){
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("assets/" + "english.txt");
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
    /**
     * 获取3500常用汉字随机一个
     * @return
     */
    public String getChinese(){
        int position = getRandomNumber();
        Log.i("test", "位置:" + position);
        Log.i("test", "长度:" + text.length());
        String chinses = text.substring(position, position+1);      //随机获取的汉字
        String chineseSpell = getChineseSpell(chinses);                          //获取拼音
        Log.i("test", "内容:" + chinses+"拼音:"+chineseSpell);
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
    private String getChineseSpell(String chinese){
        String[] spell = new String[0];
        String[] pyStrs = PinyinHelper.toHanyuPinyinStringArray('重');

        for (String s : pyStrs) {
            System.out.println(s);
        }
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
        return s;
    }

}
