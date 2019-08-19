package com.example.lz.babyperceive.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lz on 2019/8/19.
 */

public class Utils {
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
}
