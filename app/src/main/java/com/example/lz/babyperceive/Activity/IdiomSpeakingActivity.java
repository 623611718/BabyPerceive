package com.example.lz.babyperceive.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lz.babyperceive.Bean.AsrJson;
import com.example.lz.babyperceive.Bean.Idiom;
import com.example.lz.babyperceive.R;
import com.example.lz.babyperceive.Utils.Speek;
import com.example.lz.babyperceive.Utils.Utils;
import com.example.lz.babyperceive.View.ButtonView;
import com.example.lz.babyperceive.View.TitleView;

import java.util.ArrayList;
import java.util.List;

public class IdiomSpeakingActivity extends BaseActivity {

    private Utils utils;
    private TitleView titleView;
    private TextView paraphrase_tv,idiom_tv,story_tv;
    private ButtonView buttonView;
    private String idiom,story,paraphrase;
    private int id;
    private List<Idiom> idiomList = new ArrayList<>();
    private int random_number;  //随机数
    private Speek speek; //百度语音合成

    @Override
    public void widgetClick(View v) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_idiom_speaking);
        initData();
        initView1();
        setData();

    }
    private void setData() {
        random_number = utils.getRandomNumber(5);
        idiom = idiomList.get(random_number).getIdiom();
        story = idiomList.get(random_number).getStory();
        paraphrase = idiomList.get(random_number).getParaphrase();
        Log.i("test","/////////////////:"+idiomList.get(random_number).toString());
        paraphrase_tv.setText(paraphrase);
        story_tv.setText(story);
        idiom_tv.setText(idiom);
        speek.Speeking(idiom);
    }

    private void initData() {
        utils = new Utils();
        AsrJson asrJson = new AsrJson();
        Log.i("test","!!!!!"+utils.getTextIdiom());
        idiomList = asrJson.parseJSONidiom(utils.getTextIdiom());
        speek = new Speek(this);

    }

    private void initView1() {
        buttonView.setCustomOnClickListener(new ButtonView.ClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.previous_bt:
                        Log.i("test","!!!!!1");
                        break;
                    case R.id.play_bt:
                        Log.i("test","!!!!!1");
                        speek.Speeking(idiom);
                        break;
                    case R.id.next_bt:
                        Log.i("test","!!!!!1");
                        break;
                }
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
        return R.layout.activity_idiom_speaking;
    }

    @Override
    public void initView(View view) {
        titleView=$(R.id.titleview);
        buttonView=$(R.id.buttonview);
        paraphrase_tv=$(R.id.paraphrase_tv);
        idiom_tv=$(R.id.idiom_tv);
        story_tv=$(R.id.story_tv);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void doBusiness(Context mContext) {

    }
}
